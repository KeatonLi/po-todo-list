package cn.todolist.po.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public enum TaskEnum {


    /**
     * 标星状态
     */
    STAR(1, "标星"),
    NOT_STAR(0, "未标星"),

    /**
     * 完成状态
     */
    COMPLETED(1, "已经完成"),
    NOT_COMPLETE(0, "未完成"),
    ;


    /**
     * 实际编码
     */
    private final Integer code;
    /**
     * 状态
     */
    private final String msg;
}
