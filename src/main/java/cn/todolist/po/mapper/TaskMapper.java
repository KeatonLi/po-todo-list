package cn.todolist.po.mapper;

import cn.todolist.po.model.Task;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TaskMapper extends BaseMapper<Task> {

    List<Task> getTaskList(@Param("user_id") Long userId);
}
