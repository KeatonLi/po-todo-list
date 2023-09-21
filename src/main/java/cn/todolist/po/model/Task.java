package cn.todolist.po.model;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName(value = "todo_task")
public class Task {
    @TableId
    Long id;

    @TableField(value = "user_id")
    Long userId;

    @TableField(value = "title")
    String title;

    @TableField(value = "`describe`")
    String describe;

    @TableField(value = "star")
    Integer star;

    @TableField(value = "status")
    Integer status;

    @TableField(value = "create_at", fill = FieldFill.INSERT_UPDATE)
    LocalDateTime createAt;

    @TableField(value = "update_at", fill = FieldFill.INSERT_UPDATE)
    LocalDateTime updateAt;
}
