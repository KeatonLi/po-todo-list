package cn.todolist.po.controller;


import cn.todolist.po.common.ApiResponse;
import cn.todolist.po.model.Task;
import cn.todolist.po.service.TaskService;
import cn.todolist.po.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/task")
@Slf4j
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/")
    public ApiResponse getTaskList(HttpServletRequest request) {
        try {
            return ApiResponse.ok(taskService.getTaskList((Long) request.getAttribute("id")));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/{taskId}")
    public ApiResponse getTaskDetail(@PathVariable("taskId") Long taskId) {
        try {
            return ApiResponse.ok(taskService.getById(taskId));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResponse.error(e.getMessage());
        }
    }



    @PutMapping()
    public ApiResponse insertTask(@RequestBody Task task, HttpServletRequest request) {
        try {
            task.setUserId(Long.parseLong(request.getHeader("userId")));
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
