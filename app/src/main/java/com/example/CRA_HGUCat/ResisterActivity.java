package com.example.CRA_HGUCat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class ResisterActivity extends AppCompatActivity {

    EditText[] profile = new EditText[7];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resister);
        profile[0] = findViewById(R.id.loginId);
        profile[1] = findViewById(R.id.loginPassword);
        profile[2] = findViewById(R.id.name);
        profile[3] = findViewById(R.id.age);
        profile[4] = findViewById(R.id.studentId);
        profile[5] = findViewById(R.id.nickname);
        profile[6] = findViewById(R.id.HandongEmail);
    }

    public void callEmail(View v)
    {
        String HandongMail = profile[6].getText().toString();
        if(!HandongMail.contains("@") && !HandongMail.contains("."))
        {
            Toast.makeText(this,"이메일의 형태로 입력해주세요!",Toast.LENGTH_SHORT).show();
            profile[6].setText("");
        }
        else if(HandongMail.length() != "00000000@handong.edu".length())
        {
            Toast.makeText(this,"오탈자가 있는지 확인해주세요!",Toast.LENGTH_SHORT).show();
        }
        else if(HandongMail.substring(HandongMail.length() - "@handong.edu".length(),HandongMail.length()) != "@handong.edu")
        {
            Toast.makeText(this,"한동대학교 이메일이 맞는지 확인해주세요!",Toast.LENGTH_SHORT).show();
        }

    }
}