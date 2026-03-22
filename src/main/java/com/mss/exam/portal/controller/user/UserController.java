package com.mss.exam.portal.controller.user;

import com.mss.exam.portal.Routes;
import com.mss.exam.portal.dto.DataTablesResponse;
import com.mss.exam.portal.dto.user.UserDto;
import com.mss.exam.portal.dto.user.UserFilter;
import com.mss.exam.portal.service.UserService;
import com.mss.exam.portal.service.geo.GeoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
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
    private final GeoService geoService;

    @Autowired
    public UserController(UserService userService, GeoService geoService) {
        this.userService = userService;
        this.geoService = geoService;
    }

    @GetMapping(Routes.USER)
    public String listUsers(Model model) {
        model.addAttribute("lang", LocaleContextHolder.getLocale().getLanguage());
        model.addAttribute("divisions", geoService.findAllDivisions());
        return "pages/user/index";
    }

    @GetMapping(Routes.API_USER)
    @ResponseBody
    public ResponseEntity<DataTablesResponse<UserDto>> getUsers(
            UserFilter userFilter,
            @RequestParam(defaultValue = "1") int draw,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int length) {

        PageRequest pageRequest = PageRequest.of(
                start / length,
                length,
                Sort.by(Sort.Direction.DESC, "userId")
        );

        Page<UserDto> page = userService.findAll(userFilter, pageRequest);

        DataTablesResponse<UserDto> response = new DataTablesResponse<>();
        response.setDraw(draw);
        response.setRecordsTotal(page.getTotalElements());
        response.setRecordsFiltered(page.getTotalElements());
        response.setData(page.getContent());

        return ResponseEntity.ok(response);
    }

}
