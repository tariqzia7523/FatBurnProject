package com.fvt.fatburnproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
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
    EditText mondayTime,tusdayTime,wednesdayTime,thurdayTime,fridayTime,saturdayTime,sundayTime;
    EditText mondayseats,tusdayseats,wednesdayseats,thurdayseats,fridayseats,saturdayseats,sundayseats;
    DatabaseReference myRef;
    Spinner spinner,veneuNameSpinner;
    String selectedOption;
    LinearLayout onlineLayout,veneuLayout;
    EditText price,title,veneClassDelay;
    String selectedVeneu;
    ArrayList<DayTimeModel> dayTimeModels;
    ProgressDialog progressDialog;
    RadioGroup radioGroup;
    String TAG;
    CheckBox wbx,fbx;
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
        veneClassDelay=findViewById(R.id.delay);
        myRef = FirebaseDatabase.getInstance().getReference("Classes");
        selectedVeneu=getString(R.string.greatpark);
        onlineLayout=findViewById(R.id.online_layout);
        veneuLayout=findViewById(R.id.venue_layout);
        veneuLayout.setVisibility(View.GONE);
        price=findViewById(R.id.price);
        title=findViewById(R.id.title);
        cbMonday=findViewById(R.id.cb_monday);
        fbx=findViewById(R.id.fbx);
        wbx=findViewById(R.id.wbx);
        mondayseats=findViewById(R.id.monday_seats);
        tusdayseats=findViewById(R.id.tuesday_seats);
        wednesdayseats=findViewById(R.id.wednesday_seats);
        thurdayseats=findViewById(R.id.thursday_seats);
        fridayseats=findViewById(R.id.friday_seats);
        saturdayseats=findViewById(R.id.saturday_seats);
        sundayseats=findViewById(R.id.sunday_seats);
        cbMonday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mondayTime.setEnabled(isChecked);
                mondayseats.setEnabled(isChecked);
            }
        });
        cbTuesday=findViewById(R.id.cb_tuesday);
        cbTuesday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tusdayTime.setEnabled(isChecked);
                tusdayTime.setEnabled(isChecked);
            }
        });
        cbWednessday=findViewById(R.id.cb_wednesday);
        cbWednessday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                wednesdayTime.setEnabled(isChecked);
                wednesdayseats.setEnabled(isChecked);
            }
        });
        cbThurday=findViewById(R.id.cb_thursday);
        cbThurday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                thurdayTime.setEnabled(isChecked);
                thurdayseats.setEnabled(isChecked);
            }
        });
        cbFriDay=findViewById(R.id.cb_friday);
        cbFriDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                fridayTime.setEnabled(isChecked);
                fridayseats.setEnabled(isChecked);
            }
        });
        cbSaturday=findViewById(R.id.cb_saturday);
        cbSaturday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saturdayTime.setEnabled(isChecked);
                saturdayseats.setEnabled(isChecked);
            }
        });
        cbSunday=findViewById(R.id.cb_sunday);
        cbSunday.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sundayTime.setEnabled(isChecked);
                sundayseats.setEnabled(isChecked);
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
                                try{
                                    veneClassDelay.setText(dataSnapshot1.child("delay").getValue(String.class));
                                }catch (Exception c){
                                    c.printStackTrace();
                                }
                                try{
                                    if(dataSnapshot1.child("fbx").getValue(Boolean.class)){
                                        fbx.setChecked(true);
                                    }
                                }catch (Exception c){
                                    c.printStackTrace();
                                }
                                try{
                                    if(dataSnapshot1.child("wbx").getValue(Boolean.class)){
                                        wbx.setChecked(true);
                                    }
                                }catch (Exception c){
                                    c.printStackTrace();
                                }
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
                            price.getText().toString()+"£",
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
                    if(fbx.isChecked() || wbx.isChecked()){
                        ArrayList<DayTimeModel> days=new ArrayList<>();
                        if(cbMonday.isChecked()){
                            if(mondayTime.getText().toString().isEmpty() ){
                                Toast.makeText(getApplicationContext(),"Please Add Monday Time",Toast.LENGTH_LONG).show();
                                return;
                            }else if(mondayseats.getText().toString().isEmpty() ){
                                Toast.makeText(getApplicationContext(),"Please Add Monday Seats",Toast.LENGTH_LONG).show();
                                return;
                            }
                            else{
                                DayTimeModel dayTimeModel=new DayTimeModel();
                                dayTimeModel.setDay("Monday");
                                dayTimeModel.setTime(mondayTime.getText().toString());
                                dayTimeModel.setSeats(mondayseats.getText().toString());
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
                            }else{
                                DayTimeModel dayTimeModel=new DayTimeModel();
                                dayTimeModel.setDay("Tuesday");
                                dayTimeModel.setTime(tusdayTime.getText().toString());
                                dayTimeModel.setSeats(tusdayseats.getText().toString());
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
                            }else{
                                DayTimeModel dayTimeModel=new DayTimeModel();
                                dayTimeModel.setDay("Wednesday");
                                dayTimeModel.setTime(wednesdayTime.getText().toString());
                                dayTimeModel.setSeats(wednesdayseats.getText().toString());
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
                            }else{
                                DayTimeModel dayTimeModel=new DayTimeModel();
                                dayTimeModel.setDay("Thursday");
                                dayTimeModel.setTime(thurdayTime.getText().toString());
                                dayTimeModel.setSeats(thurdayseats.getText().toString());
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
                            }else{
                                DayTimeModel dayTimeModel=new DayTimeModel();
                                dayTimeModel.setDay("Friday");
                                dayTimeModel.setTime(fridayTime.getText().toString());
                                dayTimeModel.setSeats(fridayseats.getText().toString());
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
                            }else{
                                DayTimeModel dayTimeModel=new DayTimeModel();
                                dayTimeModel.setDay("Saturday");
                                dayTimeModel.setTime(saturdayTime.getText().toString());
                                dayTimeModel.setSeats(saturdayseats.getText().toString());
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
                            }else{
                                DayTimeModel dayTimeModel=new DayTimeModel();
                                dayTimeModel.setDay("Sunday");
                                dayTimeModel.setTime(sundayTime.getText().toString());
                                dayTimeModel.setSeats(sundayseats.getText().toString());
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
                        Map<String, Object> update = new HashMap<>();
                        if(fbx.isChecked())
                            update.put("fbx", true);
                        if(wbx.isChecked())
                            update.put("wbx", true);
                        update.put("delay",veneClassDelay.getText().toString());
                        myRef.child("VeneuClass").child(venue).child("types").updateChildren(update).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        }).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                try{
                                    progressDialog.dismiss();
                                }catch (Exception c){
                                    c.printStackTrace();
                                }
                            }
                        });

                    }else{
                        Toast.makeText(AdminAddNewActivity.this, "Please select any type ", Toast.LENGTH_SHORT).show();
                    }




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
            }else if(dayTimeModels.get(i).getDay().equalsIgnoreCase("Tuesday")){
                cbTuesday.setChecked(true);
                tusdayTime.setText(dayTimeModels.get(i).getTime());
                tusdayseats.setText(dayTimeModels.get(i).getSeats());
            }else if(dayTimeModels.get(i).getDay().equalsIgnoreCase("Wednesday")){
                cbWednessday.setChecked(true);
                wednesdayTime.setText(dayTimeModels.get(i).getTime());
                wednesdayseats.setText(dayTimeModels.get(i).getSeats());
            }else if(dayTimeModels.get(i).getDay().equalsIgnoreCase("Thursday")){
                cbThurday.setChecked(true);
                thurdayTime.setText(dayTimeModels.get(i).getTime());
                thurdayseats.setText(dayTimeModels.get(i).getSeats());
            }else if(dayTimeModels.get(i).getDay().equalsIgnoreCase("Friday")){
                cbFriDay.setChecked(true);
                fridayTime.setText(dayTimeModels.get(i).getTime());
                fridayseats.setText(dayTimeModels.get(i).getSeats());
            }else if(dayTimeModels.get(i).getDay().equalsIgnoreCase("Saturday")){
                cbSaturday.setChecked(true);
                saturdayTime.setText(dayTimeModels.get(i).getTime());
                saturdayseats.setText(dayTimeModels.get(i).getSeats());
            }else if(dayTimeModels.get(i).getDay().equalsIgnoreCase("Sunday")){
                cbSunday.setChecked(true);
                sundayTime.setText(dayTimeModels.get(i).getTime());
                sundayseats.setText(dayTimeModels.get(i).getSeats());
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
//                timing.setText(timing.getText().toString()+ selectedHour + ":" + selectedMinute);
                String am_pm = "";

                Calendar datetime = Calendar.getInstance();
                datetime.set(Calendar.HOUR_OF_DAY, selectedHour);
                datetime.set(Calendar.MINUTE, selectedMinute);

                if (datetime.get(Calendar.AM_PM) == Calendar.AM)
                    am_pm = "am";
                else if (datetime.get(Calendar.AM_PM) == Calendar.PM)
                    am_pm = "pm";

                int min=datetime.get(Calendar.MINUTE);
                String mints=min+"";
                if(min<10){
                    mints="0"+mints;
                }

                String strHrsToShow = (datetime.get(Calendar.HOUR) == 0) ?"12":datetime.get(Calendar.HOUR)+"";

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
                if(datetime.get(Calendar.HOUR) == 0){
                    editText.setText(String.format("%02d:%02d", 12, datetime.get(Calendar.MINUTE))+" "+am_pm );
                }else{
                    editText.setText( String.format("%02d:%02d", datetime.get(Calendar.HOUR), datetime.get(Calendar.MINUTE))+" "+am_pm );
                }


//                getEndTime();
            }
        }, hour, minute, false);//Yes 24 hour time
        mTimePicker.setTitle("Select Start Time");

        mTimePicker.show();
    }
}
