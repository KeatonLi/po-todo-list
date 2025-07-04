package cn.todolist.po.service;

import cn.todolist.po.model.Task;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

public interface TaskService extends IService<Task> {

    List<Task> getTaskList(Long userId);

    boolean save(Task task);

    Map<String, Integer> getDailyCompletedStats(Long userId);

}
