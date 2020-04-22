package org.tongji.traffic.dispatch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.tongji.traffic.dispatch.service.DataScreenWSService;

import javax.servlet.http.HttpServletResponse;

@RestController
public class DispatchController {

    @Autowired
    DataScreenWSService dataScreenWSService;

    @PostMapping("newTime")
    public void changeRunMode(@RequestBody String newTime, HttpServletResponse response) {
        dataScreenWSService.sendMessage(newTime);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
