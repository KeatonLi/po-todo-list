package cn.todolist.po.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * 全局响应状态枚举
 *
 * @author libokai
 * @since 2023/09/12
 **/
@Getter
@ToString
@AllArgsConstructor
public enum RespStatusEnum {

    /**
     * 错误
     */
    ERROR_500("500", "服务器未知错误"),
    ERROR_400("400", "错误请求"),

    /**
     * OK：操作成功
     */
    SUCCESS("200", "操作成功"),
    FAIL("-1", "操作失败"),

    /**
     * 登陆校验
     */
    WRONG_PASSWORD("500", "用户名或者密码错误"),
    EMPTY_INPUT("500", "用户名或者密码输入为空"),
    ;

    /**
     * 响应状态
     */
    private final String code;
    /**
     * 响应编码
     */
    private final String msg;
}
