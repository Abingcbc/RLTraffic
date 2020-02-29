package org.sse.traffic.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class TrafficController {

    @PostMapping()
    public void init() {

    }
}
