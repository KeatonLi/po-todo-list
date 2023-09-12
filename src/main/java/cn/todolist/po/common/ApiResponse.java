package cn.todolist.po.common;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;

@Data
@Accessors(chain = true)
public final class ApiResponse {

    private Integer code;
    private String errorMsg;
    private Object data;

    private ApiResponse(int code, String errorMsg, Object data) {
        this.code = code;
        this.errorMsg = errorMsg;
        this.data = data;
    }

    public static ApiResponse ok() {
        return new ApiResponse(200, "", new HashMap<>());
    }

    public static ApiResponse ok(Object data) {
        return new ApiResponse(200, "", data);
    }

    public static ApiResponse error(String errorMsg) {
        return new ApiResponse(500, errorMsg, new HashMap<>());
    }
}
