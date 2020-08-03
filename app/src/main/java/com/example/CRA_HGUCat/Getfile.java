package com.example.CRA_HGUCat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.icu.util.Output;
import android.os.Bundle;
import android.view.View;

import android.widget.TextView;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Getfile extends AppCompatActivity {

    ImageView imgVwSelected;
    Button btnImageSend, btnImageSelection;
    File tempSelectFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getfile);

        btnImageSend = findViewById(R.id.btnImageSend);
        btnImageSend.setEnabled(false);
        btnImageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goClick();
                String imageOk = "파일이 저장되었습니다";

                Intent file = new Intent(getApplicationContext(), Community_add.class);
                file.putExtra("imageOk", imageOk);
                setResult(RESULT_OK, file);

                finish();
            }
        });

        btnImageSelection = findViewById(R.id.btnImageSelection);
        btnImageSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent를 통해 이미지를 선택
                Intent intent = new Intent();
                // intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });

        imgVwSelected = findViewById(R.id.imgVwSelected);
    }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode != 1 || resultCode != RESULT_OK) {
                return;
            }

            Uri dataUri = data.getData();
            imgVwSelected.setImageURI(dataUri);

            try {
                // ImageView 에 이미지 출력
                InputStream in = getContentResolver().openInputStream(dataUri);
                Bitmap image = BitmapFactory.decodeStream(in);
                imgVwSelected.setImageBitmap(image);
                in.close();

            } catch (IOException ioe) {
                ioe.printStackTrace();
            }


            btnImageSend.setEnabled(true);
        }

    public void goClick()
    {
        new Thread() {
            public void run(){
                try
                {
                    JSch jsch = new JSch();
                    Session session = jsch.getSession("cat", "49.143.69.123", 22);
                    // cat@49.143.69.123::22 에서 세션을 받아옴
                    session.setPassword("hgucat");
                    // 비밀번호를 통해 접속인증을 받음 (안받으면 접속 불가)
                    java.util.Properties config = new java.util.Properties();
                    config.put("StrictHostKeyChecking", "no");
                    // ssh의 hostkeychecking를 비활성화 (간단히 접속)
                    session.setConfig(config);
                    session.connect();
                    // 세션 연결

                    Channel channel = session.openChannel("sftp");
                    // sftp 사용으로 생
                    channel.connect();
                    ChannelSftp channelSftp = (ChannelSftp) channel;

                    ImageView img = findViewById(R.id.imgVwSelected);
                    Bitmap bitmap = ((BitmapDrawable)img.getDrawable()).getBitmap();
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,90,outputStream);
                    // 이미지를 받아와서 비트맵으로 전환

                    byte[] data = outputStream.toByteArray();
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
                    String date = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss").format(new Date());
                    channelSftp.put(new ByteArrayInputStream("".getBytes()),"/home/cat/Hello/" + date + ".png");
                    channelSftp.put(inputStream,"/home/cat/Hello/" + date + ".png");
                    // 현재 시간으로 파일명을 지정하여 새 파일 생성 후 저장
                    session.disconnect();

                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        }.start();
        // 새 스레드를 만들어주지 않으면 충돌 비스무리한게 일어남
    }
}
//출처: https://derveljunit.tistory.com/302 [Derveljun's Programming Log]