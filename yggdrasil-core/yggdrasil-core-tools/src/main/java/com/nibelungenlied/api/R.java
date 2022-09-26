package com.nibelungenlied.api;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class R<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;


    private int code;

    private boolean success;

    private T data;

    private String msg;

    private R(IReturnCode returnCode) {
        this(returnCode, null, returnCode.getMessage());
    }

    private R(IReturnCode returnCode, String msg) {
        this(returnCode, null, msg);
    }

    private R(IReturnCode returnCode, T data) {
        this(returnCode, data, returnCode.getMessage());
    }

    private R(IReturnCode returnCode, T data, String msg) {
        this(returnCode.getCode(), data, msg);
    }

    private R(int code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
        this.success = ReturnCode.SUCCESS.code == code;
    }


}
