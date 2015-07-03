package de.pm.mindcloud.web.controller;

import de.pm.mindcloud.web.domain.response.MindCloudMessage;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpSession;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(Exception.class)
    public String handleMaxUploadException(Exception e, HttpSession session) {
        e.printStackTrace();
        session.setAttribute("message", new MindCloudMessage("Es ist ein Fehler aufgetreten!", MindCloudMessage.Type.ERROR));
        return "redirect:/";
    }
}