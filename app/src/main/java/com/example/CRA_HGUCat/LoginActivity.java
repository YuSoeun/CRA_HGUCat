package com.example.CRA_HGUCat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 상황봐서 Maven도 추가

    }

    public void Goresister(View v)
    {
        Intent cmr = new Intent(this, ResisterActivity.class);
        startActivity(cmr);
    }

    /*public void Gohome(View v)
    {
        Intent cmr = new Intent(this, home.class);
        startActivity(cmr);
    }*/
}