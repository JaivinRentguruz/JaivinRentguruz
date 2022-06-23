package com.rentguruz.app.apicall;

import android.text.TextUtils;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interceptors.HttpLoggingInterceptor;
import com.androidnetworking.interfaces.OkHttpResponseAndStringRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.rentguruz.app.model.DoHeader;
import com.rentguruz.app.model.base.UploadImage;

import org.json.JSONObject;

import java.io.File;
import java.nio.channels.ShutdownChannelGroupException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.internal.http2.ConnectionShutdownException;
import okhttp3.internal.http2.Http2Connection;

import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.UPLOAD_BASE_URL;


public class ApiService
{
    private OnResponseListener onResponseListener;

    public ApiService(OnResponseListener locationList, RequestType get, String list, HashMap<String, String> stringStringHashMap, JSONObject bodyParam) {
    }
    public ApiService() {
    }

    public ApiService(OnResponseListener onResponseListener, RequestType requestType, String endPoint,String baseurl,
                      HashMap<String, String> header, JSONObject bodyParam) {
        this.onResponseListener = onResponseListener;

        switch (requestType) {
            case GET:
                GET_REQUEST(baseurl,endPoint, header, null);
                break;
            case POST:
                POST_REQUEST(baseurl,endPoint, header, bodyParam);
                break;
            case PUT:
                PUT_REQUEST(baseurl,endPoint, header, bodyParam);
                break;
            case DELETE:
                DELETE_REQUEST(baseurl,endPoint, header, bodyParam);
                break;
            default:
                break;
        }
    }

    public ApiService(OnResponseListener onResponseListener, RequestType requestType, String endPoint,String baseurl,
                      HashMap<String, String> header, String bodyParam) {
        this.onResponseListener = onResponseListener;

        switch (requestType) {
            case GET:
                GET_REQUEST(baseurl,endPoint, header, bodyParam);
                break;
            default:
                break;
        }
    }
    private void GET_REQUEST(String baseurl,String endPoint, HashMap<String, String> header, String bodyParam)
    {
    /*    String credentials = Credentials.basic("Car4446668888","1KzWo/O7IeX5GoOQ/U2S/Q==");
        header.put("Authorization", credentials);*/

        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);

        AndroidNetworking.get(baseurl + endPoint+"?"+bodyParam)
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


    private void POST_REQUEST(String baseurl, String endPoint, HashMap<String, String> header, JSONObject bodyParam)
    {
      /*  String credentials = Credentials.basic("Car4446668888","1KzWo/O7IeX5GoOQ/U2S/Q==");

        header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");
        header.put("Authorization", credentials);*/

        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);

        AndroidNetworking.post(baseurl + endPoint)
                .addHeaders(header)
                .addJSONObjectBody(bodyParam)
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

    private void PUT_REQUEST(String baseurl,String endPoint, HashMap<String, String> header, JSONObject bodyParam)
    {
     /*   header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");
        String credentials = Credentials.basic("Car4446668888","1KzWo/O7IeX5GoOQ/U2S/Q==");
        header.put("Authorization", credentials);*/

        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);

        AndroidNetworking.put(baseurl + endPoint)
                .addHeaders(header)
                .addJSONObjectBody(bodyParam)
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

    private void DELETE_REQUEST(String baseurl,String endPoint, HashMap<String, String> header, JSONObject bodyParam)
    {
       /* header.put("Accept", "application/json");
        header.put("Content-Type", "application/json");
        String credentials = Credentials.basic("Car4446668888","1KzWo/O7IeX5GoOQ/U2S/Q==");
        header.put("Authorization", credentials);*/

        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);

        AndroidNetworking.delete(baseurl + endPoint)
                .addHeaders(header)
                .addJSONObjectBody(bodyParam)
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

    public void UPLOAD_REQUEST(final OnResponseListener onResponseListener,
                               String endPoint,
                               UploadImage uploadImage, File file) {
        HashMap<String, String> header = new HashMap<>();
        //header.put("Accept", "application/json");
        header.put("Content-Type", "multipart/form-data");
      //  String credentials = Credentials.basic("Car4446668888","1KzWo/O7IeX5GoOQ/U2S/Q==");
     //   header.put("Authorization", credentials);
         header.put("Id","16023");
         header.put("fileUploadType","1");
         header.put("CompanyId","1");
        // header.put("", String.valueOf(file));
        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
        AndroidNetworking.upload(BASE_URL_LOGIN + endPoint)
                .addMultipartParameter("Id","16025")
                .addMultipartParameter("fileUploadType","1")
                .addMultipartParameter("CompanyId","1")
                .addMultipartFile("file",file)
                .setTag("uploadTest")
                .setOkHttpClient(getConfigOkHttpClient())
            //        .setTag("uploadArtistData")
                .setPriority(Priority.HIGH)
                .setExecutor(Executors.newSingleThreadExecutor())
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        System.out.println(bytesUploaded);
                        System.out.println(totalBytes);

                    }
                })
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
                    public void onError(ANError error) {
                        System.out.println(error.getErrorDetail());
                        if (onResponseListener != null)
                            onResponseListener.onError(error.getErrorBody());
                    }
                });


    }



    public void UPLOAD_REQUEST(final OnResponseListener onResponseListener,
                               String endPoint,
                               HashMap<String, String> header, File file) {

        // header.put("", String.valueOf(file));
        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
        AndroidNetworking.upload(BASE_URL_LOGIN + endPoint)
                //.addHeaders(header2)
                .addMultipartParameter(header)
                .addMultipartFile("",file)
                .setTag("uploadTest")
                .setOkHttpClient(getConfigOkHttpClient())
                //        .setTag("uploadArtistData")
                .setPriority(Priority.HIGH)
                .setExecutor(Executors.newSingleThreadExecutor())
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {


                    }
                })
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
                    public void onError(ANError error) {
                        System.out.println(error.getErrorDetail());
                        if (onResponseListener != null)
                            onResponseListener.onError(error.getErrorBody());
                    }
                });


    }


    public void UPLOAD_REQUEST(final OnResponseListener onResponseListener,
                               String endPoint,
                              JSONObject header, File file) {

        // header.put("", String.valueOf(file));
        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
        AndroidNetworking.upload(BASE_URL_LOGIN + endPoint)
                //.addHeaders(header2)
                .addMultipartParameter(header)
                .addMultipartFile("",file)
                .setTag("uploadTest")
                .setOkHttpClient(getConfigOkHttpClient())
                //        .setTag("uploadArtistData")
                .setPriority(Priority.HIGH)
                .setExecutor(Executors.newSingleThreadExecutor())
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {


                    }
                })
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
                    public void onError(ANError error) {
                        System.out.println(error.getErrorDetail());
                        if (onResponseListener != null)
                            onResponseListener.onError(error.getErrorBody());
                    }
                });


    }



    public void UPLOAD_REQUEST(final OnResponseListener onResponseListener,
                               String endPoint,DoHeader header2,
                               HashMap<String, String> header, File file) {

        // header.put("", String.valueOf(file));
        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
        AndroidNetworking.upload(BASE_URL_LOGIN + endPoint)
                .addHeaders(header2)
                .addMultipartParameter(header)
                .addMultipartFile("",file)
                .setTag("uploadTest")
                .setOkHttpClient(getConfigOkHttpClient())
                //        .setTag("uploadArtistData")
                .setPriority(Priority.HIGH)
                .setExecutor(Executors.newSingleThreadExecutor())
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {


                    }
                })
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
                    public void onError(ANError error) {
                        System.out.println(error.getErrorDetail());
                        if (onResponseListener != null)
                            onResponseListener.onError(error.getErrorBody());
                    }
                });


    }

    public void UPLOAD_REQUEST(final OnResponseListener onResponseListener,
                               String endPoint,
                               HashMap<String, String> header, List<File> file) {

        // header.put("", String.valueOf(file));
        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
        AndroidNetworking.upload(BASE_URL_LOGIN + endPoint)
                .addMultipartParameter(header)
                //.addMultipartFile("", (File) file)
                .addMultipartFileList("", file)
                .setTag("uploadTest")
                .setOkHttpClient(getConfigOkHttpClient())
                //        .setTag("uploadArtistData")
                .setPriority(Priority.HIGH)
                .setExecutor(Executors.newSingleThreadExecutor())
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {


                    }
                })
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
                    public void onError(ANError error) {
                        System.out.println(error.getErrorDetail());
                        if (onResponseListener != null)
                            onResponseListener.onError(error.getErrorBody());
                    }
                });


    }

    public void UPLOAD_REQUEST(final OnResponseListener onResponseListener,
                               String endPoint,
                               HashMap<String, String> header) {

        // header.put("", String.valueOf(file));
        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
        AndroidNetworking.upload(BASE_URL_LOGIN + endPoint)
                .addMultipartParameter(header)
                .setTag("uploadTest")
                .setOkHttpClient(getConfigOkHttpClient())
                .setTag("uploadArtistData")
                .setPriority(Priority.HIGH)
                .setExecutor(Executors.newSingleThreadExecutor())
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        System.out.println(bytesUploaded);
                        System.out.println(totalBytes);

                    }
                })
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
                    public void onError(ANError error) {
                        System.out.println(error.getErrorDetail());
                        if (onResponseListener != null)
                            onResponseListener.onError(error.getErrorBody());
                    }
                });


    }

    public ApiService(OnResponseListener onResponseListener, RequestType requestType, String endPoint, String baseurl,
                      DoHeader header, JSONObject bodyParam){

        this.onResponseListener = onResponseListener;

        switch (requestType) {
            case GET:
                GET_REQUEST(baseurl,endPoint, header, bodyParam);
                break;
            case POST:
                POST_REQUEST(baseurl,endPoint, header, bodyParam);
                break;
            case PUT:
                PUT_REQUEST(baseurl,endPoint, header, bodyParam);
                break;
            case DELETE:
                DELETE_REQUEST(baseurl,endPoint, header, bodyParam);
                break;
            default:
                break;
        }
    }

    public ApiService(OnResponseListener onResponseListener, RequestType requestType, String endPoint,String baseurl,
                     DoHeader header, String bodyParam) {
        this.onResponseListener = onResponseListener;

        switch (requestType) {
            case GET:
                GET_REQUEST(baseurl,endPoint, header, bodyParam);
                break;

            case DELETE:
                DELETE_REQUEST(baseurl,endPoint, header, bodyParam);
                break;
            default:
                break;
        }
    }

    private void GET_REQUEST(String baseurl,String endPoint, DoHeader header, String bodyParam){
        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
        AndroidNetworking.get(baseurl + endPoint + bodyParam)
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

    private void GET_REQUEST(String baseurl,String endPoint, DoHeader header, JSONObject bodyParam){
        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
        AndroidNetworking.get(baseurl + endPoint)
                .addHeaders(header)
                .addQueryParameter(bodyParam)
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

    private void POST_REQUEST(String baseurl, String endPoint, DoHeader header, JSONObject bodyParam){
        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
        AndroidNetworking.post(baseurl + endPoint)
                .addHeaders(header)
                .addJSONObjectBody(bodyParam)
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

    private void PUT_REQUEST(String baseurl, String endPoint, DoHeader header, JSONObject bodyParam){
        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
        AndroidNetworking.put(baseurl + endPoint)
                .addHeaders(header)
                .addJSONObjectBody(bodyParam)
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

    private void DELETE_REQUEST(String baseurl, String endPoint, DoHeader header, JSONObject bodyParam){
        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
        AndroidNetworking.delete(baseurl + endPoint)
                .addHeaders(header)
                //.addJSONObjectBody(bodyParam)
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

    private void DELETE_REQUEST(String baseurl, String endPoint, DoHeader header, String bodyParam){
        AndroidNetworking.enableLogging(HttpLoggingInterceptor.Level.BODY);
        AndroidNetworking.delete(baseurl + endPoint+bodyParam)
                .addHeaders(header)
                //.addJSONObjectBody(bodyParam)
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

    public static OkHttpClient getConfigOkHttpClient() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BASIC);
        return new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .retryOnConnectionFailure(true)
                .build();
    }
}
