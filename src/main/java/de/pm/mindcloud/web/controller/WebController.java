package de.pm.mindcloud.web.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.xml.parsers.ParserConfigurationException;

import static de.pm.mindcloud.web.XMLSource.*;

/**
 * Created by samuel on 17.06.15.
 */
@Controller
public class WebController {
    private static Logger logger = Logger.getLogger(WebController.class);

    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView authenticateLogin(@RequestParam("username") String username, @RequestParam("password") String password) {
        logger.info("Log in '" + username + "' with password '" + password + "'");
        ModelAndView model = new ModelAndView("login");
        model.addObject("xmlSource", xml().node("error", "Login fehlgeschlagen").build());
        return model;
    }

    @RequestMapping(value = "/login")
    public ModelAndView login() throws ParserConfigurationException {
        ModelAndView model = new ModelAndView("login");
        model.addObject("xmlSource", xml().build());
        return model;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView authenticateRegistration(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("password2") String password2) {
        logger.info("Register '" + username + "' with password '" + password + "' and '" + password2 + "'");
        ModelAndView model = new ModelAndView("register");
        model.addObject("xmlSource", xml().node("error", "Registrieren fehlgeschlagen").build());
        return model;
    }

    @RequestMapping(value = "/register")
    public ModelAndView register() {
        ModelAndView model = new ModelAndView("register");
        model.addObject("xmlSource", xml().build());
        return model;
    }
}