package com.rentguruz.app.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import com.rentguruz.app.R;

import com.rentguruz.app.model.base.UserData;
import com.rentguruz.app.model.common.OnDropDownList;
import com.rentguruz.app.model.response.CompanyLabel;

import java.util.ArrayList;
import java.util.List;

public class CustomeView {


    public List<OnDropDownList> vehicleType(){
        List<OnDropDownList> vehicleType = new ArrayList<>();
        vehicleType.add(new OnDropDownList(1,"Available"));
        vehicleType.add(new OnDropDownList(2,"Unavailable"));
        vehicleType.add(new OnDropDownList(3,"Accident"));
        vehicleType.add(new OnDropDownList(4,"Maintenance"));
        vehicleType.add(new OnDropDownList(5,"OnRent"));
        vehicleType.add(new OnDropDownList(6,"CertiAndLicExpire"));
        vehicleType.add(new OnDropDownList(7,"OnHold"));
        vehicleType.add(new OnDropDownList(8,"Sell"));
        return vehicleType;
    }

    public void vehicleTypeSpinner(Context context, Spinner spinner){
        List<String> vehicleType = new ArrayList<>();
        vehicleType.add("Available");
        vehicleType.add("Unavailable");
        vehicleType.add("Accident");
        vehicleType.add("Maintenance");
        vehicleType.add("OnRent");
        vehicleType.add("CertiAndLicExpire");
        vehicleType.add("OnHold");
        vehicleType.add("Sell");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>( context, R.layout.spinner_layout, R.id.text1,vehicleType);
        spinner.setAdapter(adapter);
    }

    public void vehicleTypeSpinner(Context context, Spinner spinner,int value){
        int id = value-1;
        List<String> vehicleType = new ArrayList<>();
        vehicleType.add("Available");
        vehicleType.add("Unavailable");
        vehicleType.add("Accident");
        vehicleType.add("Maintenance");
        vehicleType.add("OnRent");
        vehicleType.add("CertiAndLicExpire");
        vehicleType.add("OnHold");
        vehicleType.add("Sell");

     /*   for (int i = 0; i <vehicleType.size() ; i++) {
            if (vehicleType.get(i).equals(value)){
                id = i;
            }
        }*/


        ArrayAdapter<String> adapter = new ArrayAdapter<String>( context, R.layout.spinner_layout, R.id.text1,vehicleType);
        spinner.setAdapter(adapter);
        spinner.setSelection(id);
    }


    public void vehicleTransmissionSpinner(Context context, Spinner spinner){

        List<String> vehicleTransmission = new ArrayList<>();
        vehicleTransmission.add("Transmission");
        vehicleTransmission.add("Automatic");
        vehicleTransmission.add("Manual");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>( context, R.layout.spinner_layout, R.id.text1,vehicleTransmission);
        spinner.setAdapter(adapter);
    }


    public void vehicleTransmissionSpinner(Context context, Spinner spinner,String value){
        int id = 0;
        List<String> vehicleTransmission = new ArrayList<>();
        vehicleTransmission.add("Transmission");
        vehicleTransmission.add("Automatic");
        vehicleTransmission.add("Manual");

        for (int i = 0; i <vehicleTransmission.size() ; i++) {
            if (vehicleTransmission.get(i).equals(value)){
                id = i;
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>( context, R.layout.spinner_layout, R.id.text1,vehicleTransmission);
        spinner.setAdapter(adapter);
        spinner.setSelection(id);
    }
    public void vehicleFuel(Context context, Spinner spinner){
        List<String> vehicleFuel = new ArrayList<>();
        vehicleFuel.add("Fuel Type");
        vehicleFuel.add("Petrol");
        vehicleFuel.add("Diesel");
        vehicleFuel.add("Gasoline");
        vehicleFuel.add("Electric");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>( context, R.layout.spinner_layout, R.id.text1,vehicleFuel);
        spinner.setAdapter(adapter);
    }


    public void vehicleFuel(Context context, Spinner spinner,String value){
        int id = 0;
        List<String> vehicleFuel = new ArrayList<>();
        vehicleFuel.add("Fuel Type");
        vehicleFuel.add("Petrol");
        vehicleFuel.add("Diesel");
        vehicleFuel.add("Gasoline");
        vehicleFuel.add("Electric");

        for (int i = 0; i <vehicleFuel.size() ; i++) {
            if (vehicleFuel.get(i).equals(value)){
                id = i;
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>( context, R.layout.spinner_layout, R.id.text1,vehicleFuel);
        spinner.setAdapter(adapter);
        spinner.setSelection(id);
    }


    public void setCompanyLabel(View v, CompanyLabel companyLabel){

        if (UserData.loginResponse.CompanyLabel != null){

        } else {
            companyLabel = new CompanyLabel();
        }

        try {
        TextView textView = v.findViewById(R.id.txthome);
        TextView reservation = v.findViewById(R.id.txtreservation);
        reservation.setText(companyLabel.Reservation);
        TextView agreement = v.findViewById(R.id.txtagreement);
        agreement.setText(companyLabel.Reservation);
        TextView vehicle = v.findViewById(R.id.txtvehicle);
        vehicle.setText(companyLabel.Vehicle);
        //textView.setText(UserData.loginResponse.CompanyLabel.);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public CompanyLabel  getCompanyLabel(View v, CompanyLabel companyLabel){

      /*  if (UserData.loginResponse.CompanyLabel != null){

        } else {
            companyLabel = new CompanyLabel();

        }*/

        try {
        TextView textView = v.findViewById(R.id.txthome);
        TextView reservation = v.findViewById(R.id.txtreservation);
        setText(reservation,companyLabel.Reservation);
      //  reservation.setText(companyLabel.Reservation);
        TextView agreement = v.findViewById(R.id.txtagreement);
        ///agreement.setText(companyLabel.Agreement);
            setText(agreement,companyLabel.Reservation);
        TextView vehicle = v.findViewById(R.id.txtvehicle);
        //vehicle.setText(companyLabel.Vehicle);
            setText(vehicle,companyLabel.Vehicle);
        //textView.setText(UserData.loginResponse.CompanyLabel.);

        TextView carAgr = v.findViewById(R.id.carAgr);
        //carAgr.setText(companyLabel.Agreement);
            setText(carAgr, companyLabel.Reservation);
            TextView txtopntick = v.findViewById(R.id.txtopntick);
            setText(txtopntick,"Traffic \nTickets");
         //   txtopntick.setText("Traffic \nTickets");

            String open = "Open \n ";

            setText(getText(v,R.id.txtopnres),open + companyLabel.Reservation);
            setText(getText(v,R.id.txtopnagr),open + companyLabel.Reservation);
            setText(getText(v,R.id.opntxt), open + companyLabel.Reservation);
            setText(getText(v,R.id.txt1agr), companyLabel.Reservation);
            setText(getText(v,R.id.txt2agr),companyLabel.Reservation);
            setText(getText(v,R.id.txt2res),companyLabel.Reservation);
            setText(getText(v,R.id.txt1res), companyLabel.Reservation);
            setText(getText(v,R.id.pickup), companyLabel.PickUp);
            setText(getText(v,R.id.drop), companyLabel.Delivery);
            setText(getText(v,R.id.vehrent),"Current " +companyLabel.Vehicle + "On Rent");
            setText(getText(v,R.id.drvlic),"Driver’s " + companyLabel.License + " Details");
            setText(getText(v,R.id.dob),companyLabel.DateOfBirth);
            setText(getText(v,R.id.deposit),companyLabel.Deposit);
            setText(getText(v,R.id.basicvihinfo),"Basic "+ companyLabel.Vehicle +" Information");
            setText(getText(v,R.id.insdet),companyLabel.Insurance + " Details");
            setText(getText(v,R.id.vimg),companyLabel.Vehicle + " Images");
            setText(getText(v,R.id.emc), "Extra "+companyLabel.Miles + " " +companyLabel.Charge);
            setText(getText(v,R.id.rd),"Refundable " + companyLabel.Deposit);
            setText(getText(v,R.id.srra),"Select Rental " + companyLabel.Rate);
            setText(getText(v,R.id.rf),companyLabel.Rate + " Features");
            setText(getText(v,R.id.coa),companyLabel.CheckOut + " Available");
            setText(getText(v,R.id.cia),companyLabel.CheckIn + " Available");
            setText(getText(v,R.id.equi),companyLabel.Equipment );

            setText(getText(v,R.id.fcurrency),Helper.displaycurrency);
            setText(getText(v,R.id.fcurrency1),Helper.displaycurrency);
            setText(getText(v,R.id.fcurrency2),Helper.displaycurrency);
            setText(getText(v,R.id.fcurrency3),Helper.displaycurrency);
            setText(getText(v,R.id.fcurrency4),Helper.displaycurrency);
            setText(getText(v,R.id.fcurrency5),Helper.displaycurrency);
            setText(getText(v,R.id.fcurrency6),Helper.displaycurrency);
            setText(getText(v,R.id.fcurrency7),Helper.displaycurrency);
            setText(getText(v,R.id.fcurrency8),Helper.displaycurrency);
            setText(getText(v,R.id.kmmm), "Unlimited "+ Helper.fueltype  + " Included");
            setText(getText(v,R.id.fueltype),Helper.fueltype);
            setText(getText(v,R.id.fueltype1),Helper.fueltype);
            setText(getText(v,R.id.dpid), companyLabel.Deposit + " \nPaid");
            setText(getText(v,R.id.pmtmad),companyLabel.Payment + " \nMade");
        } catch (Exception e){
            e.printStackTrace();
        }

        return companyLabel;
    }

    public void setText(TextView textView, String text){
        try {
            if (textView!=null) {
                textView.setText(text);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public TextView getText(View v, int id)  {
        TextView textView;
        try {
            textView = (TextView) v.findViewById(id);
        } catch (Exception e){
            e.printStackTrace();
            textView = (TextView) v.findViewById(R.id.jvm);
        }
        return textView;
    }


    public void uploadImage(View view, LoginRes loginRes, Activity activity){
        try {
            ImageView icon = view.findViewById(R.id.icon);
            CustomBindingAdapter.loadImage(icon,loginRes.getData(activity.getResources().getString(R.string.icon)));
        } catch (Exception e){
            e.printStackTrace();
        }
        try {
            ImageView logo = view.findViewById(R.id.logo);
            CustomBindingAdapter.loadImage(logo,loginRes.getData(activity.getResources().getString(R.string.logo)));
        } catch (Exception e){
            e.printStackTrace();
        }
        try {
            ImageView homeImage = view.findViewById(R.id.homeImage);
            CustomBindingAdapter.loadImage(homeImage,loginRes.getData(activity.getResources().getString(R.string.homescreenimage)));
        } catch (Exception e){
            e.printStackTrace();
        }
        try {
            ImageView cardss = view.findViewById(R.id.cardss);
            CustomBindingAdapter.loadImage(cardss,loginRes.getData(activity.getResources().getString(R.string.cardimg)));
        } catch (Exception e){
            e.printStackTrace();
        }
    }



}
