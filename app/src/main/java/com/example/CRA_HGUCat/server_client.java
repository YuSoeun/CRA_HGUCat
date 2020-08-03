package com.example.CRA_HGUCat;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

//import org.apache.http.util.ByteArrayBuffer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class server_client extends Activity {
    private String html = "";
    private Handler mHandler;
    private Socket socket;
    private String name;
    private BufferedReader networkReader;
    private BufferedWriter networkWriter;
    private String ip = "cat@49.143.69.123"; // SERVER IP를 잡습니다.
    private int port = 22; // PORT를 설정합니다.

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        try {
            socket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_client);
        mHandler = new Handler();
        try {
            setSocket(ip, port);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        checkUpdate.start();

        Intent i = getIntent();
        String text = i.getStringExtra("Textbox");
        assert text != null;

        if (text.equals("")) {
            PrintWriter out = new PrintWriter(networkWriter, true);
            out.println(text);
        }
    };

    private Thread checkUpdate = new Thread() {
        public void run() {
            try {
                String line;
                Log.w("ChattingStart", "Start Thread");
                while (true) {
                    Log.w("Chatting is running", "chatting is running");
                    line = networkReader.readLine();
                    html = line;
                    mHandler.post(showUpdate);
                }
            } catch (Exception e) {
            }
        }
    };
        private Runnable showUpdate = new Runnable() {
            public void run() {
                Toast.makeText(server_client.this, "Coming word: " + html,
                        Toast.LENGTH_SHORT).show();
            }
        };

        public void setSocket(String ip, int port) throws IOException {
            try {
                socket = new Socket(ip, port);
                networkWriter =
                        new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                networkReader =
                        new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                System.out.println(e);
                e.printStackTrace();
            };
        };
}





//출처: https://webprogrammer.tistory.com/1817 [개발자(開發者) a developer]