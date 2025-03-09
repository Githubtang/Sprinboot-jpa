package com.example.springboottyy.enums;

/**
 * @Author: GithubTang
 * @Description: 用户状态
 * @Date: 2025/3/9 0:59
 * @Version: 1.0
 */
public enum UserStatus {
    /** 正常 */
//    OK("0", "正常"),
    OK("0", "正常"),
    /** 停用 */
    DISABLE("1", "停用"),
    /** 删除 */
    DELETED("2", "删除");

    private final String code;
    private final String info;

    UserStatus(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public String getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }
}
