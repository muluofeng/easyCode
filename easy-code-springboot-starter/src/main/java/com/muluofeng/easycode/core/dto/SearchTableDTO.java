package com.muluofeng.easycode.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author xiexingxing
 * @date 2024/10/14 11:45
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchTableDTO {
    // 当前页码
    private Integer page = 1;
    private Long offset;
    // 每页条数
    private Integer limit = 10;
    private String dataSource;
    private String tableName;

    public Long getOffset() {
        return (long) (page - 1) * limit;
    }


}
