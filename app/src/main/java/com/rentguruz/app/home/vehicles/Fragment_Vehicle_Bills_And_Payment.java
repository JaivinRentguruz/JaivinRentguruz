package com.rentguruz.app.home.vehicles;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.R;
import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.adapters.Helper;
import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentBillsAndPaymentsBinding;
import com.rentguruz.app.databinding.ListAccountStatementBinding;
import com.rentguruz.app.model.base.UserData;
import com.rentguruz.app.model.response.VehicleModel;
import com.androidnetworking.AndroidNetworking;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_LOGIN;
import static com.rentguruz.app.apicall.ApiEndPoint.PMTGETALL;

public class Fragment_Vehicle_Bills_And_Payment extends BaseFragment
{
    ImageView backarrow;
   // Handler handler = new Handler();
    public static Context context;
    public String id = "";
    EditText edt_search;
    LinearLayout filter_icon;
    Boolean StatmentType;
    Bundle AccountFilterList;
    TextView txtDiscard;
    JSONArray getAccountstatementlist = new JSONArray();
    VehicleModel vehicleModel;
   /* DoHeader header;
    CommonParams params;*/
    FragmentBillsAndPaymentsBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
       /* header = new DoHeader();
        header.exptime = "7/24/2021 11:47:18 PM";
        header.token = "f00498bf-efe8-4e67-a3fa-2e4c5fc6aeea";
        header.islogin = "1";
        header.ut =  "PYOtYmuTsLQ=";
        params = new CommonParams();*/
        binding = FragmentBillsAndPaymentsBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        binding.setUiColor(UiColor);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding.header.screenHeader.setText("Bills Payments");
        backarrow=view.findViewById(R.id.back);
        edt_search=view.findViewById(R.id.edt_searchAccountStatment);
        filter_icon=view.findViewById(R.id.filter_iconForAC);
        txtDiscard=view.findViewById(R.id.discard);

        vehicleModel = new VehicleModel();
        vehicleModel  = (VehicleModel) getArguments().getSerializable("Model");


        StatmentType = getArguments().getBoolean("StatmentType");
        System.out.println(StatmentType);

        backarrow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                NavHostFragment.findNavController(Fragment_Vehicle_Bills_And_Payment.this)
                        .popBackStack();
                       // .navigate(R.id.action_Bills_and_Payment_to_User_Details);
            }
        });
        txtDiscard.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                NavHostFragment.findNavController(Fragment_Vehicle_Bills_And_Payment.this)
                        .popBackStack();
                        //.navigate(R.id.action_Bills_and_Payment_to_User_Details);
            }
        });
        SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
        id = sp.getString(getString(R.string.id), "");

        new ApiService(GetAccountstatementlist,RequestType.POST,PMTGETALL, BASE_URL_LOGIN,header,params.statmentvbyvehicle(vehicleModel.Id));

        if (StatmentType)
        {
            String bodyParam = "";

            try
            {
                bodyParam+="customerId="+id;

                System.out.println(bodyParam);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }


          /*  ApiService ApiService = new ApiService(GetAccountstatementlist, RequestType.GET,
                    GETACCOUNTSTATEMENT, BASE_URL_CUSTOMER, new HashMap<String, String>(), bodyParam);*/

            //  ApiService apiService2 = new ApiService(GetAccountstatementlist,RequestType.POST,PMTGETALL, BASE_URL_LOGIN,header,params.payment(UserData.loginResponse.User.UserFor));
        }
        else
        {
            try {
                AccountFilterList = getArguments().getBundle("AccountFilterList");

                int billStatus = AccountFilterList.getInt("BillStatus");
                String StartDateStr = AccountFilterList.getString("FilterStartDate");
                String EndDateStr = AccountFilterList.getString("FilterEndDate");
                String FromAmountStr = AccountFilterList.getString("FilterFromAmount");
                String ToAmountStr = AccountFilterList.getString("FilterToAmount");
                String TransacTypeStr = AccountFilterList.getString("FilterTransacType");

                JSONObject bodyParam = new JSONObject();
                try {
                    bodyParam.accumulate("CustomerID", Integer.parseInt(id));
                    bodyParam.accumulate("BillID", 0);
                    if(billStatus > 0)
                        bodyParam.accumulate("BillStatus", (billStatus-1));
                    bodyParam.accumulate("FilterStartDate", StartDateStr);
                    bodyParam.accumulate("FilterEndDate", EndDateStr);
                    bodyParam.accumulate("FilterToAmount", ToAmountStr);
                    bodyParam.accumulate("FilterFromAmount", FromAmountStr);
                    bodyParam.accumulate("FilterTransacType", TransacTypeStr);
                    System.out.println(bodyParam);
                } catch (JSONException e)
                {
                    e.printStackTrace();
                }
            /*    ApiService ApiService = new ApiService(GetAccountstatementlist, RequestType.POST,
                        GETACCOUNTSTATEMENT, BASE_URL_CUSTOMER, new HashMap<String, String>(), bodyParam);*/

                //     ApiService apiService3 = new ApiService(GetAccountstatementlist,RequestType.POST,PMTGETALL, BASE_URL_LOGIN,header,params.payment(UserData.loginResponse.User.UserFor));

            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        edt_search.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                try
                {
                    final RelativeLayout rlAccountStatement = getActivity().findViewById(R.id.rl_AccountStatement);
                    int len;
                    len = getAccountstatementlist.length();
                    rlAccountStatement.removeAllViews();

                    for (int j = 0; j < len; j++)
                    {
                        final JSONObject test = (JSONObject) getAccountstatementlist.get(j);
                        final String transacType = test.getString("TransactionType");

                        if (transacType.contains(charSequence))
                        {
                         /*   final int billID = test.getInt("billID");
                            final int billStatus = test.getInt("billStatus");
                            final String forDate = test.getString("forDate");
                            final String billNumber = test.getString("billNumber");
                            final double totalAmount = test.getDouble("totalAmount");
                            final double paid = test.getDouble("paid");
                            final double balance = test.getDouble("balance");
                            final int forTranID = test.getInt("forTranID");
                            final String createdOn = test.getString("createdOn");
                            final int modifiedBy = test.getInt("modifiedBy");
                            final String path = test.getString("path");
                            final int type = test.getInt("type");
                            final String description = test.getString("description");*/

                            String dates =test.getString("CreatedDate");
                            //  String transacType = test.getString("");
                            Double amount = test.getDouble("Amount");
                            String billNumber = test.getString("Id");
                            String description = test.getString("PaymentForName");

                            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                            lp.addRule(RelativeLayout.BELOW, (200 + j - 1));
                            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                            lp.setMargins(0, 10, 0, 0);

                            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            final LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.list_account_statement, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
                            linearLayout.setId(200 + j);
                            linearLayout.setLayoutParams(lp);

                            LinearLayout layout_invoice = linearLayout.findViewById(R.id.layout_invoice);
                            LinearLayout Payment_Status_Layout = linearLayout.findViewById(R.id.Payment_Status_Layout);
                            final TextView Name = linearLayout.findViewById(R.id.txt_StatementName);
                            TextView txtOneTimeCharge = linearLayout.findViewById(R.id.txt_oneTimeCharge);
                            TextView txtDate = linearLayout.findViewById(R.id.txtDate);
                            TextView txtMonth = linearLayout.findViewById(R.id.txtMonth);
                            TextView txtinvoiceNo = linearLayout.findViewById(R.id.txt_invoiceNo);
                            TextView txtPaymentStatus = linearLayout.findViewById(R.id.txt_PaymentStatus);
                            TextView txt_TotalAmount = linearLayout.findViewById(R.id.text_totalAmount);

                  /*          if (billStatus == 1)
                            {
                                txtPaymentStatus.setText("PAID");
                                Payment_Status_Layout.setBackgroundResource(R.drawable.ic_rectangle_darkgreen);
                                Name.setTextColor(getResources().getColor(R.color.footerButtonBC));
                            } else {
                                txtPaymentStatus.setText("UNPAID");
                                Payment_Status_Layout.setBackgroundResource(R.drawable.ic_rectangle_red);
                                Name.setTextColor(getResources().getColor(R.color.btn2));
                            }*/
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                            Date date = dateFormat.parse(dates);

                            SimpleDateFormat sdf = new SimpleDateFormat("MMMM");
                            String Monthstr = sdf.format(date);

                            SimpleDateFormat sdf1 = new SimpleDateFormat("dd,yyyy");
                            String datestr = sdf1.format(date);

                            txtMonth.setText(Monthstr);
                            txtDate.setText(datestr);

                            if (transacType.equals(1))
                                Name.setText(companyLabel.Payment);
                            else
                                Name.setText(companyLabel.Deposit);

                            txt_TotalAmount.setText(((String.format(Locale.US, "%.2f", amount))));
                            txtinvoiceNo.setText(billNumber);
                            txtOneTimeCharge.setText(description);
                            rlAccountStatement.addView(linearLayout);

                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });
    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    OnResponseListener GetAccountstatementlist = new OnResponseListener()
    {
        @Override
        public void onSuccess(final String response, HashMap<String, String> headers)
        {
            handler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        System.out.println("Success");
                        System.out.println(response);

                        JSONObject responseJSON = new JSONObject(response);
                        Boolean status = responseJSON.getBoolean("Status");

                        if (status)
                        {
                            JSONObject resultSet = responseJSON.getJSONObject("Data");
                            getAccountstatementlist = resultSet.getJSONArray("Data");

                            final RelativeLayout rlAccountStatement = getActivity().findViewById(R.id.rl_AccountStatement);
                            int len;
                            len = getAccountstatementlist.length();

                            for (int j = 0; j < len; j++)
                            {
                                final JSONObject test = (JSONObject) getAccountstatementlist.get(j);

                           /*     final int billID = test.getInt("billID");
                                final int billStatus = test.getInt("billStatus");
                                final String forDate = test.getString("forDate");
                                final String billNumber = test.getString("billNumber");
                                final String transacType = test.getString("transacType");
                                final double totalAmount = test.getDouble("totalAmount");
                                final double paid = test.getDouble("paid");
                                final double balance = test.getDouble("balance");
                                final int forTranID = test.getInt("forTranID");
                                final String createdOn = test.getString("createdOn");
                                final int modifiedBy = test.getInt("modifiedBy");
                                final String path = test.getString("path");
                                final int type = test.getInt("type");
                                final String description = test.getString("description");*/


                                String dates =test.getString("CreatedDate");
                                String transacType = test.getString("TransactionType");
                                String TransactionTypeDesc = test.getString("TransactionTypeDesc");
                                Double amount = test.getDouble("Amount");
                                String billNumber = test.getString("Id");
                                String description = test.getString("PaymentForName");

                                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                lp.addRule(RelativeLayout.BELOW, (200 + j - 1));
                                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                lp.setMargins(0, 10, 0, 0);

                                LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                                final LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.list_account_statement, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
                                linearLayout.setId(200 + j);
                                linearLayout.setLayoutParams(lp);
                                getSubview(j);
                                ListAccountStatementBinding listAccountStatementBinding = ListAccountStatementBinding.inflate(subinflater,
                                        getActivity().findViewById(android.R.id.content), false);
                                listAccountStatementBinding.getRoot().setId(200 + j);
                                listAccountStatementBinding.getRoot().setLayoutParams(subparams);
                                listAccountStatementBinding.setUiColor(UiColor);

                               // linearLayout.addView(listAccountStatementBinding.getRoot());

                                LinearLayout layout_invoice=linearLayout.findViewById(R.id.layout_invoice);
                                LinearLayout Payment_Status_Layout=linearLayout.findViewById(R.id.Payment_Status_Layout);
                                final TextView Name = linearLayout.findViewById(R.id.txt_StatementName);
                                TextView txtOneTimeCharge = linearLayout.findViewById(R.id.txt_oneTimeCharge);
                                TextView txtDate= linearLayout.findViewById(R.id.txtDate);
                                TextView txtMonth= linearLayout.findViewById(R.id.txtMonth);
                                TextView txtinvoiceNo = linearLayout.findViewById(R.id.txt_invoiceNo);
                                TextView txtPaymentStatus = linearLayout.findViewById(R.id.txt_PaymentStatus);
                                TextView txt_TotalAmount = linearLayout.findViewById(R.id.text_totalAmount);
                                TextView currency = linearLayout.findViewById(R.id.currency);

                                linearLayout.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        bundle.putInt("IId", Integer.parseInt(billNumber));
                                        NavHostFragment.findNavController(Fragment_Vehicle_Bills_And_Payment.this)
                                                .navigate(R.id.action_Bills_and_Payment_to_Payment_Reciept_2,bundle);
                                    }
                                });


                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                                Date date = dateFormat.parse(dates);

                                SimpleDateFormat sdf = new SimpleDateFormat("MMMM");
                                String Monthstr = sdf.format(date);

                                SimpleDateFormat sdf1 = new SimpleDateFormat("dd,yyyy");
                                String datestr = sdf1.format(date);

                                listAccountStatementBinding.txtMonth.setText(Monthstr);
                                listAccountStatementBinding.txtDate.setText(datestr);
                                listAccountStatementBinding.txtStatementName.setText(TransactionTypeDesc);
                                listAccountStatementBinding.currency.setText(Helper.currencyName + " ");
                                listAccountStatementBinding.textTotalAmount.setText(((String.format(Locale.US, "%.2f", amount))));
                                listAccountStatementBinding.txtInvoiceNo.setText(billNumber);
                                listAccountStatementBinding.txtOneTimeCharge.setText(description);

                                txtMonth.setText(Monthstr);
                                txtDate.setText(datestr);

                                Name.setText(TransactionTypeDesc);

                              /*  if (transacType.equals(1))
                                    Name.setText("Payment");
                                else
                                Name.setText("Deposits");*/
                                currency.setText(Helper.currencyName + " ");
                                txt_TotalAmount.setText(((String.format(Locale.US, "%.2f", amount))));
                                txtinvoiceNo.setText(billNumber);
                                txtOneTimeCharge.setText(description);



                                rlAccountStatement.addView(listAccountStatementBinding.getRoot());
                            }


                        }
                        else
                        {
                            String errorString = responseJSON.getString("Message");
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
        public void onError(String error)
        {
            System.out.println("Error-" + error);
        }
    };

    @Override
    public void onClick(View v) {

    }
}
