package com.example.CRA_HGUCat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.util.ArrayList;
import java.util.Properties;
import java.util.Vector;

public class CommunityRead extends AppCompatActivity {

    ListView BulletinBoard;
    ArrayList<String> Bulletin;
    ArrayAdapter<String> BulletinAdapter;
    Vector<ChannelSftp.LsEntry> fileLsEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_read);
        BulletinBoard = findViewById(R.id.CommunityBulletinBoard);
        Bulletin = new ArrayList();
        BulletinAdapter = new ArrayAdapter(CommunityRead.this,android.R.layout.simple_list_item_1,Bulletin);
        BulletinBoard.setAdapter(BulletinAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread() {
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

                final ChannelSftp channelSftp = (ChannelSftp)channel;
                fileLsEntry = channelSftp.ls("/home/cat/RequestFix");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    for(int fileIndex = 0; fileIndex < fileLsEntry.size(); fileIndex++) {
                        ChannelSftp.LsEntry lsEntry = fileLsEntry.get(fileIndex);
                        if(!lsEntry.getAttrs().isDir()) {
                            BulletinAdapter.add(lsEntry.getFilename());
                        }
                    }

                    BulletinBoard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent ReadBulletin = new Intent(CommunityRead.this,CommunityBulletinActivity.class);
                        ReadBulletin.putExtra("FileName",fileLsEntry.get(i+1).getFilename());
                        // 첫 인덱스는 ..으로 취급하므로 (인덱스 + 1)번째의 파일명을 전송한다.
                        startActivity(ReadBulletin);
                        }
                    });
                    }
                });

                channel.disconnect();
                session.disconnect();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            }
        }.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Bulletin.removeAll(Bulletin);
        BulletinAdapter.notifyDataSetChanged();
        // 중복 방지를 위해 매번 킬때마다 목록 삭제 후 재생성
    }
}