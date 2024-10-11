package com.muluofeng.easycode.core.controller;

import cn.hutool.json.JSONUtil;
import com.muluofeng.easycode.core.service.SysGeneratorService;
import com.muluofeng.easycode.core.utils.PageUtils;
import com.muluofeng.easycode.core.utils.Query;
import com.muluofeng.easycode.core.utils.R;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
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
@RequestMapping("/generator")
@RequiredArgsConstructor
public class SysGeneratorController {
    @Resource
    org.apache.commons.configuration.Configuration easyCodePropertiesConfiguration;
    @Autowired
    private SysGeneratorService sysGeneratorService;
    private final ResourceLoader resourceLoader;

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
                easyCodePropertiesConfiguration.setProperty(key, value);
            }
        }
        return R.ok(200, "保存成功");
    }

    @GetMapping("/getconfig")
    @ResponseBody
    public R getconfig() {
        Map map = new HashMap();
        map.put("generatorBackendPath", easyCodePropertiesConfiguration.getString("generatorBackendPath"));
        map.put("author", easyCodePropertiesConfiguration.getString("author"));
        map.put("email", easyCodePropertiesConfiguration.getString("email"));
        map.put("tablePrefix", Arrays.stream(easyCodePropertiesConfiguration.getStringArray("tablePrefix")).collect(Collectors.joining(",")));
        map.put("openLombok", easyCodePropertiesConfiguration.getBoolean("openLombok"));
        map.put("openDoc", easyCodePropertiesConfiguration.getString("openDoc"));
        map.put("serviceInterface", easyCodePropertiesConfiguration.getBoolean("serviceInterface"));
        map.put("onlyBackend", easyCodePropertiesConfiguration.getBoolean("onlyBackend"));
        map.put("openFrontLowercase", easyCodePropertiesConfiguration.getBoolean("openFrontLowercase"));
        map.put("package", easyCodePropertiesConfiguration.getString("package"));
        map.put("generatorFrontPath", easyCodePropertiesConfiguration.getString("generatorFrontPath"));
        return R.ok(200, "操作成功").put("config", map);
    }
}
