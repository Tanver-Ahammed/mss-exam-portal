package com.mss.exam.portal.controller;

import com.mss.exam.portal.Routes;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    @GetMapping(Routes.DASHBOARD)
    public String dashboard(Model model) {
        model.addAttribute("pageTitle", "Home");
        return "pages/dashboard";
    }

}
