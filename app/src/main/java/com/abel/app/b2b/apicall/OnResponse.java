package com.abel.app.b2b.apicall;

public interface OnResponse<T>  {

    void onSuccess(T response);

    void onError(String error);
}
