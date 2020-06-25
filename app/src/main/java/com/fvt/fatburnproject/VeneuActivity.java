package com.fvt.fatburnproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class VeneuActivity extends AppCompatActivity {
    DatabaseReference myRef,myRefPrice,myRefBooked;
    ArrayList<String> venuesNames;
    ArrayList<VeneuModel> classTimings;
    Spinner spinner;
    MyAdapter myAdapter;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;
    String TAG;
    TypeModel typeModel;
    CheckBox fbx,wbx;
    String fbxPrice,wbxPrice;
    TextView totalText;
    double totalPrice;
    VeneuModel veneuModel;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_veneu);

        getSupportActionBar().hide();
        android_id = Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        progressDialog=new ProgressDialog(VeneuActivity.this);
        progressDialog.setMessage("Please Wait...");
        TAG="***veneu";
        typeModel=null;
        secretKey=StripsIDsClass.getSecretEKey();
//        secretKey="sk_test_t2qvlU7H89Zpv06cvs9CSGxf00QK7CthR0";
        publishableKey=StripsIDsClass.getPubliserKey();
//        publishableKey="pk_test_hUE52VSBGt7oUf7AORrcPRpD0024H5exOY";
//        PaymentConfiguration.init(getApplicationContext(), StripsIDsClass.getPubliserKey());
        PaymentConfiguration.init(getApplicationContext(), publishableKey);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        fbx=findViewById(R.id.fbx);
        wbx=findViewById(R.id.wbx);
        totalText=findViewById(R.id.total_text);
        Log.e(TAG,"vence activity called");
        spinner=findViewById(R.id.spinner);
        venuesNames=new ArrayList<>();
        classTimings=new ArrayList<>();
        recyclerView=findViewById(R.id.class_list);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(VeneuActivity.this));
        myAdapter=new MyAdapter(getApplicationContext(),VeneuActivity.this,classTimings);
        recyclerView.setAdapter(myAdapter);
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
                    fbx.setChecked(false);
                    wbx.setChecked(false);
                    fbx.setVisibility(View.GONE);
                    wbx.setVisibility(View.GONE);
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
                            veneuModel.setTime(dataSnapshot2.child("time").getValue(String.class));
                            veneuModel.setSelected(false);
                            if(!dataSnapshot2.getKey().equalsIgnoreCase("types")) {
                                Log.e(TAG,"If is true ");
                                classTimings.add(veneuModel);
                            }else{
                                typeModel=new TypeModel();
                                typeModel.setDelay(dataSnapshot2.child("delay").getValue(String.class));
//                                    typeModel=dataSnapshot2.getValue(TypeModel.class);
                                try{
                                    typeModel.setFbx(dataSnapshot2.child("fbx").getValue(Boolean.class));
                                    if(typeModel.isFbx()){
                                        fbx.setVisibility(View.VISIBLE);
                                    }
                                }catch (Exception c){
                                    c.printStackTrace();
                                }
                                try{
                                    typeModel.setWbx(dataSnapshot2.child("wbx").getValue(Boolean.class));
                                    if(typeModel.isWbx()){
                                        wbx.setVisibility(View.VISIBLE);
                                    }
                                }catch (Exception c){
                                    c.printStackTrace();
                                }

                            }
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
                fbx.setChecked(false);
                wbx.setChecked(false);
                fbx.setVisibility(View.GONE);
                wbx.setVisibility(View.GONE);
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
                                veneuModel.setTime(dataSnapshot2.child("time").getValue(String.class));
                                veneuModel.setSelected(false);
                                if(!dataSnapshot2.getKey().equalsIgnoreCase("types")) {
                                    Log.e(TAG,"If is true ");
                                    classTimings.add(veneuModel);
                                }else{
                                    typeModel=new TypeModel();
                                    typeModel.setDelay(dataSnapshot2.child("delay").getValue(String.class));
//                                    typeModel=dataSnapshot2.getValue(TypeModel.class);
                                    try{
                                        typeModel.setFbx(dataSnapshot2.child("fbx").getValue(Boolean.class));
                                        if(typeModel.isFbx()){
                                            fbx.setVisibility(View.VISIBLE);
                                        }
                                    }catch (Exception c){
                                        c.printStackTrace();
                                    }
                                    try{
                                        typeModel.setWbx(dataSnapshot2.child("wbx").getValue(Boolean.class));
                                        if(typeModel.isWbx()){
                                            wbx.setVisibility(View.VISIBLE);
                                        }
                                    }catch (Exception c){
                                        c.printStackTrace();
                                    }

                                }
//                                classTimings.add(veneuModel);
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
        fbx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setLayoutManager(new LinearLayoutManager(VeneuActivity.this));
                myAdapter=new MyAdapter(getApplicationContext(),VeneuActivity.this,classTimings);
                recyclerView.setAdapter(myAdapter);
            }
        });
        wbx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                recyclerView.setNestedScrollingEnabled(false);
                recyclerView.setLayoutManager(new LinearLayoutManager(VeneuActivity.this));
                myAdapter=new MyAdapter(getApplicationContext(),VeneuActivity.this,classTimings);
                recyclerView.setAdapter(myAdapter);
            }
        });
        findViewById(R.id.pay_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    int seats=Integer.parseInt(veneuModel.getTotalSeats());
                    if(seats<=0){
                        Toast.makeText(getApplicationContext(),"No seats available ",Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(totalPrice>0)
                        paymentiflatecall(totalPrice);
                    else
                        Toast.makeText(getApplicationContext(),"Please select Class type",Toast.LENGTH_LONG).show();
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
                    int seats=Integer.parseInt(veneuModel.getTotalSeats());
                    if(seats<=0){
                        Toast.makeText(getApplicationContext(),"No seats available ",Toast.LENGTH_LONG).show();
                        return;
                    }
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
                    veneuModel.setPersonName(perName.getText().toString());
                    veneuModel.setPhoneNumber(phoneNumber.getText().toString());
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
            ImageView image;
            TextView day,fbxTime,wbxtime;

            public MyViewHolder(View view) {
                super(view);
//                sideImage=view.findViewById(R.id.side_image);
                image=view.findViewById(R.id.select_image);
                day=view.findViewById(R.id.day);
                fbxTime=view.findViewById(R.id.fbx_time);
                wbxtime=view.findViewById(R.id.wbx_time);
                wbxtime.setVisibility(View.GONE);
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
            boolean tempStatus=false;
            if(data.get(position).isSelected()){
                holder.image.setImageResource(R.drawable.check);
            }else{
                holder.image.setImageResource(R.drawable.uncheck);
            }
            holder.day.setText(data.get(position).getDay());

//            holder.day.setText(data.get(position).getDay()+" Time "+data.get(position).getTime()+ " price "+fbxPrice);
            if(fbx.isChecked()){
                holder.fbxTime.setVisibility(View.VISIBLE);
                holder.fbxTime.setText("FBX ("+fbxPrice+")"+ " -> "+ data.get(position).getTime());
                tempStatus=true;
            }
            if(wbx.isChecked()){
                String endTime = null;
                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("hh:mm aa", Locale.US);
                    Date now = formatter.parse(data.get(position).getTime());
                    int dely=Integer.parseInt(typeModel.getDelay());
//
                    Calendar calendar=Calendar.getInstance(Locale.US);
                    calendar.setTime(now);
                    DateFormat timeFormat = new SimpleDateFormat("hh:mm aa");
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(now.getTime());


                    cal.add(Calendar.MINUTE, dely);

                    endTime = timeFormat.format(cal.getTime());

                } catch (Exception e) {
                    e.printStackTrace();
                }

                holder.wbxtime.setVisibility(View.VISIBLE);
                holder.wbxtime.setText(endTime+" Wbx " +" price "+wbxPrice);
                holder.wbxtime.setText("WTB ("+wbxPrice+")"+ " -> "+ endTime);
                if(tempStatus){

                }else{
                    holder.fbxTime.setVisibility(View.GONE);
//                    holder.otherTime.setVisibility(View.VISIBLE);
////                    holder.otherTime.setText(data.get(position).getDay()+" Time "+data.get(position).getTime()+" Delay with "+typeModel.getDelay()+" min Wbx");
//                    holder.otherTime.setText(data.get(position).getDay()+" Time "+endTime+" Wbx"+" price "+wbxPrice);
                }
            }

            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(fbx.isChecked() || wbx.isChecked()){
                        if(data.get(position).isSelected()){
                            data.get(position).setSelected(false);
                            totalText.setText("Pay ");
                            veneuModel=null;
                        }else{
                            for(int i=0;i<data.size();i++){
                                data.get(i).setSelected(false);
                            }
                            double wbxpriceint=0;
                            double fbxpirceint=0;
                            try{
                                wbxpriceint=Double.parseDouble(wbxPrice.substring(0,wbxPrice.length()-1));
                                fbxpirceint=Double.parseDouble(fbxPrice.substring(0,wbxPrice.length()-1));
                            }catch (Exception c){
                                c.printStackTrace();
                            }
                            Log.e(TAG,"fbx price "+fbxPrice);
                            Log.e(TAG,"wbx price "+wbxPrice);
                            Log.e(TAG,"fbx price int "+fbxpirceint);
                            Log.e(TAG,"wbx price int  "+wbxpriceint);
                            if(fbx.isChecked() && wbx.isChecked()){
                                totalPrice=wbxpriceint+fbxpirceint;
                                totalText.setText("Pay : "+totalPrice+"£");
                            }else if(fbx.isChecked()){
                                totalPrice=fbxpirceint;
                                totalText.setText("Pay : "+totalPrice+"£");
                            }else if(wbx.isChecked()){
                                totalPrice=wbxpriceint;
                                totalText.setText("Pay : "+totalPrice+"£");
                            }else{
                                totalPrice=0;
                                totalText.setText("Pay ");
                            }

                            data.get(position).setSelected(true);
                            veneuModel=data.get(position);
                        }

                        notifyDataSetChanged();
                    }else{
                        Toast.makeText(getApplicationContext(),"please select Class type",Toast.LENGTH_LONG).show();
                    }



                }
            });



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
                    veneuModel.setPersonName(perName.getText().toString());
                    veneuModel.setPhoneNumber(phoneNumber.getText().toString());
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
                        veneuModel.setPaymentId(paymentConfirmation.getProofOfPayment().getPaymentId());
                        veneuModel.setTransectionId(paymentConfirmation.getProofOfPayment().getTransactionId());
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
                        veneuModel.setTransectionId(jsonObject.getString("id"));
                        veneuModel.setPaymentId(jsonObject.getString("created"));
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

    public void updateBookedTable(){
        String type="";
        if(fbx.isChecked()){
            type=",FBX";
        }
        if(fbx.isChecked()){
            type=",WTB";
        }
        try{
            type=type.substring(1,type.length());
        }catch (Exception c){
            c.printStackTrace();
        }

        veneuModel.setType(type);

        myRefBooked.child(veneuModel.getVenue()).child(personName).setValue(veneuModel);
        int seats=Integer.parseInt(veneuModel.getTotalSeats());
        seats--;
        myRef.child(veneuModel.getVenue()).child(veneuModel.getDay()).child("seats").setValue(seats+"");
        progressDialog.dismiss();
        aleartDialog();

    }
    public void aleartDialog(){
        String text="Transaction id is "+veneuModel.getTransectionId()+"\nPayment id is : "+veneuModel.getPaymentId()+"\n\nPlease take screen shot and save it for further use if required";
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

}

