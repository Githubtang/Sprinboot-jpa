package com.example.springboottyy.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.Date;

/**
 * @Author: GithubTang
 * @Description: 操作日志记录表 oper_log
 * @Date: 2025/3/8 2:11
 * @Version: 1.0
 */
@Entity
@Data
@Schema(title = "操作日志记录表")
public class SysOperLog extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 日志主键
     */

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Schema(title = "操作序号")
    private Long id;

    /**
     * 操作模块
     */
    @Schema(title = "操作模块")
    private String title;

    /**
     * 业务类型（0其它 1新增 2修改 3删除）
     */
    @Schema(title = "业务类型", description = "0=其它,1=新增,2=修改,3=删除,4=授权,5=导出,6=导入,7=强退,8=生成代码,9=清空数据")
    private Integer businessType;

    /**
     * 请求方法
     */
    @Schema(title = "请求方法")
    private String method;

    /**
     * 请求方式
     */
    @Schema(title = "请求方式")
    private String requestMethod;

    /**
     * 操作类别（0其它 1后台用户 2手机端用户）
     */
    @Schema(title = "操作类别", description = "0=其它,1=后台用户,2=手机端用户")
    private Integer operatorType;

    /**
     * 操作人员
     */
    @Schema(title = "操作人员")
    private String operName;

    /**
     * 部门名称
     */
    @Schema(title = "部门名称")
    private String deptName;

    /**
     * 请求url
     */
    @Schema(title = "请求地址")
    private String operUrl;

    /**
     * 操作地址
     */
    @Schema(title = "操作地址")
    private String operIp;

    /**
     * 操作地点
     */
    @Schema(title = "操作地点")
    private String operLocation;

    /**
     * 请求参数
     */
    @Schema(title = "请求参数")
    private String operParam;

    /**
     * 返回参数
     */
    @Schema(title = "返回参数")
    private String jsonResult;

    /**
     * 操作状态（0正常 1异常）
     */
    @Schema(title = "操作状态", description = "0=正常,1=异常")
    private Integer status;

    /**
     * 错误消息
     */
    @Schema(title = "错误消息")
    private String errorMsg;

    /**
     * 操作时间
     */
    @Schema(title = "操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date operTime;

    /**
     * 消耗时间
     */
    @Schema(title = "消耗时间", description = "毫秒")
    private Long costTime;

}
