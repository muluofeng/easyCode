package ${package}.${moduleName}.convert;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ${package}.${moduleName}.entity.${className};
import ${package}.${moduleName}.dto.CreateUpdate${className}DTO;
import ${package}.${moduleName}.dto.${className}ListDTO;
import ${package}.${moduleName}.dto.${className}InfoDTO;

/**
 * ${comments}
 *
 * @author ${author}
 * @email ${email}
 * @date ${datetime}
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface  ${className}Convert  {

    ${className} toEntity(CreateUpdate${className}DTO dto);

    ${className}InfoDTO toInfoDTO(${className} entity);

    ${className}ListDTO toDTO(${className} entity);

    List<${className}ListDTO> toDTOList(List<${className}> entities);

}
