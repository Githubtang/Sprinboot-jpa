package com.example.springboottyy.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @Author: GithubTang
 * @Description: 系统访问记录表 sys_logininfor
 * @Date: 2025/3/8 2:06
 * @Version: 1.0
 */
@Getter
@Setter
@Schema(title = "系统访问记录表")
@Entity
public class SysLogininfor extends BaseEntity{
    private static final long serialVersionUID = 1L;

    /** ID */
    @Id
    @Schema(title = "序号")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 用户账号 */
    @Schema(title = "用户账号")
    private String userName;

    /** 登录状态 0成功 1失败 */
    @Schema(title = "登录状态")
    private String status;

    /** 登录IP地址 */
    @Schema(title = "登录地址")
    private String ipaddr;

    /** 登录地点 */
    @Schema(title = "登录地点")
    private String loginLocation;

    /** 浏览器类型 */
    @Schema(title = "浏览器")
    private String browser;

    /** 操作系统 */
    @Schema(title = "操作系统")
    private String os;

    /** 提示消息 */
    @Schema(title = "提示消息")
    private String msg;

    /** 访问时间 */
    @Schema(title = "访问时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime loginTime;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        loginTime = now;
    }

    @PreUpdate
    public void preUpdate() {
        loginTime = LocalDateTime.now();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        SysLogininfor that = (SysLogininfor) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
