package com.sparrow.file.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttachRemark {
    private Long fileId;
    private String remark;
}
