package ${package}.${moduleName}.dto;


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
public class ${className}PageReqDTO implements Serializable {

}
