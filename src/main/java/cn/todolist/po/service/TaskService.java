package cn.todolist.po.service;

import cn.todolist.po.model.Task;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface TaskService extends IService<Task> {

    List<Task> getTaskList(Long userId);

}
