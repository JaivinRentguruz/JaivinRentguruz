package com.rentguruz.app.flexiicar.user;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.rentguruz.app.R;
import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.androidnetworking.AndroidNetworking;
import com.github.barteksc.pdfviewer.PDFView;
import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentPaymentReceipt2Binding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import static android.content.Context.MODE_PRIVATE;
import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.REPORT;

public class Fragment_Payment_Reciept_2 extends BaseFragment
{
    ImageView backarrow;
    TextView Payment_Total,txt_Payment_Status,Payment_TotalAmount;
    String serverpath="";
    Bundle PaymentBundle;
    PDFView web_view;
    public String id = "";
    TextView txt_CardNo,txt_CardExDate;
    //Handler handler = new Handler();
    LinearLayout lblPay;
    JSONObject creditCardJSON;
    String transactionId;
    LinearLayout llForUnpaid,llforPaid;
    FragmentPaymentReceipt2Binding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_payment_receipt_2, container, false);
        binding = FragmentPaymentReceipt2Binding.inflate(inflater,container,false);
        return  binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        try {
            super.onViewCreated(view, savedInstanceState);

            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            binding.setUiColor(UiColor);
            fullProgressbar.show();
            Payment_Total = view.findViewById(R.id.Payment_Total);
            backarrow = view.findViewById(R.id.back);
            web_view = view.findViewById(R.id.webview);
            txt_CardNo = view.findViewById(R.id.txt_CardNo);
            txt_CardExDate = view.findViewById(R.id.txt_CardExDate);
            lblPay = view.findViewById(R.id.Lbl_Pay);

            llforPaid=view.findViewById(R.id.llForPaid);
            llForUnpaid=view.findViewById(R.id.llForUnpaid);
            SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
            serverpath = sp.getString("serverPath", "");
            id = sp.getString(getString(R.string.id), "");
            binding.header.screenHeader.setText("Payment Receipt");
//            binding.header.discard.setVisibility(View.GONE);
            binding.header.discard.setText("");
            Drawable drawable = getResources().getDrawable(R.drawable.ic_baseline_cloud_download_24);
            drawable.setTint(Color.parseColor(UiColor.primary));
            binding.header.discard.setBackground( drawable);
            binding.header.optionmenu.setVisibility(View.VISIBLE);
            binding.header.optionmenu.setImageResource(R.drawable.ic_baseline_share_24);
            binding.header.optionmenu.setColorFilter(Color.parseColor(UiColor.primary));

           /* PaymentBundle = getArguments().getBundle("PaymentBundle");
            System.out.println(PaymentBundle);

            String pdfFileName = (PaymentBundle.getString("path"));
            Double TotalAmount = (PaymentBundle.getDouble("totalAmount"));
            int billStatus= (PaymentBundle.getInt("billStatus"));*/

            txt_Payment_Status=view.findViewById(R.id.txt_Payment_Status);
            Payment_TotalAmount=view.findViewById(R.id.Payment_TotalAmount);

            /*if(billStatus==1)
            {
                txt_Payment_Status.setText("PAID");
                txt_Payment_Status.setTextColor(getResources().getColor(R.color.footerButtonBC));
                llforPaid.setVisibility(View.VISIBLE);
            }
            else {
                txt_Payment_Status.setText("UNPAID");
                txt_Payment_Status.setTextColor(getResources().getColor(R.color.btn2));
                llForUnpaid.setVisibility(View.VISIBLE);
            }
            //forUnpaid
            Payment_Total.setText(((String.format(Locale.US, "%.2f", TotalAmount))));
            //forPaid
            Payment_TotalAmount.setText(((String.format(Locale.US, "%.2f", TotalAmount))));*/

            JSONObject jsonObject = new JSONObject();

            jsonObject.accumulate("ReportType",55);
            jsonObject.accumulate("Id",getArguments().getInt("IId"));
            jsonObject.accumulate("CompanyId",Integer.valueOf(loginRes.getData("CompanyId")));
            jsonObject.accumulate("RenderFormat","PDF");
            jsonObject.accumulate("FilterBy",1);
            jsonObject.accumulate("IsExternalReport",false);

            new ApiService(GetReceipt,RequestType.POST,REPORT,BASE_URL_LOGIN,header,jsonObject);

            /*String url1 = serverpath + pdfFileName;
            System.out.println(url1);
            web_view.setWebViewClient(new WebViewClient());
            web_view.getSettings().setJavaScriptEnabled(true);
            web_view.getSettings().setLoadWithOverviewMode(true);
            web_view.getSettings().setUseWideViewPort(true);
            web_view.getSettings().setAllowFileAccess(true);
            web_view.loadUrl(url1);
*/
            TextView changeCreditCard = view.findViewById(R.id.changeCCPayment);
            changeCreditCard.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Bundle bundle = new Bundle();
                    bundle.putInt("backTo",3);
                    bundle.putBundle("PaymentBundle",PaymentBundle);
                    NavHostFragment.findNavController(Fragment_Payment_Reciept_2.this)
                            .navigate(R.id.action_Payment_Reciept_2_to_CardsOnAccount, bundle);
                }
            });
            backarrow.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("StatmentType",true);
                    NavHostFragment.findNavController(Fragment_Payment_Reciept_2.this)
                            .navigate(R.id.action_Payment_Reciept_2_to_Bills_and_Payment,bundle);
                }
            });

            String bodyParam1 = "";
            try {
                bodyParam1 += "customerId=" + id;

                System.out.println(bodyParam1);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                String creditCardJSONStr = getArguments().getString("creditcard");

                creditCardJSON = new JSONObject(creditCardJSONStr);

                String card_No = creditCardJSON.getString("card_No");
                String card_Name = creditCardJSON.getString("card_Name");
                String expiry_Date = creditCardJSON.getString("expiry_Date");

                //txtcardname.setText(card_Name);
                txt_CardNo.setText("**** **** **** " + card_No.substring(card_No.length() - 4));
                txt_CardExDate.setText(expiry_Date);

                lblPay.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {

                        JSONObject bodyParam3 = new JSONObject();
                        try
                        {
                            bodyParam3.accumulate("ForTranId", PaymentBundle.getInt("forTranID"));
                            bodyParam3.accumulate("billID", PaymentBundle.getInt("billID"));
                            bodyParam3.accumulate("CardName", creditCardJSON.get("card_Name"));
                            bodyParam3.accumulate("CreditCardNo", creditCardJSON.getString("card_No"));
                            bodyParam3.accumulate("ExpiryDate", creditCardJSON.getString("expiry_Date"));
                            bodyParam3.accumulate("CVSNo", creditCardJSON.getString("cvS_Code"));
                            bodyParam3.accumulate("Amount",PaymentBundle.getDouble("totalAmount"));
                            bodyParam3.accumulate("Street", creditCardJSON.getString("card_PStreet"));
                            bodyParam3.accumulate("UnitNo", creditCardJSON.getString("card_PUnitNo"));
                            bodyParam3.accumulate("City", creditCardJSON.getString("card_PCity"));
                            bodyParam3.accumulate("CountryID", creditCardJSON.getInt("card_PCountry"));
                            bodyParam3.accumulate("StateID", creditCardJSON.getInt("card_PState"));
                            bodyParam3.accumulate("ZipCode", creditCardJSON.getString("card_SZipCode"));
                            bodyParam3.accumulate("TransType", 0);
                            bodyParam3.accumulate("ChargeType", 0);
                            bodyParam3.accumulate("Type", 1);
                            bodyParam3.accumulate("Remark", "");
                            bodyParam3.accumulate("CardType", "visa");
                            bodyParam3.accumulate("CountryCode", "CA");
                            bodyParam3.accumulate("StateName", "ONTARIO");
                            bodyParam3.accumulate("MobileNumber", "9921023213");
                            bodyParam3.accumulate("CurrencyISO", "USD");
                            bodyParam3.accumulate("CustomerId", Integer.parseInt(id));
                            bodyParam3.accumulate("Email", "info@customer.com");

                            System.out.println(creditCardJSON);
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }

                       /* ApiService ApiService1 = new ApiService(processPayment, RequestType.POST,
                                PAYMENTPROCESS, BASE_URL_PAYMENT, new HashMap<String, String>(), bodyParam3);*/

                    }
                });
            }
            catch (Exception e)
            {

/*
                ApiService ApiService = new ApiService(GetDefaultCreditCard, RequestType.GET,
                        GETDEFAULTCREDITCARD, BASE_URL_BOOKING, new HashMap<String, String>(), bodyParam1);*/

            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_payment_receipt_2;
    }

    OnResponseListener GetDefaultCreditCard = new OnResponseListener()
    {
        @Override
        public void onSuccess(final String response, HashMap<String, String> headers)
        {
            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    try {
                        System.out.println("Success");
                        System.out.println(response);

                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("status");

                        if (status)
                        {
                            try
                            {
                                JSONObject resultSet = responseJSON.getJSONObject("resultSet");
                                creditCardJSON = resultSet.getJSONObject("t0050_Customer_Card_Details");


                                final int card_ID=creditCardJSON.getInt("card_ID");
                                final int customer_ID=creditCardJSON.getInt("customer_ID");
                                final String card_Type_ID=creditCardJSON.getString("card_Type_ID");
                                final String card_No=creditCardJSON.getString("card_No");
                                final String card_Name=creditCardJSON.getString("card_Name");
                                final String expiry_Date=creditCardJSON.getString("expiry_Date");
                                final String cvS_Code=creditCardJSON.getString("cvS_Code");

                                txt_CardNo.setText("**** **** **** "+card_No.substring(card_No.length()-4));
                                txt_CardExDate.setText("("+expiry_Date+")");

                                lblPay.setOnClickListener(new View.OnClickListener()
                                {
                                    @Override
                                    public void onClick(View view)
                                    {

                                        JSONObject bodyParam3 = new JSONObject();
                                        try
                                        {
                                            bodyParam3.accumulate("ForTranId", PaymentBundle.getInt("forTranID"));
                                            bodyParam3.accumulate("billID", PaymentBundle.getInt("billID"));
                                            bodyParam3.accumulate("CardName", creditCardJSON.get("card_Name"));
                                            bodyParam3.accumulate("CreditCardNo", creditCardJSON.getString("card_No"));
                                            bodyParam3.accumulate("ExpiryDate", creditCardJSON.getString("expiry_Date"));
                                            bodyParam3.accumulate("CVSNo", creditCardJSON.getString("cvS_Code"));
                                            bodyParam3.accumulate("Amount",PaymentBundle.getDouble("totalAmount"));
                                            bodyParam3.accumulate("Street", creditCardJSON.getString("card_PStreet"));
                                            bodyParam3.accumulate("UnitNo", creditCardJSON.getString("card_PUnitNo"));
                                            bodyParam3.accumulate("City", creditCardJSON.getString("card_PCity"));
                                            bodyParam3.accumulate("CountryID", creditCardJSON.getInt("card_PCountry"));
                                            bodyParam3.accumulate("StateID", creditCardJSON.getInt("card_PState"));
                                            bodyParam3.accumulate("ZipCode", creditCardJSON.getString("card_SZipCode"));
                                            bodyParam3.accumulate("TransType", 0);
                                            bodyParam3.accumulate("ChargeType", 0);
                                            bodyParam3.accumulate("Type", 1);
                                            bodyParam3.accumulate("Remark", "");
                                            bodyParam3.accumulate("CardType", "visa");
                                            bodyParam3.accumulate("CountryCode", "CA");
                                            bodyParam3.accumulate("StateName", "ONTARIO");
                                            bodyParam3.accumulate("MobileNumber", "9921023213");
                                            bodyParam3.accumulate("CurrencyISO", "USD");
                                            bodyParam3.accumulate("CustomerId", Integer.parseInt(id));
                                            bodyParam3.accumulate("Email", "info@customer.com");

                                            System.out.println(creditCardJSON);
                                        }
                                        catch (JSONException e)
                                        {
                                            e.printStackTrace();
                                        }

                                       /* ApiService ApiService1 = new ApiService(processPayment, RequestType.POST,
                                                PAYMENTPROCESS, BASE_URL_PAYMENT, new HashMap<String, String>(), bodyParam3);*/

                                    }
                                });

                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }

                        else
                        {
                            String errorString = responseJSON.getString("message");
                            CustomToast.showToast(getActivity(),errorString,1);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }
        @Override
        public void onError(String error) {
            System.out.println("Error-" + error);
        }
    };


    OnResponseListener processPayment = new OnResponseListener()
    {
        @Override
        public void onSuccess(final String response, HashMap<String, String> headers)
        {
            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    try {
                        System.out.println("Success");
                        System.out.println(response);

                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("status");

                        if (status)
                        {
                            try
                            {
                                transactionId = responseJSON.getString("transactionId");
                                String message = responseJSON.getString("message");
                                CustomToast.showToast(getActivity(),message,0);

                                Bundle paymentBundle = new Bundle();
                                paymentBundle.putInt("sendTo",2);
                                paymentBundle.putString("message",message);
                                paymentBundle.putString("transactionId",transactionId);
                                paymentBundle.putDouble("total",PaymentBundle.getDouble("totalAmount"));

                                NavHostFragment.findNavController(Fragment_Payment_Reciept_2.this)
                                        .navigate(R.id.action_Payment_Reciept_2_Payment_Success, paymentBundle);
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }

                        else
                        {
                            String errorString = responseJSON.getString("message");
                            CustomToast.showToast(getActivity(),errorString,1);
                        }
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
        }
        @Override
        public void onError(String error) {
            System.out.println("Error-" + error);
        }
    };

    OnResponseListener GetReceipt = new OnResponseListener() {
        @Override
        public void onSuccess(String response, HashMap<String, String> headers) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject object = new JSONObject(response);
                        String pdfFileName =    object.getString("Data");
                        String url1 = serverpath + pdfFileName;
                        //web_view.loadUrl("https://api.rentguruz.online/Uploads/35/Reports/PaymentReceiptReport.pdf");

                        //web_view.fromUri(Uri.parse(pdfFileName)).load();

                        binding.header.discard.setOnClickListener(v -> {
                            Log.e(TAG, "onClick: "+ pdfFileName.substring(pdfFileName.lastIndexOf('/'), pdfFileName.length()).split("/")[1]);
                            downloadFile(pdfFileName,pdfFileName.substring(pdfFileName.lastIndexOf('/'), pdfFileName.length()).split("/")[1]);
                        });


                        binding.header.optionmenu.setOnClickListener(v -> {
                            try {
                                Log.e(TAG, "onClick: " + pdfFileName );
                                Log.e(TAG, "onClick: " + new File(pdfFileName).toURI().toURL() );
                                ShareFile(new File(pdfFileName).toURI().toURL() );
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                        });

                        new RetrivePDFfromUrl().execute(pdfFileName);

                   /*     web_view.getSettings().setJavaScriptEnabled(true);
                        web_view.getSettings().setLoadWithOverviewMode(true);
                        web_view.getSettings().setUseWideViewPort(true);
                        web_view.getSettings().setAllowFileAccess(true);*/
                        // web_view.setWebViewClient(new WebViewClient());


                    } catch (Exception e){
                        e.printStackTrace();
                        fullProgressbar.dismiss();
                    }

                }
            });
        }

        @Override
        public void onError(String error) {

        }
    };

    @Override
    public void onClick(View v) {

    }

    public void ShareFile(URL path)
    {
        Intent shareToneIntent=new Intent(Intent.ACTION_SEND);
        shareToneIntent.setType("text/plain");
        shareToneIntent.putExtra(Intent.EXTRA_SUBJECT,"Insert Subject here");
        String data = "Your Receipt Copy " + path;
        shareToneIntent.putExtra(Intent.EXTRA_TEXT, data);
        //shareToneIntent.setType(GetMimeType(Uri.parse(path.toString())));
        // shareToneIntent.setType(String.valueOf(path));
        requireContext().startActivity(Intent.createChooser(shareToneIntent, "Share Via"));
    }

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
                fullProgressbar.dismiss();
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            // after the execution of our async
            // task we are loading our pdf in our pdf view.
            web_view.fromStream(inputStream).load();
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
