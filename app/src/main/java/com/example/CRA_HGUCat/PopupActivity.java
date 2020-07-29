package com.example.CRA_HGUCat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PopupActivity extends Activity {

    CheckBox checkbox1, checkbox2, checkbox3, checkbox4;
    public ArrayList checklist = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //타이틀바 없애기
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_popup);

        //UI 객체생성
        checkbox1 = (CheckBox) findViewById(R.id.checkBox1);
        checkbox2 = (CheckBox)findViewById(R.id.checkBox2);
        checkbox3 = (CheckBox)findViewById(R.id.checkBox3);
        checkbox4 = (CheckBox)findViewById(R.id.checkBox4);

        Button btn_choose = (Button)findViewById(R.id.btn_choose);
        final TextView tv = (TextView)findViewById(R.id.textView2);

        btn_choose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                checklist.clear();
                if(checkbox1.isChecked() == true) checklist.add(checkbox1.getText().toString());
                if(checkbox2.isChecked() == true) checklist.add(checkbox2.getText().toString());
                if(checkbox3.isChecked() == true) checklist.add(checkbox3.getText().toString());
                if(checkbox4.isChecked() == true) checklist.add(checkbox4.getText().toString());

                if (checklist.size() == 1){
                    Toast.makeText(PopupActivity.this, "설정되었습니다.", Toast.LENGTH_SHORT).show();
                    String check = (String) checklist.get(0);
                    Intent intent = new Intent(getApplicationContext(), community_add.class);
                    intent.putExtra("checked", check);
                    setResult(RESULT_OK, intent);

                    //액티비티(팝업) 닫기
                    finish();
                }

                else{
                    Toast.makeText(PopupActivity.this, "하나만 체크", Toast.LENGTH_SHORT).show();
                }
            } // end onClick
        }); // end setOnClickListener
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}