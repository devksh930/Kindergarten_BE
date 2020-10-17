package com.kindergarten.api.common.result;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SingleResult<T> extends CommonResult {
    //    단건의 result
    private T data;
}
