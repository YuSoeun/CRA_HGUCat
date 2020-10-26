package com.example.CRA_HGUCat.CatCommunity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.CRA_HGUCat.CatCommunity.CommunityAdd;
import com.example.CRA_HGUCat.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class GetFile extends AppCompatActivity {

    ImageView imgViewSelected;
    Button btnImageSend, btnImageSelection;
    Uri imgFileUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_file);
        imgViewSelected = findViewById(R.id.imgViewSelected);

        btnImageSend = findViewById(R.id.btnImageSend);
        btnImageSend.setEnabled(false);
        btnImageSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            /*Bitmap bitmap = ((BitmapDrawable)imgViewSelected.getDrawable()).getBitmap();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,90,outputStream);*/

            String ImageOk = "파일이 저장되었습니다";

            Intent addImage2CommunityAdd = new Intent(getApplicationContext(), CommunityAdd.class);
            addImage2CommunityAdd.putExtra("imageOk", ImageOk);
            addImage2CommunityAdd.putExtra("imgPath", imgFileUri.getPath());
            setResult(RESULT_OK, addImage2CommunityAdd);

            finish();
            }
        });

        btnImageSelection = findViewById(R.id.btnImageSelection);
        btnImageSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent imgSelectionIntent = new Intent();
            imgSelectionIntent.setType("image/*");
            imgSelectionIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(imgSelectionIntent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != 1 || resultCode != RESULT_OK) return;

        imgFileUri = data.getData();
        imgViewSelected.setImageURI(imgFileUri);

        try {
            InputStream GalleryImgInputStream = getContentResolver().openInputStream(imgFileUri);
            Bitmap image = BitmapFactory.decodeStream(GalleryImgInputStream);
            imgViewSelected.setImageBitmap(image);
            GalleryImgInputStream.close();
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }

        btnImageSend.setEnabled(true);
    }
}
//출처: https://derveljunit.tistory.com/302 [Derveljun's Programming Log]