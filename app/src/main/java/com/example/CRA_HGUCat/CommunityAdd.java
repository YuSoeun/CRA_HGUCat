package com.example.CRA_HGUCat;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class CommunityAdd extends AppCompatActivity {

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
        startActivityForResult(pop, 0);

    }

    //버튼
    public void mOnPopupClick(View v){
        //데이터 담아서 팝업(액티비티) 호출
        Intent intent = new Intent(this, PopupActivity.class);
        intent.putExtra("data", "Test Popup");
        startActivityForResult(intent, 0);
    }

    public void Fileuplode(View v){
        Intent file = new Intent(this, GetFile.class);
        file.putExtra("data_file", "data");
        startActivityForResult(file, 1);
    }

    public void onClickPost(View v){
        /*Intent post = new Intent ( this, server_client.class);
        post.putExtra ( "Textbox", editText1.getText().toString());
        startActivity(post);*/

        new Thread() {
            public void run(){
            try {
                JSch jsch = new JSch();
                Session session = jsch.getSession("", "", 0);
                session.setPassword("");
                java.util.Properties config = new java.util.Properties();
                config.put("StrictHostKeyChecking", "no");
                session.setConfig(config);
                session.connect();

                Channel channel = session.openChannel("sftp");
                channel.connect();
                ChannelSftp channelSftp = (ChannelSftp) channel;

                byte[] data = editText1.getText().toString().getBytes();
                ByteArrayInputStream inputTextStream = new ByteArrayInputStream(data);

                String date = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss").format(new Date());
                channelSftp.put(inputTextStream,"/home/cat/Hello/" + date + "_asdf" +  ".txt");
                ByteArrayInputStream inputImgStream = null;

                ImageView sampleImg = findViewById(R.id.SampleImage);
                if(sampleImg.getDrawable() != null) {
                    Bitmap bitmap = ((BitmapDrawable) sampleImg.getDrawable()).getBitmap();
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,95,outputStream);
                    data = outputStream.toByteArray();
                    inputImgStream = new ByteArrayInputStream(data);
                    channelSftp.put(inputImgStream,"/home/cat/Hello/"+date+"_adsf"+".png");
                }
                session.disconnect();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            }
        }.start();
        // 새 스레드를 만들어주지 않으면 충돌 비스무리한게 일어남

        finish();
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
                byte[] dataBytes = data.getByteArrayExtra("data_byte");
                ByteArrayInputStream inputStream = new ByteArrayInputStream(dataBytes);
                ImageView SampleImg = findViewById(R.id.SampleImage);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                SampleImg.setImageBitmap(bitmap);
            }
        }
//        String data_conveyed = data.getStringExtra("data_byte");
//        text_addfile.setText(data_conveyed);
//        System.out.println(data_conveyed);

    }
}