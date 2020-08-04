package com.example.CRA_HGUCat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class CommunityMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_main);
    }

    public void AddCommunity(View v) {
        Intent intent = new Intent(this, CommunityAdd.class);
        startActivity(intent);
    }
}