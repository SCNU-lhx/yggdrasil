package com.nibelungenlied.api;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.nibelungenlied.constant.HTTPResponse.SC_OK;

@Getter
@AllArgsConstructor
public enum ReturnCode implements IReturnCode{
    SUCCESS(SC_OK,"SUCCESS!");
    final int code;

    final String message;
}

