package com.example.springboottyy.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author: GithubTang
 * @Description: TODO
 * @Date: 2025/3/16 20:36
 * @Version: 1.0
 */
@Getter
@Setter
@ToString
@Schema(title = "系统访问记录表")
@Entity
public class SysNotice extends BaseEntity {
    /** 公告ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(title = "公告ID")
    private Long id;

    /** 公告标题 */
    @Schema(title = "公告标题")
    private String noticeTitle;

    /** 公告类型（1通知 2公告） */
    @Schema(title = "公告类型")
    private String noticeType;

    /** 公告内容 */
    @Schema(title = "公告内容")
    private String noticeContent;

    /** 公告状态（0正常 1关闭） */
    @Schema(title = "公告状态")
    private String status;

}
