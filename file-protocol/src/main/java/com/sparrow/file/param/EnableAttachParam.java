package com.sparrow.file.param;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnableAttachParam {
    private String belongBusiness;
    private Long belongId;
    private List<AttachRemark> attachRemarks;
}
