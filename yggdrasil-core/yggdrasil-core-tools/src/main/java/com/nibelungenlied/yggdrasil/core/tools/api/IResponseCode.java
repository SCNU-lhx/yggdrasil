package com.nibelungenlied.yggdrasil.core.tools.api;

import java.io.Serializable;

public interface IResponseCode extends Serializable {
    String getMessage();
    int getCode();
}
