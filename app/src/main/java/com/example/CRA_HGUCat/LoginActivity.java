package com.example.CRA_HGUCat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 상황봐서 Maven도 추가

    }

    public void Goresister(View v)
    {
        Intent Regist = new Intent(this, ResisterActivity.class);
        startActivity(Regist);
    }

    EditText Id;
    EditText Password;

    public void GoLogin(View v)
    {
        Id = findViewById(R.id.et_id);
        Password = findViewById(R.id.et_pass);
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithEmailAndPassword(Id.getText().toString(), Password.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                if(firebaseAuth.getCurrentUser().isEmailVerified())
                                {
                                    Toast.makeText(LoginActivity.this, "안녕하세요! " + "" + "님!", Toast.LENGTH_LONG).show();
                                    finish();
                                }
                            }
                            else{
                                if(firebaseAuth.getCurrentUser().isEmailVerified())
                                {
                                    Toast.makeText(LoginActivity.this, "비밀번호를 잘못 입력하였습니다.\n비밀번호를 다시 입력해주세요.", Toast.LENGTH_LONG).show();
                                    Password.setText("");
                                }
                                else
                                {
                                    Toast.makeText(LoginActivity.this, "등록되지 않은 학번입니다.\n먼저 회원가입을 해주세요.", Toast.LENGTH_LONG).show();
                                    Id.setText("");
                                    Password.setText("");
                                }
                            }
                        }
                    });
    }
}