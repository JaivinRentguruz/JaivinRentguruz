package com.rentguruz.app.apicall;

public interface OnResponse<T>  {

    void onSuccess(T response);

    void onError(String error);
}
