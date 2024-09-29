package ${package}.${moduleName}.controller;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.validation.annotation.Validated;
<#if openShiro>
import org.apache.shiro.authz.annotation.RequiresPermissions;
</#if>
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ${package}.${moduleName}.entity.${className};
import ${package}.${moduleName}.service.${className}Service;
import ${package}.${moduleName}.entity.${className};
import ${package}.${moduleName}.dto.CreateUpdate${className}DTO;
import ${package}.${moduleName}.dto.${className}ListDTO;
import ${package}.${moduleName}.dto.${className}InfoDTO;
import ${package}.${moduleName}.dto.${className}PageReqDTO;


import ${baseResponseClass};
import ${baseResponsePageClass};



<#if openDoc=='swagger'>
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
</#if>
<#if openDoc=='openApi'>
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
</#if>

/**
 * ${comments}
 *
 * @author ${author}
 * @email ${email}
 * @date ${datetime}
 */

<#if openDoc=='swagger'>@Api(tags = "${comments}")</#if>
<#if openDoc=='openApi'>@Tag(name = "${comments}")</#if>
@RestController
@RequestMapping("${moduleName}/${pathName}")
public class ${className}Controller {
    @Autowired
    private ${className}Service ${classLowerName}Service;

    /**
     * 列表
     */
    @PostMapping("/queryPage")
<#if openShiro>
    @RequiresPermissions("${moduleName}:${pathName}:list")
</#if>
<#if openDoc=='swagger'>
    @ApiOperation("列表")
</#if>
<#if openDoc=='openApi'>
    @Operation(summary = "列表")
</#if>
    public  ${baseResponseClassName}<${baseResponsePageClassName}<${className}ListDTO>> queryPage(@Validated @RequestBody ${className}PageReqDTO pageReq){
        ${baseResponsePageClassName}<${className}ListDTO> iPage = ${classLowerName}Service.queryByPage(pageReq);
        return ${baseResponseClassName}.${baseResponseClassSuccessMethod}(iPage);
    }



    /**
     * 详情
     */
    @GetMapping("/info/{${pk.attrLowerName}}")
<#if openDoc=='swagger'>
    @ApiOperation("详情")
</#if>
<#if openDoc=='openApi'>
    @Operation(summary = "详情")
</#if>
    public ${baseResponseClassName}<${className}InfoDTO> info(@PathVariable("${pk.attrLowerName}") ${pk.attrType} ${pk.attrLowerName}){
        ${className}InfoDTO ${classLowerName} = ${classLowerName}Service.info(${pk.attrLowerName});
        return ${baseResponseClassName}.${baseResponseClassSuccessMethod}(${classLowerName});
    }

    /**
     * 创建
     */
    @PostMapping("/create")
<#if openShiro>
    @RequiresPermissions("${moduleName}:${pathName}:create")
</#if>
<#if openDoc=='swagger'>
    @ApiOperation("创建")
</#if>
<#if openDoc=='openApi'>
    @Operation(summary = "创建")
</#if>
    public ${baseResponseClassName}<${pk.attrType}> create(@Validated @RequestBody CreateUpdate${className}DTO ${classLowerName}){
        ${pk.attrType}  id = ${classLowerName}Service.create(${classLowerName});
        return ${baseResponseClassName}.${baseResponseClassSuccessMethod}(id);
    }

    /**
     * 修改
     */
    @PostMapping("/update")
<#if openShiro>
    @RequiresPermissions("${moduleName}:${pathName}:update")
</#if>
<#if openDoc=='swagger'>
    @ApiOperation("修改")
</#if>
<#if openDoc=='openApi'>
    @Operation(summary = "修改")
</#if>
    public ${baseResponseClassName}<${pk.attrType}> update(@Validated @RequestBody CreateUpdate${className}DTO ${classLowerName}){
        ${pk.attrType}  id = ${classLowerName}Service.update(${classLowerName});
        return ${baseResponseClassName}.${baseResponseClassSuccessMethod}(id);
    }

    /**
     * 删除
     */
    @PostMapping("/batchDelete")
<#if openShiro>
    @RequiresPermissions("${moduleName}:${pathName}:batchDelete")
</#if>
<#if openDoc=='swagger'>
    @ApiOperation("批量删除")
</#if>
<#if openDoc=='openApi'>
    @Operation(summary = "批量删除")
</#if>
    public ${baseResponseClassName}<Void> batchDelete(@RequestBody ${pk.attrType}[] ${pk.attrLowerName}s){
        ${classLowerName}Service.batchDelete(new ArrayList<>(Arrays.asList(${pk.attrLowerName}s)));
        return ${baseResponseClassName}.${baseResponseClassSuccessMethod}();
    }

}
