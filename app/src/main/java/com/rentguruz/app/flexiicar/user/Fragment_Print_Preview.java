package com.rentguruz.app.flexiicar.user;

import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.mbms.DownloadRequest;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.fragment.NavHostFragment;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.rentguruz.app.R;
import com.rentguruz.app.adapters.DownloadTask;
import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentPrintPreviewBinding;
import com.rentguruz.app.model.parameter.ReportType;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.REPORT;

public class Fragment_Print_Preview extends BaseFragment {

    FragmentPrintPreviewBinding binding;
    DownloadManager manager;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = FragmentPrintPreviewBinding.inflate(inflater, container,false);
        return binding.getRoot();
    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setUiColor(UiColor);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding.header.screenHeader.setText("Invoice Print");
        binding.header.discard.setText("");
        Drawable drawable = getResources().getDrawable(R.drawable.ic_baseline_cloud_download_24);
        drawable.setTint(Color.parseColor(UiColor.primary));
        binding.header.discard.setBackground( drawable);
        binding.header.optionmenu.setVisibility(View.VISIBLE);
        binding.header.optionmenu.setImageResource(R.drawable.ic_baseline_share_24);
        binding.header.optionmenu.setColorFilter(Color.parseColor(UiColor.primary));


        fullProgressbar.show();
        binding.header.back.setOnClickListener(this);
        new ApiService(GetReceipt, RequestType.POST,REPORT,BASE_URL_LOGIN,header,params.getReport(ReportType.AgreementPrint.inte,getArguments().getInt("Id")));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                NavHostFragment.findNavController(Fragment_Print_Preview.this).popBackStack();
                break;
        }
    }

    OnResponseListener GetReceipt = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            handler.post(() -> {
                try {
                    JSONObject object = new JSONObject(response);
                    String pdfFileName =    object.getString("Data");
                    //URL u = (URL) object.get("Data");
                    binding.header.discard.setOnClickListener(v -> {
                        Log.e(TAG, "onClick: "+ pdfFileName.substring(pdfFileName.lastIndexOf('/'), pdfFileName.length()).split("/")[1]);
                        downloadFile(pdfFileName,pdfFileName.substring(pdfFileName.lastIndexOf('/'), pdfFileName.length()).split("/")[1]);

                    });

                    binding.header.optionmenu.setOnClickListener(v -> {
                                   /* manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
                                    Uri uri = Uri.parse(pdfFileName);
                                    DownloadManager.Request request = new DownloadManager.Request(uri);
                                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                                    long reference = manager.enqueue(request);
                                    Log.e(TAG, "onClick: " + reference);
                        Log.e(TAG, "onClick: " + "1" );*/
                        //new DownloadTask(context,pdfFileName);

                      /*  Ion.with(context).load(pdfFileName).progress(new ProgressCallback() {
                            @Override
                            public void onProgress(long downloaded, long total) {
                                Log.e(TAG, "onProgress: " + downloaded + " : " + total );
                            }
                        }).write(new File(Environment.getExternalStorageDirectory()
                                +"/Android/data/"
                                + getContext().getPackageName()
                                + "/Files"))
                        .setCallback(new FutureCallback<File>() {
                            @Override
                            public void onCompleted(Exception e, File result) {
                                 Log.e(TAG, "onCompleted: " +  e.getMessage() + " : " + result );
                            }
                        })
                        ;*/

                        //new DownloadFileFromURL().execute(pdfFileName);

                        try {
                            Log.e(TAG, "onClick: " + pdfFileName );
                            Log.e(TAG, "onClick: " + new File(pdfFileName).toURI().toURL() );
                            ShareFile(new File(pdfFileName).toURI().toURL() );



                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }


                    });

                    new RetrivePDFfromUrl().execute(pdfFileName);
                    //fullProgressbar.hide();
                } catch (Exception e){
                    e.printStackTrace();
                    fullProgressbar.hide();
                }

            });
        }

        @Override
        public void onError(String error) {
            fullProgressbar.hide();
            Log.e(TAG, "onError: "+ error.toString() );
        }
    };

    // create an async task class for loading pdf file from URL.
    class RetrivePDFfromUrl extends AsyncTask<String, Void, InputStream> {
        @Override
        protected InputStream doInBackground(String... strings) {
            // we are using inputstream
            // for getting out PDF.
            InputStream inputStream = null;
            try {
                URL url = new URL(strings[0]);
                // below is the step where we are
                // creating our connection.
                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    // response is success.
                    // we are getting input stream from url
                    // and storing it in our variable.
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
                }

            } catch (IOException e) {
                // this is the method
                // to handle errors.
                e.printStackTrace();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            // after the execution of our async
            // task we are loading our pdf in our pdf view.
            binding.webview.fromStream(inputStream).load();
            fullProgressbar.hide();
        }
    }

    public void ShareFile(URL path)
    {
        Intent shareToneIntent=new Intent(Intent.ACTION_SEND);
        shareToneIntent.setType("text/plain");
        shareToneIntent.putExtra(Intent.EXTRA_SUBJECT,"Insert Subject here");
        String data = "Your Invoice Copy " + path;
        shareToneIntent.putExtra(Intent.EXTRA_TEXT, data);
        //shareToneIntent.setType(GetMimeType(Uri.parse(path.toString())));
        // shareToneIntent.setType(String.valueOf(path));
        requireContext().startActivity(Intent.createChooser(shareToneIntent, "Share Via"));
    }

    public String GetMimeType(Uri uri) {
        String mimeType = null;

        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            mimeType = requireContext().getContentResolver().getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileExtension.toLowerCase());
        }
        return mimeType;
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.e(TAG, "onPreExecute: " );
            //showDialog(progress_bar_type);
            fullProgressbar.show();
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            Log.e(TAG, "doInBackground: " );
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = connection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                // Output stream
                OutputStream output = new FileOutputStream(Environment
                        .getDownloadCacheDirectory().toString()
                        + "/2011.pdf");



                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            //pDialog.setProgress(Integer.parseInt(progress[0]));
            fullProgressbar.dismiss();
            Log.e(TAG, "onProgressUpdate: " );
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            //dismissDialog(progress_bar_type);
            Log.e(TAG, "onPostExecute: " );
            fullProgressbar.dismiss();

        }

    }


    public void downloadFile(String url, String filename){
        try {
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri filelink = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(filelink);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE).setMimeType("pdf").setAllowedOverRoaming(true)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION)
            .setTitle("Rentguruz")
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, File.pathSeparator+filename);

            downloadManager.enqueue(request);
        } catch (Exception e){
            e.printStackTrace();
            Log.e(TAG, "downloadFile: " + e.getMessage());
        }
    };



}