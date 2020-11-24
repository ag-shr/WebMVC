package com.webapp.controllers;

import com.webapp.services.ClientAccessTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("v1")
public class MainController {

    @Autowired
    private ClientAccessTokenService clientAccessTokenService;

    @GetMapping("health")
    public ResponseEntity<Boolean> healthCheck() {
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @GetMapping
    public @ResponseBody String accessToken(){
        return clientAccessTokenService.getAccessToken();
    }

}
