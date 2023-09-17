package cn.todolist.po.model;

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

    String username;

    String password;

    String wechatId;

    LocalDateTime createAt;

    LocalDateTime updateAt;
}
