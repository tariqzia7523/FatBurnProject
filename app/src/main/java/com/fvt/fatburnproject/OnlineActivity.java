package com.fvt.fatburnproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.stripe.android.ApiResultCallback;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.PaymentIntentResult;
import com.stripe.android.Stripe;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentIntent;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.view.CardMultilineWidget;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OnlineActivity extends AppCompatActivity {
    DatabaseReference myRef,myRefBooked;
    ArrayList<OnlineClassModel> list;
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    String TAG;
    ProgressDialog progressDialog;
    private Stripe stripe;
    CardMultilineWidget cardInputWidget;
    private String secretKey ;
    private String publishableKey;
    String personName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);
//        secretKey=StripsIDsClass.getSecretEKey();
        secretKey="sk_test_t2qvlU7H89Zpv06cvs9CSGxf00QK7CthR0";
//        publishableKey=StripsIDsClass.getPubliserKey();
        publishableKey="pk_test_hUE52VSBGt7oUf7AORrcPRpD0024H5exOY";
//        PaymentConfiguration.init(getApplicationContext(), StripsIDsClass.getPubliserKey());
        PaymentConfiguration.init(getApplicationContext(), "pk_test_hUE52VSBGt7oUf7AORrcPRpD0024H5exOY");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
//        PaymentConfiguration.init(OnlineActivity.this, StripsIDsClass.getPubliserKey());
        getSupportActionBar().hide();
        progressDialog=new ProgressDialog(OnlineActivity.this);
        progressDialog.setMessage("Please Wait");
        TAG="***Online";
        list=new ArrayList<>();
        recyclerView=findViewById(R.id.class_list);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(OnlineActivity.this));
        myAdapter=new MyAdapter(getApplicationContext(),OnlineActivity.this,list);
        recyclerView.setAdapter(myAdapter);
        myRefBooked = FirebaseDatabase.getInstance().getReference("Classes").child("Booked").child("OnlineClasses");
        myRef = FirebaseDatabase.getInstance().getReference("Classes").child("OnlineClasses");
        progressDialog.show();
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try{
                    list.clear();
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        OnlineClassModel onlineClassModel=dataSnapshot1.getValue(OnlineClassModel.class);
                        onlineClassModel.setId(dataSnapshot1.getKey());
                        list.add(onlineClassModel);
                    }
                    progressDialog.dismiss();
                    myAdapter.notifyDataSetChanged();
                }catch (Exception vc){
                    vc.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        findViewById(R.id.pay_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sum=0;
                Log.e(TAG,"size is "+list.size());
                for(int i=0;i<list.size();i++){
                    Log.e(TAG,"val  for"+ i+" is "+list.get(i).isChecked());
                    if(list.get(i).isChecked()){
                        String p=list.get(i).getPrice();
                        p=p.substring(0,p.length()-1);
                        int price=Integer.parseInt(p);
                        sum=sum+price;
                    }else{

                    }
                }
                if(sum<=0){
                    Toast.makeText(getApplicationContext(),"Please select any option",Toast.LENGTH_LONG).show();
                }else
                    paymentiflatecall(sum);
//                paymentiflatecall(100);

            }
        });

    }


    public int  paymentiflatecall(final double price) {
        Log.e(TAG,"payment called");
//        Context c = getApplicationContext();
//        LinearLayout layout = new LinearLayout(c);
//        layout.setBackgroundColor(getResources().getColor(R.color.transbackground));
//        layout.setOrientation(LinearLayout.VERTICAL);
//        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        AlertDialog.Builder al = new AlertDialog.Builder(OnlineActivity.this);
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
        Button payButton = view.findViewById(R.id.payButton);
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cardInputWidget.getCard().validateCard() && !perName.getText().toString().isEmpty()) {
                    personName=perName.getText().toString();
                   progressDialog.show();
                    paymentCall(price);
                    value.dismiss();
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
//            paymentIntentParams.put("amount", ((int)price * 100)); //1 is the amount to be deducted, 100 is must since it accepts cents & 1$=100cents
            paymentIntentParams.put("amount", ((int)Math.round(price) * 100)); //1 is the amount to be deducted, 100 is must since it accepts cents & 1$=100cents
            Log.e(TAG,"rounded value "+(int)Math.round(price));
            paymentIntentParams.put("currency", "usd");
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
                stripe.confirmPayment(OnlineActivity.this, confirmParams);
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

        // Handle the result of stripe.confirmPayment
        stripe.onPaymentResult(requestCode, data, new PaymentResultCallback(OnlineActivity.this));
    }

    private final class PaymentResultCallback
            implements ApiResultCallback<PaymentIntentResult> {
        @NonNull private final WeakReference<OnlineActivity> activityRef;

        PaymentResultCallback(@NonNull OnlineActivity activity) {
            activityRef = new WeakReference<>(activity);
        }

        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {
            final OnlineActivity activity = activityRef.get();
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
                        Toast.makeText(getApplicationContext(),"amount paid",Toast.LENGTH_LONG).show();
                        addDataToBookedNode();
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
            final OnlineActivity activity = activityRef.get();
            if (activity == null) {
                return;
            }
            Log.e("stripePayment", "Error: " +e.getLocalizedMessage());
            e.printStackTrace();
        }

    }

    private void addDataToBookedNode() {
//        myRefBooked.child(personName).
        for(int i=0;i<list.size();i++){
            myRefBooked.child(personName).child(list.get(i).getId()).setValue("Booked");
        }
        progressDialog.dismiss();
        aleartDialog();
    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        ArrayList<OnlineClassModel> data;
        Context context;
        Activity activity;
        String TAG;
        public class MyViewHolder extends RecyclerView.ViewHolder  {
            ImageView image;
            TextView name,day;

            public MyViewHolder(View view) {
                super(view);
//                sideImage=view.findViewById(R.id.side_image);
                image=view.findViewById(R.id.select_image);
                name=view.findViewById(R.id.name);
                day=view.findViewById(R.id.day);


            }
        }
        public MyAdapter(Context c, Activity a , ArrayList<OnlineClassModel> moviesList){
            this.data =moviesList;
            context=c;
            activity=a;
            TAG="***Adapter";
        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.class_item_layout, parent, false);
            return new MyViewHolder(itemView);
        }
        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
//        if (flag==1){// beign called from my profile so we have to set visible following image
//            holder.menuImage.setVisibility(View.VISIBLE);
//        }

//        final OnlineClassModel model=data.get(position);
            holder.name.setText(list.get(position).getTitle()+" price "+list.get(position).getPrice());
            holder.day.setText(list.get(position).getDay()+" time "+list.get(position).getTime());
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        if(list.get(position).isChecked()){
                            list.get(position).setChecked(false);
                            holder.image.setImageResource(R.drawable.uncheck);
                        }else{
                            list.get(position).setChecked(true);
                            holder.image.setImageResource(R.drawable.check);
                        }
                        notifyDataSetChanged();
                    }catch (Exception c){
                        c.printStackTrace();
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

    public void aleartDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(OnlineActivity.this).create();
        alertDialog.setTitle("Payment Alert");
        alertDialog.setMessage("Transection is completed ");

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog.show();
    }
}
