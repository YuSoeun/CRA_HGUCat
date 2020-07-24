package com.example.CRA_HGUCat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void StartList(View v)
    {
        Intent cmr = new Intent(this, catlist.class);
        startActivity(cmr);
    }

    public void Startaccount(View v)
    {
        Intent cmr = new Intent(this, personal_account.class);
        startActivity(cmr);
    }

    public void StartComu(View v)
    {
        Intent cmr = new Intent(this, Community_main.class);
        startActivity(cmr);
    }

    public void StartCamera(View v)
    {
        Intent cmr = new Intent(this,CaptureCat.class);
        startActivity(cmr);
    }

    /*
    public void Startfavo(View v)
    {
        Intent cmr = new Intent(this, favorite.class);
        startActivity(cmr);
    }
    */
}
