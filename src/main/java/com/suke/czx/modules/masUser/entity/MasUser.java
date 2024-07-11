package com.suke.czx.modules.masUser.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * 用户
 *
 * @author admin
 * @email admin@qq.com
 * @date 2024-07-07 16:13:06
 */
@Data
@TableName("mas_user")
public class MasUser implements Serializable {
    public static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "")
    @JsonProperty(value = "id")
    public Long id;

    @ApiModelProperty(value = "用户名")
    @JsonProperty(value = "username")
    public String username;

    @ApiModelProperty(value = "手机号")
    @JsonProperty(value = "phone")
    public String phone;

    @ApiModelProperty(value = "openid")
    @JsonProperty(value = "openid")
    public String openid;

    @ApiModelProperty(value = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonProperty(value = "createTime")
    public Date createTime;

    @ApiModelProperty(value = "")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonProperty(value = "updateTime")
    public Date updateTime;


}
