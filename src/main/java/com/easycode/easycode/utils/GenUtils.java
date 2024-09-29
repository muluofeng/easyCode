package com.easycode.easycode.utils;

import com.easycode.easycode.entity.ColumnEntity;
import com.easycode.easycode.entity.TableEntity;
import freemarker.template.TemplateException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 代码生成器 工具类
 */
@Slf4j
public class GenUtils {


    static List<String> walk(String templatePath, String path) throws Exception {
        try (Stream<Path> paths = Files.walk(Paths.get(path))) {
            return paths.filter(Files::isRegularFile).map(f -> {
                        String filePathName = f.toString();
                        return  StringUtils.substring(filePathName, filePathName.indexOf(path) + path.length()+1);
                    })
                    .collect(Collectors.toList());
        }
    }

    @SneakyThrows
    public static List<String> getTemplates(boolean onlyBackend, boolean generatorServiceInterface) {
        String templatePath = "template";
        Resource resource = new ClassPathResource(templatePath);
        File file = resource.getFile();
        boolean directory = file.isDirectory();
        if (directory) {
            List<String> templates = walk(templatePath, file.getPath());
            if (!generatorServiceInterface) {
                templates.removeIf(t -> t.contains("backend/ServiceImpl.java.ftl"));
            }
            if (onlyBackend) {
                templates.removeIf(t -> t.startsWith("front"));
            }
            return templates;
        }
        return Lists.newArrayList();
    }

    /**
     * 生成代码
     */
    public static void generatorCode(org.apache.commons.configuration.Configuration config,
            Map<String, String> table,
            List<Map<String, String>> columns,
            ZipArchiveOutputStream zip) throws IOException, TemplateException {
        //是否 输出代码到目录
        boolean isToDirect = zip == null;
        Boolean hasBigDecimal = false;
        // 表信息
        TableEntity tableEntity = new TableEntity();
        tableEntity.setTableName(table.get("tableName"));
        tableEntity.setComments(table.get("tableComment"));
        // 表名转换成Java类名
        String className = tableToJava(tableEntity.getTableName(), config.getStringArray("tablePrefix"));
        tableEntity.setClassName(className);
        tableEntity.setClassLowerName(StringUtils.uncapitalize(className));

        // 列信息
        List<ColumnEntity> columsList = new ArrayList<>();
        for (Map<String, String> column : columns) {
            ColumnEntity columnEntity = new ColumnEntity();
            columnEntity.setColumnName(column.get("columnName"));
            columnEntity.setDataType(column.get("dataType"));
            columnEntity.setComments(column.get("columnComment"));
            columnEntity.setExtra(column.get("extra"));

            // 列名转换成Java属性名
            String attrName = columnToJava(columnEntity.getColumnName());
            columnEntity.setAttrName(attrName);
            columnEntity.setAttrLowerName(StringUtils.uncapitalize(attrName));

            // 列的数据类型，转换成Java类型
            String attrType = config.getString(columnEntity.getDataType(), "unknowType");
            columnEntity.setAttrType(attrType);
            if (!hasBigDecimal && attrType.equals("BigDecimal")) {
                hasBigDecimal = true;
            }
            // 是否主键
            if ("PRI".equalsIgnoreCase(column.get("columnKey")) && tableEntity.getPk() == null) {
                tableEntity.setPk(columnEntity);
            }

            columsList.add(columnEntity);
        }
        tableEntity.setColumns(columsList);

        // 没主键，则第一个字段为主键
        if (tableEntity.getPk() == null) {
            tableEntity.setPk(tableEntity.getColumns().get(0));
        }

        // 封装模板数据
        Map<String, Object> map = new HashMap<>();
        map.put("tableName", tableEntity.getTableName());
        map.put("comments", tableEntity.getComments());
        map.put("pk", tableEntity.getPk());
        map.put("className", tableEntity.getClassName());
        map.put("classLowerName", tableEntity.getClassLowerName());
        map.put("pathName", tableEntity.getClassLowerName().toLowerCase());
        map.put("columns", tableEntity.getColumns());
        map.put("hasBigDecimal", hasBigDecimal);
        map.put("package", config.getString("package"));
        map.put("moduleName", config.getString("moduleName"));
        map.put("author", config.getString("author"));
        map.put("email", config.getString("email"));
        map.put("datetime", DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));

        //实体是否开启lombok 注解
        boolean openLombok = config.getBoolean("openLombok");
        //使用swagger
        String openDoc = config.getString("openDoc");
        boolean openShiro = config.getBoolean("open-controller-shiro-RequiresPermissions");
        //前端忽略大小写
        boolean openFrontLowercase = config.getBoolean("openFrontLowercase");
        //后端文件输出路径
        String generatorBackendPath = config.getString("generatorBackendPath");
        //前端文件输出目录
        String generatorFrontPath = config.getString("generatorFrontPath");
        //是否生成service  interface
        boolean generatorServiceInterface = config.getBoolean("serviceInterface");
        //是否生成
        boolean onlyBackend = config.getBoolean("onlyBackend");

        map.put("openLombok", openLombok);
        map.put("generatorServiceInterface", generatorServiceInterface);
        map.put("openDoc", openDoc);
        map.put("openShiro", openShiro);
        map.put("openFrontLowercase", openFrontLowercase);
        map.put("baseResponsePackage", config.getString("base-response-package"));
        map.put("baseResponseClass", config.getString("base-response-class"));
        map.put("baseResponseClassName", config.getString("base-response-class")
                .substring(config.getString("base-response-class").lastIndexOf(".") + 1));
        map.put("baseResponsePageClass", config.getString("base-response-page-class"));
        map.put("baseResponsePageClassName", config.getString("base-response-page-class")
                .substring(config.getString("base-response-page-class").lastIndexOf(".") + 1));
        map.put("baseResponseClassSuccessMethod", config.getString("base-response-class-success-method"));
        map.put("isSpringboot3", config.getString("isSpringboot3"));

        freemarker.template.Configuration configuration = new freemarker.template.Configuration();
        configuration.setClassForTemplateLoading(GenUtils.class, "/template");


        // 获取模板列表
        List<String> templates = getTemplates(onlyBackend, generatorServiceInterface);
        for (String template : templates) {
            // 渲染模板
            try (StringWriter sw = new StringWriter()) {
                // vue 采用freemarker 替换
                freemarker.template.Template tp = configuration.getTemplate(template, "UTF-8");
                tp.process(map, sw);

                String fileName = getFileName(template, tableEntity.getClassName(), config.getString("package"), config.getString("moduleName"), openFrontLowercase);

                File file = new File(fileName);


                if (template.endsWith("sql.ftl")) {
                    //todo 执行sql
                    continue;
                }

                Boolean isBackend = isBackendTemplate(template);

                if (isToDirect) {
                    //输出到目录
                    String path = isBackend ? generatorBackendPath : generatorFrontPath;
                    generatorFileToDir(path, fileName, sw.toString());
                } else {
                    zip.putArchiveEntry(new ZipArchiveEntry(file, fileName));
                    IOUtils.write(sw.toString(), zip, "UTF-8");
                }
            } catch (IOException e) {
                log.error("", e);
                throw new RRException("渲染模板失败，表名：" + tableEntity.getTableName(), e);
            } catch (TemplateException e) {
                log.error("", e);
            }
        }
    }

    /**
     * 是否后端文件
     *
     * @param template
     * @return
     */
    private static Boolean isBackendTemplate(String template) {
        return template.contains("java.ftl") || template.contains("Dao.xml.ftl");
    }

    /**
     * 输出文件到目录
     *
     * @param basePath
     * @param filename
     * @param outPutData
     */
    private static void generatorFileToDir(String basePath, String filename, String outPutData) {
        String path = filename.substring(0, filename.lastIndexOf("/"));
        String dirPath = basePath + path;
        File file = new File(dirPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        String filePath = basePath + filename;
        System.out.println(filePath);
        File file1 = new File(filePath);
        if (!file1.exists()) {
            try {
                file1.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file1);
                IOUtils.write(outPutData, fileOutputStream, "UTF-8");
            } catch (IOException e) {
                log.error("", e);
            }
        }

    }

    /**
     * 列名转换成Java属性名
     */
    public static String columnToJava(String columnName) {
        return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "");
    }

    /**
     * 表名转换成Java类名
     */
    public static String tableToJava(String tableName, String[] prefixs) {
        if (prefixs != null && prefixs.length > 0) {
            int i = tableName.indexOf("_");
            if (i != -1) {
                String thisPrefix = tableName.substring(0, i + 1);
                boolean havePrefix = Arrays.stream(prefixs).anyMatch((prefix) -> thisPrefix.equals(prefix));
                if (havePrefix) {
                    tableName = tableName.substring(i + 1, tableName.length());
                }
            }
        }
        return columnToJava(tableName);
    }

    /**
     * 获取配置信息
     */
    public static Configuration getConfig() {
        try {
            return new PropertiesConfiguration("generator.properties");
        } catch (ConfigurationException e) {
            throw new RRException("获取配置文件失败，", e);
        }
    }

    /**
     * 获取文件名
     */
    public static String getFileName(String template, String className, String packageName, String moduleName, boolean openFrontLowercase) {
        String packagePath = "src" + File.separator + "main" + File.separator + "java" + File.separator;
        if (StringUtils.isNotBlank(packageName)) {
            packagePath += packageName.replace(".", File.separator) + File.separator + moduleName + File.separator;
        }

        if (template.contains("Entity.java.ftl")) {
            return packagePath + "entity" + File.separator + className + ".java";
        }

        if (template.contains("Dao.java.ftl")) {
            return packagePath + "Mapper" + File.separator + className + "Mapper.java";
        }

        if (template.contains("Service.java.ftl")) {
            return packagePath + "service" + File.separator + className + "Service.java";
        }

        if (template.contains("ServiceImpl.java.ftl")) {
            return packagePath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
        }

        if (template.contains("Controller.java.ftl")) {
            return packagePath + "controller" + File.separator + className + "Controller.java";
        }

        if (template.contains("Dao.xml.ftl")) {
            return "src" + File.separator + "main" + File.separator + "resources" + File.separator + "mapper" + File.separator + moduleName + File.separator + className + "Mapper.xml";
        }

        if (template.contains("CreateUpdateDTO.java.ftl")) {
            return packagePath + "dto" + File.separator + "CreateUpdate" + className + "DTO.java";
        }
        if (template.contains("InfoDTO.java.ftl")) {
            return packagePath + "dto" + File.separator + className + "InfoDTO.java";
        }
        if (template.contains("ListDTO.java.ftl")) {
            return packagePath + "dto" + File.separator + className + "ListDTO.java";
        }
        if (template.contains("PageReqDTO.java.ftl")) {
            return packagePath + "dto" + File.separator + className + "PageReqDTO.java";
        }
        if (template.contains("Convert.java.ftl")) {
            return packagePath + "convert" + File.separator + className + "Convert.java";
        }


        if (template.contains("menu.sql.ftl")) {
            return className.toLowerCase() + "_menu.sql";
        }

        String dirName = className;
        if (openFrontLowercase) {
            dirName = StringUtils.lowerCase(className);
        }

        if (template.contains("index.vue.ftl")) {
            return "src" + File.separator + "view" + File.separator + moduleName + File.separator + dirName + File.separator + "index.vue";
        }
        if (template.contains("edit.vue.ftl")) {
            return "src" + File.separator + "view" + File.separator + moduleName + File.separator + dirName + File.separator + "edit.vue";
        }
        if (template.contains("api.js.ftl")) {
            return "src" + File.separator + "api" + File.separator + dirName + ".js";
        }
        return null;
    }

    /**
     * 生成代码到目录
     *
     * @param configuration
     * @param table
     * @param columns
     */
    public static void generatorCodeToDirect(Configuration configuration, Map<String, String> table, List<Map<String, String>> columns) {
        try {
            generatorCode(configuration, table, columns, null);
        } catch (IOException | TemplateException e) {
            log.error("", e);
        }
    }
}
