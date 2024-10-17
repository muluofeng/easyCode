package com.muluofeng.easycode.core.service;

import com.muluofeng.easycode.core.config.DataSourceHandle;
import com.muluofeng.easycode.core.dao.SysGeneratorDao;
import com.muluofeng.easycode.core.dto.DataSourceDTO;
import com.muluofeng.easycode.core.dto.GeneratorCodeDTO;
import com.muluofeng.easycode.core.dto.SearchTableDTO;
import com.muluofeng.easycode.core.utils.GenUtils;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    private DataSourceHandle dataSourceHandle;


    public List<Map<String, Object>> queryList(SearchTableDTO req) {
        String dataSourceKey = req.getDataSource();
        changeDataSource(dataSourceKey);
        List<Map<String, Object>> res = sysGeneratorDao.queryList(req);
        changeAfterDataSource(dataSourceKey);
        return res;
    }


    public int queryTotal(SearchTableDTO req) {
        String dataSourceKey = req.getDataSource();
        changeDataSource(dataSourceKey);
        int res = sysGeneratorDao.queryTotal(req);
        changeAfterDataSource(dataSourceKey);
        return res;
    }

    public Map<String, String> queryTable(String tableName, String dataSourceKey) {
        changeDataSource(dataSourceKey);
        Map<String, String> res = sysGeneratorDao.queryTable(tableName);
        changeAfterDataSource(dataSourceKey);
        return res;
    }

    public List<Map<String, String>> queryColumns(String tableName, String dataSourceKey) {
        changeDataSource(dataSourceKey);
        List<Map<String, String>> res = sysGeneratorDao.queryColumns(tableName);
        changeAfterDataSource(dataSourceKey);
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
     *
     * @param req
     */
    public void codeToDirect(GeneratorCodeDTO req) {
        List<String> tableNames = req.getTableNames();
        String dataSourceKey = req.getDataSource();
        for (String tableName : tableNames) {
            // 查询表信息
            Map<String, String> table = queryTable(tableName, dataSourceKey);
            // 查询列信息
            List<Map<String, String>> columns = queryColumns(tableName, dataSourceKey);
            // 生成代码
            GenUtils.generatorCodeToDirect(easyCodePropertiesConfiguration, table, columns);
        }

    }

    public List<DataSourceDTO> getDataSources() {
        return dataSourceHandle.dynamicDataSources();
    }

    @SneakyThrows
    public void changeDataSource(String dataSourceKey) {
        dataSourceHandle.changeDataSource(dataSourceKey);
    }

    @SneakyThrows
    public void changeAfterDataSource(String dataSourceKey) {
        dataSourceHandle.changeAfterDataSource(dataSourceKey);

    }
}
