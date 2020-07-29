package com.example.CRA_HGUCat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class PasswordFinderActivity extends AppCompatActivity {

    TextInputEditText resettingPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_finder);

        resettingPassword = findViewById(R.id.PasswordResetting);
    }

    public void ChangePasswordCall(View v)
    {
        final View pwFinder = v;
        final Button btn = findViewById(R.id.PasswordResetBtn);
        btn.setEnabled(false);
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("UserProfile").document("Nickname");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful())
                {
                    String Handongmail = resettingPassword.getText().toString()+"@handong.edu";
                    if(task.getResult().getData().containsKey(Handongmail))
                    {
                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        firebaseAuth.setLanguageCode("ko");
                        firebaseAuth.sendPasswordResetEmail(Handongmail)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        final Snackbar snackbar = Snackbar.make(pwFinder,
                                                "이메일로 비밀번호 변경 문자를\n수신했습니다.",Snackbar.LENGTH_INDEFINITE);
                                        snackbar.setAction("확인", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                snackbar.dismiss();
                                                finish();
                                            }
                                        });
                                        snackbar.show();
                                    }
                                });
                    }
                    else
                    {
                        Snackbar.make(pwFinder,"등록되지 않은 학번입니다.\n다시 확인해주세요.",Snackbar.LENGTH_LONG).show();
                        btn.setEnabled(true);
                    }
                }
                else
                {
                    Snackbar.make(pwFinder,"오류가 발생헀습니다.\n다시 확인해주세요.",Snackbar.LENGTH_LONG).show();
                    btn.setEnabled(true);
                }
            }
        });
    }
}