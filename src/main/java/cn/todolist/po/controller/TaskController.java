package cn.todolist.po.controller;


import cn.todolist.po.common.ApiResponse;
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
            log.error(e.getMessage());
            return ApiResponse.error(e.getMessage());
        }
    }

    @PutMapping()
    public ApiResponse insertTask(@RequestBody Task task) {
        try {
            taskService.save(task);
            return ApiResponse.ok();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResponse.error(e.getMessage());
        }
    }

    @DeleteMapping("/{task_id}")
    public  ApiResponse deleteTask(@PathVariable("task_id") Long taskId) {
        try {
            taskService.removeById(taskId);
            return ApiResponse.ok();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResponse.error(e.getMessage());
        }
    }

    @PostMapping()
    public ApiResponse updateTask(@RequestBody Task task) {
        try {
            taskService.updateById(task);
            return ApiResponse.ok();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResponse.error(e.getMessage());
        }
    }
}
