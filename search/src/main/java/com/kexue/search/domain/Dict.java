package com.kexue.search.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import com.kexue.common.domain.BaseModel;
import lombok.Data;

@Data
@TableName("t_dict")
public class Dict extends BaseModel<Dict> {

    private Long typeId;
    private String name;
    private Integer code;
    private String remark;

}
