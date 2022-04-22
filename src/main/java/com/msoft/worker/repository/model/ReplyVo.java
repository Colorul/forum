package com.msoft.worker.repository.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
@ApiOperation(value = "回复VO")
public class ReplyVo {
    private String id;
    @ApiModelProperty(value = "回复内容")
    private String content;
    @ApiModelProperty(value = "用户信息")
    private UserVo user;
    @ApiModelProperty(value = "父节点 根节点为0")
    private String parentId;
    @ApiModelProperty(value = "话题ID")
    private String topicId;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime createdDate;
    @ApiModelProperty(value = "用户信息")
    private UserVo parentUser;
    @ApiModelProperty(value = "回复的回复")
    private List<ReplyVo> children = new ArrayList<>();
}
