package com.example.CRA_HGUCat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class CommunityBulletinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_bulletin);
        final String BulletinFileName = getIntent().getStringExtra("FileName");
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
                channelSftp.cd("/home/cat/RequestFix/");
                InputStream getFileStream = channelSftp.get(BulletinFileName);
                BufferedReader Stream2Line = new BufferedReader(new InputStreamReader(getFileStream));
                final StringBuilder Line2String = new StringBuilder();
                String stringLine;
                while((stringLine = Stream2Line.readLine()) != null) {
                    Line2String.append(stringLine);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    try {
                        TextView BulletinTextView = findViewById(R.id.textviewBulletin);
                        BulletinTextView.setText(Line2String.toString());
                        // UI 스레드에는 UI 관련 코드만 넣도록 하자 (아마 이 부분이 메인스레드라 여기서 서버 연결하면 android.os.NetworkOnMainThreadException 오류가 발생)
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    }
                });

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