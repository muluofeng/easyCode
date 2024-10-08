package ${package}.${moduleName}.${daoLowerSuffix};

import ${package}.${moduleName}.entity.${className};

<#if generatorController>
import  ${package}.${moduleName}.dto.${className}ListDTO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
</#if>
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
/**
 * ${comments}
 * 
 * @author ${author}
 * @email ${email}
 * @date ${datetime}
 */
public interface ${className}${daoSuffix} extends BaseMapper<${className}> {
<#if generatorController>
    Page<${className}ListDTO> queryByPage(Page<?> page);
</#if>
}
