package de.pm.mindcloud.web.controller;

import com.google.zxing.EncodeHintType;
import de.pm.mindcloud.persistence.domain.Mindmap;
import de.pm.mindcloud.persistence.domain.User;
import de.pm.mindcloud.persistence.repository.UserAccess;
import de.pm.mindcloud.web.domain.response.MindCloudMessage;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.soap.SOAPBinding;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static de.pm.mindcloud.web.XMLSource.xml;
import static de.pm.mindcloud.web.controller.SessionController.USER;

/**
 * Created by samuel on 17.06.15.
 */
@Controller
public class WebController {
    public static final String XML_SOURCE = "xmlSource";

    @Autowired
    private SessionController sessionController;

    @Autowired
    private UserAccess userAccess;

    @Autowired
    private ProfileImageController imageController;

    @RequestMapping(value = "/")
    public ModelAndView index(HttpSession session) {
        if (session.getAttribute("user") == null) {
            return new ModelAndView("redirect:/login");
        }
        if (session.getAttribute("share") != null) {
            User user = (User) session.getAttribute(USER);
            user.putSharedMindmap((Mindmap) session.getAttribute("share"));
            userAccess.save(user);
            session.setAttribute("session", null);
        }
        ModelAndView model = new ModelAndView("index");
        if (session.getAttribute("message") != null) {
            MindCloudMessage message = (MindCloudMessage) session.getAttribute("message");
            model.addObject(XML_SOURCE, xml(session).node("message", xml().node("text", message.getText()).node("type", message.getType().getValue())).build());
            session.setAttribute("message", null);
        } else {
            model.addObject(XML_SOURCE, xml(session).build());
        }
        return model;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView authenticateLogin(HttpSession session, @RequestParam("username") String username, @RequestParam("password") String password) {
        if (loginUser(username, password, session)) {
            return new ModelAndView("redirect:/");
        }
        return createErrorXSLTModel("login", session, "Login fehlgeschlagen");
    }

    @RequestMapping(value = "/login")
    public ModelAndView login(HttpSession session) throws ParserConfigurationException {
        if (session.getAttribute(USER) != null) {
            return new ModelAndView("redirect:/");
        }
        return createEmptyXSLTModel("login", session);
    }

    private boolean loginUser(String username, String password, HttpSession session) {
        User user = userAccess.findByName(username);
        if (user != null && user.getPassword().equals(password)) {
            session.setAttribute(USER, user);
            return true;
        }
        return false;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView authenticateRegistration(HttpSession session, @RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("password2") String password2) {
        if (username == null || username.length() == 0 || userAccess.usernameExists(username)) {
            return createErrorXSLTModel("register", session, "Der Username ist leider bereits vergeben!");
        }
        if (username.contains(" ")) {
            return createErrorXSLTModel("register", session, "Der Username darf keine Leerzeichen enthalten!");
        }
        if (password == null || password.length() < 8) {
            return createErrorXSLTModel("register", session, "Passwort zu kurz. Es muss mindestens 8 Zeichen lang sein!");
        }
        if (!password.equals(password2)) {
            return createErrorXSLTModel("register", session, "Die Passwörter stimmen nicht überein");
        }
        User user = new User(username, password);
        userAccess.save(user);
        if (loginUser(username, password, session)) {
            return new ModelAndView("redirect:/");
        }
        return new ModelAndView("redirect:/login");
    }

    @RequestMapping(value = "/register")
    public ModelAndView register(HttpSession session) {
        if (session.getAttribute(USER) != null) {
            return new ModelAndView("redirect:/");
        }
        return createEmptyXSLTModel("register", session);
    }

    @RequestMapping(value = "/logout")
    public ModelAndView logout(HttpSession session) {
        session.invalidate();
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/profileimage")
    @ResponseBody
    public byte[] getProfileImage(HttpSession session) throws IOException {
        if (session.getAttribute(USER) == null) {
            return null;
        }
        User user = (User) session.getAttribute(USER);
        return imageController.getImage(user);
    }

    @RequestMapping(value = "/updateProfile", method = RequestMethod.POST)
    public String updateProfile(HttpSession session, @RequestParam(value = "username", required = false) String username,
                                @RequestParam(value = "displayedname", required = false) String displayedName,
                                @RequestParam(value = "password", required = false) String password,
                                @RequestParam(value = "password2", required = false) String password2,
                                @RequestParam(value = "image", required = false) MultipartFile image,
                                @RequestParam(value = "delete-image", required = false) boolean deleteImage) throws IOException {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        List<String> changes = new ArrayList<>();
        User user = (User) session.getAttribute(USER);
        if (username != null && username.length() > 0 && !username.equals(user.getName()) && !username.contains(" ")) {
            User namedUser = userAccess.findByName(username);
            if (namedUser == null) {
                user.setName(username);
                changes.add("Benutzername geändert");
            }
        }
        if (displayedName != null && !displayedName.equals(user.getDisplayedName())) {
            user.setDisplayedName(displayedName);
            changes.add("Angezeigter Name geändert");
        }
        if (password != null && password.length() >= 8 && password.equals(password2)) {
            user.setPassword(password);
            changes.add("Passwort geändert");
        }
        userAccess.save(user);
        if (!image.isEmpty()) {
            imageController.setImage(user, image.getBytes());
            changes.add("Profilbild geändert");
        }
        if (deleteImage) {
            imageController.setImage(user, null);
            changes.add("Profilbild gelöscht");
        }
        if (!changes.isEmpty()) {
            String text = "Folgende Dinge wurden übernommen:<br/>";
            for (String change : changes) {
                text += "<br/>" + change;
            }
            session.setAttribute("message", new MindCloudMessage(text, MindCloudMessage.Type.SUCCESS));
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/shareurl/{mindmapId}")
    @ResponseBody
    public String getUrlForShareMindmap(HttpServletRequest request, HttpSession session, @PathVariable("mindmapId") String mindmapId) throws IOException {
        if (session.getAttribute(USER) == null) {
            return null;
        }
        User user = (User) session.getAttribute(USER);
        Mindmap mindmap = user.getMindmap(mindmapId);
        if (mindmap != null) {
            URL url = new URL(request.getRequestURL().toString());

            String scheme = url.getProtocol();
            String host = url.getHost();
            int port = url.getPort();

            return  scheme + "://" + host + ":" + port + "/share/" + mindmap.getId() + "/" + user.getId();
        }
        return null;
    }

    @RequestMapping(value = "/shareqrcode/{mindmapId}")
    @ResponseBody
    public byte[] getQrCodeForShareMindmap(HttpServletRequest request, HttpSession session, @PathVariable("mindmapId") String mindmapId) throws IOException {
        if (session.getAttribute(USER) == null) {
            return null;
        }
        User user = (User) session.getAttribute(USER);
        Mindmap mindmap = user.getMindmap(mindmapId);
        if (mindmap != null) {
            String url = getUrlForShareMindmap(request, session, mindmapId);
            File qrcode = QRCode.from(url).to(ImageType.JPG).withSize(250, 250).file();
            return IOUtils.toByteArray(new FileInputStream(qrcode));
        }
        return null;
    }

    @RequestMapping(value = "/share/{mindmapId}/{sharedUserId}")
    public String shareMindmap(HttpSession session, @PathVariable("mindmapId") String mindmapId, @PathVariable("sharedUserId") String userId) {
        User sharedUser = userAccess.find(userId);
        Mindmap mindmap = sharedUser.getMindmap(mindmapId);
        session.setAttribute("share", mindmap);
        return "redirect:/";
    }

    private ModelAndView createEmptyXSLTModel(String view, HttpSession session) {
        ModelAndView model = new ModelAndView(view);
        model.addObject(XML_SOURCE, xml(session).build());
        return model;
    }

    private ModelAndView createErrorXSLTModel(String view, HttpSession session, String error) {
        ModelAndView model = new ModelAndView(view);
        model.addObject(XML_SOURCE, xml(session).node("error", error).build());
        return model;
    }
}