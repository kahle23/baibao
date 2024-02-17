/*
 * Copyright (c) 2019. the original author or authors.
 * BaiBao is licensed under the "LICENSE" file in the project's root directory.
 */

package baibao.extension.aliyun.nls.dto;

import java.io.Serializable;

public class TokenResp implements Serializable {
    private String token;
    private Long   expireTime;

    public TokenResp(String token, Long expireTime) {
        this.expireTime = expireTime;
        this.token = token;
    }

    public TokenResp() {

    }

    public String getToken() {

        return token;
    }

    public void setToken(String token) {

        this.token = token;
    }

    public Long getExpireTime() {

        return expireTime;
    }

    public void setExpireTime(Long expireTime) {

        this.expireTime = expireTime;
    }

}
