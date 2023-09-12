package cn.todolist.po.controller;


import cn.todolist.po.common.ApiResponse;
import cn.todolist.po.enums.RespStatusEnum;
import cn.todolist.po.exception.CommonException;
import cn.todolist.po.model.Task;
import cn.todolist.po.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/task")
@Slf4j
public class TaskController {

    @Resource
    TaskService taskService;

    @GetMapping("/{user_id}")
    public ApiResponse getTaskList(@PathVariable("user_id") Long userId) {
        try {
            return ApiResponse.ok(taskService.getTaskList(userId));
        } catch (Exception e) {
            throw new CommonException(RespStatusEnum.ERROR_500);
        }
    }

    @PutMapping()
    public ApiResponse insertTask(@RequestBody Task task) {
        try {
            taskService.insert(task);
            return ApiResponse.ok();
        } catch (Exception e) {
            throw new CommonException(RespStatusEnum.ERROR_500);
        }
    }
}
