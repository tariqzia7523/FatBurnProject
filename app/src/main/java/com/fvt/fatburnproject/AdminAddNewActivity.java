package com.fvt.fatburnproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AdminAddNewActivity extends AppCompatActivity {

    CheckBox cbMonday,cbTuesday,cbWednessday,cbThurday,cbFriDay,cbSaturday,cbSunday;
    CheckBox monFbx,monWbx,tueFbx,tueWbx,wedFbx,wedWbx,thursFbx,thursWbx,friFbx,friWbx,satFbx,satWbx,sunFbx,sunWbx;
    EditText mondayTime,tusdayTime,wednesdayTime,thurdayTime,fridayTime,saturdayTime,sundayTime;
    EditText mondayTimeWbx,tusdayTimeWbx,wednesdayTimeWbx,thurdayTimeWbx,fridayTimeWbx,saturdayTimeWbx,sundayTimeWbx;
    EditText mondayseats,tusdayseats,wednesdayseats,thurdayseats,fridayseats,saturdayseats,sundayseats;
    DatabaseReference myRef,myRefBook;
    Spinner spinner,veneuNameSpinner;
    String selectedOption;
    LinearLayout onlineLayout,veneuLayout;
    EditText price,title;
    String selectedVeneu;
    ArrayList<DayTimeModel> dayTimeModels;
    ProgressDialog progressDialog;
    RadioGroup radioGroup;
    String TAG;
//    CheckBox wbx,fbx;
    ArrayList<VeneuModel> veneuModels;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        setContentView(R.layout.activity_admin_add_new);
        getSupportActionBar().hide();
        radioGroup=findViewById(R.id.radio_group);
        TAG="***Addclass";
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        dayTimeModels=new ArrayList<>();
        spinner=findViewById(R.id.spiner);
        veneuNameSpinner=findViewById(R.id.spiner_veneu_name);
        selectedOption="Online";
//        veneClassDelay=findViewById(R.id.delay);
        myRef = FirebaseDatabase.getInstance().getReference("Classes");
        selectedVeneu=getString(R.string.greatpark);
        onlineLayout=findViewById(R.id.online_layout);
        veneuLayout=findViewById(R.id.venue_layout);
        veneuLayout.setVisibility(View.GONE);
        price=findViewById(R.id.price);
        price.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(2)});
        title=findViewById(R.id.title);
        cbMonday=findViewById(R.id.cb_monday);


        monFbx=findViewById(R.id.fbx_monday);
        monWbx=findViewById(R.id.wbx_monday);
        tueFbx=findViewById(R.id.fbx_tuesday);
        tueWbx=findViewById(R.id.wbx_tuesday);
        wedFbx=findViewById(R.id.fbx_wednessday);
        wedWbx=findViewById(R.id.wbx_wednessday);
        thursFbx=findViewById(R.id.fbx_thursday);
        thursWbx=findViewById(R.id.wbx_thursday);
        friFbx=findViewById(R.id.fbx_friday);
        friWbx=findViewById(R.id.wbx_friday);
        satFbx=findViewById(R.id.fbx_saturday);
        satWbx=findViewById(R.id.wbx_saturday);
        sunFbx=findViewById(R.id.fbx_sunday);
        sunWbx=findViewById(R.id.wbx_sunday);



        mondayseats=findViewById(R.id.monday_seats);
        tusdayseats=findViewById(R.id.tuesday_seats);
        wednesdayseats=findViewById(R.id.wednesday_seats);
        thurdayseats=findViewById(R.id.thursday_seats);
        fridayseats=findViewById(R.id.friday_seats);
        saturdayseats=findViewById(R.id.saturday_seats);
        sundayseats=findViewById(R.id.sunday_seats);
        myRefBook = FirebaseDatabase.getInstance().getReference("Classes").child("Booked").child("VeneuClasses");
        myRefBook.child(selectedVeneu).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                veneuModels=new ArrayList<>();
                try{
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        VeneuModel veneuModel=dataSnapshot1.getValue(VeneuModel.class);
                        veneuModel.setId(dataSnapshot1.getKey());
                        veneuModels.add(veneuModel);
                    }

                }catch (Exception c){
                    c.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        cbMonday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mondayTime.setEnabled(isChecked);
                mondayseats.setEnabled(isChecked);
                mondayTimeWbx.setEnabled(isChecked);
                monFbx.setEnabled(isChecked);
                monWbx.setEnabled(isChecked);
            }
        });
        cbTuesday=findViewById(R.id.cb_tuesday);
        cbTuesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tusdayTime.setEnabled(isChecked);
                tusdayTime.setEnabled(isChecked);
                tusdayTimeWbx.setEnabled(isChecked);
                tueFbx.setEnabled(isChecked);
                tueWbx.setEnabled(isChecked);
            }
        });
        cbWednessday=findViewById(R.id.cb_wednesday);
        cbWednessday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                wednesdayTime.setEnabled(isChecked);
                wednesdayTimeWbx.setEnabled(isChecked);
                wednesdayseats.setEnabled(isChecked);
                wedFbx.setEnabled(isChecked);
                wedWbx.setEnabled(isChecked);
            }
        });
        cbThurday=findViewById(R.id.cb_thursday);
        cbThurday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                thurdayTime.setEnabled(isChecked);
                thurdayTimeWbx.setEnabled(isChecked);
                thurdayseats.setEnabled(isChecked);
                thursFbx.setEnabled(isChecked);
                thursWbx.setEnabled(isChecked);

            }
        });
        cbFriDay=findViewById(R.id.cb_friday);
        cbFriDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fridayTime.setEnabled(isChecked);
                fridayTimeWbx.setEnabled(isChecked);
                fridayseats.setEnabled(isChecked);
                friFbx.setEnabled(isChecked);
                friWbx.setEnabled(isChecked);
            }
        });
        cbSaturday=findViewById(R.id.cb_saturday);
        cbSaturday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saturdayTime.setEnabled(isChecked);
                saturdayTimeWbx.setEnabled(isChecked);
                saturdayseats.setEnabled(isChecked);
                satFbx.setEnabled(isChecked);
                satWbx.setEnabled(isChecked);
            }
        });
        cbSunday=findViewById(R.id.cb_sunday);
        cbSunday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sundayTime.setEnabled(isChecked);
                sundayTimeWbx.setEnabled(isChecked);
                sundayseats.setEnabled(isChecked);
                sunFbx.setEnabled(isChecked);
                sunWbx.setEnabled(isChecked);
            }
        });
        mondayTimeWbx=findViewById(R.id.monday_time_wbx);
        mondayTimeWbx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStartTime(mondayTimeWbx);
            }
        });
        tusdayTimeWbx=findViewById(R.id.tuesday_time_wbx);
        tusdayTimeWbx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStartTime(tusdayTimeWbx);
            }
        });
        wednesdayTimeWbx=findViewById(R.id.wednesday_time_wbx);
        wednesdayTimeWbx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStartTime(wednesdayTimeWbx);
            }
        });
        thurdayTimeWbx=findViewById(R.id.thursday_time_wbx);
        thurdayTimeWbx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStartTime(thurdayTimeWbx);
            }
        });
        fridayTimeWbx=findViewById(R.id.friday_time_wbx);
        fridayTimeWbx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStartTime(fridayTimeWbx);
            }
        });
        sundayTimeWbx=findViewById(R.id.sunday_time_wbx);
        sundayTimeWbx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStartTime(sundayTimeWbx);
            }
        });
        saturdayTimeWbx=findViewById(R.id.saturday_time_wbx);
        saturdayTimeWbx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStartTime(saturdayTimeWbx);
            }
        });

        mondayTime=findViewById(R.id.monday_time);
        mondayTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStartTime(mondayTime);
            }
        });

        mondayTime.setEnabled(cbMonday.isChecked());
        tusdayTime=findViewById(R.id.tuesday_time);
        tusdayTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStartTime(tusdayTime);
            }
        });
        tusdayTime.setEnabled(cbTuesday.isChecked());
        wednesdayTime=findViewById(R.id.wednesday_time);
        wednesdayTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStartTime(wednesdayTime);
            }
        });
        wednesdayTime.setEnabled(cbWednessday.isChecked());
        thurdayTime=findViewById(R.id.thursday_time);
        thurdayTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStartTime(thurdayTime);
            }
        });
        thurdayTime.setEnabled(cbThurday.isChecked());
        fridayTime=findViewById(R.id.friday_time);
        fridayTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStartTime(fridayTime);
            }
        });
        fridayTime.setEnabled(cbFriDay.isChecked());
        saturdayTime=findViewById(R.id.saturday_time);
        saturdayTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStartTime(saturdayTime);
            }
        });
        saturdayTime.setEnabled(cbSaturday.isChecked());
        sundayTime=findViewById(R.id.sunday_time);
        sundayTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getStartTime(sundayTime);
            }
        });
        sundayTime.setEnabled(cbSunday.isChecked());

        veneuNameSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e(TAG,"vene item listenser called");
                monFbx.setChecked(false);  monWbx.setChecked(false);;
                monFbx.setEnabled(false);  monWbx.setEnabled(false);

                tueFbx.setEnabled(false);  tueWbx.setChecked(false);
                tueFbx.setChecked(false);  tueWbx.setEnabled(false);

                wedFbx.setEnabled(false);  wedWbx.setEnabled(false);
                wedFbx.setChecked(false);  wedWbx.setChecked(false);

                thursFbx.setEnabled(false); thursWbx.setEnabled(false);
                thursFbx.setChecked(false); thursWbx.setChecked(false);

                friFbx.setEnabled(false); friWbx.setEnabled(false);
                friFbx.setChecked(false); friWbx.setChecked(false);

                satFbx.setEnabled(false); satWbx.setEnabled(false);
                satFbx.setChecked(false); satWbx.setChecked(false);

                sunFbx.setEnabled(false); sunWbx.setEnabled(false);
                sunFbx.setChecked(false); sunWbx.setChecked(false);


                selectedVeneu = (String) parent.getItemAtPosition(position);
                dayTimeModels.clear();
                myRef.child("VeneuClass").child(selectedVeneu).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        try{
                            Log.e(TAG,"vene change called id "+dataSnapshot.getKey());
                            for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                Log.e(TAG,"data snap shot 1 id "+dataSnapshot1.getKey());
                                DayTimeModel dayTimeModel=new DayTimeModel();
                                dayTimeModel.setDay(dataSnapshot1.getKey());
                                dayTimeModel.setTime(dataSnapshot1.child("time").getValue(String.class));
                                dayTimeModel.setSeats(dataSnapshot1.child("seats").getValue(String.class));
                                dayTimeModel.setWbxTime(dataSnapshot1.child("wbxTime").getValue(String.class));
//                                try{
//                                    veneClassDelay.setText(dataSnapshot1.child("delay").getValue(String.class));
//                                }catch (Exception c){
//                                    c.printStackTrace();
//                                }

                                try{
                                    if(dataSnapshot1.getKey().equalsIgnoreCase("Monday")){
                                        monFbx.setChecked(dataSnapshot1.child("fbx").getValue(Boolean.class));
                                        monFbx.setEnabled(dataSnapshot1.child("fbx").getValue(Boolean.class));
                                        monWbx.setChecked(dataSnapshot1.child("wbx").getValue(Boolean.class));
                                        monWbx.setEnabled(dataSnapshot1.child("wbx").getValue(Boolean.class));
                                    }else if(dataSnapshot1.getKey().equalsIgnoreCase("Tuesday")){
                                        tueFbx.setChecked(dataSnapshot1.child("fbx").getValue(Boolean.class));
                                        tueFbx.setEnabled(dataSnapshot1.child("fbx").getValue(Boolean.class));
                                        tueWbx.setChecked(dataSnapshot1.child("wbx").getValue(Boolean.class));
                                        tueWbx.setEnabled(dataSnapshot1.child("wbx").getValue(Boolean.class));
                                    }else if(dataSnapshot1.getKey().equalsIgnoreCase("Wednesday")){
                                        wedFbx.setChecked(dataSnapshot1.child("fbx").getValue(Boolean.class));
                                        wedFbx.setEnabled(dataSnapshot1.child("fbx").getValue(Boolean.class));
                                        wedWbx.setChecked(dataSnapshot1.child("wbx").getValue(Boolean.class));
                                        wedWbx.setEnabled(dataSnapshot1.child("wbx").getValue(Boolean.class));
                                    }else if(dataSnapshot1.getKey().equalsIgnoreCase("Thursday")){
                                        thursFbx.setChecked(dataSnapshot1.child("fbx").getValue(Boolean.class));
                                        thursFbx.setEnabled(dataSnapshot1.child("fbx").getValue(Boolean.class));
                                        thursWbx.setChecked(dataSnapshot1.child("wbx").getValue(Boolean.class));
                                        thursWbx.setEnabled(dataSnapshot1.child("wbx").getValue(Boolean.class));
                                    }else if(dataSnapshot1.getKey().equalsIgnoreCase("Friday")){
                                        friFbx.setChecked(dataSnapshot1.child("fbx").getValue(Boolean.class));
                                        friFbx.setEnabled(dataSnapshot1.child("fbx").getValue(Boolean.class));
                                        friWbx.setChecked(dataSnapshot1.child("wbx").getValue(Boolean.class));
                                        friWbx.setEnabled(dataSnapshot1.child("wbx").getValue(Boolean.class));
                                    }else if(dataSnapshot1.getKey().equalsIgnoreCase("Saturday")){
                                        satFbx.setChecked(dataSnapshot1.child("fbx").getValue(Boolean.class));
                                        satFbx.setEnabled(dataSnapshot1.child("fbx").getValue(Boolean.class));
                                        satWbx.setChecked(dataSnapshot1.child("wbx").getValue(Boolean.class));
                                        satWbx.setEnabled(dataSnapshot1.child("wbx").getValue(Boolean.class));
                                    }else if(dataSnapshot1.getKey().equalsIgnoreCase("Sunday")){
                                        sunFbx.setChecked(dataSnapshot1.child("fbx").getValue(Boolean.class));
                                        sunFbx.setEnabled(dataSnapshot1.child("fbx").getValue(Boolean.class));
                                        sunWbx.setChecked(dataSnapshot1.child("wbx").getValue(Boolean.class));
                                        sunWbx.setEnabled(dataSnapshot1.child("wbx").getValue(Boolean.class));
                                    }
                                }catch (Exception c){
                                    c.printStackTrace();
                                }


//                                try{
//                                    if(dataSnapshot1.child("fbx").getValue(Boolean.class)){
//                                        fbx.setChecked(true);
//                                    }
//                                }catch (Exception c){
//                                    c.printStackTrace();
//                                }
//                                try{
//                                    if(dataSnapshot1.child("wbx").getValue(Boolean.class)){
//                                        wbx.setChecked(true);
//                                    }
//                                }catch (Exception c){
//                                    c.printStackTrace();
//                                }
                                dayTimeModels.add(dayTimeModel);
                            }
                            dataMenuplater();
                        }catch (Exception c){
                            c.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedOption = (String) parent.getItemAtPosition(position);
                if(selectedOption.equalsIgnoreCase("Online")){
                    onlineLayout.setVisibility(View.VISIBLE);
                    veneuLayout.setVisibility(View.GONE);
                }else{
                    onlineLayout.setVisibility(View.GONE);
                    veneuLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        findViewById(R.id.add_class).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(selectedOption.equalsIgnoreCase("online")){
                    progressDialog.show();
                    AdminOnlineClassModel adminOnlineClassModel=new AdminOnlineClassModel(
                            "£"+price.getText().toString(),
                            title.getText().toString());
                    myRef.child("OnlineClasses").push().setValue(adminOnlineClassModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Class Added",Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Class not Added",Toast.LENGTH_LONG).show();
                        }
                    });
                }else{
//                    String days=""
//                    if(fbx.isChecked() || wbx.isChecked()){
                        ArrayList<DayTimeModel> days=new ArrayList<>();
                        if(cbMonday.isChecked()){
                            if(mondayTime.getText().toString().isEmpty() ){
                                Toast.makeText(getApplicationContext(),"Please Add Monday Time",Toast.LENGTH_LONG).show();
                                return;
                            }else if(mondayseats.getText().toString().isEmpty() ){
                                Toast.makeText(getApplicationContext(),"Please Add Monday Seats",Toast.LENGTH_LONG).show();
                                return;
                            }else if(!monFbx.isChecked() && !monWbx.isChecked()){
                                Toast.makeText(getApplicationContext(),"Please Add Monday class type",Toast.LENGTH_LONG).show();
                                return;
                            }else if(monFbx.isChecked() && mondayTime.getText().toString().isEmpty()){
                                Toast.makeText(getApplicationContext(),"Please Add Monday Time",Toast.LENGTH_LONG).show();
                                return;
                            }else if(monWbx.isChecked() && mondayTimeWbx.getText().toString().isEmpty()){
                                Toast.makeText(getApplicationContext(),"Please Add Monday Time Wbx",Toast.LENGTH_LONG).show();
                                return;
                            }
                            else{
                                DayTimeModel dayTimeModel=new DayTimeModel();
                                dayTimeModel.setDay("Monday");
                                dayTimeModel.setTime(mondayTime.getText().toString());
                                dayTimeModel.setSeats(mondayseats.getText().toString());
                                if(monFbx.isChecked()){
                                    dayTimeModel.setFbxSeats(mondayseats.getText().toString());
                                }else{
                                    dayTimeModel.setFbxSeats("0");
                                }
                                if(monWbx.isChecked()){
                                    dayTimeModel.setWbxSeats(mondayseats.getText().toString());
                                    dayTimeModel.setWbxTime(mondayTimeWbx.getText().toString());
                                }else{
                                    dayTimeModel.setWbxSeats("0");
                                    dayTimeModel.setWbxTime("");
                                }
                                dayTimeModel.setFbx(monFbx.isChecked());
                                dayTimeModel.setWbx(monWbx.isChecked());
                                days.add(dayTimeModel);
                            }
                        }
                        if(cbTuesday.isChecked()){
                            if(tusdayTime.getText().toString().isEmpty()){
                                Toast.makeText(getApplicationContext(),"Please Add Tuesday Time",Toast.LENGTH_LONG).show();
                                return;
                            }else if(tusdayseats.getText().toString().isEmpty() ){
                                Toast.makeText(getApplicationContext(),"Please Add Tuesday Seats",Toast.LENGTH_LONG).show();
                                return;
                            }else if(!tueFbx.isChecked() && !tueWbx.isChecked()){
                                Toast.makeText(getApplicationContext(),"Please Add Tuesday class type",Toast.LENGTH_LONG).show();
                                return;
                            }else if(tueFbx.isChecked() && tusdayTime.getText().toString().isEmpty()){
                                Toast.makeText(getApplicationContext(),"Please Add Tuesday Time",Toast.LENGTH_LONG).show();
                                return;
                            }else if(tueWbx.isChecked() && tusdayTimeWbx.getText().toString().isEmpty()){
                                Toast.makeText(getApplicationContext(),"Please Add Tuesday Time Wbx",Toast.LENGTH_LONG).show();
                                return;
                            }
                            else{
                                DayTimeModel dayTimeModel=new DayTimeModel();
                                dayTimeModel.setDay("Tuesday");
                                dayTimeModel.setTime(tusdayTime.getText().toString());
                                dayTimeModel.setSeats(tusdayseats.getText().toString());
                                if(tueFbx.isChecked()){
                                    dayTimeModel.setFbxSeats(tusdayseats.getText().toString());
                                }else{
                                    dayTimeModel.setFbxSeats("0");
                                }
                                if(tueWbx.isChecked()){
                                    dayTimeModel.setWbxSeats(tusdayseats.getText().toString());
                                    dayTimeModel.setWbxTime(tusdayTimeWbx.getText().toString());
                                }else{
                                    dayTimeModel.setWbxSeats("0");
                                    dayTimeModel.setWbxTime("");
                                }
//                                dayTimeModel.setDelay(veneClassDelay.getText().toString());
                                dayTimeModel.setFbx(tueFbx.isChecked());
                                dayTimeModel.setWbx(tueWbx.isChecked());
                                days.add(dayTimeModel);
                            }
                        }
                        if(cbWednessday.isChecked()){
                            if(wednesdayTime.getText().toString().isEmpty()){
                                Toast.makeText(getApplicationContext(),"Please Add Wednesday Time",Toast.LENGTH_LONG).show();
                                return;
                            }
                            else if(wednesdayseats.getText().toString().isEmpty() ){
                                Toast.makeText(getApplicationContext(),"Please Add Wednesday Seats",Toast.LENGTH_LONG).show();
                                return;
                            }else if(!wedFbx.isChecked() && !wedWbx.isChecked()){
                                Toast.makeText(getApplicationContext(),"Please Add Wednesday class type",Toast.LENGTH_LONG).show();
                                return;
                            }else if(wedFbx.isChecked() && wednesdayTime.getText().toString().isEmpty()){
                                Toast.makeText(getApplicationContext(),"Please Add Wednesday Time",Toast.LENGTH_LONG).show();
                                return;
                            }else if(wedWbx.isChecked() && wednesdayTimeWbx.getText().toString().isEmpty()){
                                Toast.makeText(getApplicationContext(),"Please Add Wednesday Time Wbx",Toast.LENGTH_LONG).show();
                                return;
                            }
                            else{
                                DayTimeModel dayTimeModel=new DayTimeModel();
                                dayTimeModel.setDay("Wednesday");
                                dayTimeModel.setTime(wednesdayTime.getText().toString());
                                dayTimeModel.setSeats(wednesdayseats.getText().toString());
                                if(wedFbx.isChecked()){
                                    dayTimeModel.setFbxSeats(wednesdayseats.getText().toString());
                                }else{
                                    dayTimeModel.setFbxSeats("0");
                                }
                                if(wedWbx.isChecked()){
                                    dayTimeModel.setWbxSeats(wednesdayseats.getText().toString());
                                    dayTimeModel.setWbxTime(wednesdayTimeWbx.getText().toString());
                                }else{
                                    dayTimeModel.setWbxSeats("0");
                                    dayTimeModel.setWbxTime("");

                                }
//                                dayTimeModel.setDelay(veneClassDelay.getText().toString());
                                dayTimeModel.setFbx(wedFbx.isChecked());
                                dayTimeModel.setWbx(wedWbx.isChecked());
                                days.add(dayTimeModel);
                            }
                        }
                        if(cbThurday.isChecked()){
                            if(thurdayTime.getText().toString().isEmpty()){
                                Toast.makeText(getApplicationContext(),"Please Add Thursday Time",Toast.LENGTH_LONG).show();
                                return;
                            }else if(thurdayseats.getText().toString().isEmpty() ){
                                Toast.makeText(getApplicationContext(),"Please Add Thursday Seats",Toast.LENGTH_LONG).show();
                                return;
                            }else if(!thursFbx.isChecked() && !thursWbx.isChecked()){
                                Toast.makeText(getApplicationContext(),"Please Add Thursday class type",Toast.LENGTH_LONG).show();
                                return;
                            }else if(thursFbx.isChecked() && thurdayTime.getText().toString().isEmpty()){
                                Toast.makeText(getApplicationContext(),"Please Add Thursday Time",Toast.LENGTH_LONG).show();
                                return;
                            }else if(thursWbx.isChecked() && thurdayTimeWbx.getText().toString().isEmpty()){
                                Toast.makeText(getApplicationContext(),"Please Add Thursday Time Wbx",Toast.LENGTH_LONG).show();
                                return;
                            }
                            else{
                                DayTimeModel dayTimeModel=new DayTimeModel();
                                dayTimeModel.setDay("Thursday");
                                dayTimeModel.setTime(thurdayTime.getText().toString());
                                dayTimeModel.setSeats(thurdayseats.getText().toString());
                                if(thursFbx.isChecked()){
                                    dayTimeModel.setFbxSeats(thurdayseats.getText().toString());
                                }else{
                                    dayTimeModel.setFbxSeats("0");
                                }
                                if(thursWbx.isChecked()){
                                    dayTimeModel.setWbxSeats(thurdayseats.getText().toString());
                                    dayTimeModel.setWbxTime(thurdayTimeWbx.getText().toString());
                                }else{
                                    dayTimeModel.setWbxSeats("0");
                                    dayTimeModel.setWbxTime("");
                                }
                                dayTimeModel.setFbx(thursFbx.isChecked());
                                dayTimeModel.setWbx(thursWbx.isChecked());
                                days.add(dayTimeModel);
                            }
                        }
                        if(cbFriDay.isChecked()){
                            if(fridayTime.getText().toString().isEmpty()){
                                Toast.makeText(getApplicationContext(),"Please Add Friday Time",Toast.LENGTH_LONG).show();
                                return;
                            }else if(fridayseats.getText().toString().isEmpty() ){
                                Toast.makeText(getApplicationContext(),"Please Add Friday Seats",Toast.LENGTH_LONG).show();
                                return;
                            }else if(!friFbx.isChecked() && !friWbx.isChecked()){
                                Toast.makeText(getApplicationContext(),"Please Add Friday class type",Toast.LENGTH_LONG).show();
                                return;
                            }else if(friFbx.isChecked() && fridayTime.getText().toString().isEmpty()){
                                Toast.makeText(getApplicationContext(),"Please Add Friday Time",Toast.LENGTH_LONG).show();
                                return;
                            }else if(friWbx.isChecked() && fridayTimeWbx.getText().toString().isEmpty()){
                                Toast.makeText(getApplicationContext(),"Please Add Friday Time Wbx",Toast.LENGTH_LONG).show();
                                return;
                            }
                            else{
                                DayTimeModel dayTimeModel=new DayTimeModel();
                                dayTimeModel.setDay("Friday");
                                dayTimeModel.setTime(fridayTime.getText().toString());
                                dayTimeModel.setSeats(fridayseats.getText().toString());
//                                dayTimeModel.setSeats(saturdayseats.getText().toString());
                                if(friFbx.isChecked()){
                                    dayTimeModel.setFbxSeats(fridayseats.getText().toString());
                                }else{
                                    dayTimeModel.setFbxSeats("0");
                                }
                                if(friWbx.isChecked()){
                                    dayTimeModel.setWbxSeats(fridayseats.getText().toString());
                                    dayTimeModel.setWbxTime(fridayTimeWbx.getText().toString());
                                }else{
                                    dayTimeModel.setWbxSeats("0");
                                }
//                                dayTimeModel.setDelay(veneClassDelay.getText().toString());
                                dayTimeModel.setFbx(friFbx.isChecked());
                                dayTimeModel.setWbx(friWbx.isChecked());
                                days.add(dayTimeModel);
                            }
                        }
                        if(cbSaturday.isChecked()){
                            if(saturdayTime.getText().toString().isEmpty()){
                                Toast.makeText(getApplicationContext(),"Please Add Saturday Time",Toast.LENGTH_LONG).show();
                                return;
                            }else if(saturdayseats.getText().toString().isEmpty() ){
                                Toast.makeText(getApplicationContext(),"Please Add Saturday Seats",Toast.LENGTH_LONG).show();
                                return;
                            }else if(!satFbx.isChecked() && !satWbx.isChecked()){
                                Toast.makeText(getApplicationContext(),"Please Add Saturday class type",Toast.LENGTH_LONG).show();
                                return;
                            }else if(satFbx.isChecked() && saturdayTime.getText().toString().isEmpty()){
                                Toast.makeText(getApplicationContext(),"Please Add Saturday Time",Toast.LENGTH_LONG).show();
                                return;
                            }else if(satWbx.isChecked() && saturdayTimeWbx.getText().toString().isEmpty()){
                                Toast.makeText(getApplicationContext(),"Please Add Saturday Time Wbx",Toast.LENGTH_LONG).show();
                                return;
                            }
                            else{
                                DayTimeModel dayTimeModel=new DayTimeModel();
                                dayTimeModel.setDay("Saturday");
                                dayTimeModel.setTime(saturdayTime.getText().toString());
                                dayTimeModel.setSeats(saturdayseats.getText().toString());
                                if(satFbx.isChecked()){
                                    dayTimeModel.setFbxSeats(saturdayseats.getText().toString());
                                }else{
                                    dayTimeModel.setFbxSeats("0");
                                }
                                if(satWbx.isChecked()){
                                    dayTimeModel.setWbxSeats(saturdayseats.getText().toString());
                                    dayTimeModel.setWbxTime(saturdayTimeWbx.getText().toString());
                                }else{
                                    dayTimeModel.setWbxSeats("0");
                                }
//                                dayTimeModel.setDelay(veneClassDelay.getText().toString());
                                dayTimeModel.setFbx(satFbx.isChecked());
                                dayTimeModel.setWbx(satWbx.isChecked());
                                days.add(dayTimeModel);
                            }
                        }
                        if(cbSunday.isChecked()){
                            if(sundayTime.getText().toString().isEmpty()){
                                Toast.makeText(getApplicationContext(),"Please Add Sunday Time",Toast.LENGTH_LONG).show();
                                return;
                            }else if(sundayseats.getText().toString().isEmpty() ){
                                Toast.makeText(getApplicationContext(),"Please Add Sunday Seats",Toast.LENGTH_LONG).show();
                                return;
                            }else if(!sunFbx.isChecked() && !sunWbx.isChecked()){
                                Toast.makeText(getApplicationContext(),"Please Add Sunday class type",Toast.LENGTH_LONG).show();
                                return;
                            }else if(sunFbx.isChecked() && sundayTime.getText().toString().isEmpty()){
                                Toast.makeText(getApplicationContext(),"Please Add Sunday Time",Toast.LENGTH_LONG).show();
                                return;
                            }else if(sunWbx.isChecked() && sundayTimeWbx.getText().toString().isEmpty()){
                                Toast.makeText(getApplicationContext(),"Please Add Sunday Time Wbx",Toast.LENGTH_LONG).show();
                                return;
                            }
                            else{
                                DayTimeModel dayTimeModel=new DayTimeModel();
                                dayTimeModel.setDay("Sunday");
                                dayTimeModel.setTime(sundayTime.getText().toString());
                                dayTimeModel.setSeats(sundayseats.getText().toString());
                                if(sunFbx.isChecked()){
                                    dayTimeModel.setFbxSeats(sundayseats.getText().toString());
                                }else{
                                    dayTimeModel.setFbxSeats("0");
                                }
                                if(sunWbx.isChecked()){
                                    dayTimeModel.setWbxSeats(sundayseats.getText().toString());
                                    dayTimeModel.setWbxTime(sundayTimeWbx.getText().toString());
                                }else{
                                    dayTimeModel.setWbxSeats("0");
                                    dayTimeModel.setWbxTime("");
                                }

//                                dayTimeModel.setWbxSeats(mondayseats.getText().toString());
//                                dayTimeModel.setDelay(veneClassDelay.getText().toString());
                                dayTimeModel.setFbx(sunFbx.isChecked());
                                dayTimeModel.setWbx(sunWbx.isChecked());
                                days.add(dayTimeModel);
                            }
                        }
                        progressDialog.show();
                        String venue=veneuNameSpinner.getSelectedItem().toString();

                        myRef.child("VeneuClass").child(venue).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                for(int i=0;i<days.size();i++){
                                    myRef.child("VeneuClass").child(venue).child(days.get(i).getDay()).setValue(days.get(i)).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

//                                            try{
//                                                progressDialog.dismiss();
//                                            }catch (Exception c){
//                                                c.printStackTrace();
//                                            }
                                        }
                                    }).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            try{
                                                for(int i=0;i<veneuModels.size();i++){
                                                    boolean status=false;
                                                    for(int j=0;j<days.size();j++){
                                                        if(veneuModels.get(i).getDay().equalsIgnoreCase(days.get(j).getDay())){
                                                            status=true;
                                                            break;
                                                        }
                                                    }
                                                    if(!status){
                                                        Log.e(TAG,"data found");
                                                        Log.e(TAG,"selected veneu "+selectedVeneu);
                                                        Log.e(TAG,"child id "+veneuModels.get(i).getId());
                                                        myRefBook.child(selectedVeneu).child(veneuModels.get(i).getId()).removeValue();
                                                    }

                                                }



                                                progressDialog.dismiss();
                                            }catch (Exception c){
                                                c.printStackTrace();
                                            }
                                        }
                                    });
                                }
                                Toast.makeText(getApplicationContext(),"class Added",Toast.LENGTH_LONG).show();
                            }
                        });
//                        Map<String, Object> update = new HashMap<>();
//                        if(fbx.isChecked())
//                            update.put("fbx", true);
//                        if(wbx.isChecked())
//                            update.put("wbx", true);
//                        update.put("delay",veneClassDelay.getText().toString());
//                        myRef.child("VeneuClass").child(venue).child("types").updateChildren(update).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//
//                            }
//                        }).addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                try{
//                                    progressDialog.dismiss();
//                                }catch (Exception c){
//                                    c.printStackTrace();
//                                }
//                            }
//                        });

//                    }else{
//                        Toast.makeText(AdminAddNewActivity.this, "Please select any type ", Toast.LENGTH_SHORT).show();
//                    }




                }

            }
        });


    }


    private void dataMenuplater() {

        cbMonday.setChecked(false);
        cbTuesday.setChecked(false);
        cbThurday.setChecked(false);
        cbWednessday.setChecked(false);
        cbFriDay.setChecked(false);
        cbSaturday.setChecked(false);
        cbSunday.setChecked(false);
        for(int i=0;i<dayTimeModels.size();i++){
            if(dayTimeModels.get(i).getDay().equalsIgnoreCase("Monday")){
                cbMonday.setChecked(true);
                mondayTime.setText(dayTimeModels.get(i).getTime());
                mondayseats.setText(dayTimeModels.get(i).getSeats());
                mondayTimeWbx.setText(dayTimeModels.get(i).getWbxTime());
            }else if(dayTimeModels.get(i).getDay().equalsIgnoreCase("Tuesday")){
                cbTuesday.setChecked(true);
                tusdayTime.setText(dayTimeModels.get(i).getTime());
                tusdayseats.setText(dayTimeModels.get(i).getSeats());
                tusdayTimeWbx.setText(dayTimeModels.get(i).getWbxTime());
            }else if(dayTimeModels.get(i).getDay().equalsIgnoreCase("Wednesday")){
                cbWednessday.setChecked(true);
                wednesdayTime.setText(dayTimeModels.get(i).getTime());
                wednesdayseats.setText(dayTimeModels.get(i).getSeats());
                wednesdayTimeWbx.setText(dayTimeModels.get(i).getWbxTime());
            }else if(dayTimeModels.get(i).getDay().equalsIgnoreCase("Thursday")){
                cbThurday.setChecked(true);
                thurdayTime.setText(dayTimeModels.get(i).getTime());
                thurdayseats.setText(dayTimeModels.get(i).getSeats());
                thurdayTimeWbx.setText(dayTimeModels.get(i).getWbxTime());
            }else if(dayTimeModels.get(i).getDay().equalsIgnoreCase("Friday")){
                cbFriDay.setChecked(true);
                fridayTime.setText(dayTimeModels.get(i).getTime());
                fridayseats.setText(dayTimeModels.get(i).getSeats());
                fridayTimeWbx.setText(dayTimeModels.get(i).getWbxTime());
            }else if(dayTimeModels.get(i).getDay().equalsIgnoreCase("Saturday")){
                cbSaturday.setChecked(true);
                saturdayTime.setText(dayTimeModels.get(i).getTime());
                saturdayseats.setText(dayTimeModels.get(i).getSeats());
                saturdayTimeWbx.setText(dayTimeModels.get(i).getWbxTime());
            }else if(dayTimeModels.get(i).getDay().equalsIgnoreCase("Sunday")){
                cbSunday.setChecked(true);
                sundayTime.setText(dayTimeModels.get(i).getTime());
                sundayseats.setText(dayTimeModels.get(i).getSeats());
                sundayTimeWbx.setText(dayTimeModels.get(i).getWbxTime());
            }
        }
    }

    public void getStartTime(EditText editText){
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        final TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(AdminAddNewActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                String hou=selectedHour+"";
                String min=selectedMinute+"";
                if(selectedHour<10)
                    hou="0"+selectedHour;
                if(selectedMinute<10)
                    min="0"+selectedMinute;
                editText.setText( selectedHour + ":" + selectedMinute);

//                String am_pm = "";
//
//                Calendar datetime = Calendar.getInstance();
//                datetime.set(Calendar.HOUR_OF_DAY, selectedHour);
//                datetime.set(Calendar.MINUTE, selectedMinute);
//
//                if (datetime.get(Calendar.AM_PM) == Calendar.AM)
//                    am_pm = "am";
//                else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
//                    am_pm = "pm";
//
//                int min=datetime.get(Calendar.MINUTE);
//                String mints=min+"";
//                if(min<10){
//                    mints="0"+mints;
//                }
//
//                String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ?"12":datetime.get(Calendar.HOUR)+"";

//                timing.setText(timing.getText().toString()+ strHrsToShow+":"+datetime.get(Calendar.MINUTE)+" "+am_pm+" to " );
//                if(selectedOption.equalsIgnoreCase("online")){
//                    if(datetime.get(Calendar.HOUR) == 0){
//                        time.setText(String.format("%02d:%02d", 12, datetime.get(Calendar.MINUTE))+" "+am_pm );
//                    }else{
//                        time.setText( String.format("%02d:%02d", datetime.get(Calendar.HOUR), datetime.get(Calendar.MINUTE))+" "+am_pm );
//                    }
//                }else{
//                    if(datetime.get(Calendar.HOUR) == 0){
//                        venuTime.setText(String.format("%02d:%02d", 12, datetime.get(Calendar.MINUTE))+" "+am_pm );
//                    }else{
//                        venuTime.setText( String.format("%02d:%02d", datetime.get(Calendar.HOUR), datetime.get(Calendar.MINUTE))+" "+am_pm );
//                    }
//                }
//                if(datetime.get(Calendar.HOUR) == 0){
//                    editText.setText(String.format("%02d:%02d", 12, datetime.get(Calendar.MINUTE))+" "+am_pm );
//                }else{
//                    editText.setText( String.format("%02d:%02d", datetime.get(Calendar.HOUR), datetime.get(Calendar.MINUTE))+" "+am_pm );
//                }


//                getEndTime();
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Start Time");


        mTimePicker.show();
    }
}
