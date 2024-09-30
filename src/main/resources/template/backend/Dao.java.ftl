package ${package}.${moduleName}.${daoLowerSuffix};

import ${package}.${moduleName}.entity.${className};
import  ${package}.${moduleName}.dto.${className}ListDTO;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
/**
 * ${comments}
 * 
 * @author ${author}
 * @email ${email}
 * @date ${datetime}
 */
public interface ${className}${daoSuffix} extends BaseMapper<${className}> {
    Page<${className}ListDTO> queryByPage(Page<?> page);
}
