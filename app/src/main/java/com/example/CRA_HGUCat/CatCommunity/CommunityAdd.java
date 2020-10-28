package com.example.CRA_HGUCat.CatCommunity;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.CRA_HGUCat.R;
import com.example.CRA_HGUCat.SecureFromGit;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

public class CommunityAdd extends AppCompatActivity {

    TextView TitleText, BulletinText, PostSelectText, AddFileText;
    String AddBulletinDirectory;
    Button btn_post;
    SecureFromGit sshSvr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_add);

        TitleText = (TextView)findViewById(R.id.TitleText);
        BulletinText = (TextView)findViewById(R.id.BulletinText);
        PostSelectText = (TextView)findViewById(R.id.Text_wheretopost);
        AddFileText = (TextView)findViewById(R.id.Text_addfile);
        btn_post = (Button)findViewById(R.id.btn_post);

        Intent Capture = getIntent();
        // 새로운 창을 열거나 정보를 입력받아 가져올 떄는 new Intent지만, 기존의 activity에서 extras를 가져올 때는 getIntent이다.
        String capturePath = Capture.getStringExtra("captureData");
        if(capturePath != null) {
            ImageView SampleImg = findViewById(R.id.SampleImage);
            try {
                File file = new File(capturePath);
                FileInputStream inputStream = new FileInputStream(file);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                SampleImg.setImageBitmap(bitmap);
                inputStream.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Toast.makeText(this, "오류가 발생하였습니다.\n다시 시도해 주세요.", Toast.LENGTH_SHORT);
            }
        }

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
                Session session = jsch.getSession(sshSvr.username, sshSvr.host, sshSvr.port);
                // TODO - 2(보안문제 해결)
                session.setPassword(sshSvr.password);
                java.util.Properties config = new java.util.Properties();
                config.put("StrictHostKeyChecking", "no");
                session.setConfig(config);
                session.connect();

                Channel channel = session.openChannel("sftp");  //채널 접속
                channel.connect();
                ChannelSftp channelSftp = (ChannelSftp) channel;    //명령어 전송 채널 사용

                byte[] data = BulletinText.getText().toString().getBytes();
                ByteArrayInputStream inputTextStream = new ByteArrayInputStream(data);

                channelSftp.put(inputTextStream,sshSvr.dirPath+ AddBulletinDirectory +"/" + TitleText.getText().toString() +  ".txt");
                // 현재 Title을 이름으로 저장
                ByteArrayInputStream inputImgStream = null;

                ImageView sampleImg = findViewById(R.id.SampleImage);
                if(sampleImg.getDrawable() != null) {
                    Bitmap bitmap = ((BitmapDrawable)sampleImg.getDrawable()).getBitmap();
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,95,outputStream);
                    data = outputStream.toByteArray();
                    inputImgStream = new ByteArrayInputStream(data);
                    channelSftp.put(inputImgStream,sshSvr.dirPath+ AddBulletinDirectory +"/"+ TitleText.getText().toString() +".png");
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
                if(result.equals("고양이 커뮤니티"))
                    AddBulletinDirectory = "CatCommunity";
                else if(result.equals("새로운 고양이를 찾았다"))
                    AddBulletinDirectory = "NewCatFound";
                else if(result.equals("수정해주세요"))
                    AddBulletinDirectory = "RequestFix";
            }
        }

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String result2 = data.getStringExtra("imageOk");
                AddFileText.setText(result2);
                String imgDir = data.getStringExtra("imgPath");
                if(imgDir.contains("msf")) {
                    Toast.makeText(this, "바로가기 경로는 호환되지 않습니다\n직접 폴더로 이동하여 선택해주세요", Toast.LENGTH_LONG).show();
                }
                else {
                    if (imgDir.indexOf(':') != -1)
                        imgDir = imgDir.substring(imgDir.indexOf(':') + 1);
                    ImageView SampleImg = findViewById(R.id.SampleImage);
                    try {
                        File file = Environment.getExternalStoragePublicDirectory(imgDir);
                        FileInputStream inputStream = new FileInputStream(file);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        SampleImg.setImageBitmap(bitmap);
                        inputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "오류가 발생하였습니다.\n다시 시도해 주세요.", Toast.LENGTH_SHORT);
                    }
                }
            }
        }

    }
}