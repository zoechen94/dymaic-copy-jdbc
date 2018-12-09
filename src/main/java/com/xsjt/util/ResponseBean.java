package com.xsjt.util;

import lombok.Getter;
import lombok.Setter;

/**
 * @author: 喜🐑
 * @create: 2018-12-05 11:37
 * 服务器json封装返回
 */

@Setter
@Getter
public class ResponseBean<T>{
    private Integer code;
    private T content;
    private String msg;

    public static <T> ResponseBean<T> success(T t) {
        ResponseBean responseBean = new ResponseBean();
        responseBean.setCode(200);
        responseBean.setContent(t);
        responseBean.setMsg("success");
        return responseBean;
    }

    public static <T> ResponseBean<T> error(T t) {
        ResponseBean responseBean = new ResponseBean();
        responseBean.setCode(500);
        responseBean.setContent(t);
        responseBean.setMsg("error");
        return responseBean;
    }
}
