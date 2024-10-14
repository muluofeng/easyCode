package com.muluofeng.easycode.core.controller;

import com.muluofeng.easycode.core.dto.GeneratorCodeDTO;
import com.muluofeng.easycode.core.dto.SearchTableDTO;
import com.muluofeng.easycode.core.service.SysGeneratorService;
import com.muluofeng.easycode.core.utils.PageUtils;
import com.muluofeng.easycode.core.utils.R;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 代码生成器
 */
@RestController
@RequestMapping("/generator")
@RequiredArgsConstructor
public class SysGeneratorController {
    @Resource
    org.apache.commons.configuration.Configuration easyCodePropertiesConfiguration;
    @Autowired
    private SysGeneratorService sysGeneratorService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list( SearchTableDTO params) {
        // 查询列表数据
        List<Map<String, Object>> list = sysGeneratorService.queryList(params);
        int total = sysGeneratorService.queryTotal(params);

        PageUtils pageUtil = new PageUtils(list, total, params.getLimit(), params.getPage());

        return R.ok().put("page", pageUtil);
    }


    /**
     * 生成代码 到目录
     */
    @PostMapping("/codeToDirect")
    public R codeToDirect( @RequestBody GeneratorCodeDTO req) throws IOException {
        sysGeneratorService.codeToDirect(req);
        return R.ok(200, "操作成功");
    }


    @PostMapping("/config")
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
    public R getConfig() {
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

    @GetMapping("/dataSources")
    public R dataSources() {
        return R.ok().put("dataSources", sysGeneratorService.getDataSources());
    }
}
