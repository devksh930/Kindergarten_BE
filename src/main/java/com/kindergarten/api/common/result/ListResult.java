package com.kindergarten.api.common.result;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class ListResult<T> extends CommonResult {
    //다건의 결과
    private List<T> list;
}
