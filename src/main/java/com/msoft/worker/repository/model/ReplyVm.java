package com.msoft.worker.repository.model;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@ToString
@ApiOperation(value = "回复VO")
public class ReplyVm {
    @NotBlank(message = "回复内容不能为空")
    @ApiModelProperty(value = "回复内容")
    private String content;
    @NotBlank(message = "回复内容不能为空")
    @ApiModelProperty(value = "回复内容")
    private String topicId;
    @NotBlank(message = "被回复的信息不能为空 根节点为0")
    @ApiModelProperty(value = "被回复的信息 根节点为0")
    private String parentId;
    @NotBlank(message = "accessCode不能为空")
    @ApiModelProperty(value = "accessCode")
    private String accessCode;
}
