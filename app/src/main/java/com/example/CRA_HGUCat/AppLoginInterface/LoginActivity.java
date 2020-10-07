package com.example.CRA_HGUCat.AppLoginInterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.CRA_HGUCat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void goResisterActivity(View v) {
        Intent goResister = new Intent(this, RegisterActivity.class);
        startActivity(goResister);
    }

    public void goLoginActivity(View v) {
        final EditText Id = findViewById(R.id.SignInID);
        final EditText Password = findViewById(R.id.SignInPassword);
        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        Toast.makeText(LoginActivity.this, "접속 중...", Toast.LENGTH_SHORT).show();
        final String EmailID = Id.getText().toString() + "@handong.edu";
        firebaseAuth.signInWithEmailAndPassword(EmailID, Password.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        DocumentReference docRef = FirebaseFirestore.getInstance().collection("UserProfile").document("Nickname");
                        if(firebaseAuth.getCurrentUser().isEmailVerified()) {
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    Map<String, Object> mail2name = task.getResult().getData();
                                    for (Map.Entry<String, Object> i : mail2name.entrySet()) {
                                        if (i.getKey().equals(EmailID)) {
                                            Toast.makeText(LoginActivity.this, "안녕하세요! " + i.getValue() + "님!", Toast.LENGTH_LONG).show();
                                            finish();
                                            // 회원가입 때에 (이메일-별명)의 딕셔너리 방식에서 이메일에 해당하는 별명을 가져와 출력합니다
                                        }
                                    }
                                }
                                }
                            });
                        }
                        else {
                            Toast.makeText(LoginActivity.this,"인증되지 않은 학번입니다.\n한동대 이메일에서 승인해주세요!",Toast.LENGTH_LONG).show();
                            // 한동대 이메일에서 인증 URL을 누르지 않으면 로그인되지 않습니다
                            // 이를 통해 타인의 학번을 도용하는 방식을 방지합니다
                        }
                    }
                    else {
                        DocumentReference docRef = FirebaseFirestore.getInstance().collection("UserProfile").document("Nickname");
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()) {
                                Map<String, Object> mail2name = task.getResult().getData();
                                if(mail2name.containsKey(EmailID)) {
                                    Toast.makeText(LoginActivity.this, "비밀번호를 잘못 입력하였습니다.\n비밀번호를 다시 입력해주세요.", Toast.LENGTH_LONG).show();
                                    // 이메일이 데이터베이스에 존재하지만 로그인할 수 없던 경우 비밀번호를 잘못 입력한 것으로 간주합니다
                                }
                                else {
                                    Toast.makeText(LoginActivity.this, "등록되지 않은 학번입니다.\n먼저 회원가입을 해주세요.", Toast.LENGTH_LONG).show();
                                    Id.setText("");
                                    // 이메일이 데이터베이스에 없다면 회원가입되지 않은 것으로 간주합니다
                                }
                            }
                            else {
                                Toast.makeText(LoginActivity.this, "오류가 발생했습니다.\n다시 시도해주세요.", Toast.LENGTH_LONG).show();
                                Id.setText("");
                                // 그 외에는 오류로 간주합니다
                            }
                            Password.setText("");
                            }
                        });
                    }
                    }
                });
    }

    public void goPasswordFinderActivity(View v) {
        Intent passwordFind = new Intent(this,PasswordFinderActivity.class);
        startActivity(passwordFind);
    }

    public void onGuestLogin(View v) {
        final TextView GuestLogin = findViewById(R.id.guestLogin);
        GuestLogin.setTextColor(Color.parseColor("#FF80FF"));
        GuestLogin.setEnabled(false);
        Toast.makeText(LoginActivity.this,"익명의 계정으로 로그인 중.",Toast.LENGTH_SHORT).show();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user == null) {
            firebaseAuth.signInAnonymously()
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "익명의 계정으로 로그인 되었습니다.", Toast.LENGTH_SHORT).show();
                            GuestLogin.setEnabled(true);
                            finish();
                        }
                        }
                    });
        }
        else {
            Toast.makeText(LoginActivity.this, "이미 접속된 계정입니다.\n로그아웃 후 다시 실행해주세요.", Toast.LENGTH_LONG).show();
            GuestLogin.setEnabled(true);
        }
    }
}