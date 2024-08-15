package com.suke.czx.modules.masOrder.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.suke.czx.modules.masItem.entity.MasItem;
import com.suke.zhjg.common.autofull.annotation.AutoFullBeanSQL;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.annotation.JsonProperty;


/**
 * 订单
 *
 * @author admin
 * @email admin@qq.com
 * @date 2024-07-08 20:14:41
 */
@Data
@TableName("mas_order")
public class MasOrder implements Serializable {
    public static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "")
    @JsonProperty(value = "id")
    public Long id;

    @ApiModelProperty(value = "订单号")
    @JsonProperty(value = "orderNo")
    public String orderNo;

    @ApiModelProperty(value = "商品ID")
    @JsonProperty(value = "itemId")
    public String itemId;

    @ApiModelProperty(value = "状态,订单状态: 1已支付,2待支付,3超时, 4已核销, 5已过期")
    @JsonProperty(value = "status")
    public Integer status;

    @ApiModelProperty(value = "是否成功通知WX服: 0未通知,1已通知创建,2已通知成功/超时")
    @JsonProperty(value = "nofityStatus")
    public Integer nofityStatus;

    @ApiModelProperty(value = "用户ID")
    @JsonProperty(value = "userId")
    public Long userId;

    @ApiModelProperty(value = "抖音订单ID")
    @JsonProperty(value = "douyinOrderId")
    public String douyinOrderId;

    @ApiModelProperty(value = "抖音订单token")
    @JsonProperty(value = "douyinOrderToken")
    public String douyinOrderToken;

    @ApiModelProperty(value = "抖音callback")
    @JsonProperty(value = "douyinCallback")
    public String douyinCallback;

    @ApiModelProperty(value = "支付流水线号")
    @JsonProperty(value = "puchasedNo")
    public String puchasedNo;

    @ApiModelProperty(value = "支付金额（分）")
    @JsonProperty(value = "puchasedAmount")
    public Long puchasedAmount;

    @ApiModelProperty(value = "支付成功时间")
    @JsonProperty(value = "puchasedTime")
    public Date puchasedTime;

    @ApiModelProperty(value = "过期时间")
    @JsonProperty(value = "expireTime")
    public Date expireTime;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonProperty(value = "createTime")
    public Date createTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @JsonProperty(value = "updateTime")
    public Date updateTime;

    @TableField(exist = false)
    @ApiModelProperty(value = "优惠券")
    @AutoFullBeanSQL(sql = "select * from mas_item where id = {itemId}")
    public MasItem item;

}
