package com.kindergarten.api.common.result;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
public class PageResult<T> extends CommonResult {
    //페이징 결과
    private Page<T> data;

}
