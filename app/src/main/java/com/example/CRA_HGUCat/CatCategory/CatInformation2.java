package com.example.CRA_HGUCat.CatCategory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.CRA_HGUCat.CatCommunity.CommunityMain;
import com.example.CRA_HGUCat.R;

public class CatInformation2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_information2);
    }

    public void StartCommunity(View v) {
        Intent mainCommunity = new Intent(this, CommunityMain.class);
        startActivity(mainCommunity);
        // 커뮤니티 액티비티로 이동
    }
}