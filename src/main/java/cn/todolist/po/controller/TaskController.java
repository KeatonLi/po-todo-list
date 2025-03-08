
package cn.todolist.po.controller;

import cn.todolist.po.common.ApiResponse;
import cn.todolist.po.model.Task;
import cn.todolist.po.service.TaskService;
import cn.todolist.po.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 任务控制器
 */
@RestController
@RequestMapping("/task")
@CrossOrigin
@Slf4j
public class TaskController {

    private final TaskService taskService;

    /**
     * 构造函数
     * @param taskService 任务服务
     */
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * 获取任务列表
     * @param request HTTP请求
     * @return 任务列表的API响应
     */
    @GetMapping("/")
    public ApiResponse getTaskList(HttpServletRequest request) {
        try {
            return ApiResponse.ok(taskService.getTaskList((Long) request.getAttribute("userId")));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 获取任务详情
     * @param taskId 任务ID
     * @return 任务详情的API响应
     */
    @GetMapping("/{taskId}")
    public ApiResponse getTaskDetail(@PathVariable("taskId") Long taskId) {
        try {
            return ApiResponse.ok(taskService.getById(taskId));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 插入任务
     * @param task 任务对象
     * @param request HTTP请求
     * @return 插入任务的API响应
     */
    @PutMapping()
    public ApiResponse insertTask(@RequestBody Task task, HttpServletRequest request) {
        try {
            task.setUserId((Long) request.getAttribute("userId"));
            taskService.save(task);
            return ApiResponse.ok();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 删除任务
     * @param taskId 任务ID
     * @return 删除任务的API响应
     */
    @DeleteMapping("/{task_id}")
    public ApiResponse deleteTask(@PathVariable("task_id") Long taskId) {
        try {
            taskService.removeById(taskId);
            return ApiResponse.ok();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResponse.error(e.getMessage());
        }
    }

    /**
     * 更新任务
     * @param task 任务对象
     * @return 更新任务的API响应
     */
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