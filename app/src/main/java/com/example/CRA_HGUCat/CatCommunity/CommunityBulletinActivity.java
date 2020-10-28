package com.example.CRA_HGUCat.CatCommunity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.TextView;

import com.example.CRA_HGUCat.R;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class CommunityBulletinActivity extends AppCompatActivity {

    String BulletinFileName;
    Boolean BulletinHasPNG;
    String contentsDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_bulletin);
        BulletinFileName = getIntent().getStringExtra("FileName");
        BulletinHasPNG = getIntent().getStringExtra("hasPNG") != null;
        contentsDir = getIntent().getStringExtra("ReadContent");
        // txt파일명과 파일명이 같은 png 파일이 있는지에 대해 입력 받음
        new Thread() {
            @Override
            public void run() {
            try {
                JSch jsch = new JSch();
                Session session = jsch.getSession("", "", 0);
                session.setPassword("");
                Properties config = new Properties();
                config.put("StrictHostKeyChecking", "no");
                session.setConfig(config);
                session.connect();
                //TODO Delete this.
                final Channel channel = session.openChannel("sftp");
                channel.connect();
                ChannelSftp channelSftp = (ChannelSftp)channel;
                channelSftp.cd("/home/""/hdd/" + contentsDir);
                InputStream getFileStream = channelSftp.get(BulletinFileName);
                BufferedReader Stream2Line = new BufferedReader(new InputStreamReader(getFileStream));
                final StringBuilder Line2String = new StringBuilder();
                String stringLine;
                while((stringLine = Stream2Line.readLine()) != null) {
                    Line2String.append(stringLine);
                }
                InputStream getFileImgStream = BulletinHasPNG?channelSftp.get(BulletinFileName.substring(0,BulletinFileName.length()-3) + "png"):null;
                final Drawable FileImg = Drawable.createFromStream(getFileImgStream,null);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                        TextView BulletinTextView = findViewById(R.id.textviewBulletin);
                        if(BulletinHasPNG) {
                            BulletinTextView.setCompoundDrawablesWithIntrinsicBounds(null,FileImg,null,null);
                        }
                        BulletinTextView.setText(Line2String.toString());
                        // 가급적 UI 스레드에는 UI 관련 코드만 넣는 것을 추천
                        // 이 부분이 메인스레드라 여기서 서버 연결하면 android.os.NetworkOnMainThreadException 오류가 발생
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    }
                });

                if(BulletinHasPNG) getFileImgStream.close();
                Stream2Line.close();
                getFileStream.close();
                channel.disconnect();
                session.disconnect();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            }
        }.start();
    }
}