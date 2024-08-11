package com.suke.czx.modules.masPicture.entity;

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
 * 图片管理
 *
 * @author admin
 * @email admin@qq.com
 * @date 2024-08-05 21:32:04
 */
@Data
@TableName("mas_picture")
public class MasPicture implements Serializable {
public static final long serialVersionUID = 1L;

		@TableId(type = IdType.AUTO)
				@ApiModelProperty(value = "id")
	@JsonProperty(value = "id")
public Long id;

				@ApiModelProperty(value = "键值")
	@JsonProperty(value = "pkey")
public String pkey;

				@ApiModelProperty(value = "图片url")
	@JsonProperty(value = "url")
public String url;

				@ApiModelProperty(value = "描述")
	@JsonProperty(value = "description")
public String description;


}
