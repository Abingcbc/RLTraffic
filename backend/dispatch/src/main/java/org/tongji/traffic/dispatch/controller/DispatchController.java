package org.tongji.traffic.dispatch.controller;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class DispatchController {

    public void changeRunMode(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
