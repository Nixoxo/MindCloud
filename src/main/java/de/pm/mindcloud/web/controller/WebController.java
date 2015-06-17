package de.pm.mindcloud.web.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;

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
    public String authenticateLogin(@RequestParam("username") String username, @RequestParam("password") String password) {
        logger.info("Log in '" + username + "' with password '" + password + "'");
        return "login";
    }

    @RequestMapping(value = "/login")
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String authenticateRegistration(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("password2") String password2) {
        logger.info("Register '" + username + "' with password '" + password + "' and '" + password2 + "'");
        return "register";
    }

    @RequestMapping(value = "/register")
    public String register() {
        return "register";
    }

    @RequestMapping(value="/viewXSLT")
    public ModelAndView viewXSLT(HttpServletRequest request,
                                 HttpServletResponse response) throws IOException {
        // builds absolute path of the XML file
        String xmlFile = "citizens.xml";
        String contextPath = request.getServletContext().getRealPath("");
        String xmlFilePath = contextPath + File.separator + xmlFile;

        Source source = new StreamSource(ClassLoader.getSystemResourceAsStream(xmlFile));

        ModelAndView model = new ModelAndView("XSLTView");
        model.addObject("xmlSource", source);

        return model;
    }
}