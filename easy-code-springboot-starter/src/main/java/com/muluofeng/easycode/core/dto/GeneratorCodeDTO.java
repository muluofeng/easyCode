package com.muluofeng.easycode.core.dto;

import lombok.Data;

import java.util.List;

/**
 * @author xiexingxing
 * @date 2024/10/14 13:03
 */
@Data
public class GeneratorCodeDTO {
    private List<String> tableNames;
    private String dataSource;
}
