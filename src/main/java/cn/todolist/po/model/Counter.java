package cn.todolist.po.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class Counter implements Serializable {

    private Integer id;

    private Integer count;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
