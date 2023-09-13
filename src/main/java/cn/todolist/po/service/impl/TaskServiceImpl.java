package cn.todolist.po.service.impl;

import cn.todolist.po.mapper.TaskMapper;
import cn.todolist.po.model.Task;
import cn.todolist.po.service.TaskService;
import cn.todolist.po.utils.SnowFlakeUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService{
    @Resource
    TaskMapper taskMapper;

    @Resource
    SnowFlakeUtil flakeUtil;

    @Override
    public void insert(Task task) {
        task.setId(flakeUtil.getNextId());
        taskMapper.insert(task);
    }

    @Override
    public void update(Task task) {
        taskMapper.updateById(task);
    }

    @Override
    public List<Task> getTaskList(Long userId) {
        return taskMapper.getTaskList(userId);
    }

    @Override
    public void deleteById(Long id) {
        taskMapper.deleteById(id);
    }
}
