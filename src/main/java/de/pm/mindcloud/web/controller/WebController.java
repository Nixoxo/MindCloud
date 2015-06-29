package de.pm.mindcloud.web.controller;

import de.pm.mindcloud.persistence.domain.User;
import de.pm.mindcloud.persistence.repository.UserAccess;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;

import static de.pm.mindcloud.web.XMLSource.*;

/**
 * Created by samuel on 17.06.15.
 */
@Controller
public class WebController {
    public static final String USER = "user";
    private static Logger logger = Logger.getLogger(WebController.class);

    @Autowired
    private UserAccess userAccess;

    @RequestMapping(value = "/")
    public ModelAndView index(HttpSession session) {
        if (session.getAttribute("user") == null) {
            return new ModelAndView("redirect:/login");
        }
        return createEmptyXSLTModel("index");
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView authenticateLogin(HttpSession session, @RequestParam("username") String username, @RequestParam("password") String password) {
        User user = userAccess.findByName(username);
        logger.info(user == null);
        if (user != null && user.getPassword().equals(password)) {
            logger.info(user);
            session.setAttribute(USER, user);
            return new ModelAndView("redirect:/");
        }
        return createErrorXSLTModel("login", "Login fehlgeschlagen");
    }

    @RequestMapping(value = "/login")
    public ModelAndView login() throws ParserConfigurationException {
        return createEmptyXSLTModel("login");
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView authenticateRegistration(HttpSession session, @RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("password2") String password2) {
        if (username == null || username.length() == 0 || userAccess.usernameExists(username)) {
            return createErrorXSLTModel("register", "Der Username ist leider bereits vergeben!");
        }
        if (username.contains(" ")) {
            return createErrorXSLTModel("register", "Der Username darf keine Leerzeichen enthalten!");
        }
        if (password == null || password.length() < 8) {
            return createErrorXSLTModel("register", "Passwort zu kurz. Es muss mindestens 8 Zeichen lang sein!");
        }
        if (!password.equals(password2)) {
            return createErrorXSLTModel("register", "Die Passwörter stimmen nicht überein");
        }
        User user = new User(username, password);
        userAccess.save(user);
        logger.info(user);
        session.setAttribute(USER, user);
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value = "/register")
    public ModelAndView register(HttpSession session) {
        return createEmptyXSLTModel("register");
    }

    @RequestMapping(value = "/logout")
    public ModelAndView logout(HttpSession session) {
        session.invalidate();
        return new ModelAndView("redirect:/");
    }

    private ModelAndView createEmptyXSLTModel(String view) {
        ModelAndView model = new ModelAndView(view);
        model.addObject("xmlSource", xml().build());
        return model;
    }

    private ModelAndView createErrorXSLTModel(String view, String error) {
        ModelAndView model = new ModelAndView(view);
        model.addObject("xmlSource", xml().node("error", error).build());
        return model;
    }
}