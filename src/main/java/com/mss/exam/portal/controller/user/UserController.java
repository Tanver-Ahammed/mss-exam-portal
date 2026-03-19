package com.mss.exam.portal.controller.user;

import com.mss.exam.portal.Routes;
import com.mss.exam.portal.dto.DataTablesResponse;
import com.mss.exam.portal.dto.user.UserDto;
import com.mss.exam.portal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public ResponseEntity<DataTablesResponse<UserDto>> getUsers(
            @RequestParam(defaultValue = "1") int draw,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int length) {

        PageRequest pageRequest = PageRequest.of(
                start / length,
                length,
                Sort.by(Sort.Direction.DESC, "id")
        );

        Page<UserDto> page = userService.findAll(pageRequest)
                .map(UserDto::from);

        DataTablesResponse<UserDto> response = new DataTablesResponse<>();
        response.setDraw(draw);
        response.setRecordsTotal(page.getTotalElements());
        response.setRecordsFiltered(page.getTotalElements());
        response.setData(page.getContent());

        return ResponseEntity.ok(response);
    }

}
