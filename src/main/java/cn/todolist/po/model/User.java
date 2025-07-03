package cn.todolist.po.model;

import com.baomidou.mybatisplus.annotation.TableField;
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
@TableName(value = "todo_user")
public class User {
    Long id;

    @TableField(value = "username")
    String username;

    @TableField(value = "password")
    String password;

    @TableField(value = "wechat_id")
    String wechatId;

    @TableField(value = "create_at", typeHandler = cn.todolist.po.config.LocalDateTimeTypeHandler.class)
    LocalDateTime createAt;

    @TableField(value = "update_at", typeHandler = cn.todolist.po.config.LocalDateTimeTypeHandler.class)
    LocalDateTime updateAt;
}
