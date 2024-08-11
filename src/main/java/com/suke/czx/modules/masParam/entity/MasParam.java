package com.suke.czx.modules.masParam.entity;

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
 * 
 *
 * @author admin
 * @email admin@qq.com
 * @date 2024-08-07 23:39:32
 */
@Data
@TableName("mas_param")
public class MasParam implements Serializable {
public static final long serialVersionUID = 1L;

		@TableId(type = IdType.AUTO)
				@ApiModelProperty(value = "")
	@JsonProperty(value = "id")
public Integer id;

				@ApiModelProperty(value = "KEY")
	@JsonProperty(value = "pkey")
public String pkey;

				@ApiModelProperty(value = "内容")
	@JsonProperty(value = "value")
public String value;


}
