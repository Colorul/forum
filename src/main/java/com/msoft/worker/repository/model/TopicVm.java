package com.msoft.worker.repository.model;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Setter
@Getter
@ToString
@ApiOperation(value = "回复VO")
public class TopicVm {
    @NotBlank(message = "话题内容不能为空")
    @ApiModelProperty(value = "话题内容")
    private String content;
    @ApiModelProperty(value = "标题")
    @NotBlank(message = "话题标题不能为空")
    private String title;
    @NotBlank(message = "accessCode不能为空")
    @ApiModelProperty(value = "accessCode")
    private String accessCode;
}
