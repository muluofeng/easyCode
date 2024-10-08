package ${package}.${moduleName}.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.IdType;
<#if hasBigDecimal>
import java.math.BigDecimal;
</#if>
import java.io.Serializable;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.LocalDate;
<#if openLombok>
import lombok.Data;
</#if>
<#if openDoc=='swagger'>
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
</#if>
<#if openDoc=='openApi'>
import io.swagger.v3.oas.annotations.media.Schema;
</#if>

/**
 * ${comments}
 * 
 * @author ${author}
 * @email ${email}
 * @date ${datetime}
 */
<#if openLombok>
@Data
</#if>
<#if openDoc=='swagger'>
@ApiModel
</#if>
<#if openDoc=='openApi'>
@Schema
</#if>
@TableName("${tableName}")
public class ${className} implements Serializable {
	private static final long serialVersionUID = 1L;

<#list columns as column>

	/**
	 * ${column.comments}
	 */
<#if column.columnName == pk.columnName>
	@TableId<#if column.extra?has_content &&  column.extra== 'auto_increment'>(type = IdType.AUTO)</#if>
<#else>
	@TableField(value = "${column.columnName}")
</#if>
<#if openDoc=='swagger'>
	@ApiModelProperty("${column.comments}")
</#if>
<#if openDoc=='openApi'>
	@Schema(description = "${column.comments}")
</#if>
	private ${column.attrType} ${column.attrLowerName};
</#list>


<#if openLombok>

<#else>
	<#list columns as column>

	/**
	 * 设置：${column.comments}
	 */
	public void set${column.attrName}(${column.attrType} ${column.attrLowerName}) {
		this.${column.attrLowerName} = ${column.attrLowerName};
	}
	/**
	 * 获取：${column.comments}
	 */
	public ${column.attrType}get${column.attrName}() {
		return ${column.attrLowerName};
	}

	</#list>
</#if>

}
