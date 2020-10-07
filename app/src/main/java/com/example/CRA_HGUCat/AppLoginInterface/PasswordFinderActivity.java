package com.example.CRA_HGUCat.AppLoginInterface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.CRA_HGUCat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PasswordFinderActivity extends AppCompatActivity {

    TextInputEditText resettingPassword;
    Button passwordResettingBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_finder);
    }

    public void ChangePasswordCall(View v) {
        final View passwordFinder = v;
        passwordResettingBtn = findViewById(R.id.PasswordResetBtn);
        resettingPassword = findViewById(R.id.PasswordResetting);
        passwordResettingBtn.setEnabled(false);
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("UserProfile").document("Nickname");
        // UserProfile/Nickname/에서의 문서항목들을 불러옵니다(이떄, 이메일-별명 방식의 딕셔너리를 사용합니다)
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if(task.isSuccessful()) {
                String HandongMail = resettingPassword.getText().toString()+"@handong.edu";
                if(task.getResult().getData().containsKey(HandongMail)) {
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.setLanguageCode("ko");
                    // ko는 한국어를 의미합니다
                    firebaseAuth.sendPasswordResetEmail(HandongMail)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                final Snackbar snackbar = Snackbar.make(passwordFinder,
                                        "이메일로 비밀번호 변경 문자를\n수신했습니다.",Snackbar.LENGTH_INDEFINITE);
                                snackbar.setAction("확인", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                    snackbar.dismiss();
                                    finish();
                                    }
                                });
                                snackbar.show();
                                // 실수로 비밀번호 재설정 문자가 발송되는 것을 미확인할 것을 우려해 토스트 대신 스낵바를 이용하였습니다
                                }
                            });
                }
                else {
                    Snackbar.make(passwordFinder,"등록되지 않은 학번입니다.\n다시 확인해주세요.",Snackbar.LENGTH_LONG).show();
                    passwordResettingBtn.setEnabled(true);
                    // 데이터베이스에 이메일이 없다면 회원가입되지 않은 것으로 간주합니다
                }
            }
            else {
                Snackbar.make(passwordFinder,"오류가 발생헀습니다.\n다시 확인해주세요.",Snackbar.LENGTH_LONG).show();
                passwordResettingBtn.setEnabled(true);
                // 그 외에는 오류로 간주합니다
            }
            }
        });
    }
}