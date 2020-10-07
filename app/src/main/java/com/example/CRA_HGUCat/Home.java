package com.example.CRA_HGUCat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.CRA_HGUCat.AccountSetting.PersonalAccount;
import com.example.CRA_HGUCat.AppLoginInterface.LoginActivity;
import com.example.CRA_HGUCat.CameraCapture.CaptureCat;
import com.example.CRA_HGUCat.CatCategory.CatList;
import com.example.CRA_HGUCat.CatCommunity.CommunityMain;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class Home extends AppCompatActivity{

    private static final int RC_SIGN_IN = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Intent Login = new Intent(this, LoginActivity.class);
            startActivity(Login);
        }
        // 로그인 되지 않은 경우 로그인 액티비티로 이동
    }

    public void StartList(View v) {
        Intent catList = new Intent(this, CatList.class);
        startActivity(catList);
        // 고양이 리스트 액티비티로 이동
    }

    public void StartAccount(View v) {
        Intent personalAccount = new Intent(this, PersonalAccount.class);
        startActivity(personalAccount);
        // 개인 설정 액티비티로 이동
    }

    public void StartCommunity(View v) {
        Intent mainCommunity = new Intent(this, CommunityMain.class);
        startActivity(mainCommunity);
        // 커뮤니티 액티비티로 이동
    }

    public void StartCamera(View v) {
        Intent captureMode = new Intent(this, CaptureCat.class);
        startActivity(captureMode);
        // 카메라 액티비티로 이동
    }

    public void StartFavoriteCat(View v) {
        Toast.makeText(this,"개발 중인 항목입니다.",Toast.LENGTH_SHORT).show();
        // 우선적으로 개발되지 않은 부분이라 아직 완성되지 않았습니다
        // 원래는 즐겨찾기 형식의 액티비티로 이동하는 코드입니다
    }
}
