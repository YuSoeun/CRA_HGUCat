package com.example.CRA_HGUCat.AccountSetting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.CRA_HGUCat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PersonalAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_account);
    }

    public void Logout(View v) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser().isAnonymous()) {
            firebaseAuth.getCurrentUser().delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(PersonalAccount.this, "익명 계정에서 로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                            finish();
                            // 익명 계정인 상태에서 로그아웃을 누르면 계정 탈퇴로 인정되어 계정이 자동 삭제됩니다
                        }
                        }
                    });
        }
        else {
            firebaseAuth.signOut();
            Toast.makeText(PersonalAccount.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
            finish();
            // 익명 계정이 아닌 경우 로그아웃 기능만 수행합니다(로그인 하면 기존의 정보를 불러올 수 있습니다)
        }
    }

}