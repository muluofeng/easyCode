package com.muluofeng.easycode.core.config;

import com.muluofeng.easycode.core.dto.DataSourceDTO;

import java.util.List;

/**
 * @author xiexingxing
 * @date 2024/10/17 14:11
 */

public class DefaultDataSourceHandle implements DataSourceHandle{


    @Override
    public List<DataSourceDTO> dynamicDataSources() {
        DataSourceDTO defaultSource = DataSourceDTO.builder().sourceName("默认数据数据源").sourceKey(masterSourceKey).build();
        return List.of(defaultSource);
    }

    @Override
    public void changeAfterDataSource(String dataSourceKey) {

    }

    @Override
    public void changeDataSource(String dataSourceKey) {

    }
}
