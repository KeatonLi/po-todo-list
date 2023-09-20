package cn.todolist.po.service.impl;

import cn.todolist.po.mapper.TaskMapper;
import cn.todolist.po.model.Task;
import cn.todolist.po.service.TaskService;
import cn.todolist.po.utils.SnowFlakeUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {
    @Resource
    TaskMapper taskMapper;

    @Resource
    SnowFlakeUtil flakeUtil;


    @Override
    public List<Task> getTaskList(Long userId) {
        return super.lambdaQuery().eq(Task::getUserId, userId)
                .list();
    }

    @Override
    public boolean save(Task task) {
        super.save(task.setStatus(0));
        return true;
    }


}
