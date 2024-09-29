package com.easycode.easycode.controller;

import cn.hutool.json.JSONUtil;
import com.easycode.easycode.service.SysGeneratorService;
import com.easycode.easycode.utils.PageUtils;
import com.easycode.easycode.utils.Query;
import com.easycode.easycode.utils.R;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 代码生成器
 */
@Controller
@RequestMapping("/sys/generator")
public class SysGeneratorController {
    @Autowired
    @Qualifier(value = "propertiesConfiguration")
    org.apache.commons.configuration.Configuration configuration;
    @Autowired
    private SysGeneratorService sysGeneratorService;

    /**
     * 列表
     */
    @ResponseBody
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        // 查询列表数据
        Query query = new Query(params);
        List<Map<String, Object>> list = sysGeneratorService.queryList(query);
        int total = sysGeneratorService.queryTotal(query);

        PageUtils pageUtil = new PageUtils(list, total, query.getLimit(), query.getPage());

        return R.ok().put("page", pageUtil);
    }

    /**
     * 生成代码
     */
    @RequestMapping("/code")
    public void code(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String[] tableNames = new String[]{};
        String tables = request.getParameter("tables");
        tableNames = JSONUtil.parseArray(tables).toArray(tableNames);

        byte[] data = sysGeneratorService.generatorCode(tableNames);

        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"framework.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        IOUtils.write(data, response.getOutputStream());
    }


    /**
     * 生成代码 到目录
     */
    @PostMapping("/codeToDirect")
    @ResponseBody
    public R codeToDirect(HttpServletRequest request, @RequestBody List<String> tableNames) throws IOException {
        String[] tableArrays = tableNames.toArray(new String[tableNames.size()]);
        sysGeneratorService.codeToDirect(tableArrays);
        return R.ok(200, "操作成功");
    }


    @PostMapping("/config")
    @ResponseBody
    public R config(@RequestBody Map<String, Object> config) {
        for (Map.Entry<String, Object> entry : config.entrySet()) {
            Object value = entry.getValue();
            String key = entry.getKey();
            if (value != null) {
                configuration.setProperty(key, value);
            }
        }
        return R.ok(200, "保存成功");
    }

    @GetMapping("/getconfig")
    @ResponseBody
    public R getconfig() {
        Map map = new HashMap();
        map.put("generatorBackendPath", configuration.getString("generatorBackendPath"));
        map.put("author", configuration.getString("author"));
        map.put("email", configuration.getString("email"));
        map.put("tablePrefix", Arrays.stream(configuration.getStringArray("tablePrefix")).collect(Collectors.joining(",")));
        map.put("openLombok", configuration.getBoolean("openLombok"));
        map.put("openDoc", configuration.getString("openDoc"));
        map.put("serviceInterface", configuration.getBoolean("serviceInterface"));
        map.put("onlyBackend", configuration.getBoolean("onlyBackend"));
        map.put("openFrontLowercase", configuration.getBoolean("openFrontLowercase"));
        map.put("package", configuration.getString("package"));
        map.put("generatorFrontPath", configuration.getString("generatorFrontPath"));
        return R.ok(200, "操作成功").put("config", map);
    }
}
