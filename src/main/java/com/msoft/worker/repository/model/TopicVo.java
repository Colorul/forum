package com.msoft.worker.repository.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@ApiOperation(value = "话题VO")
public class TopicVo {
    private String id;
    @ApiModelProperty(value = "话题内容")
    private String content;
    @ApiModelProperty(value = "用户信息")
    private UserVo user;
    private String school;
    @ApiModelProperty(value = "话题标题")
    private String title;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime createdDate;
    @ApiModelProperty(value = "是否属于当前用户 若是 才可以删除 编辑")
    private boolean belongToCurrent;
    @ApiModelProperty(value = "点击数量")
    private Long click = 0L;
    @ApiModelProperty(value = "回复数量")
    private Long reply = 0L;
}
