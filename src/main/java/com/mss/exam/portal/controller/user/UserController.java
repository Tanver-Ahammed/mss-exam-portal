package com.mss.exam.portal.controller.user;

import com.mss.exam.portal.Routes;
import com.mss.exam.portal.dto.DataTablesResponse;
import com.mss.exam.portal.entity.user.User;
import com.mss.exam.portal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(Routes.USER)
    public String listUsers(Model model) {
        model.addAttribute("pageTitle", "Users");
        return "pages/user/index";
    }

    @GetMapping(Routes.API_USER)
    @ResponseBody
    public ResponseEntity<DataTablesResponse<User>> getUsers(
            @RequestParam("draw") int draw,
            @RequestParam("start") int start,
            @RequestParam("length") int length) {

        List<User> users = userService.findAll();

        DataTablesResponse<User> response = new DataTablesResponse<>();
        response.setDraw(draw);
        response.setRecordsTotal(userService.count());
        response.setRecordsFiltered(users.size());
        response.setData(users);

        return ResponseEntity.ok(response);
    }

}
