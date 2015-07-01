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

import static de.pm.mindcloud.web.XMLSource.*;
import static de.pm.mindcloud.web.controller.SessionController.USER;

/**
 * Created by samuel on 17.06.15.
 */
@Controller
public class WebController {
    @Autowired
    private SessionController sessionController;

    @Autowired
    private UserAccess userAccess;

    @RequestMapping(value = "/")
    public ModelAndView index(HttpSession session) {
        if (session.getAttribute("user") == null) {
            return new ModelAndView("redirect:/login");
        }
        return createEmptyXSLTModel("index", session);
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

    private ModelAndView createEmptyXSLTModel(String view, HttpSession session) {
        ModelAndView model = new ModelAndView(view);
        model.addObject("xmlSource", xml(session).build());
        return model;
    }

    private ModelAndView createErrorXSLTModel(String view, HttpSession session, String error) {
        ModelAndView model = new ModelAndView(view);
        model.addObject("xmlSource", xml(session).node("error", error).build());
        return model;
    }
}