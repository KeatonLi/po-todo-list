package cn.todolist.po.model;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;



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

    @TableField(value = "create_at", fill = FieldFill.INSERT)
    Long createAt;

    @TableField(value = "update_at", fill = FieldFill.INSERT_UPDATE)
    Long updateAt;
}
