package com.rentguruz.app.flexiicar.booking2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.rentguruz.app.R;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.rentguruz.app.adapters.CustomToast;

import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

public class Fragment_Guest_Driver_Details extends Fragment
{
    LinearLayout Submit;
    Button lblLogin;
    ImageView imgback;
    Bundle BookingBundle, VehicleBundle;
    Bundle returnLocationBundle, locationBundle;
    Boolean locationType, initialSelect;
    EditText FirstName,LastName,Email,ContactNo;
    TextView txtDiscard;
    Handler handler = new Handler();
    public String id = "", role = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guest_driver_details, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        try {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

            Submit = view.findViewById(R.id.lblSubmitbtn);
            imgback = view.findViewById(R.id.back_guestdriver);
            FirstName=view.findViewById(R.id.GuestDriverName);
            LastName=view.findViewById(R.id.GuestDriverLastName);
            Email=view.findViewById(R.id.GuestDriverEmail);
            ContactNo=view.findViewById(R.id.GuestDriverPhoneNo);
            txtDiscard=view.findViewById(R.id.Discardtxt);

            lblLogin=view.findViewById(R.id.LoginBtn);

            BookingBundle = getArguments().getBundle("BookingBundle");
            VehicleBundle = getArguments().getBundle("VehicleBundle");
            returnLocationBundle = getArguments().getBundle("returnLocation");
            locationBundle = getArguments().getBundle("location");
            locationType = getArguments().getBoolean("locationType");
            initialSelect = getArguments().getBoolean("initialSelect");

            SharedPreferences sp = getActivity().getSharedPreferences("FlexiiCar", MODE_PRIVATE);
            id = sp.getString(getString(R.string.id), "");

            Submit.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try {
                        if (FirstName.getText().toString().equals(""))
                            CustomToast.showToast(getActivity(), "Enter First Name.", 1);
                        else if (LastName.getText().toString().equals(""))
                            CustomToast.showToast(getActivity(), "Enter Last Name.", 1);
                        else if (Email.getText().toString().equals(""))
                            CustomToast.showToast(getActivity(), "Enter Your Email.", 1);
                        else if (ContactNo.getText().toString().equals(""))
                            CustomToast.showToast(getActivity(), "Enter Your Contact No.", 1);

                        JSONObject GuestDriverModel = new JSONObject();

                        GuestDriverModel.put("firstName", FirstName.getText().toString());
                        GuestDriverModel.put("lastName", FirstName.getText().toString());
                        GuestDriverModel.put("driverEmail", Email.getText().toString());
                        GuestDriverModel.put("driverPhoneNo", ContactNo.getText().toString());

                        Bundle Booking = new Bundle();
                        BookingBundle.putInt("BookingStep", 4);
                        Booking.putBundle("BookingBundle", BookingBundle);
                        Booking.putBundle("VehicleBundle", VehicleBundle);
                        Booking.putBundle("returnLocation", returnLocationBundle);
                        Booking.putBundle("location", locationBundle);
                        Booking.putBoolean("locationType", locationType);
                        Booking.putBoolean("initialSelect", initialSelect);
                        BookingBundle.putString("GuestDriverDetails", GuestDriverModel.toString());
                        System.out.println(BookingBundle);
                        NavHostFragment.findNavController(Fragment_Guest_Driver_Details.this)
                                .navigate(R.id.action_Guest_driver_details_to_Finalize_your_rental, Booking);
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
            imgback.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Bundle Booking = new Bundle();
                    BookingBundle.putInt("BookingStep", 4);
                    Booking.putBundle("BookingBundle", BookingBundle);
                    Booking.putBundle("VehicleBundle", VehicleBundle);
                    Booking.putBundle("returnLocation", returnLocationBundle);
                    Booking.putBundle("location", locationBundle);
                    Booking.putBoolean("locationType", locationType);
                    Booking.putBoolean("initialSelect", initialSelect);
                    NavHostFragment.findNavController(Fragment_Guest_Driver_Details.this)
                            .navigate(R.id.action_Guest_driver_details_to_Finalize_your_rental,Booking);
                }
            });
            lblLogin.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Bundle Booking = new Bundle();
                    Booking.putBundle("BookingBundle", BookingBundle);
                    Booking.putBundle("VehicleBundle", VehicleBundle);
                    Booking.putBundle("returnLocation", returnLocationBundle);
                    Booking.putBundle("location", locationBundle);
                    Booking.putBoolean("locationType", locationType);
                    Booking.putBoolean("initialSelect", initialSelect);
                    Booking.putBoolean("LoginForUser",false);
                    System.out.println(BookingBundle);
                    NavHostFragment.findNavController(Fragment_Guest_Driver_Details.this)
                            .navigate(R.id.action_Guest_driver_details_to_LoginFragment,Booking);
                }
            });
            txtDiscard.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Bundle Booking = new Bundle();
                    BookingBundle.putInt("BookingStep", 4);
                    Booking.putBundle("BookingBundle", BookingBundle);
                    Booking.putBundle("VehicleBundle", VehicleBundle);
                    Booking.putBundle("returnLocation", returnLocationBundle);
                    Booking.putBundle("location", locationBundle);
                    Booking.putBoolean("locationType", locationType);
                    Booking.putBoolean("initialSelect", initialSelect);
                    NavHostFragment.findNavController(Fragment_Guest_Driver_Details.this)
                            .navigate(R.id.action_Guest_driver_details_to_Finalize_your_rental,Booking);
                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
