package com.nibelungenlied.api;

import com.nibelungenlied.constant.YggdrasilConstant;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.servlet.http.HttpServletResponse;
import java.io.Serial;
import java.io.Serializable;

/**
 * Unified API response information
 * @param <T>
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
@ApiModel(description = "Response information")
public class R<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;


    private int code;

    private boolean success;

    private T data;

    private String msg;

    private R(IResponseCode responseCode) {
        this(responseCode, null, responseCode.getMessage());
    }

    private R(IResponseCode responseCode, String msg) {
        this(responseCode, null, msg);
    }

    private R(IResponseCode responseCode, T data) {
        this(responseCode, data, responseCode.getMessage());
    }

    private R(IResponseCode responseCode, T data, String msg) {
        this(responseCode.getCode(), data, msg);
    }

    private R(int code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
        this.success = ResponseCode.SUCCESS.code == code;
    }

    /**
     *
     * @param data data
     * @return R
     * @param <T> T
     */
    public static <T> R<T> data(T data) {
        return data(data, YggdrasilConstant.DEFAULT_SUCCESS_MESSAGE);
    }

    /**
     *
     * @param data data
     * @param msg message
     * @return R
     * @param <T> T
     */
    public static <T> R<T> data(T data, String msg) {
        return data(HttpServletResponse.SC_OK, data, msg);
    }

    /**
     *
     * @param code status code
     * @param data data
     * @param msg message
     * @return R
     * @param <T> T
     */
    public static <T> R<T> data(int code, T data, String msg) {
        return new R<>(code, data, data == null ? YggdrasilConstant.DEFAULT_NULL_MESSAGE : msg);
    }

    /**
     *
     * @param msg message
     * @return R
     * @param <T> T
     */
    public static <T> R<T> success(String msg) {
        return new R<>(ResponseCode.SUCCESS, msg);
    }

    /**
     *
     * @param responseCode response code
     * @return R
     * @param <T> T
     */
    public static <T> R<T> success(IResponseCode responseCode) {
        return new R<>(responseCode);
    }

    /**
     *
     * @param responseCode response code
     * @param msg message
     * @return R
     * @param <T> T
     */
    public static <T> R<T> success(IResponseCode responseCode, String msg) {
        return new R<>(responseCode, msg);
    }

    /**
     *
     * @param code code
     * @param msg message
     * @return R
     * @param <T> T
     */
    public static <T> R<T> fail(int code, String msg) {
        return new R<>(code, null, msg);
    }

    /**
     *
     * @param responseCode response code
     * @return R
     * @param <T> T
     */
    public static <T> R<T> fail(IResponseCode responseCode) {
        return new R<>(responseCode);
    }

    /**
     *
     * @param responseCode response code
     * @param msg message
     * @return R
     * @param <T> T
     */
    public static <T> R<T> fail(IResponseCode responseCode, String msg) {
        return new R<>(responseCode, msg);
    }

}
