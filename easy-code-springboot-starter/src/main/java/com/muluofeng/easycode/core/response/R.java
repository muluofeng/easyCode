package com.muluofeng.easycode.core.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 响应信息主体
 */
@Builder
@ToString
@Accessors(chain = true)
@AllArgsConstructor
public class R<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    private int code = CommonCode.SUCCESS.code;

    @Getter
    @Setter
    private String msg = "success";


    @Getter
    @Setter
    private T data;

    public R() {
        super();
    }

    public R(T data) {
        super();
        this.data = data;
    }

    public R(T data, String msg) {
        super();
        this.data = data;
        this.msg = msg;
    }

    public R(Throwable e) {
        super();
        this.msg = e.getMessage();
        this.code = CommonCode.FAIL.code;
    }

    public static R error(String msg) {
        R r = new R();
        r.code = CommonCode.FAIL.code;
        r.msg = msg;
        return r;
    }

    public static R error(int code, String msg) {
        R r = new R();
        r.code = code;
        r.msg = msg;
        return r;
    }


    public static R error(CommonCode commonCode) {
        R r = new R();
        r.code = commonCode.code;
        r.msg = commonCode.message;
        return r;
    }

    public static R error(ResponseCode code) {
        R r = new R();
        r.code = code.code();
        r.msg = code.message();
        return r;
    }


    public static <T> R<T> ok(T data) {
        R r = new R();
        r.code = CommonCode.SUCCESS.code;
        r.data = data;
        return r;
    }

    public static R ok() {
        R r = new R();
        r.code = CommonCode.SUCCESS.code;
        r.data = "操作成功";
        return r;
    }
}
