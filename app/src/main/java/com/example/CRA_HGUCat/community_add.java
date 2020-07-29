package com.example.CRA_HGUCat;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class community_add extends AppCompatActivity {

    TextView editText1;
    TextView text_wheretopost, tv;
    TextView text_addfile;
    Button btn_post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_add);

        editText1 = (TextView)findViewById(R.id.editText1);
        tv = (TextView) findViewById(R.id.TextView01);
        text_wheretopost = (TextView)findViewById(R.id.Text_wheretopost);
        text_addfile = (TextView)findViewById(R.id.Text_addfile);
        btn_post = (Button) findViewById(R.id.btn_post);

        Intent pop = new Intent(this, PopupActivity.class);
        pop.putExtra("data", "Test Popup");
        startActivityForResult(pop, 1);

    }

    //버튼
    public void mOnPopupClick(View v){
        //데이터 담아서 팝업(액티비티) 호출
        Intent intent = new Intent(this, PopupActivity.class);
        intent.putExtra("data", "Test Popup");
        startActivityForResult(intent, 0);
    }

    public void Fileuplode(View v){
        Intent file = new Intent(this, getfile.class);
        file.putExtra("data", "Test Popup");
        startActivityForResult(file, 1);
    }

    public void onClickPost(View v){
        Intent post = new Intent ( this, server_client.class);
        post.putExtra ( "Textbox", editText1.getText().toString());
        startActivity(post);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                //데이터 받기
                String result = data.getStringExtra("checked");
                text_wheretopost.setText(result);
            }
        }

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                //데이터 받기
                String result2 = data.getStringExtra("imageOk");
                text_addfile.setText(result2);
            }
        }
    }
}