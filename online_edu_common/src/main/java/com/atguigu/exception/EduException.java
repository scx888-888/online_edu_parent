package com.atguigu.exception;

//继承RuntimeException    遇到事务可以进行回滚
//继承Exception           不能回滚

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EduException extends RuntimeException {
    @ApiModelProperty(value = "异常编码")
    private Integer code;
    @ApiModelProperty(value = "异常信息")
    private String message;
}
