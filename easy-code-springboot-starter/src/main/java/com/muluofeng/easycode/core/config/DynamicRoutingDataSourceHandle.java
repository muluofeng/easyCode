package com.muluofeng.easycode.core.config;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.muluofeng.easycode.core.dto.DataSourceDTO;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * @author xiexingxing
 */

public class DynamicRoutingDataSourceHandle implements DataSourceHandle {

    private final List<DataSourceDTO> dataSourceDTOS;
    private final List<String> dynamicDataSourceKeys;

    public DynamicRoutingDataSourceHandle(DynamicRoutingDataSource dataSource) {
        Map<String, DataSource> dataSources = dataSource.getDataSources();
        this.dataSourceDTOS = dataSources.keySet().stream()
                .map(key -> DataSourceDTO.builder().sourceKey(key).sourceName(key + "数据源").build()).toList();
        dynamicDataSourceKeys = this.dataSourceDTOS.stream().map(DataSourceDTO::getSourceKey).toList();
    }

    @Override
    public List<DataSourceDTO> dynamicDataSources() {
        return dataSourceDTOS;
    }

    @Override
    public void changeAfterDataSource(String dataSourceKey) {
        if (CollUtil.isNotEmpty(dynamicDataSourceKeys) && dynamicDataSourceKeys.contains(dataSourceKey)) {
            DynamicDataSourceContextHolder.poll();
        }
    }

    @Override
    public void changeDataSource(String dataSourceKey) {
        if (CollUtil.isNotEmpty(dynamicDataSourceKeys) && dynamicDataSourceKeys.contains(dataSourceKey)) {
            DynamicDataSourceContextHolder.push(dataSourceKey);
        }
    }
}
