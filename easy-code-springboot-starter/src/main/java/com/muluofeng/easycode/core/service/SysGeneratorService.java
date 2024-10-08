package com.muluofeng.easycode.core.service;

import com.muluofeng.easycode.core.dao.SysGeneratorDao;
import com.muluofeng.easycode.core.utils.GenUtils;
import freemarker.template.TemplateException;
import jakarta.annotation.Resource;
import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 代码生成器
 */
@Service
public class SysGeneratorService {
    @Resource
    org.apache.commons.configuration.Configuration easyCodePropertiesConfiguration;
    @Autowired
    private SysGeneratorDao sysGeneratorDao;

    public List<Map<String, Object>> queryList(Map<String, Object> map) {
        return sysGeneratorDao.queryList(map);
    }

    public int queryTotal(Map<String, Object> map) {
        return sysGeneratorDao.queryTotal(map);
    }

    public Map<String, String> queryTable(String tableName) {
        return sysGeneratorDao.queryTable(tableName);
    }

    public List<Map<String, String>> queryColumns(String tableName) {
        return sysGeneratorDao.queryColumns(tableName);
    }

    public byte[] generatorCode(String[] tableNames) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             ZipArchiveOutputStream zip = new ZipArchiveOutputStream(outputStream)) {
            zip.setUseZip64(Zip64Mode.AsNeeded);

            for (String tableName : tableNames) {
                // 查询表信息
                Map<String, String> table = queryTable(tableName);
                // 查询列信息
                List<Map<String, String>> columns = queryColumns(tableName);
                // 生成代码
                try {
                    GenUtils.generatorCode(easyCodePropertiesConfiguration,table, columns, zip);
                } catch (TemplateException e) {
                    e.printStackTrace();
                }
            }

            zip.closeArchiveEntry();
            zip.finish();
            zip.close();

            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成代码到目录
     * @param tableNames
     */
    public void codeToDirect(String[] tableNames) {
        for (String tableName : tableNames) {
            // 查询表信息
            Map<String, String> table = queryTable(tableName);
            // 查询列信息
            List<Map<String, String>> columns = queryColumns(tableName);
            // 生成代码
            GenUtils.generatorCodeToDirect(easyCodePropertiesConfiguration,table, columns);
        }

    }
}
