package cn.todolist.po.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class User {
    Long id;

    String wechatId;

    LocalDateTime createAt;

    LocalDateTime updateAt;
}
