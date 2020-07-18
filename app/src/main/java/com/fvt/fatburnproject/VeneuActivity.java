package com.fvt.fatburnproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nimbusds.jwt.util.DateUtils;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardMultilineWidget;

import org.json.JSONObject;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class VeneuActivity extends AppCompatActivity {
    DatabaseReference myRef,myRefPrice,myRefBooked,myRefHistory,myRefCart,myRefDiscount;
    ArrayList<String> venuesNames;
    ArrayList<VeneuModel> classTimings;
    Spinner spinner;
    MyAdapter myAdapter;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    String TAG;
//    CheckBox fbx,wbx;
    String fbxPrice,wbxPrice;
    TextView totalText;
    double totalPrice;
    String paymentId,transectionId,phone;
//    VeneuModel veneuModel;
    private static final String PAYPAL_KEY = "ASwRlhlkFphl3dvNIF8ADgmYu1IkcqIawzklRbALqu0GpT3GFXFkoEND6Q9ptrrvjBzAvjMVY8Mi5qvi";
    private static final int REQUEST_CODE_PAYMENT = 1;
    private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_PRODUCTION;
    private static PayPalConfiguration config;
    PayPalPayment thingstoBuy;
    PayUserModel payUserModel;
    private Stripe stripe;
    CardMultilineWidget cardInputWidget;
    private String secretKey ;
    private String publishableKey;
    String android_id,personName;
    ArrayList<VeneuModel> veneuModels;
    TextView discountPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veneu);
        veneuModels=new ArrayList<>();
        getSupportActionBar().hide();
        android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        progressDialog=new ProgressDialog(VeneuActivity.this);
        progressDialog.setMessage("Please Wait...");
        TAG="***veneu";
        secretKey=StripsIDsClass.getSecretEKey();
//        secretKey="sk_test_t2qvlU7H89Zpv06cvs9CSGxf00QK7CthR0";
        publishableKey=StripsIDsClass.getPubliserKey();
//        publishableKey="pk_test_hUE52VSBGt7oUf7AORrcPRpD0024H5exOY";
//        PaymentConfiguration.init(getApplicationContext(), StripsIDsClass.getPubliserKey());
        PaymentConfiguration.init(getApplicationContext(), publishableKey);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        discountPrice=findViewById(R.id.discount_price);
        totalText=findViewById(R.id.total_text);
        totalText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(VeneuActivity.this,MyCartClient.class).putExtra("flag",1));
                finish();
            }
        });
        Log.e(TAG,"vence activity called");
        spinner=findViewById(R.id.spinner);
        venuesNames=new ArrayList<>();
        classTimings=new ArrayList<>();
        recyclerView=findViewById(R.id.class_list);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(VeneuActivity.this));
        myAdapter=new MyAdapter(getApplicationContext(),VeneuActivity.this,classTimings);
        recyclerView.setAdapter(myAdapter);
        myRefDiscount = FirebaseDatabase.getInstance().getReference("Classes").child("Discount");
        myRefDiscount.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String discount="";
                boolean discountCheck=false;
                try{
                    discountCheck=dataSnapshot.child("discountCheck").getValue(Boolean.class);
                }catch (Exception c){
                    c.printStackTrace();
                }
                try{
                    discount=dataSnapshot.child("discount").getValue(String.class);
                }catch (Exception c){
                    c.printStackTrace();
                }

                if(discountCheck){
                    discountPrice.setVisibility(View.VISIBLE);
                    discountPrice.setText(discount+" discount with loyal10");
                }else{
                    discountPrice.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        myRefCart = FirebaseDatabase.getInstance().getReference("Classes").child("Cart");
        myRefHistory = FirebaseDatabase.getInstance().getReference("Classes").child("History");
        myRefBooked = FirebaseDatabase.getInstance().getReference("Classes").child("Booked").child("VeneuClasses");

        myRefPrice = FirebaseDatabase.getInstance().getReference("Classes").child("Prices");
        myRefPrice.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                try{
                    fbxPrice=dataSnapshot.child("FBX").child("price").getValue(String.class);
                }catch (Exception c){
                    c.printStackTrace();
                }
                try{
                    wbxPrice=dataSnapshot.child("WBX").child("price").getValue(String.class);
                }catch (Exception c){
                    c.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        myRef = FirebaseDatabase.getInstance().getReference("Classes").child("VeneuClass");
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        progressDialog.show();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    venuesNames.clear();
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        venuesNames.add(dataSnapshot1.getKey());
                    }
                    ArrayAdapter<String> adapter =
//                            new ArrayAdapter<String>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, venuesNames);
                            new ArrayAdapter<String>(getApplicationContext(),  R.layout.spinner_item, venuesNames);
                    adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);

                        for(DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()){
                            Log.e(TAG,dataSnapshot2.getKey()+" is id");
                            VeneuModel veneuModel=new VeneuModel();
                            veneuModel.setVenue(venuesNames.get(0));
                            veneuModel.setDay(dataSnapshot2.getKey());
                            veneuModel.setTotalSeats(dataSnapshot2.child("seats").getValue(String.class));
                            veneuModel.setFbxSeats(dataSnapshot2.child("fbxSeats").getValue(String.class));
                            veneuModel.setWbxSeats(dataSnapshot2.child("wbxSeats").getValue(String.class));
                            veneuModel.setTime(dataSnapshot2.child("time").getValue(String.class));
                            veneuModel.setDelay(dataSnapshot2.child("delay").getValue(String.class));
                            veneuModel.setFbx(dataSnapshot2.child("fbx").getValue(Boolean.class));
                            veneuModel.setWbx(dataSnapshot2.child("wbx").getValue(Boolean.class));
                            veneuModel.setSelected(false);
                            try{
                                int s=Integer.parseInt(dataSnapshot2.child("fbxSeats").getValue(String.class));
                                if(s<=0){
                                    veneuModel.setFbxEnabled(false);
//                                    classTimings.add(veneuModel);
                                }else{
                                    veneuModel.setFbxSeats(s+"");
                                    veneuModel.setFbxEnabled(true);
//                                    classTimings.add(veneuModel);
                                }
                            }catch (Exception c){
                                c.printStackTrace();
                            }
                            try{
                                int s=Integer.parseInt(dataSnapshot2.child("wbxSeats").getValue(String.class));
                                if(s<=0){
                                    veneuModel.setWbxEnabled(false);
//                                    classTimings.add(veneuModel);
                                }else{
                                    veneuModel.setWbxSeats(s+"");
                                    veneuModel.setWbxEnabled(true);
//                                    classTimings.add(veneuModel);
                                }
                            }catch (Exception c){
                                c.printStackTrace();
                            }
                            classTimings.add(veneuModel);
                        }


                    progressDialog.dismiss();
                    myAdapter.notifyDataSetChanged();

                }catch (Exception c){
                    c.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                String selectedItemText = (String) parentView.getItemAtPosition(position);
                progressDialog.show();
                myRef.child(selectedItemText).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        classTimings.clear();
                            for(DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()){
                                Log.e(TAG,dataSnapshot2.getKey()+" is id");
                                VeneuModel veneuModel=new VeneuModel();
                                veneuModel.setVenue(selectedItemText);
                                veneuModel.setDay(dataSnapshot2.getKey());

                                veneuModel.setTotalSeats(dataSnapshot2.child("seats").getValue(String.class));
                                veneuModel.setFbxSeats(dataSnapshot2.child("fbxSeats").getValue(String.class));
                                veneuModel.setWbxSeats(dataSnapshot2.child("wbxSeats").getValue(String.class));
                                veneuModel.setTime(dataSnapshot2.child("time").getValue(String.class));
                                veneuModel.setSelected(false);
                                veneuModel.setDelay(dataSnapshot2.child("delay").getValue(String.class));
                                veneuModel.setFbxEnabled(dataSnapshot2.child("fbx").getValue(Boolean.class));
                                veneuModel.setWbxEnabled(dataSnapshot2.child("wbx").getValue(Boolean.class));
                                try{
                                    int s=Integer.parseInt(dataSnapshot2.child("fbxSeats").getValue(String.class));
                                    if(s<=0){
                                        veneuModel.setFbxEnabled(false);
//                                        classTimings.add(veneuModel);
                                    }else{
                                        veneuModel.setFbxSeats(s+"");
                                        veneuModel.setFbxEnabled(true);
//                                        classTimings.add(veneuModel);
                                    }

                                }catch (Exception c){
                                    c.printStackTrace();
                                }
                                try{
                                    int s=Integer.parseInt(dataSnapshot2.child("wbxSeats").getValue(String.class));
                                    if(s<=0){
                                        veneuModel.setWbxEnabled(false);
//                                        classTimings.add(veneuModel);
                                    }else{
                                        veneuModel.setWbxSeats(s+"");
                                        veneuModel.setWbxEnabled(true);
//                                        classTimings.add(veneuModel);
                                    }
//                                    classTimings.add(veneuModel);
                                }catch (Exception c){
                                    c.printStackTrace();
                                }
                                classTimings.add(veneuModel);
                            }
                        progressDialog.dismiss();
                        myAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        findViewById(R.id.pay_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
//                    int seats=Integer.parseInt(veneuModel.getTotalSeats());
//                    if(seats<=0){
//                        Toast.makeText(getApplicationContext(),"No seats available ",Toast.LENGTH_LONG).show();
//                        return;
//                    }
                    if(totalPrice>0)
                        paymentiflatecall(totalPrice);
                    else
                        Toast.makeText(getApplicationContext(),"Please select Class type",Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(),"Price "+totalPrice,Toast.LENGTH_LONG).show();
                }catch (Exception c){
                    c.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Please select class timming",Toast.LENGTH_LONG).show();
                }
            }
        });
        findViewById(R.id.pay_with_paypal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
//                    int seats=Integer.parseInt(veneuModel.getTotalSeats());
//                    if(seats<=0){
//                        Toast.makeText(getApplicationContext(),"No seats available ",Toast.LENGTH_LONG).show();
//                        return;
//                    }
                    if(totalPrice>0)
//                    makePayment();
                        paymentpaypalname();
                    else
                        Toast.makeText(getApplicationContext(),"Please select Class type",Toast.LENGTH_LONG).show();
                }catch (Exception c){
                    c.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Please select class timming",Toast.LENGTH_LONG).show();
                }

            }
        });
        congifPaypal();
    }

    @Override
    protected void onStart() {
        super.onStart();
        myAdapter.notifyDataSetChanged();
    }

    public int  paymentpaypalname() {
        Log.e(TAG,"payment called");
//        Context c = getApplicationContext();
//        LinearLayout layout = new LinearLayout(c);
//        layout.setBackgroundColor(getResources().getColor(R.color.transbackground));
//        layout.setOrientation(LinearLayout.VERTICAL);
//        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        AlertDialog.Builder al = new AlertDialog.Builder(VeneuActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
//        view = inflater.inflate(R.layout.payment_layout, null);
        view = inflater.inflate(R.layout.activity_add_payment, null);
        al.setView(view);
        final AlertDialog value = al.create();
        value.setCancelable(false);
        //final ListView lv=new ListView(this);
        cardInputWidget = view.findViewById(R.id.cardInputWidget);
        cardInputWidget.setVisibility(View.GONE);
        final EditText perName = view.findViewById(R.id.person_name);
        final EditText phoneNumber = view.findViewById(R.id.phone_number);
        Button payButton = view.findViewById(R.id.payButton);
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!perName.getText().toString().isEmpty()
                        && !phoneNumber.getText().toString().isEmpty()) {
                    payUserModel=new PayUserModel();
                    personName=perName.getText().toString()+":_:"+android_id;
                    payUserModel.setName(personName);
                    payUserModel.setPhoneNumber(phoneNumber.getText().toString());
                    phone=phoneNumber.getText().toString();
//                    veneuModel.setPersonName(perName.getText().toString());
//                    veneuModel.setPhoneNumber(phoneNumber.getText().toString());
                    progressDialog.show();
                    makePayment();
                    value.dismiss();
                }
                else
                    Toast.makeText(getApplicationContext(),"Enter name",Toast.LENGTH_LONG).show();

                Log.e(TAG,"Funtion called");
            }
        });

        value.show();
        value.setCancelable(true);
        return 0;
    }
    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        ArrayList<VeneuModel> data;
        Context context;
        Activity activity;
        String TAG;
        public class MyViewHolder extends RecyclerView.ViewHolder  {
//            ImageView image;
            TextView day,fbxTime,wbxtime;
            CheckBox fbx,wbx;
            CardView cardView;

            public MyViewHolder(View view) {
                super(view);
//                sideImage=view.findViewById(R.id.side_image);
//                image=view.findViewById(R.id.select_image);
                day=view.findViewById(R.id.day);
                fbxTime=view.findViewById(R.id.fbx_time);
//                fbxTime.setVisibility(View.GONE);
                wbxtime=view.findViewById(R.id.wbx_time);
//                wbxtime.setVisibility(View.GONE);
                fbx=view.findViewById(R.id.fbx);
                wbx=view.findViewById(R.id.wbx);
                cardView=view.findViewById(R.id.card_view);
                cardView.setBackgroundResource(R.drawable.shadow);

//                day=view.findViewById(R.id.day);
//                day.setVisibility(View.GONE);


            }
        }
        public MyAdapter(Context c, Activity a , ArrayList<VeneuModel> moviesList){
            this.data =moviesList;
            context=c;
            activity=a;
            TAG="***Adapter";
        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.revenue_class_item, parent, false);
            return new MyViewHolder(itemView);
        }
        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
//        if (flag==1){// beign called from my profile so we have to set visible following image
//            holder.menuImage.setVisibility(View.VISIBLE);
//        }

//        final OnlineClassModel model=data.get(position);
            Log.e(TAG,"delye is "+classTimings.get(position).getDelay());
            boolean tempStatus=false;
            if(!data.get(position).isFbxEnabled()) {
                holder.fbx.setVisibility(View.GONE);
                holder.fbxTime.setVisibility(View.GONE);
            }else{
                holder.fbx.setVisibility(View.VISIBLE);
                holder.fbxTime.setVisibility(View.VISIBLE);
            }

            if(!data.get(position).isWbxEnabled()) {
                holder.wbx.setVisibility(View.GONE);
                holder.wbxtime.setVisibility(View.GONE);
            }else {
                holder.wbx.setVisibility(View.VISIBLE);
                holder.wbxtime.setVisibility(View.VISIBLE);
            }
            holder.day.setText(data.get(position).getDay());
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
            Date d = new Date();
            String dayOfTheWeek = sdf.format(d);
            if(dayOfTheWeek.equalsIgnoreCase(data.get(position).getDay())){
                Calendar cal = GregorianCalendar.getInstance();
                cal.setTime(new Date());
                cal.add(Calendar.DAY_OF_YEAR, 7);
                String date = new SimpleDateFormat("EEEE dd/MM/yyyy", Locale.getDefault()).format(cal.getTime());
                holder.day.setText(date);


            }else{
                Calendar cal = Calendar.getInstance();
//                cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
                SimpleDateFormat sdf2 = new SimpleDateFormat("EEEE dd/MM/yyyy");
                for (int i = 0; i < 7; i++) {
                    String string=sdf2.format(cal.getTime());
                    if(string.contains(data.get(position).getDay())){
                        holder.day.setText(string);
                        break;
                    }
                    Log.i("dateTag", sdf2.format(cal.getTime()));
                    cal.add(Calendar.DAY_OF_WEEK, 1);
                }
            }

            classTimings.get(position).setFbx(false);
            classTimings.get(position).setWbx(false);

//            holder.day.setText(data.get(position).getDay()+" Time "+data.get(position).getTime()+ " price "+fbxPrice);
            myRefCart.child(android_id).child(data.get(position).getVenue()).child(data.get(position).getDay()).child("fbx").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try{
                        holder.fbx.setChecked(dataSnapshot.getValue(Boolean.class));
                    }catch (Exception c){
                        c.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            holder.fbxTime.setText("FBX ("+fbxPrice+")"+ " -> "+
                    data.get(position).getTime()+" -> " +"Available spaces : "+data.get(position).getFbxSeats());
            String endTime = null;
            try {
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm", Locale.US);
                Date now = formatter.parse(data.get(position).getTime());
                int dely=Integer.parseInt(classTimings.get(0).getDelay());
                Log.e(TAG,"delay is "+dely);
//
                Calendar calendar=Calendar.getInstance(Locale.US);
                calendar.setTime(now);
                DateFormat timeFormat = new SimpleDateFormat("HH:mm");
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(now.getTime());


                cal.add(Calendar.MINUTE, dely);

                endTime = timeFormat.format(cal.getTime());
                holder.wbxtime.setText("WTB ("+wbxPrice+")"+ " -> "+ endTime
                        +" Available spaces : "+data.get(position).getFbxSeats());
//                            totalText.setText("£"+priceAdder(holder.fbx,holder.wbx)+"");
            } catch (Exception e) {
                e.printStackTrace();
            }

//            holder.wbxtime.setVisibility(View.VISIBLE);
//                        holder.wbxtime.setText(endTime+" Wbx " +" price "+wbxPrice);
//            holder.wbxtime.setText("WTB ("+wbxPrice+")"+ " -> "+ endTime
//                    +" Available spaces : "+data.get(position).getFbxSeats());
            holder.fbx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
               @Override
               public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                   if(isChecked){
//                       holder.fbxTime.setVisibility(View.VISIBLE);
                     data.get(position).setSelected(true);
//                       totalText.setText("£"+priceAdder(holder.fbx,holder.wbx)+"");
//                       tempStatus=true;
                   }else{
//                       holder.fbxTime.setVisibility(View.GONE);
//                       totalText.setText("£"+pricesubstractor(holder.fbx,holder.wbx)+"");
                       if(!holder.wbx.isChecked()){
                           data.get(position).setSelected(false);
                       }else{
                           data.get(position).setSelected(true);
                       }
                   }
                   classTimings.get(position).setFbx(isChecked);
                   totalText.setText("Added to cart : £"+priceChanger()+"");

                   addtoCart(position);

               }
           });
//            if(holder.fbx.isChecked()){
//                holder.fbxTime.setVisibility(View.VISIBLE);
//                holder.fbxTime.setText("FBX ("+fbxPrice+")"+ " -> "+ data.get(position).getTime());
//                tempStatus=true;
//            }
            myRefCart.child(android_id).child(data.get(position).getVenue()).child(data.get(position).getDay()).child("wbx").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try{
                        holder.wbx.setChecked(dataSnapshot.getValue(Boolean.class));
                    }catch (Exception c){
                        c.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            holder.wbx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        data.get(position).setSelected(true);
//                        totalText.setText("£"+priceAdder(holder.fbx,holder.wbx)+"");
//                        String endTime = null;
//                        try {
//                            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm ", Locale.US);
//                            Date now = formatter.parse(data.get(position).getTime());
//                            int dely=Integer.parseInt(classTimings.get(0).getDelay());
//                            Log.e(TAG,"delay is "+dely);
////
//                            Calendar calendar=Calendar.getInstance(Locale.US);
//                            calendar.setTime(now);
//                            DateFormat timeFormat = new SimpleDateFormat("hh:mm ");
//                            Calendar cal = Calendar.getInstance();
//                            cal.setTimeInMillis(now.getTime());
//
//
//                            cal.add(Calendar.MINUTE, dely);
//
//                            endTime = timeFormat.format(cal.getTime());
////                            totalText.setText("£"+priceAdder(holder.fbx,holder.wbx)+"");
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                        holder.wbxtime.setVisibility(View.VISIBLE);
////                        holder.wbxtime.setText(endTime+" Wbx " +" price "+wbxPrice);
//                        holder.wbxtime.setText("WTB ("+wbxPrice+")"+ " -> "+ endTime
//                                +" Available spaces : "+data.get(position).getFbxSeats());

                    }else{
//                        holder.wbxtime.setVisibility(View.GONE);
                        totalText.setText("£"+pricesubstractor(holder.fbx,holder.wbx)+"");

                        if(!holder.fbx.isChecked()){
                            data.get(position).setSelected(false);
                        }else{
                            data.get(position).setSelected(true);
                        }

                    }
                    classTimings.get(position).setWbx(isChecked);
                    totalText.setText("Added to cart : £"+priceChanger()+"");

                    addtoCart(position);
                }



            });


//            holder.image.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    if(fbx.isChecked() || wbx.isChecked()){
//                        if(data.get(position).isSelected()){
//                            data.get(position).setSelected(false);
//                            totalText.setText("£"+pricesubstractor()+"");
//                            veneuModels.remove(data.get(position));
//                        }else{
//
////                            double wbxpriceint=0;
////                            double fbxpirceint=0;
////                            try{
////                                wbxpriceint=Double.parseDouble(wbxPrice.substring(1,wbxPrice.length()));
////                                fbxpirceint=Double.parseDouble(fbxPrice.substring(1,wbxPrice.length()));
////                            }catch (Exception c){
////                                c.printStackTrace();
////                            }
////                            Log.e(TAG,"fbx price "+fbxPrice);
////                            Log.e(TAG,"wbx price "+wbxPrice);
////                            Log.e(TAG,"fbx price int "+fbxpirceint);
////                            Log.e(TAG,"wbx price int  "+wbxpriceint);
////                            if(fbx.isChecked() && wbx.isChecked()){
////                                totalPrice=wbxpriceint+fbxpirceint;
////                                totalText.setText("Pay : £"+totalPrice);
////                            }else if(fbx.isChecked()){
////                                totalPrice=fbxpirceint;
////                                totalText.setText("Pay : £"+totalPrice);
////                            }else if(wbx.isChecked()){
////                                totalPrice=wbxpriceint;
////                                totalText.setText("Pay : £"+totalPrice);
////                            }else{
////                                totalPrice=0;
////                                totalText.setText("Pay ");
////                            }
//
//                            data.get(position).setSelected(true);
//                            veneuModel=data.get(position);
//                            veneuModels.add(data.get(position));
//                            totalText.setText("£"+priceAdder()+"");
//                        }
//
//                        notifyDataSetChanged();
//                    }else{
//                        Toast.makeText(getApplicationContext(),"please select Class type",Toast.LENGTH_LONG).show();
//                    }
//
//
//
//                }
//            });



        }
        @Override
        public int getItemCount() {
//        return  5;
            return data.size();
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
    }


    public void addtoCart(int i){
        String type="";
        if(classTimings.get(i).isFbx()){
            type=type+",FBX";
//            int seats=Integer.parseInt( classTimings.get(i).getFbxSeats());
//            seats--;
//            if(seats<0){
//
//            }else
//                myRef.child( classTimings.get(i).getVenue()).child( classTimings.get(i).getDay()).child("fbxSeats").setValue(seats+"");

        }
        if(classTimings.get(i).isFbx()){
            type=type+",WTB";
//            int seats=Integer.parseInt( classTimings.get(i).getWbxSeats());
//            seats--;
//            if(seats<0){
//
//            }else{
//                myRef.child( classTimings.get(i).getVenue()).child( classTimings.get(i).getDay()).child("wbxSeats").setValue(seats+"");
//            }

        }
        try{
            type=type.substring(1,type.length());
        }catch (Exception c){
            c.printStackTrace();
        }

        classTimings.get(i).setType(type);
//        classTimings.get(i).setTransectionId(transectionId);
//        classTimings.get(i).setPaymentId(paymentId);
//        classTimings.get(i).setPhoneNumber(phone);
        if(classTimings.get(i).isSelected()){
            myRefCart.child(android_id).child( classTimings.get(i).getVenue())
                    .child(classTimings.get(i).getDay()).setValue(classTimings.get(i));
//            myRefCart.child( classTimings.get(i).getVenue()).child(personName+i).setValue( classTimings.get(i));
//                int seats=Integer.parseInt( classTimings.get(i).getTotalSeats());
//                seats--;
//                myRef.child( classTimings.get(i).getVenue()).child( classTimings.get(i).getDay()).child("seats").setValue(seats+"");

        }

    }



    private void congifPaypal() {
        config = new PayPalConfiguration()
                .environment(CONFIG_ENVIRONMENT)
                .clientId(PAYPAL_KEY)
                .merchantName("Paypal Login")
                .merchantPrivacyPolicyUri(Uri.parse("https:www.example.com/privacy"))
                .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));
    }
    private void makePayment() {
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
        //******************* hard coded amount ************************
        thingstoBuy = new PayPalPayment(new BigDecimal(String.valueOf(totalPrice+"")), "GBP", "Payment", PayPalPayment.PAYMENT_INTENT_ORDER);
//        thingstoBuy = new PayPalPayment(new BigDecimal(String.valueOf("0.1")), "GBP", "Payment", PayPalPayment.PAYMENT_INTENT_ORDER);
        Intent paymentIntent = new Intent(this, PaymentActivity.class);
        paymentIntent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingstoBuy);
        paymentIntent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startActivityForResult(paymentIntent, REQUEST_CODE_PAYMENT);
    }



    public int  paymentiflatecall(final double price) {
        Log.e(TAG,"payment called");
//        Context c = getApplicationContext();
//        LinearLayout layout = new LinearLayout(c);
//        layout.setBackgroundColor(getResources().getColor(R.color.transbackground));
//        layout.setOrientation(LinearLayout.VERTICAL);
//        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        AlertDialog.Builder al = new AlertDialog.Builder(VeneuActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
//        view = inflater.inflate(R.layout.payment_layout, null);
        view = inflater.inflate(R.layout.activity_add_payment, null);
        al.setView(view);
        final AlertDialog value = al.create();
        value.setCancelable(false);
        //final ListView lv=new ListView(this);
        cardInputWidget = view.findViewById(R.id.cardInputWidget);
        final EditText perName = view.findViewById(R.id.person_name);
        final EditText phoneNumber = view.findViewById(R.id.phone_number);
        Button payButton = view.findViewById(R.id.payButton);
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cardInputWidget.getCard().validateCard() && !perName.getText().toString().isEmpty()
                        && !phoneNumber.getText().toString().isEmpty()) {
//                    value.dismiss();
                    payUserModel=new PayUserModel();
                    personName=perName.getText().toString()+":_:"+android_id;
                    payUserModel.setName(personName);
                    payUserModel.setPhoneNumber(phoneNumber.getText().toString());
                    phone=phoneNumber.getText().toString();
//                    veneuModel.setPersonName(perName.getText().toString());
//                    veneuModel.setPhoneNumber(phoneNumber.getText().toString());
//                    /
                    progressDialog.show();
                    value.dismiss();
                    paymentCall(price);
                }
                else
                    Toast.makeText(getApplicationContext(),"Invalid  Details",Toast.LENGTH_LONG).show();

                Log.e(TAG,"Funtion called");
            }
        });

        value.show();
        value.setCancelable(true);
        return 0;
    }

    public void paymentCall(double price){
        com.stripe.Stripe.apiKey = secretKey;
        PaymentMethodCreateParams params = cardInputWidget.getPaymentMethodCreateParams();
        if (params != null) {
            List<Object> paymentMethodTypes =
                    new ArrayList<>();
            paymentMethodTypes.add("card");
            Map<String, Object> paymentIntentParams = new HashMap<>();
//            paymentIntentParams.put("amount", ((int)price * 100)); //1 is the amount to be deducted, 100 is must since it accepts cents & 1£=100cents
            paymentIntentParams.put("amount", ((int)Math.round(price) * 100)); //1 is the amount to be deducted, 100 is must since it accepts cents & 1£=100cents
            Log.e(TAG,"rounded value "+(int)Math.round(price));
//            paymentIntentParams.put("currency", "usd");
            paymentIntentParams.put("currency", "GBP");
            paymentIntentParams.put("payment_method_types", paymentMethodTypes);
            try {
                com.stripe.model.PaymentIntent paymentIntent = new com.stripe.model.PaymentIntent().create(paymentIntentParams);
                ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams
                        .createWithPaymentMethodCreateParams(params, paymentIntent.getClientSecret());
                final Context context = getApplicationContext();
                stripe = new Stripe(
                        context,
                        PaymentConfiguration.getInstance(context).getPublishableKey()
                );
                stripe.confirmPayment(VeneuActivity.this, confirmParams);
            }
            catch (Exception e){
                e.printStackTrace();
            }catch (Error e){
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_PAYMENT){
            if(resultCode == RESULT_OK) {
                PaymentConfirmation paymentConfirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if(paymentConfirmation != null) {
                    try {
                        Log.e("paypalPayment", "" + paymentConfirmation.toJSONObject().toString(4));
//                        veneuModel.setPaymentId(paymentConfirmation.getProofOfPayment().getPaymentId());
                        paymentId=paymentConfirmation.getProofOfPayment().getPaymentId();
//                        veneuModel.setTransectionId(paymentConfirmation.getProofOfPayment().getTransactionId());
                        transectionId= paymentConfirmation.getProofOfPayment().getTransactionId();
                        updateBookedTable();
                    }
                    catch(Exception e) {
                        Log.e("paypalPayment", "Catch: " + e.getLocalizedMessage());
                        e.printStackTrace();
                    }
                }
            } else if(resultCode == RESULT_CANCELED) {
//                Log.e("paypalPayment", "Payment Cancelled");
//                Toast.makeText(getApplicationContext(),"Payment canceled",Toast.LENGTH_LONG).show();
            }
            else if(resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
//                Toast.makeText(getApplicationContext(),"Payment INVALID",Toast.LENGTH_LONG).show();
            }
        }else{
            stripe.onPaymentResult(requestCode, data, new PaymentResultCallback(VeneuActivity.this));
        }

        // Handle the result of stripe.confirmPayment
//        stripe.onPaymentResult(requestCode, data, new PaymentResultCallback(VeneClassDetailActivity.this));
    }

    private final class PaymentResultCallback
            implements ApiResultCallback<PaymentIntentResult> {
        @NonNull
        private final WeakReference<VeneuActivity> activityRef;

        PaymentResultCallback(@NonNull VeneuActivity activity) {
            activityRef = new WeakReference<>(activity);
        }

        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {
            final VeneuActivity activity = activityRef.get();
            if (activity == null) {
                return;
            }

            PaymentIntent paymentIntent = result.getIntent();
            PaymentIntent.Status status = paymentIntent.getStatus();
            if (status == PaymentIntent.Status.Succeeded) {
                // Payment completed successfully
                Gson gson = new GsonBuilder().setPrettyPrinting().create();
                Log.e("stripePayment", "Payment Completed: " + gson.toJson(paymentIntent));
                try{
                    JSONObject jsonObject=new JSONObject(gson.toJson(paymentIntent));
                    if(jsonObject.getString("status").equalsIgnoreCase("Succeeded")){
                        //add order to database
                        //succsess a gai
                        Log.e(TAG,"amount paid");
//                        veneuModel.setTransectionId(jsonObject.getString("id"));
                        transectionId=jsonObject.getString("id");
//                        veneuModel.setPaymentId(jsonObject.getString("created"));
                        paymentId=jsonObject.getString("created");
                        Toast.makeText(getApplicationContext(),"amount paid",Toast.LENGTH_LONG).show();
                        updateBookedTable();

                    }
                }catch (Exception c){
                    c.printStackTrace();
                    Log.e("stripePayment","json exception gai ");
                }

            } else if (status == PaymentIntent.Status.RequiresPaymentMethod) {
                try{
                    //un secc
                }catch (Exception c){
                    c.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Unable to proceed",Toast.LENGTH_LONG).show();
                }

                Log.e("stripePayment", "Payment Failed: " + paymentIntent.getLastPaymentError().getMessage());
            }
        }

        @Override
        public void onError(@NonNull Exception e) {
            final VeneuActivity activity = activityRef.get();
            if (activity == null) {
                return;
            }
            Log.e("stripePayment", "Error: " +e.getLocalizedMessage());
            e.printStackTrace();
        }

    }

//    public void updateBookedTable(){
//
//
//        for(int i=0;i<veneuModels.size();i++){
//            String type="";
//            if(veneuModels.get(i).isFbx()){
//                type=",FBX";
//            }
//            if(veneuModels.get(i).isFbx()){
//                type=",WTB";
//            }
//            try{
//                type=type.substring(1,type.length());
//            }catch (Exception c){
//                c.printStackTrace();
//            }
//
//            veneuModels.get(i).setType(type);
//
//            myRefBooked.child( veneuModels.get(i).getVenue()).child(personName+i).setValue( veneuModels.get(i));
//            int seats=Integer.parseInt( veneuModels.get(i).getTotalSeats());
//            seats--;
//            myRef.child( veneuModels.get(i).getVenue()).child( veneuModels.get(i).getDay()).child("seats").setValue(seats+"");
//
//        }
//
//        progressDialog.dismiss();
//        aleartDialog();
//
//    }
    public void updateBookedTable(){


        for(int i=0;i<classTimings.size();i++){
            String type="";
            if(classTimings.get(i).isFbx()){
                type=type+",FBX";
                int seats=Integer.parseInt( classTimings.get(i).getFbxSeats());
                seats--;
                if(seats<0){

                }else
                    myRef.child( classTimings.get(i).getVenue()).child( classTimings.get(i).getDay()).child("fbxSeats").setValue(seats+"");

            }
            if(classTimings.get(i).isFbx()){
                type=type+",WTB";
                int seats=Integer.parseInt( classTimings.get(i).getWbxSeats());
                seats--;
                if(seats<0){

                }else{
                    myRef.child( classTimings.get(i).getVenue()).child( classTimings.get(i).getDay()).child("wbxSeats").setValue(seats+"");
                }

            }
            try{
                type=type.substring(1,type.length());
            }catch (Exception c){
                c.printStackTrace();
            }

            classTimings.get(i).setType(type);
            classTimings.get(i).setTransectionId(transectionId);
            classTimings.get(i).setPaymentId(paymentId);
            classTimings.get(i).setPhoneNumber(phone);
            if(classTimings.get(i).isSelected()){
                myRefBooked.child( classTimings.get(i).getVenue()).child(personName+i).setValue( classTimings.get(i));
//                int seats=Integer.parseInt( classTimings.get(i).getTotalSeats());
//                seats--;
//                myRef.child( classTimings.get(i).getVenue()).child( classTimings.get(i).getDay()).child("seats").setValue(seats+"");

            }

        }
        myRefCart.child(android_id).removeValue();
        progressDialog.dismiss();
        aleartDialog();

    }
    public void aleartDialog(){
        String[] separated = personName.split(":_:");
        HistoryModel historyModel=new HistoryModel();
        historyModel.setName(separated[0]);
        historyModel.setPaymentId(paymentId);
        historyModel.setTransectoionid(transectionId);
        myRefHistory.push().setValue(historyModel);
        String text="Transaction id is "+transectionId+"\nPayment id is : "+paymentId+"\n\nPlease take screen shot and save it for further use if required";
        AlertDialog alertDialog = new AlertDialog.Builder(VeneuActivity.this).create();
        alertDialog.setTitle("Payment Alert");
        alertDialog.setMessage(text);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
//                Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
            }
        });


        alertDialog.show();
    }



     class TypeModel implements Serializable{
        String delay;
        boolean fbx;
        boolean wbx;

        public TypeModel() {
        }

        public String getDelay() {
            return delay;
        }

        public void setDelay(String delay) {
            this.delay = delay;
        }

        public boolean isFbx() {
            return fbx;
        }

        public void setFbx(boolean fbx) {
            this.fbx = fbx;
        }

        public boolean isWbx() {
            return wbx;
        }

        public void setWbx(boolean wbx) {
            this.wbx = wbx;
        }
    }

    public double priceAdder(CheckBox fbx,CheckBox wbx){
        double wbxpriceint=0;
        double fbxpirceint=0;
        try{
            wbxpriceint=Double.parseDouble(wbxPrice.substring(1,wbxPrice.length()));
            fbxpirceint=Double.parseDouble(fbxPrice.substring(1,fbxPrice.length()));
        }catch (Exception c){
            c.printStackTrace();
        }
        if(fbx.isChecked() && wbx.isChecked()){
            totalPrice=totalPrice+wbxpriceint+fbxpirceint;
        }else if(fbx.isChecked()){
            totalPrice=totalPrice+fbxpirceint;
        }else if(wbx.isChecked()){
            totalPrice=totalPrice+wbxpriceint;
        }
        totalPrice=Double.parseDouble(String.format("%.2f", totalPrice));
        return totalPrice;


    }
    public double pricesubstractor(CheckBox fbx,CheckBox wbx){
        double wbxpriceint=0;
        double fbxpirceint=0;
        try{
            wbxpriceint=Double.parseDouble(wbxPrice.substring(1,wbxPrice.length()));
            fbxpirceint=Double.parseDouble(fbxPrice.substring(1,fbxPrice.length()));
        }catch (Exception c){
            c.printStackTrace();
        }
         if(!fbx.isChecked()){
            totalPrice=totalPrice-fbxpirceint;
        }else if(!wbx.isChecked()){
            totalPrice=totalPrice-wbxpriceint;
        }else{
//            totalPrice;
        }
        if(totalPrice<0)
            totalPrice=0;

        totalPrice=Double.parseDouble(String.format("%.2f", totalPrice));

        return totalPrice;
    }


    public double priceChanger(){
        Log.e(TAG,"price updater");
        totalPrice=0;
        for(int i =0;i<classTimings.size();i++){
            Log.e(TAG,"price updater loop started");
            if(classTimings.get(i).isFbx()){
                Log.e(TAG,"price updater fbx is true");
                double price=0;
                try{
                    price=Double.parseDouble(fbxPrice.substring(1,fbxPrice.length()));
                    totalPrice=totalPrice+price;
                    Log.e(TAG,"price updater updated");
                }catch (Exception c){
                    c.printStackTrace();
                    Log.e(TAG,"price updater fbx exception");
                }
            }
            if(classTimings.get(i).isWbx()){
                Log.e(TAG,"price updater wbx is true");
                double wbxpriceint=0;
                try{
                    wbxpriceint=Double.parseDouble(wbxPrice.substring(1,wbxPrice.length()));
                    totalPrice=totalPrice+wbxpriceint;
                    Log.e(TAG,"price updater wbx is updated ");
                }catch (Exception c){
                    c.printStackTrace();
                    Log.e(TAG,"price updater wbx in exce3ption ");
                }
            }
        }
        totalPrice=Double.parseDouble(String.format("%.2f", totalPrice));
        return  totalPrice;
    }
//    public double priceChanger(int i){
//        if(classTimings.get(i).isFbx()){
//            Log.e(TAG,"price updater fbx is true");
//            double price=0;
//            try{
//                price=Double.parseDouble(fbxPrice.substring(1,fbxPrice.length()));
//                totalPrice=totalPrice+price;
//                Log.e(TAG,"price updater updated");
//            }catch (Exception c){
//                c.printStackTrace();
//                Log.e(TAG,"price updater fbx exception");
//            }
//        }else{
//            double price=0;
//            try{
//                price=Double.parseDouble(fbxPrice.substring(1,fbxPrice.length()));
//                totalPrice=totalPrice-price;
//                Log.e(TAG,"price updater updated");
//            }catch (Exception c){
//                c.printStackTrace();
//                Log.e(TAG,"price updater fbx exception");
//            }
//        }
//        if(classTimings.get(i).isWbx()){
//            Log.e(TAG,"price updater wbx is true");
//            double wbxpriceint=0;
//            try{
//                wbxpriceint=Double.parseDouble(wbxPrice.substring(1,wbxPrice.length()));
//                totalPrice=totalPrice+wbxpriceint;
//                Log.e(TAG,"price updater wbx is updated ");
//            }catch (Exception c){
//                c.printStackTrace();
//                Log.e(TAG,"price updater wbx in exce3ption ");
//            }
//        }else{
//            double wbxpriceint=0;
//            try{
//                wbxpriceint=Double.parseDouble(wbxPrice.substring(1,wbxPrice.length()));
//                totalPrice=totalPrice-wbxpriceint;
//                Log.e(TAG,"price updater wbx is updated ");
//            }catch (Exception c){
//                c.printStackTrace();
//                Log.e(TAG,"price updater wbx in exce3ption ");
//            }
//        }
//        totalPrice=Double.parseDouble(String.format("%.2f", totalPrice));
//        return  totalPrice;
//    }

}

