package com.muluofeng.easycode.core.config;

import com.muluofeng.easycode.core.dto.DataSourceDTO;

import java.util.List;

/**
 * @author xiexingxing
 * @date 2024/10/17 14:00
 */

public interface DataSourceHandle {
    String masterSourceKey = "master";

    List<DataSourceDTO> dynamicDataSources();

    void changeAfterDataSource(String dataSourceKey);

    void changeDataSource(String dataSourceKey);
}
