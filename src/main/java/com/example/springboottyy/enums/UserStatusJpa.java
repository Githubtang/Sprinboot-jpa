package com.example.springboottyy.enums;

/**
 * @Author: GithubTang
 * @Description: 用户状态
 * @Date: 2025/3/9 0:59
 * @Version: 1.0
 */
public enum UserStatusJpa {
    /** 正常 */
//    OK("0", "正常"),
    OK(true, "正常"),
    /** 停用 */
    DISABLE(false, "停用"),
    /** 删除 */
    DELETED(true, "删除");

    private final Boolean code;
    private final String info;

    UserStatusJpa(Boolean code, String info) {
        this.code = code;
        this.info = info;
    }

    public Boolean getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
