package com.nibelungenlied.api;

import java.io.Serializable;

public interface IReturnCode extends Serializable {
    String getMessage();
    int getCode();
}
