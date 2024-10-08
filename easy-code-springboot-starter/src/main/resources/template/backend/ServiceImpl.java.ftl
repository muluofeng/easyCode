package ${package}.${moduleName}.service.impl;

import org.springframework.stereotype.Service;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import ${package}.${moduleName}.${daoLowerSuffix}.${className}${daoSuffix};
import ${package}.${moduleName}.entity.${className};
import ${package}.${moduleName}.service.${className}Service;
import ${package}.${moduleName}.convert.${className}Convert;
import ${package}.${moduleName}.dto.${className}ListDTO;
import ${package}.${moduleName}.dto.${className}InfoDTO;
import ${package}.${moduleName}.dto.${className}PageReqDTO;
import ${package}.${moduleName}.dto.CreateUpdate${className}DTO;
import ${baseResponsePageClass};
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class ${className}ServiceImpl extends ServiceImpl<${className}${daoSuffix}, ${className}> implements ${className}Service {

    @Autowired
    private ${className}Convert convert;

<#if generatorController>
    @Override
    public ${baseResponsePageClassName}<${className}ListDTO> queryByPage(${className}PageReqDTO pageReq) {
        Page<${className}ListDTO> page = new Page<>(pageReq.getCurrentPage(), pageReq.getPageSize());
        Page<${className}ListDTO> dataPage = baseMapper.queryByPage(page);
        return ${baseResponsePageClassName}.fromPage(dataPage);
    }

    @Override
    public ${pk.attrType}  create(CreateUpdate${className}DTO reqDTO){
        ${className} ${classLowerName} =  convert.toEntity(reqDTO);
        save(${classLowerName});
        return ${classLowerName}.get${pk.attrLowerName.substring(0, 1).toUpperCase()}${pk.attrLowerName.substring(1)}();
    }
    
    @Override
    public ${pk.attrType} update(CreateUpdate${className}DTO reqDTO){
        ${className} ${classLowerName} =  convert.toEntity(reqDTO);
        updateById(${classLowerName});
        return ${classLowerName}.get${pk.attrLowerName.substring(0, 1).toUpperCase()}${pk.attrLowerName.substring(1)}();
    }
 
    @Override
    public ${className}InfoDTO info(${pk.attrType} id){
        ${className} ${classLowerName} =  getById(id);
        ${className}InfoDTO result =  convert.toInfoDTO(${classLowerName});
        return result;
    }

    @Override
    public void batchDelete(List<${pk.attrType}> ids){
        removeByIds(ids);
    }
</#if>
}
