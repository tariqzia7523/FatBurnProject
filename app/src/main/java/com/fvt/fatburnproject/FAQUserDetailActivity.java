package com.fvt.fatburnproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class FAQUserDetailActivity extends AppCompatActivity {

    FAQModel faqModel;
    TextView question,answer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_f_a_q_user);
        getSupportActionBar().hide();
        question=findViewById(R.id.question);
        answer=findViewById(R.id.anwer);
        try{
            faqModel=(FAQModel) getIntent().getExtras().get("data");
            question.setText(faqModel.getQuestion());
            answer.setText(faqModel.getAnswer());
            findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }catch (Exception c){
            c.printStackTrace();
        }
    }
}
