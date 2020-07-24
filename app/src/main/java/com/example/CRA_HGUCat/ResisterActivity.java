package com.example.CRA_HGUCat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ResisterActivity extends AppCompatActivity {

    TextInputEditText[] profile = new TextInputEditText[7];
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
        String HandongMail = profile[6].getText().toString().replace(" ","").replace("\n","");
        if(!HandongMail.contains("@") && !HandongMail.contains("."))
        {
            Toast.makeText(this,"이메일의 형태로 입력해주세요!",Toast.LENGTH_SHORT).show();
            profile[6].setText("");
        }
        else if(HandongMail.length() != "00000000@handong.edu".length())
        {
            Toast.makeText(this,"오탈자가 있는지 확인해주세요!",Toast.LENGTH_SHORT).show();
        }
        else if(!HandongMail.substring(HandongMail.length() - "@handong.edu".length()).equals("@handong.edu"))
        {
            Toast.makeText(this,"한동대학교 이메일이 맞는지 확인해주세요!",Toast.LENGTH_SHORT).show();
        }
        else
            {
                /*ActionCodeSettings actionCodeSettings =
                        ActionCodeSettings.newBuilder()
                        .setUrl(FirebaseD)
                        .setHandleCodeInApp(true)
                        .setAndroidPackageName(
                                "com.example.android",
                                true,
                                "28"
                        )
                        .build();


                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.sendSignInLinkToEmail(HandongMail,actionCodeSettings)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(ResisterActivity.this,"인증번호를 전송헀습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });*/

                /*Intent intent = getIntent();
                String emailLink = intent.getData().toString();
                if(auth.isSignInWithEmailLink(emailLink))
                {
                    auth.signInWithEmailLink(HandongMail, emailLink)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful())
                                    {
                                        AuthResult result = task    .getResult();
                                    }
                                }
                            });
                }*/

                final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                firebaseAuth.createUserWithEmailAndPassword(HandongMail,"q1!w2@e3#r4$t5%")
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful())
                                {
                                    firebaseAuth.getCurrentUser().sendEmailVerification()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(ResisterActivity.this,"문자를 이메일로 전송했습니다.", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                                else
                                {
                                    if(firebaseAuth.getCurrentUser().isEmailVerified())
                                        Toast.makeText(ResisterActivity.this,"이미 가입된 학번입니다.", Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(ResisterActivity.this,"가입에 실패했습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
    }
}