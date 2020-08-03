package com.example.CRA_HGUCat;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class Community_add extends AppCompatActivity {

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
        Intent file = new Intent(this, Getfile.class);
        file.putExtra("data_file", "data");
        startActivityForResult(file, 1);
    }

    public void onClickPost(View v){
        Intent post = new Intent ( this, server_client.class);
        post.putExtra ( "Textbox", editText1.getText().toString());
        startActivity(post);

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

                    byte[] data = text_addfile.getText().toString().getBytes();
                    System.out.println(Arrays.toString(data));

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