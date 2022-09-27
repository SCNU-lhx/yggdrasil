package com.nibelungenlied.api;

import java.io.Serializable;

public interface IResponseCode extends Serializable {
    String getMessage();
    int getCode();
}
