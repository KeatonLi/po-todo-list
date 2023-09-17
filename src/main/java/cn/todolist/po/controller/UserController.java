package cn.todolist.po.controller;


import cn.todolist.po.common.ApiResponse;
import cn.todolist.po.enums.RespStatusEnum;
import cn.todolist.po.exception.CommonException;
import cn.todolist.po.model.User;
import cn.todolist.po.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    UserService userService;

    @PostMapping("/login")
    public ApiResponse login(@RequestBody User user) {
        try {
            if (userService.userLogin(user.getUsername(), user.getPassword())) {
                return ApiResponse.ok();
            } else {
                return ApiResponse.error();
            }
        } catch (Exception e) {
            throw new CommonException(RespStatusEnum.ERROR_500);
        }
    }

    @PutMapping("/register")
    public ApiResponse register(@RequestBody User user) {
        try {
            userService.userRegister(user);
            return ApiResponse.ok();
        } catch (Exception e) {
            throw new CommonException(RespStatusEnum.ERROR_500);
        }
    }
}
