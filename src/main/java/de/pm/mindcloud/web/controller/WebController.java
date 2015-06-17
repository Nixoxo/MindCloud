package de.pm.mindcloud.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by samuel on 17.06.15.
 */
@Controller
public class WebController {

    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }
}