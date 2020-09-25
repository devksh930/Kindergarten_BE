package com.kindergarten.api.common.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResult {
    @ApiModelProperty(value = "응답 성공여부 : true/false")
    private boolean success;
    @ApiModelProperty(value = "실패 응답 코드 번호 : HTTP CODE/성공시 0")
    private int code;
    @ApiModelProperty(value = "응답 메세지")
    private String msg;
}
