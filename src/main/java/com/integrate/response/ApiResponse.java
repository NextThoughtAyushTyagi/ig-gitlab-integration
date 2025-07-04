package com.integrate.response;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
final public class ApiResponse {

    private String status; //later We will use the existing enums from common vo
    private String message;
    private Object object;
    private Long totalCount;
    private Integer code;

    public ApiResponse(String status, Integer code, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }

    public ApiResponse(String status, Integer code, String message, Object object) {
        this.code = code;
        this.status = status;
        this.message = message;
        this.object = object;
    }

    public ApiResponse(String status, Integer code, String message, Object object, Long totalCount) {
        this.code = code;
        this.status = status;
        this.message = message;
        this.object = object;
        this.totalCount = totalCount;
    }
}
