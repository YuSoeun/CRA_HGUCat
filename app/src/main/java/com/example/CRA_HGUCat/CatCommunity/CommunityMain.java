package com.example.CRA_HGUCat.CatCommunity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.CRA_HGUCat.R;

public class CommunityMain extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_main);
    }

    public void AddCommunity(View v) {
        Intent postIntent = new Intent(this, CommunityAdd.class);
        startActivity(postIntent);
        // 커뮤니티 글 추가 창으로 이동
    }

    public void VisitCommunity(View v) {
        Intent getIntent = new Intent(this,CommunityRead.class);
        startActivity(getIntent);
        // 커뮤니티 글 읽기 창으로 이동
    }
}