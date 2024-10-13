package com.house.health.entity;

import lombok.Data;

/**
 * @autor zhenrenwu
 * @date 2024/10/13 12:43 AM
 */
@Data
public class FindReq {

    private String keywords;
    private String orderBy;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
    private Integer offset;
}
