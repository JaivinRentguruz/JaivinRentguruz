package com.rentguruz.app.flexiicar.user;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;
import com.rentguruz.app.R;
import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentPrintPreviewBinding;
import com.rentguruz.app.model.parameter.ReportType;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.REPORT;

public class Fragment_Print_Preview extends BaseFragment {

    FragmentPrintPreviewBinding binding;
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
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject object = new JSONObject(response);
                        String pdfFileName =    object.getString("Data");

                        new Fragment_Print_Preview.RetrivePDFfromUrl().execute(pdfFileName);
                        fullProgressbar.hide();
                    } catch (Exception e){
                        e.printStackTrace();
                        fullProgressbar.hide();
                    }

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
        }
    }
}