package com.admin.AdminService.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class Controller
{
    @GetMapping("test")
    public String test()
    {
        return "Admin controller";
    }
}
