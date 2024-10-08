<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="${package}.${moduleName}.${daoLowerSuffix}.${className}${daoSuffix}">


    <resultMap type="${package}.${moduleName}.entity.${className}" id="${classLowerName}Map">
    <#list columns as column>
            <result property="${column.attrLowerName}" column="${column.columnName}"/>
    </#list>
    </resultMap>
    <#if generatorController>
    <select id="queryByPage" resultType="${package}.${moduleName}.dto.${className}ListDTO">
        SELECT
         *
        FROM ${tableName}
    </select>
    </#if>
</mapper>
