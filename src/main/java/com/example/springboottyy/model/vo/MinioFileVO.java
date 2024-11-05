package com.example.springboottyy.model.vo;

import lombok.Data;
import okhttp3.Headers;

import java.io.InputStream;

/**
 * @Author:     Insight
 * @Description:  TODO  
 * @Date:    2024/10/27 16:04
 * @Version:    1.0
 */
@Data
public class MinioFileVO {
    private InputStream fileInputSteam;
    private String object;
    private Headers headers;
    private String buket;
    private String region;

    public MinioFileVO(InputStream fileInputSteam, String object, Headers headers, String buket, String region) {
        this.fileInputSteam = fileInputSteam;
        this.object = object;
        this.headers = headers;
        this.buket = buket;
        this.region = region;
    }

}
