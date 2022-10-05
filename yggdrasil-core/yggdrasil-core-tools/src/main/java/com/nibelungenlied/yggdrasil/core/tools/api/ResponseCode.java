package com.nibelungenlied.yggdrasil.core.tools.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.servlet.http.HttpServletResponse;

@Getter
@AllArgsConstructor
public enum ResponseCode implements IResponseCode{

    /**
     * Success
     */
    SUCCESS(HttpServletResponse.SC_OK,"Success"),

    /**
     * Unauthorized
     */
    UNAUTHORIZED(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized"),

    /**
     * Not Found
     */
    NOT_FOUND(HttpServletResponse.SC_NOT_FOUND, "Not Found"),

    /**
     * Method Not Allowed
     */
    METHOD_NOT_ALLOWED(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Method Not Allowed"),

    /**
     * Missing Request Parameter
     */
    PARAMETER_MISS(HttpServletResponse.SC_BAD_REQUEST, "Missing Request Parameter"),

    /**
     * Parameter Type Error
     */
    PARAMETER_TYPE_ERROR(HttpServletResponse.SC_BAD_REQUEST, "Parameter Type Error"),

    /**
     * Internal Server Error
     */
    INTERNAL_SERVER_ERROR(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error"),
    ;

    final int code;

    final String message;
}

