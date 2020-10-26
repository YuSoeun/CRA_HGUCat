package com.example.CRA_HGUCat.CameraCapture;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.CRA_HGUCat.R;

public class SavePopup extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_save_popup);
    }

    public void onClose(View v) {
        Intent intent = new Intent();
        RadioGroup choice = findViewById(R.id.SaveChoice);
        if(choice.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "저장 방법을 선택해주세요!", Toast.LENGTH_LONG).show();
        }
        else {
            RadioButton selected = (RadioButton)findViewById(choice.getCheckedRadioButtonId());
            intent.putExtra("saveSelect", selected.getText().toString());
            setResult(RESULT_OK, intent);

            finish();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return false;
        }
        return true;
        // 레이어 바깥부분을 터치해도 아무 반응 없음
    }

    @Override
    public void onBackPressed() {
        return;
        // 백버튼 눌러도 아무 일도 일어나지 않음
    }
}