package cn.todolist.po;

import cn.todolist.po.mapper.TaskMapper;
import cn.todolist.po.model.Task;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

public class MyBatisPlusTest extends BaseTest{

    @Resource
    TaskMapper taskMapper;

    @Test
    public void testSelectList() {
        QueryWrapper<Task> wrapper = new QueryWrapper<>();
        List<Task> taskList = taskMapper.selectList(wrapper);
        System.out.println(taskList);
    }
}
