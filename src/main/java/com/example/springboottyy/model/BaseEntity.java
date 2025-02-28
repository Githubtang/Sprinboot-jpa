package com.example.springboottyy.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Insight
 * @Description: TODO
 * @Date: 2025/2/27 0:15
 * @Version: 1.0
 */
@Data
@Schema(title = "基类")
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 搜索值
     */
    @Schema(title = "搜索值")
    @JsonIgnore
    private String searchValue;

    /**
     * 创建者
     */
    @Schema(title = "创建者")
    private String createBy;

    /**
     * 创建时间
     */
    @Schema(title = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新者
     */
    @Schema(title = "更新者")
    private String updateBy;

    /**
     * 更新时间
     */
    @Schema(title = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 备注
     */
    @Getter
    @Schema(title = "备注")
    private String remark;

    /**
     * 请求参数
     */
    @Schema(title = "请求参数")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, Object> params;

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

}
