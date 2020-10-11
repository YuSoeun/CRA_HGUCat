package com.example.CRA_HGUCat.CatCommunity;
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

import com.example.CRA_HGUCat.R;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommunityAdd extends AppCompatActivity {

    TextView TitleText, BulletinText, PostSelectText, AddFileText;
    String AddBulletinDirectory;
    Button btn_post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_add);

        TitleText = (TextView)findViewById(R.id.TitleText);
        BulletinText = (TextView)findViewById(R.id.BulletinText);
        PostSelectText = (TextView)findViewById(R.id.Text_wheretopost);
        AddFileText = (TextView)findViewById(R.id.Text_addfile);
        btn_post = (Button)findViewById(R.id.btn_post);


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

        new Thread() {
            public void run(){
            try {
                //서버 연결
                JSch jsch = new JSch();
                Session session = jsch.getSession("", "", 0);
                // TODO - 2(보안문제 해결)
                session.setPassword("");
                java.util.Properties config = new java.util.Properties();
                config.put("StrictHostKeyChecking", "no");
                session.setConfig(config);
                session.connect();

                Channel channel = session.openChannel("sftp");  //채널 접속
                channel.connect();
                ChannelSftp channelSftp = (ChannelSftp) channel;    //명령어 전송 채널 사용

                byte[] data = BulletinText.getText().toString().getBytes();
                ByteArrayInputStream inputTextStream = new ByteArrayInputStream(data);

                channelSftp.put(inputTextStream,"/home/cat/"+ AddBulletinDirectory +"/" + TitleText.getText().toString() +  ".txt");
                // 현재 날짜를 이름으로 저장
                ByteArrayInputStream inputImgStream = null;

                ImageView sampleImg = findViewById(R.id.SampleImage);
                if(sampleImg.getDrawable() != null) {
                    Bitmap bitmap = ((BitmapDrawable)sampleImg.getDrawable()).getBitmap();
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,50,outputStream);
                    data = outputStream.toByteArray();
                    inputImgStream = new ByteArrayInputStream(data);
                    channelSftp.put(inputImgStream,"/home/cat/"+ AddBulletinDirectory +"/"+ TitleText.getText().toString() +".png");
                }
                session.disconnect();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
            }
        }.start();
        // 새 스레드를 만들어주지 않으면 충돌 발생

        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("checked");
                PostSelectText.setText(result);
                AddBulletinDirectory = result.equals("새로운 고양이를 찾았다")?"NewCat":result.equals("수정해주세요")?"RequestFix":"CatCommunity";
            }
        }

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String result2 = data.getStringExtra("imageOk");
                AddFileText.setText(result2);
                byte[] dataBytes = data.getByteArrayExtra("data_byte");
                ByteArrayInputStream inputStream = new ByteArrayInputStream(dataBytes);
                ImageView SampleImg = findViewById(R.id.SampleImage);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                SampleImg.setImageBitmap(bitmap);
            }
        }

    }
}