package com.fvt.fatburnproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminFAQActivityDetail extends AppCompatActivity {

    EditText question,answer;
    ProgressDialog progressDialog;
    FAQModel faqModel;
    DatabaseReference myRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f_a_q);
        getSupportActionBar().hide();
        question=findViewById(R.id.question);
        answer=findViewById(R.id.anwer);
        progressDialog=new ProgressDialog(AdminFAQActivityDetail.this);
        progressDialog.setMessage("Please wait");
        myRef = FirebaseDatabase.getInstance().getReference("Classes").child("FAQ");
        findViewById(R.id.add_faq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(question.getText().toString().isEmpty() || answer.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please add all data",Toast.LENGTH_LONG).show();
                }else{
                    FAQModel faq=new FAQModel();
                    faq.setAnswer(answer.getText().toString());
                    faq.setQuestion(question.getText().toString());
                    progressDialog.show();

                    if(faqModel!=null){
                        myRef.child(faqModel.getId()).setValue(faq).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();
                                question.setText("");
                                answer.setText("");
                            }
                        });
                    }else{
                        myRef.push().setValue(faq).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();
                                question.setText("");
                                answer.setText("");
                            }
                        });
                    }

                }
            }
        });
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        try{
            faqModel=(FAQModel) getIntent().getExtras().get("data");
            question.setText(faqModel.getQuestion());
            answer.setText(faqModel.getAnswer());
        }catch (Exception c){
            c.printStackTrace();
        }
    }
}
