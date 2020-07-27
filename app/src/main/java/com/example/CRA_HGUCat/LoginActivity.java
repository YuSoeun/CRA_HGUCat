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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.Map;

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
        final String EmailID = Id.getText().toString() + "@handong.edu";
        firebaseAuth.signInWithEmailAndPassword(EmailID, Password.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                DocumentReference docRef = FirebaseFirestore.getInstance().collection("UserProfile").document("Nickname");
                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(LoginActivity.this, "접속 중...", Toast.LENGTH_SHORT).show();
                                            Map<String, Object> mail2name = task.getResult().getData();
                                            for (Map.Entry<String, Object> i : mail2name.entrySet()) {
                                                if (i.getKey().equals(EmailID)) {
                                                    Toast.makeText(LoginActivity.this, "안녕하세요! " + i.getValue() + "님!", Toast.LENGTH_LONG).show();
                                                    finish();
                                                }
                                            }
                                        }
                                    }
                                });
                            }
                            else
                            {
                                DocumentReference docRef = FirebaseFirestore.getInstance().collection("UserProfile").document("Nickname");
                                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful())
                                        {
                                            Map<String, Object> mail2name = task.getResult().getData();
                                            if(mail2name.containsKey(EmailID))
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
                                        else
                                        {
                                            Toast.makeText(LoginActivity.this, "오류가 발생했습니다.\n다시 시도해주세요.", Toast.LENGTH_LONG).show();
                                            Id.setText("");
                                            Password.setText("");
                                        }
                                    }
                                });
                            }
                        }
                    });
    }
}