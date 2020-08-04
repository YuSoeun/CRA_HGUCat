package com.example.CRA_HGUCat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
        FirebaseFirestore.getInstance().disableNetwork();
    }

    /*@Override
    protected void onResume() {

        super.onResume();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Intent Login = new Intent(this, LoginActivity.class);
            startActivity(Login);
        }
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if(resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Toast.makeText(this,"Hello! " + user.getDisplayName(), Toast.LENGTH_SHORT).show();
            }
            else {
                response.getError().getErrorCode();
            }
        }
    }



    public void StartList(View v) {
        Intent catList = new Intent(this, CatList.class);
        startActivity(catList);
    }

    public void StartAccount(View v) {
        Intent personalAccount = new Intent(this, PersonalAccount.class);
        startActivity(personalAccount);
    }

    public void StartCommunity(View v) {
        Intent mainCommunity = new Intent(this, CommunityMain.class);
        startActivity(mainCommunity);
    }

    public void StartCamera(View v) {
        Intent captureMode = new Intent(this,CaptureCat.class);
        startActivity(captureMode);
    }

    public void StartFavoriteCat(View v) {
        Toast.makeText(this,"개발 중인 항목입니다.",Toast.LENGTH_SHORT).show();
    }
}
