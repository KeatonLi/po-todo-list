
package cn.todolist.po.service.impl;

import cn.todolist.po.mapper.TaskMapper;
import cn.todolist.po.model.Task;
import cn.todolist.po.service.TaskService;
import cn.todolist.po.utils.SnowFlakeUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 任务实施类
 */
@Service
@Slf4j
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {
    @Resource
    TaskMapper taskMapper;

    @Resource
    SnowFlakeUtil flakeUtil;

    /**
     * 获取指定用户的任务列表
     *
     * @param userId 用户ID
     * @return 任务列表
     */
    @Override
    public List<Task> getTaskList(Long userId) {
        List<Task> list = super.lambdaQuery().eq(Task::getUserId, userId)
                .list();
        // 把star任务的排前面
        list.sort((o1, o2) -> o2.getStar() - o1.getStar());
        return list;
    }

    /**
     * 保存任务
     *
     * @param task 任务对象
     * @return 是否保存成功
     */
    @Override
    public boolean save(Task task) {
        task.setId(flakeUtil.getNextId());
        super.save(task.setStatus(0));
        return true;
    }

    /**
     * 更新任务
     *
     * @param entity 任务对象
     * @return 是否更新成功
     */
    @Override
    public boolean updateById(Task entity) {
        // 如果任务状态变为完成，设置完成时间
        if (entity.getStatus() != null && entity.getStatus() == 1) {
            // 检查原任务状态，只有从未完成变为完成时才设置completedAt
            Task originalTask = super.getById(entity.getId());
            if (originalTask != null && originalTask.getStatus() != 1) {
                entity.setCompletedAt(System.currentTimeMillis());
            }
        } else if (entity.getStatus() != null && entity.getStatus() == 0) {
            // 如果任务状态变为未完成，清除完成时间
            entity.setCompletedAt(null);
        }
        return super.updateById(entity);
    }

    /**
     * 获取每日完成任务统计
     *
     * @param userId 用户ID
     * @return 每日完成任务统计Map，key为日期字符串，value为完成任务数量
     */
    @Override
    public Map<String, Integer> getDailyCompletedStats(Long userId) {
        // 获取最近30天的已完成任务
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(29); // 包含今天共30天
        
        long startTimestamp = startDate.atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
        long endTimestamp = endDate.plusDays(1).atStartOfDay().atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
        
        List<Task> completedTasks = super.lambdaQuery()
                .eq(Task::getUserId, userId)
                .eq(Task::getStatus, 1) // 已完成状态
                .isNotNull(Task::getCompletedAt) // 确保有完成时间
                .ge(Task::getCompletedAt, startTimestamp)
                .le(Task::getCompletedAt, endTimestamp)
                .list();
        
        // 按日期分组统计
        Map<String, Long> dailyCount = completedTasks.stream()
                .filter(task -> task.getCompletedAt() != null) // 额外过滤确保completedAt不为null
                .collect(Collectors.groupingBy(
                        task -> LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(task.getCompletedAt()), java.time.ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                        Collectors.counting()
                ));
        
        // 创建完整的30天数据，包括没有完成任务的日期
        Map<String, Integer> result = new LinkedHashMap<>();
        for (int i = 29; i >= 0; i--) {
            String dateStr = endDate.minusDays(i).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            result.put(dateStr, dailyCount.getOrDefault(dateStr, 0L).intValue());
        }
        
        return result;
    }
}