package com.suke.czx.modules.masItem.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;


/**
 * @author admin
 * @email admin@qq.com
 * @date 2024-07-05 22:12:50
 */
@Data
@TableName("mas_item")
public class MasItem implements Serializable {
    public static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "")
    @JsonProperty(value = "id")
    public Long id;

    @ApiModelProperty(value = "名称")
    @JsonProperty(value = "name")
    public String name;

    @ApiModelProperty(value = "封面URL")
    @JsonProperty(value = "indexUrl")
    public String indexUrl;

    @ApiModelProperty(value = "面额")
    @JsonProperty(value = "value")
    public BigDecimal value;

    @ApiModelProperty(value = "价格")
    @JsonProperty(value = "price")
    public BigDecimal price;

    @ApiModelProperty(value = "说明")
    @JsonProperty(value = "description")
    public String description;

    @ApiModelProperty(value = "库存")
    @JsonProperty(value = "remain")
    public BigDecimal remain;

    @ApiModelProperty(value = "有效期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonProperty(value = "expireAt")
    public Date expireAt;

    @ApiModelProperty(value = "上架")
    @JsonProperty(value = "onSale")
    public Boolean onSale;

    @ApiModelProperty(value = "排序")
    @JsonProperty(value = "sortIndex")
    public Long sortIndex;


}
