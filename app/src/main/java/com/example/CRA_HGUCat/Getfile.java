package com.example.CRA_HGUCat;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.BitmapDrawable;
import android.icu.util.Output;
import android.os.Bundle;
import android.view.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import android.widget.Button;
import android.widget.ImageView;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;


public class Getfile extends AppCompatActivity {

    ImageView imgVwSelected;
    Button btnImageSend, btnImageSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getfile);

        btnImageSend = findViewById(R.id.btnImageSend);
        btnImageSend.setEnabled(false);
        btnImageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ImageView img = findViewById(R.id.imgVwSelected);
                Bitmap bitmap = ((BitmapDrawable)img.getDrawable()).getBitmap();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG,90,outputStream);
                // 이미지를 받아와서 비트맵으로 전환

                byte[] data = outputStream.toByteArray();
                System.out.println(Arrays.toString(data));

                Intent addimage = new Intent(getApplicationContext(), Community_add.class);
                addimage.putExtra("data_file", data);
                setResult(RESULT_OK, addimage);
                finish();
                //bitmap byte로 바꾼 것 community_Add class로 전송
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


}
//출처: https://derveljunit.tistory.com/302 [Derveljun's Programming Log]