package com.rentguruz.app.home.vehicles;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.R;
import com.rentguruz.app.adapters.CustomToast;
import com.rentguruz.app.adapters.Helper;
import com.rentguruz.app.adapters.LoginRes;
import com.rentguruz.app.apicall.ApiService;
import com.rentguruz.app.apicall.OnResponseListener;
import com.rentguruz.app.apicall.RequestType;
import com.rentguruz.app.base.BaseFragment;
import com.rentguruz.app.databinding.FragmentUserTimelineBinding;
import com.rentguruz.app.flexiicar.user.User_Profile;
import com.rentguruz.app.model.DoHeader;
import com.rentguruz.app.model.base.UserData;
import com.rentguruz.app.model.response.TimeLine;
import com.rentguruz.app.model.response.TimelineData;
import com.rentguruz.app.model.response.VehicleModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

import static android.content.Context.MODE_PRIVATE;
import static android.graphics.Color.WHITE;
import static android.graphics.Color.colorSpace;
import static com.rentguruz.app.apicall.ApiEndPoint.BASE_URL_CUSTOMER;
import static com.rentguruz.app.apicall.ApiEndPoint.USERTIMELINE;
import static com.rentguruz.app.apicall.ApiEndPoint.VEHICLETIMELINE;

public class Fragment_Vehicle_Timeline extends BaseFragment
{
    ImageView backarrow,AddUserTimeLine;
    public String id = "";
    EditText Edt_searchUsertimeline;
    private HorizontalCalendar horizontalCalendar;
    int lens;
    JSONArray getActivityTimeLine;
    TimeLine timeLine;
    TimelineData timelineData;
    String selectedDateStr;
    static List<TimeLine> alltimeline = new ArrayList<>();
    ProgressBar progressBar;
    VehicleModel vehicleModel;
    FragmentUserTimelineBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        timeLine = new TimeLine();
        timelineData = new TimelineData();
        binding = FragmentUserTimelineBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        try {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            binding.setUiColor(UiColor);
            SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
            id = sp.getString(getString(R.string.id), "");
            id = "1";
            Calendar c = Calendar.getInstance();
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
            String datetime = dateformat.format(c.getTime());
            vehicleModel = new VehicleModel();
            vehicleModel  = (VehicleModel) getArguments().getSerializable("Model");

            AddUserTimeLine = view.findViewById(R.id.AddActivityimg);
            backarrow = view.findViewById(R.id.back );
            Edt_searchUsertimeline = view.findViewById(R.id.Edt_searchUsertimeline);
            progressBar = view.findViewById(R.id.progress_circular);
            progressBar.setVisibility(View.VISIBLE);

            JSONObject object = new JSONObject();
            List<Integer> ints = new ArrayList<>();
            JSONObject filter = new JSONObject();
            try
            {
                object.accumulate("limit", 0);
                object.accumulate("orderDir", "desc");
                object.accumulate("pageSize", 0);
                ints.add(10);
                ints.add(20);
                ints.add(30);
                ints.add(40);
                ints.add(50);
                object.accumulate("pageLimits", ints);
              //  filter.accumulate("CompanyId", Helper.di);
                filter.accumulate("Id", vehicleModel.Id);
                filter.accumulate("fStartDate","2021-01-01");
                filter.accumulate("fEndDate",datetime);
                object.accumulate("filterObj", filter);
                ApiService ApiServicees = new ApiService(ActivityTimeLineList, RequestType.POST,
                        VEHICLETIMELINE, BASE_URL_CUSTOMER,header, params.gettimeline(vehicleModel.Id));

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            backarrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavHostFragment.findNavController(Fragment_Vehicle_Timeline.this).popBackStack();
                   /* NavHostFragment.findNavController(Fragment_Vehicle_Timeline.this)
                            .navigate(R.id.action_User_timeline_to_User_Details);*/
                }
            });
            AddUserTimeLine.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try {
                    /*NavHostFragment.findNavController(Fragment_Vehicle_Timeline.this)
                            .navigate(R.id.action_User_timeline_to_Poston_Your_Timeline);*/
                }
                    catch (Exception e)
                {
                    e.printStackTrace();
                }
                }
            });
// horizontalCalendar
            Calendar startDate = Calendar.getInstance();
            startDate.add(Calendar.MONTH, -12);

            /* end after 1 month from now */
            Calendar endDate = Calendar.getInstance();
            endDate.add(Calendar.MONTH, 36);

            Drawable drawable = getResources().getDrawable(R.drawable.black_curve2);
            drawable.setTint(Color.parseColor(UiColor.secondary));

            horizontalCalendar = new HorizontalCalendar.Builder(getActivity(), R.id.calendar_View)
                    .range(startDate, endDate)
                    .datesNumberOnScreen(7)
                    .configure()
                    .formatTopText("EEE")
                    .formatMiddleText("dd")
                    .showTopText(true)
                    .showBottomText(false)
                    .textColor(WHITE, WHITE)
                    .selectedDateBackground(drawable)
                    .end()
                    .build();

            horizontalCalendar.setCalendarListener(new HorizontalCalendarListener()
            {
                @Override
                public void onDateSelected(Calendar date, int position)
                {
                    selectedDateStr = DateFormat.format("M/d/yyyy", date).toString();
                    showTimeline(selectedDateStr);
                }
            });


// filter_IconForTimeline
            Edt_searchUsertimeline.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    JSONObject bodyParam = new JSONObject();
                    try {
                        bodyParam.accumulate("CustomerId", Integer.parseInt(id));
                        bodyParam.accumulate("SearchString", Edt_searchUsertimeline.getText().toString());
                        //bodyParam.accumulate("ActivityType",0);
                        bodyParam.accumulate("SearchBy", 2);
                        System.out.println(bodyParam);
                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                  /*  ApiService ApiService = new ApiService(ActivityTimeLineList, RequestType.POST,
                            ACTIVITYTIMELINELIST, BASE_URL_CUSTOMER, new HashMap<String, String>(), bodyParam);*/
                }
            });

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    protected int getFragmentLayout() {
        return binding.getRoot().getId();
    }

    OnResponseListener ActivityTimeLineList = new OnResponseListener()
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
                        Boolean status = responseJSON.getBoolean("Status");

                        if (status)
                        {
                            try {
//                                JSONObject resultSet = responseJSON.getJSONObject("Data");
                                getActivityTimeLine = responseJSON.getJSONArray("Data");
                                Log.d(TAG, "run: " + getActivityTimeLine);


                                lens = getActivityTimeLine.length();
                                for (int i = 0; i < lens; i++) {
                                    final JSONObject date = (JSONObject) getActivityTimeLine.get(i);
                                    Log.d(TAG, "run: " + date.getString("Datetime"));
                                    timeLine = loginRes.getModel(date.toString(), TimeLine.class);
                                    alltimeline.add(timeLine);
                                   
                                   // final RelativeLayout rlActivity = getActivity().findViewById(R.id.rl_ActivityTimeLine);
                                   // rlActivity.removeAllViews();
                                 /*   for (int j = 0; j < alltimeline.get(i).items.size(); j++) {
                                        Log.d(TAG, "run: " + timeLine.items.get(j).Description);
                                      //  showTimeline(selectedDateStr);
                                        //   timelIne = loginRes.getModel(getActivityTimeLine.toString(), TimelIne.class);

                                        //  timelInes.add(timelIne);



                                        //  final LinearLayout rl_timeAndDate = getActivity().findViewById(R.id.rl_timeAndDate);
                                        // final LinearLayout rl_color = getActivity().findViewById(R.id.rl_color);
                                        // final LinearLayout rl_ActivityDesc = getActivity().findViewById(R.id.rl_ActivityDesc);



                                        int len;
                                        len = 2;

                                        //  for (int j = 0; j < len; j++)
                                      *//*  try {
//                                            final JSONObject test = (JSONObject) getActivityTimeLine.get(j);

                                  *//**//*          final int activityID = test.getInt("activityID");
                                            final String activityDate = test.getString("activityDate");
                                            final int activityType = test.getInt("activityType");
                                            final String activityTitle = test.getString("activityTitle");
                                            final String description = test.getString("description");
                                            final int transID = test.getInt("transID");
                                            final int cmpID = test.getInt("cmpID");
                                            final String activityName = test.getString("activityName");

                                            final String activityColor = test.getString("activityColor");*//**//*


                                            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                                            lp.addRule(RelativeLayout.BELOW, (200 + j - 1));
                                            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                                            lp.setMargins(0, 0, 0, 0);

                                            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                            final LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.list_activity_timeline, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
                                            linearLayout.setId(200 + j);
                                            linearLayout.setLayoutParams(lp);

                                            TextView txt_Activityname, ActivityTime, ActivityDate, Activity_Desc, ActivityTitle;

                                            LinearLayout UpdateActivity = linearLayout.findViewById(R.id.post_on_timeline);
                                            LinearLayout linearLayoutBG = linearLayout.findViewById(R.id.LinearLayoutActivity);
                                            Activity_Desc = linearLayout.findViewById(R.id.Activity_Desc);
                                            ActivityTitle = linearLayout.findViewById(R.id.ActivityTitle);

                                            Activity_Desc.setText(timeLine.items.get(j).Description);
                                            ActivityTitle.setText(timeLine.items.get(j).TimelineTypeDescription);

                                            ImageView img = linearLayout.findViewById(R.id.circle_bg);
                                      //      img.setColorFilter(parseColor(activityColor), PorterDuff.Mode.SRC_IN);

                                      //      linearLayoutBG.setBackgroundTintList(ColorStateList.valueOf(parseColor(activityColor)));
                                            //
                                     *//**//*  LayoutInflater inflater1 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                        final LinearLayout linearLayout1 = (LinearLayout) inflater1.inflate(R.layout.list_activity_timeline_color, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
                                        linearLayout1.setId(200 + j);
                                        linearLayout1.setLayoutParams(lp);



                                         //
                                        LayoutInflater inflater2 = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                        final LinearLayout linearLayout2 = (LinearLayout) inflater2.inflate(R.layout.list_activity_timeline_date, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
                                        linearLayout2.setId(200 + j);
                                        linearLayout2.setLayoutParams(lp);*//**//*

                                            txt_Activityname = linearLayout.findViewById(R.id.txt_Activityname);
                                            ActivityTime = linearLayout.findViewById(R.id.ActivityTime);
                                            ActivityDate = linearLayout.findViewById(R.id.ActivityDate);

                                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                                            Date dates = dateFormat.parse(timeLine.items.get(j).Datetime);

                                            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd  yyyy");
                                            String stractivityDate = sdf.format(dates);

                                            SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm aa", Locale.US);
                                            String ActivityTimeStr = sdf1.format(dates);


                                            txt_Activityname.setText(timeLine.items.get(j).TimelineTypeDescription);
                                            ActivityTime.setText(ActivityTimeStr);
                                            ActivityDate.setText(stractivityDate);

                                            rlActivity.addView(linearLayout);

                                            UpdateActivity.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Bundle ActivityTimeLine = new Bundle();
                                           *//**//*         ActivityTimeLine.putInt("activityID", activityID);
                                                    ActivityTimeLine.putString("activityDate", activityDate);
                                                    ActivityTimeLine.putInt("activityType", activityType);
                                                    ActivityTimeLine.putString("activityTitle", activityTitle);
                                                    ActivityTimeLine.putString("description", description);
                                                    ActivityTimeLine.putInt("transID", transID);
                                                    ActivityTimeLine.putInt("cmpID", cmpID);
                                                    ActivityTimeLine.putString("activityName", activityName);*//**//*
                                                    Bundle ActivityBundle = new Bundle();
                                                    ActivityBundle.putBundle("ActivityTimeLine", ActivityTimeLine);
                                                    NavHostFragment.findNavController(Fragment_User_Timeline.this)
                                                            .navigate(R.id.action_User_timeline_to_Update_CutomerActivity, ActivityBundle);
                                                }
                                            });
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }*//*
                                    }*/
                                }
                                showTimeline("");
                                progressBar.setVisibility(View.GONE);
                            }catch (Exception e)
                            {
                                e.printStackTrace();
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
        public void onError(String error) {
            System.out.println("Error-" + error);
        }
    };

    public void showTimeline(String Udate){
        if (Udate.equals("")){
            Udate = DateFormat.format("M/d/yyyy", new Date()).toString();
        } 
        final RelativeLayout rlActivity = getActivity().findViewById(R.id.rl_ActivityTimeLine);
        rlActivity.removeAllViews();
        lens = alltimeline.size();

        try {
            for (int i = 0; i < lens; i++){
                final JSONObject date = (JSONObject) getActivityTimeLine.get(i);
                if ( alltimeline.get(i).Datetime.equals(Udate)){
                    for (int j = 0; j <alltimeline.get(i).items.size(); j++){
                        final LinearLayout rl_color = getActivity().findViewById(R.id.rl_color);
                        rl_color.setVisibility(View.GONE);
                        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                        lp.addRule(RelativeLayout.BELOW, (200 + j - 1));
                        lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                        lp.setMargins(0, 0, 0, 0);

                        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.list_activity_timeline, (ViewGroup) getActivity().findViewById(android.R.id.content), false);
                        linearLayout.setId(200 + j);
                        linearLayout.setLayoutParams(lp);

                        TextView txt_Activityname, ActivityTime, ActivityDate, Activity_Desc, ActivityTitle;

                        LinearLayout UpdateActivity = linearLayout.findViewById(R.id.post_on_timeline);
                        LinearLayout linearLayoutBG = linearLayout.findViewById(R.id.LinearLayoutActivity);
                        Activity_Desc = linearLayout.findViewById(R.id.Activity_Desc);
                        ActivityTitle = linearLayout.findViewById(R.id.ActivityTitle);

                        Activity_Desc.setText(alltimeline.get(i).items.get(j).Description);
                        ActivityTitle.setText(alltimeline.get(i).items.get(j).TimelineTypeDescription);

                        ImageView img = linearLayout.findViewById(R.id.circle_bg);

                        txt_Activityname = linearLayout.findViewById(R.id.txt_Activityname);
                        ActivityTime = linearLayout.findViewById(R.id.ActivityTime);
                        ActivityDate = linearLayout.findViewById(R.id.ActivityDate);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                        Date dates = dateFormat.parse(alltimeline.get(i).items.get(j).Datetime);

                        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd  yyyy");
                        String stractivityDate = sdf.format(dates);

                        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm aa", Locale.US);
                        String ActivityTimeStr = sdf1.format(dates);


                        txt_Activityname.setText(alltimeline.get(i).items.get(j).TimelineTypeDescription);
                        ActivityTime.setText(ActivityTimeStr);
                        ActivityDate.setText(stractivityDate);

                        rlActivity.addView(linearLayout);

                        UpdateActivity.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle ActivityTimeLine = new Bundle();
                                           /*         ActivityTimeLine.putInt("activityID", activityID);
                                                    ActivityTimeLine.putString("activityDate", activityDate);
                                                    ActivityTimeLine.putInt("activityType", activityType);
                                                    ActivityTimeLine.putString("activityTitle", activityTitle);
                                                    ActivityTimeLine.putString("description", description);
                                                    ActivityTimeLine.putInt("transID", transID);
                                                    ActivityTimeLine.putInt("cmpID", cmpID);
                                                    ActivityTimeLine.putString("activityName", activityName);*/
                                Bundle ActivityBundle = new Bundle();
                                ActivityBundle.putBundle("ActivityTimeLine", ActivityTimeLine);
                                NavHostFragment.findNavController(Fragment_Vehicle_Timeline.this)
                                        .navigate(R.id.action_User_timeline_to_Update_CutomerActivity, ActivityBundle);
                            }
                        });

                    }
                }
            }


        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {

    }
}



