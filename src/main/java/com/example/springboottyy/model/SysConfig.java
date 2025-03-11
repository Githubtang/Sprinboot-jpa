package com.example.springboottyy.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "sys_config")
public class SysConfig extends BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    /** 参数名称*/
    @Schema(title = "参数名称")
    @NotBlank(message = "参数名称不能为空")
    @Size(max = 100,message = "参数名称不能超过100个字符")
    private String configName;

    /** 参数键名 */
    @Schema(title = "参数键名")
    @NotBlank(message = "参数键名长度不能为空")
    @Size(max = 100,message = "参数键名长度不能超过100个字符")
    private String configKey;

    /** 参数键值 */
    @Schema(title = "参数键值")
    @NotBlank(message = "参数键值不能为空")
    @Size(max = 500,message = "参数键值长度不能超过500个字符")
    private String configValue;

    /** 系统内置（Y是 N否） */
    @Schema(title = "参数键名",description = "Y=是,N=否")
    private String configType;



}