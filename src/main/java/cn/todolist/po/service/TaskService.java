package cn.todolist.po.service;

import cn.todolist.po.model.Task;

import java.util.List;

public interface TaskService{

    void insert(Task task);

    void update(Task task);

    List<Task> getTaskList(Long userId);

    void deleteById(Long id);
}
