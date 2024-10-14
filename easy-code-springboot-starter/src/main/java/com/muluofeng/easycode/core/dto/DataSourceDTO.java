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
public class DataSourceDTO {
    private String sourceKey;
    private String sourceName;
}
