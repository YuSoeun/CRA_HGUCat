package com.example.CRA_HGUCat.CatCommunity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.CRA_HGUCat.R;
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
                Session session = jsch.getSession("", "...", 0);
                // 이름과 ip, port를 입력
                session.setPassword("");
                // 로그인을 위해 비밀번호를 입력
                Properties config = new Properties();
                config.put("StrictHostKeyChecking", "no");
                // HostKeyChecking을 받지 않는 방식을 사용
                session.setConfig(config);
                session.connect();
                //TODO Delete this.
                final Channel channel = session.openChannel("sftp");
                channel.connect();

                final ChannelSftp channelSftp = (ChannelSftp)channel;

                Intent readContents = getIntent();
                final String contentsDir = readContents.getStringExtra("ReadContent");
                fileLsEntry = channelSftp.ls("/home/""/hdd/"+contentsDir);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    for(int fileIndex = 0; fileIndex < fileLsEntry.size(); fileIndex++) {
                        ChannelSftp.LsEntry lsEntry = fileLsEntry.get(fileIndex);
                        if(!lsEntry.getAttrs().isDir()) {
                            String FileName = lsEntry.getFilename();
                            if(FileName.substring(FileName.length()-4).equals(".txt")) {
                                BulletinAdapter.add(FileName.substring(0,FileName.length() - 4));
                                // 파일 인덱스에서 txt파일을 발견한 경우 리스트뷰에 추가
                            }
                        }
                    }

                    BulletinBoard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent ReadBulletin = new Intent(CommunityRead.this,CommunityBulletinActivity.class);
                        String FileName = Bulletin.get(i) + ".txt";
                        ReadBulletin.putExtra("FileName", FileName);
                        ReadBulletin.putExtra("ReadContent", contentsDir);
                        // .txt 파일만 전송하므로 서버 파일 인덱스 대신 ArrayList 인덱스로 대체
                        String FileNamePNGExtension = FileName.substring(0,FileName.length()-4) + ".png";
                        for(int fileIndex = 0; fileIndex < fileLsEntry.size(); fileIndex++) {
                            if(fileLsEntry.get(fileIndex).getFilename().equals(FileNamePNGExtension)) {
                                ReadBulletin.putExtra("hasPNG", "yes");
                                // 파일 인덱스 중에서 같은 이름의 .png 파일이 있는 경우 png파일의 존재를 알리는 boolean함수 전송
                            }
                        }
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