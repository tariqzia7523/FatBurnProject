package com.fvt.fatburnproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

public class VeneClassDetailActivity extends AppCompatActivity {
    VeneuModel veneuModel;
    ImageView fbxImage,wbxImage;
    boolean fbxSelected,wbxSelected;
    double totalPrice;
    TextView totalText;
    ProgressDialog progressDialog;
    private Stripe stripe;
    CardMultilineWidget cardInputWidget;
    private String secretKey ;
    private String publishableKey;
    String personName;
    String TAG;
    DatabaseReference myRefBooked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vene_class_detail);
        getSupportActionBar().hide();
        progressDialog =new ProgressDialog(VeneClassDetailActivity.this);
        progressDialog.setMessage("Please Wait");
        myRefBooked = FirebaseDatabase.getInstance().getReference("Classes").child("Booked").child("VeneuClasses");

        TAG="***Detail";
        //        secretKey=StripsIDsClass.getSecretEKey();
        secretKey="sk_test_t2qvlU7H89Zpv06cvs9CSGxf00QK7CthR0";
//        publishableKey=StripsIDsClass.getPubliserKey();
        publishableKey="pk_test_hUE52VSBGt7oUf7AORrcPRpD0024H5exOY";
//        PaymentConfiguration.init(getApplicationContext(), StripsIDsClass.getPubliserKey());
        PaymentConfiguration.init(getApplicationContext(), "pk_test_hUE52VSBGt7oUf7AORrcPRpD0024H5exOY");
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        totalText=findViewById(R.id.total_text);
        totalPrice=0;
        fbxSelected=false;
        wbxSelected=false;
        fbxImage=findViewById(R.id.fbx_image);
        wbxImage=findViewById(R.id.wtb_image);
        fbxImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fbxSelected){
                    fbxSelected=false;
                    fbxImage.setImageResource(R.drawable.uncheck);
                    getSum();
                }else{
                    fbxSelected=true;
                    fbxImage.setImageResource(R.drawable.check);
                    getSum();
                }
            }
        });
        wbxImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(wbxSelected){
                    wbxSelected=false;
                    wbxImage.setImageResource(R.drawable.uncheck);
                    getSum();
                }else{
                    wbxSelected=true;
                    wbxImage.setImageResource(R.drawable.check);
                    getSum();
                }
            }
        });
        getSum();
        try{
            veneuModel=(VeneuModel)getIntent().getExtras().get("data");
            findViewById(R.id.pay_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(totalPrice>0)
                        paymentiflatecall(totalPrice);
                    else
                        Toast.makeText(getApplicationContext(),"Please select any type",Toast.LENGTH_LONG).show();
                }
            });

        }catch (Exception c){
            c.printStackTrace();
        }
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }


    public void getSum(){
        if(fbxSelected && wbxSelected){
            totalText.setText("The total price is $5.50");
            totalPrice=5.50;
        }
        if(fbxSelected && !wbxSelected){
            totalText.setText("The total price is $3.50");
            totalPrice=3.50;
        }
        if(!fbxSelected && wbxSelected){
            totalText.setText("The total price is $2");
            totalPrice=2;
        }
        if(!fbxSelected && !wbxSelected){
            totalText.setText("The total price is 0");
            totalPrice=0;
        }
    }


    public int  paymentiflatecall(final double price) {
        Log.e(TAG,"payment called");
//        Context c = getApplicationContext();
//        LinearLayout layout = new LinearLayout(c);
//        layout.setBackgroundColor(getResources().getColor(R.color.transbackground));
//        layout.setOrientation(LinearLayout.VERTICAL);
//        layout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        AlertDialog.Builder al = new AlertDialog.Builder(VeneClassDetailActivity.this);
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
//                    value.dismiss();
                    personName=perName.getText().toString();
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
                stripe.confirmPayment(VeneClassDetailActivity.this, confirmParams);
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
        stripe.onPaymentResult(requestCode, data, new PaymentResultCallback(VeneClassDetailActivity.this));
    }

    private final class PaymentResultCallback
            implements ApiResultCallback<PaymentIntentResult> {
        @NonNull
        private final WeakReference<VeneClassDetailActivity> activityRef;

        PaymentResultCallback(@NonNull VeneClassDetailActivity activity) {
            activityRef = new WeakReference<>(activity);
        }

        @Override
        public void onSuccess(@NonNull PaymentIntentResult result) {
            final VeneClassDetailActivity activity = activityRef.get();
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
            final VeneClassDetailActivity activity = activityRef.get();
            if (activity == null) {
                return;
            }
            Log.e("stripePayment", "Error: " +e.getLocalizedMessage());
            e.printStackTrace();
        }

    }

    public void updateBookedTable(){
        myRefBooked.child(personName).child(veneuModel.getClassid()).setValue(veneuModel);
        progressDialog.dismiss();
        aleartDialog();

    }
    public void aleartDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(VeneClassDetailActivity.this).create();
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
