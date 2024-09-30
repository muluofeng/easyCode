package ${package}.${moduleName}.service;

import java.util.List;
<#if generatorServiceInterface>
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
</#if>
import ${package}.${moduleName}.entity.${className};

import ${package}.${moduleName}.convert.${className}Convert;

<#if generatorController>
import ${package}.${moduleName}.dto.${className}ListDTO;
import ${package}.${moduleName}.dto.${className}InfoDTO;
import ${package}.${moduleName}.dto.${className}PageReqDTO;
import ${package}.${moduleName}.dto.CreateUpdate${className}DTO;
import ${baseResponsePageClass};
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
</#if>

<#if !generatorServiceInterface>
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ${package}.${moduleName}.${daoLowerSuffix}.${className}${daoSuffix};
import org.springframework.beans.factory.annotation.Autowired;

</#if>

/**
 * ${comments}
 *
 * @author ${author}
 * @email ${email}
 * @date ${datetime}
 */
<#if generatorServiceInterface>
public interface ${className}Service extends IService<${className}> {
<#else>
@Service("${classLowerName}Service")
public class ${className}Service extends ServiceImpl<${className}${daoSuffix}, ${className}> {
<#if generatorServiceInterface>
<#else>
    @Autowired
    private ${className}Convert convert;
</#if>
</#if>
<#if generatorController>
    /**
    *  分页查询
    */
<#if generatorServiceInterface >
    public ${baseResponsePageClassName}<${className}ListDTO> queryByPage(${className}PageReqDTO pageReq);
<#else>
    public ${baseResponsePageClassName}<${className}ListDTO> queryByPage(${className}PageReqDTO pageReq) {
        Page<${className}ListDTO> page = new Page<>(pageReq.getCurrentPage(), pageReq.getPageSize());
        Page<${className}ListDTO> dataPage = baseMapper.queryByPage(page);
        return ${baseResponsePageClassName}.of(dataPage);
    }
</#if>
</#if>
<#if generatorController>
    /**
    *  创建
    */
<#if generatorServiceInterface>
    public ${pk.attrType}  create(CreateUpdate${className}DTO reqDTO);
<#else>
    public ${pk.attrType}  create(CreateUpdate${className}DTO reqDTO){
        ${className} ${classLowerName} =  convert.toEntity(reqDTO);
        save(${classLowerName});
        return ${classLowerName}.get${pk.attrLowerName?substring(0, 1)?upper_case}${pk.attrLowerName?substring(1)}();
    }
</#if>
</#if>

<#if generatorController>
   /**
    *  更新
    */
<#if generatorServiceInterface>
    public ${pk.attrType}  update(CreateUpdate${className}DTO reqDTO);
<#else>
    public ${pk.attrType}  update(CreateUpdate${className}DTO reqDTO){
        ${className} ${classLowerName} =  convert.toEntity(reqDTO);
        updateById(${classLowerName});
        return ${classLowerName}.get${pk.attrLowerName?substring(0, 1)?upper_case}${pk.attrLowerName?substring(1)}();
    }
</#if>
</#if>
<#if generatorController>
    /**
    *  详情
    */
<#if generatorServiceInterface>
    public ${className}InfoDTO info(${pk.attrType} id);
<#else>
    public ${className}InfoDTO info(${pk.attrType} id){
        ${className} ${classLowerName} =  getById(id);
        ${className}InfoDTO result =  convert.toInfoDTO(${classLowerName});
        return result;
    }
</#if>
</#if>
<#if generatorController>
    /**
    *  批量删除
    */
<#if generatorServiceInterface>
    public void batchDelete(List<${pk.attrType}> ids);
<#else>
    public void batchDelete(List<${pk.attrType}> ids){
        removeByIds(ids);
    }
</#if>
</#if>
}