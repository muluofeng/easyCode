package com.muluofeng.easycode.core.service;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.muluofeng.easycode.core.dao.SysGeneratorDao;
import com.muluofeng.easycode.core.dto.DataSourceDTO;
import com.muluofeng.easycode.core.dto.GeneratorCodeDTO;
import com.muluofeng.easycode.core.dto.SearchTableDTO;
import com.muluofeng.easycode.core.utils.GenUtils;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * 代码生成器
 */
@Slf4j
@Service
public class SysGeneratorService {
    @Resource
    org.apache.commons.configuration.Configuration easyCodePropertiesConfiguration;
    @Resource
    private SysGeneratorDao sysGeneratorDao;
    @Resource
    private DataSource dataSource;
   public static final  String masterSourceKey = "master";

    public List<Map<String, Object>> queryList(SearchTableDTO req) {
        String dataSourceKey = req.getDataSource();
        if (!masterSourceKey.equals(dataSourceKey)) {
            DynamicDataSourceContextHolder.push(dataSourceKey);
        }
        List<Map<String, Object>> res = sysGeneratorDao.queryList(req);
        DynamicDataSourceContextHolder.poll();
        return res;
    }



    public int queryTotal(SearchTableDTO req) {
        String dataSourceKey = req.getDataSource();
        if (!masterSourceKey.equals(dataSourceKey)) {
            DynamicDataSourceContextHolder.push(dataSourceKey);
        }
        int res = sysGeneratorDao.queryTotal(req);
        DynamicDataSourceContextHolder.poll();
        return res;
    }

    public Map<String, String> queryTable(String tableName, String dataSourceKey) {
        if (!masterSourceKey.equals(dataSourceKey)) {
            DynamicDataSourceContextHolder.push(dataSourceKey);
        }
        Map<String, String> res = sysGeneratorDao.queryTable(tableName);
        DynamicDataSourceContextHolder.poll();
        return res;
    }

    public List<Map<String, String>> queryColumns(String tableName, String dataSourceKey) {
        if(!masterSourceKey.equals(dataSourceKey)){
            DynamicDataSourceContextHolder.push(dataSourceKey);
        }
        List<Map<String, String>> res = sysGeneratorDao.queryColumns(tableName);
        DynamicDataSourceContextHolder.poll();
        return res;
    }

//    public byte[] generatorCode(String[] tableNames) {
//        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//             ZipArchiveOutputStream zip = new ZipArchiveOutputStream(outputStream)) {
//            zip.setUseZip64(Zip64Mode.AsNeeded);
//
//            for (String tableName : tableNames) {
//                // 查询表信息
//                Map<String, String> table = queryTable(tableName);
//                // 查询列信息
//                List<Map<String, String>> columns = queryColumns(tableName);
//                // 生成代码
//                try {
//                    GenUtils.generatorCode(easyCodePropertiesConfiguration,table, columns, zip);
//                } catch (TemplateException e) {
//                    log.error("generatorCode TemplateException error",e);
//                }
//            }
//
//            zip.closeArchiveEntry();
//            zip.finish();
//            zip.close();
//
//            return outputStream.toByteArray();
//        } catch (IOException e) {
//            log.error("generatorCode error",e);
//        }
//        return null;
//    }

    /**
     * 生成代码到目录
     * @param req
     */
    public void codeToDirect(GeneratorCodeDTO req) {
        List<String> tableNames = req.getTableNames();
        String dataSourceKey = req.getDataSource();
        for (String tableName : tableNames) {
            // 查询表信息
            Map<String, String> table = queryTable(tableName,dataSourceKey);
            // 查询列信息
            List<Map<String, String>> columns = queryColumns(tableName,dataSourceKey);
            // 生成代码
            GenUtils.generatorCodeToDirect(easyCodePropertiesConfiguration,table, columns);
        }

    }

    public List<DataSourceDTO> getDataSources() {
        if(dataSource instanceof HikariDataSource){
            DataSourceDTO defaultSource = DataSourceDTO.builder().sourceName("默认数据数据源").sourceKey(masterSourceKey).build();
            return List.of(defaultSource);
        }else if(dataSource instanceof  com.baomidou.dynamic.datasource.DynamicRoutingDataSource ){
            DynamicRoutingDataSource dynamicRoutingDataSource = (DynamicRoutingDataSource) dataSource;
            Map<String, DataSource> dataSources = dynamicRoutingDataSource.getDataSources();
            return  dataSources.entrySet().stream().map(
                    entry-> DataSourceDTO.builder().sourceName(entry.getKey()+"数据源").sourceKey(entry.getKey()).build()
            ).toList();
        }
        return List.of();
    }
}
