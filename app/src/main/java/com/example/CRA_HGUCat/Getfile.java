package com.example.CRA_HGUCat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
                FileUploadUtils.goSend(tempSelectFile);
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

                // 선택한 이미지 임시 저장
                String date = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss").format(new Date());
                tempSelectFile = new File(Environment.getExternalStorageDirectory() + "/Pictures/Test/", "temp_" + date + ".jpeg");
                OutputStream out = new FileOutputStream(tempSelectFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, out);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }


            btnImageSend.setEnabled(true);
        }
}
//출처: https://derveljunit.tistory.com/302 [Derveljun's Programming Log]