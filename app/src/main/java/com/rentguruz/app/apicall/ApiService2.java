package com.rentguruz.app.apicall;

import android.text.TextUtils;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.androidnetworking.interfaces.OkHttpResponseAndStringRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.rentguruz.app.model.DoHeader;
import com.rentguruz.app.model.response.DefaultResponse;

import java.util.HashMap;
import java.util.concurrent.Executors;

import okhttp3.Headers;
import okhttp3.Response;

public class ApiService2<T> {
    private OnResponseListener onResponseListener;



    public ApiService2(OnResponseListener onResponseListener, RequestType requestType, String endPoint, String baseurl,
                       DoHeader header, T bodyParam) {

        this.onResponseListener = onResponseListener;

        switch (requestType) {
            case GET:
                GET_REQUEST(baseurl, endPoint, header, null);
                break;
            case POST:
                POST_REQUEST(baseurl, endPoint, header, bodyParam);
                break;
            case PUT:
                PUT_REQUEST(baseurl, endPoint, header, bodyParam);
                break;
         /*   case DELETE:
                DELETE_REQUEST(baseurl, endPoint, header, bodyParam);
                break;*/
            default:
                break;
        }
    }
    private void PUT_REQUEST(String baseurl,String endPoint,DoHeader header,T bodyParam){

        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
        AndroidNetworking.put(baseurl + endPoint)
                .addHeaders(header)
                .addApplicationJsonBody(bodyParam)
                .setPriority(Priority.HIGH)
                .setExecutor(Executors.newSingleThreadExecutor())
                .build()
            /*    .getAsObject(endPoint.getClass(), new ParsedRequestListener<T>() {
                    @Override
                    public void onResponse(Object response) {

                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                })*/
                .getAsOkHttpResponseAndString(new OkHttpResponseAndStringRequestListener() {
                    @Override
                    public void onResponse(Response okHttpResponse, String response) {
                        if (!TextUtils.isEmpty(response)) {
                            if (onResponseListener != null) {
                                onResponseListener.onSuccess(response,
                                        convertHeadersToHashMap(okHttpResponse.headers()));
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (onResponseListener != null)
                            onResponseListener.onError(anError.getErrorBody());
                    }
                });
    }


    private void GET_REQUEST(String baseurl,String endPoint, DoHeader header, String bodyParam){

        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
        AndroidNetworking.get(baseurl + endPoint)
                .addHeaders(header)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsOkHttpResponseAndString(new OkHttpResponseAndStringRequestListener() {
                    @Override
                    public void onResponse(Response okHttpResponse, String response) {
                        if (!TextUtils.isEmpty(response)) {
                            if (onResponseListener != null) {
                                onResponseListener.onSuccess(response,
                                        convertHeadersToHashMap(okHttpResponse.headers()));

                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (onResponseListener != null)
                            onResponseListener.onError(anError.getErrorBody());
                    }
                });
    }


    private void POST_REQUEST(String baseurl, String endPoint, DoHeader header, T bodyParam){
        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
        AndroidNetworking.post(baseurl + endPoint)
                .addHeaders(header)
                .addApplicationJsonBody(bodyParam)
                .setPriority(Priority.HIGH)
                .setExecutor(Executors.newSingleThreadExecutor())
                .build()
                .getAsOkHttpResponseAndString(new OkHttpResponseAndStringRequestListener() {
                    @Override
                    public void onResponse(Response okHttpResponse, String response) {
                        if (!TextUtils.isEmpty(response)) {
                            if (onResponseListener != null) {
                                onResponseListener.onSuccess(response,
                                        convertHeadersToHashMap(okHttpResponse.headers()));
                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (onResponseListener != null)
                            onResponseListener.onError(anError.getErrorBody());
                    }
                });
    }

    private HashMap<String, String> convertHeadersToHashMap(Headers headers)
    {
        HashMap<String, String> result = new HashMap<>();
        for (int i = 0; i < headers.size(); i++)
        {
            result.put(headers.name(i), headers.value(i));
        }
        return result;
    }


    OnResponse<T> onResponse;

    public ApiService2(OnResponse<T> onResponseListener, RequestType requestType, String endPoint, String baseurl,
                       DoHeader header, T bodyParam, String classname) {

        this.onResponse = onResponseListener;

        switch (requestType) {
            case GET:
                GET_REQUEST(baseurl, endPoint, header, null, classname);
                break;
          /*  case POST:
                POST_REQUEST(baseurl, endPoint, header, bodyParam,classname);
                break;*/
          /*    case PUT:
                PUT_REQUEST(baseurl, endPoint, header, bodyParam);
                break;
          case DELETE:
                DELETE_REQUEST(baseurl, endPoint, header, bodyParam);
                break;*/
            default:
                break;
        }
    }

 /*   private void POST_REQUEST(String baseurl, String endPoint, DoHeader header, T bodyParam, String classname)
    {
        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
        AndroidNetworking.post(baseurl + endPoint)
                .addHeaders(header)
                .addApplicationJsonBody(bodyParam)
                .setPriority(Priority.HIGH)
                .setExecutor(Executors.newSingleThreadExecutor())
                .build()
               .getAsOkHttpResponseAndObject(classname.getClass(), new OkHttpResponseAndParsedRequestListener<T>() {
                   @Override
                   public void onResponse(Response okHttpResponse, T response) {
                           if (onResponse != null) {
                               onResponse.onSuccess(response,
                                       convertHeadersToHashMap(okHttpResponse.headers()));
                       }
                   }

                   @Override
                   public void onError(ANError anError) {

                   }
               });
    }*/

    private void GET_REQUEST(String baseurl,String endPoint, DoHeader header, T bodyParam, String classname)
    {

        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
        AndroidNetworking.get(baseurl + endPoint)
                .addHeaders(header)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsObjectList(DefaultResponse.class, new ParsedRequestListener<T>() {
                    @Override
                    public void onResponse(T response) {
                        onResponse(response);
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                })
               /*.getAsOkHttpResponse(new OkHttpResponseListener() {
                   @Override
                   public void onResponse(Response response) {
                       if (response.isSuccessful()) {
                           Log.d("Mungara", "onResponse: " + response.body().source().toString());
                           Log.d("Mungara", "onResponse: " + response.body());
                       }
                      // onResponse.onSuccess((T)response);
                       //response.headers.namesAndValues
                   }

                   @Override
                   public void onError(ANError anError) {

                   }
               })*/;
                /*.getAsOkHttpResponseAndString(new OkHttpResponseAndStringRequestListener() {
                    @Override
                    public void onResponse(Response okHttpResponse, String response) {
                        if (!TextUtils.isEmpty(response)) {
                            if (onResponseListener != null) {
                                onResponseListener.onSuccess(response,
                                        convertHeadersToHashMap(okHttpResponse.headers()));

                            }
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        if (onResponseListener != null)
                            onResponseListener.onError(anError.getErrorBody());
                    }
                });*/
    }
}
