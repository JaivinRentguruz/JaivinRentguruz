package com.rentguruz.app.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import com.rentguruz.app.R;
import com.rentguruz.app.databinding.ListChargesBinding;
import com.rentguruz.app.databinding.RowSummarryChargeHeadBinding;
import com.rentguruz.app.databinding.VehicleTaxDetailsBinding;
import com.rentguruz.app.model.response.ReservationSummaryModels;

import java.util.ArrayList;

public class SummaryDisplay {

    public Activity activity;
    public RelativeLayout.LayoutParams subparams;
    public LayoutInflater subinflater;
    public SummaryDisplay(Activity activity) {
        this.activity = activity;
    }

    public void getB2BSummarry(Bundle bundle, ReservationSummaryModels[] charges,RelativeLayout relativeLayout){
    for (int i = 0; i <charges.length ; i++) {

        subparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        subparams.addRule(RelativeLayout.BELOW, (200 + i - 1));
        subparams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        subparams.setMargins(0, 10, 0, 0);
        subinflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        RowSummarryChargeHeadBinding rowSummarryChargeHeadBinding = RowSummarryChargeHeadBinding.inflate(subinflater, activity.findViewById(android.R.id.content), false );
        rowSummarryChargeHeadBinding.getRoot().setId(200 + i);
        rowSummarryChargeHeadBinding.getRoot().setLayoutParams(subparams);
        rowSummarryChargeHeadBinding.chargename.setText(charges[i].SummaryName);
        rowSummarryChargeHeadBinding.charge.setText(Helper.getAmtount( charges[i].TotalAmount,true));

        if (charges[i].ReservationSummaryType==100){
            bundle.putString("netrate",DigitConvert.getDoubleDigit(charges[i].ReservationSummaryDetailModels[0].Total));
        }

                        /*    if (i==1)
                                rowSummarryChargeHeadBinding.charge.setTextColor(getResources().getColor(R.color.txt11blue));

                            if (i==3)
                                rowSummarryChargeHeadBinding.charge.setTextColor(getResources().getColor(R.color.txt11lightyellow));*/

        for (int j = 0; j <charges[i].ReservationSummaryDetailModels.length ; j++) {

            subparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            subparams.addRule(RelativeLayout.BELOW, (200 + j - 1));
            subparams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            subparams.setMargins(0, 10, 0, 0);
            subinflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            ListChargesBinding listChargesBinding = ListChargesBinding.inflate(subinflater, activity.findViewById(android.R.id.content), false );
            listChargesBinding.getRoot().setId(200 + j);
            listChargesBinding.getRoot().setLayoutParams(subparams);

            listChargesBinding.textHeader.setText(charges[i].ReservationSummaryDetailModels[j].Title);
            listChargesBinding.textdetail.setText(charges[i].ReservationSummaryDetailModels[j].Description);
/*
                                Drawable unwrappedDrawable = AppCompatResources.getDrawable(context, R.drawable.ic_ellipse_default);
                                Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
                                if (i == 1) {
                                    DrawableCompat.setTint(wrappedDrawable, getResources().getColor(R.color.txt11blue));
                                } else {
                                    DrawableCompat.setTint(wrappedDrawable, getResources().getColor(R.color.txt11lightyellow));
                                }*/

            // listChargesBinding.icon.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.AppBlack)));
            if (i==0){
                rowSummarryChargeHeadBinding.charge.setTextColor(activity.getResources().getColor(R.color.green));
                listChargesBinding.icon.setColorFilter(activity.getResources().getColor(R.color.green));
            } else if (i==1) {
                rowSummarryChargeHeadBinding.charge.setTextColor(activity.getResources().getColor(R.color.txt11blue));
                listChargesBinding.icon.setColorFilter(activity.getResources().getColor(R.color.txt11blue));
            } else if (i==2) {
                rowSummarryChargeHeadBinding.charge.setTextColor(activity.getResources().getColor(R.color.txt11lightyellow));
                listChargesBinding.icon.setColorFilter(activity.getResources().getColor(R.color.txt11lightyellow));
            } else if (i==3){
                rowSummarryChargeHeadBinding.charge.setTextColor(activity.getResources().getColor(R.color.txt11navyblue));
                listChargesBinding.icon.setColorFilter(activity.getResources().getColor(R.color.txt11navyblue));
            } else if (i==4) {
                rowSummarryChargeHeadBinding.charge.setTextColor(activity.getResources().getColor(R.color.txt11blue));
                listChargesBinding.icon.setColorFilter(activity.getResources().getColor(R.color.txt11blue));
            }else if (i==5){
                rowSummarryChargeHeadBinding.charge.setTextColor(activity.getResources().getColor(R.color.txt11navyblue));
                listChargesBinding.icon.setColorFilter(activity.getResources().getColor(R.color.txt11navyblue));
            }


            try {
                if (charges[i].ReservationSummaryDetailModels[j].Title.length() != 0) {
                    rowSummarryChargeHeadBinding.listsummarry.addView(listChargesBinding.getRoot());
                                    rowSummarryChargeHeadBinding.sumarrydetail.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (rowSummarryChargeHeadBinding.listsummarry.getVisibility() == View.VISIBLE){

                                                rowSummarryChargeHeadBinding.listsummarry.setVisibility(View.GONE);
                                            } else {
                                                rowSummarryChargeHeadBinding.listsummarry.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    });
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        relativeLayout.addView(rowSummarryChargeHeadBinding.getRoot());
    }
    }

    public void getB2CSummarry(Bundle bundle, ReservationSummaryModels[] charges,RelativeLayout relativeLayout){
        for (int i = 0; i <charges.length ; i++) {
            subparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            subparams.addRule(RelativeLayout.BELOW, (200 + i - 1));
            subparams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            subparams.setMargins(0, 10, 0, 0);
            subinflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            VehicleTaxDetailsBinding vehicleTaxDetailsBinding = VehicleTaxDetailsBinding.inflate(subinflater, activity.findViewById(android.R.id.content), false );
                            vehicleTaxDetailsBinding.getRoot().setId(200 + i);
                            vehicleTaxDetailsBinding.getRoot().setLayoutParams(subparams);
                            vehicleTaxDetailsBinding.txtChargeName.setText(charges[i].SummaryName);
                            vehicleTaxDetailsBinding.txtCharge.setText(Helper.getAmtount( charges[i].TotalAmount,false));
                            int finalI = i;
                            vehicleTaxDetailsBinding.txtChargeName.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (vehicleTaxDetailsBinding.details.getVisibility() == View.VISIBLE){
                                        vehicleTaxDetailsBinding.details.setVisibility(View.GONE);
                                    } else {
                                        vehicleTaxDetailsBinding.details.setVisibility(View.VISIBLE);
                                        String join ="";
                                        for (int j = 0; j <charges[finalI].ReservationSummaryDetailModels.length ; j++) {
                                            vehicleTaxDetailsBinding.txtDisc.setText(charges[finalI].ReservationSummaryDetailModels[j].Title + " "+
                                                    charges[finalI].ReservationSummaryDetailModels[j].Description);
                                            if (charges[finalI].ReservationSummaryDetailModels[j].Title == charges[finalI].ReservationSummaryDetailModels[j].Description){
                                                vehicleTaxDetailsBinding.details.setVisibility(View.GONE);
                                            }

                                            join +=  charges[finalI].ReservationSummaryDetailModels[j].Title + " "+
                                                    charges[finalI].ReservationSummaryDetailModels[j].Description+"\n";
                                            if (join.trim().matches("null null"))
                                                vehicleTaxDetailsBinding.details.setVisibility(View.GONE);
                                            vehicleTaxDetailsBinding.txtDisc.setText(join.trim());
                                        }

                                    }
                                }
                            });
                            relativeLayout.addView(vehicleTaxDetailsBinding.getRoot());
                        }
    }

    public String getDatafrom(ArrayList<ReservationSummaryModels> charges, int summarry){
        String data = null;
        for (ReservationSummaryModels d:charges) {
            if (d.ReservationSummaryType == summarry){
              data = String.valueOf(d.TotalAmount);
                /*for (int i = 0; i <d.ReservationSummaryDetailModels.length ; i++) {
                    if (d.ReservationSummaryDetailModels[i].Total == 0){

                    }
                }*/

            }
        }
        return data;
    }

    public String getDatafrom(ReservationSummaryModels[] charges,int summarry ){
        String data = null;
     /*   for (int i = 0; i <charges.length ; i++) {
            if (charges[i].ReservationSummaryType == 100){
                data = String.valueOf(charges[i].ReservationSummaryDetailModels[0].Total);
            }
        }*/

        for (ReservationSummaryModels d:charges) {
            if (d.ReservationSummaryType == summarry){
                data = String.valueOf(d.TotalAmount);
            }
        }
        return data;
    }

    public String getMileage(ReservationSummaryModels[] charges){
        String data = null;
        for (ReservationSummaryModels d:charges) {
            if (d.ReservationSummaryType == 1){
                for (int i = 0; i <d.ReservationSummaryDetailModels.length ; i++) {
                    if (d.ReservationSummaryDetailModels[i].ReservationSummaryDetailType == 101) {
                        data = Helper.getDistance(d.ReservationSummaryDetailModels[i].Total);
                        if (d.ReservationSummaryDetailModels[i].Total == 0) {
                            data = d.ReservationSummaryDetailModels[i].Description.trim();
                        }
                    }
                }
            }
        }
        return data;
    }
}
