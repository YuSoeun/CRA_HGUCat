package com.example.CRA_HGUCat.CatCategory;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.CRA_HGUCat.R;

public class CatList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_list);

        ListView listview;
        ListViewAdapter adapter;

        // Adapter 생성
        adapter = new ListViewAdapter();

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView)findViewById(R.id.listview2);
        listview.setAdapter(adapter);

        // 첫 번째 아이템 추가.
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.baseline_pets_black_36dp), "Nabi", "Pet icon");
        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.baseline_pets_black_36dp), "Pet2", "Pet icon");

        // 위에서 생성한 listview에 클릭 이벤트 핸들러 정의.
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
            // get item
            ListViewItem item = (ListViewItem)parent.getItemAtPosition(position);

            String titleStr = item.getTitle();
            String descStr = item.getDesc();
            Drawable iconDrawable = item.getIcon();

            if(titleStr.equals("Nabi")) {
                //LayoutInflater.from(activity_cat_list.this).inflate(Integer.parseInt(".nabi_information"), null);
                Intent intent  = new Intent(CatList.this, CatInformation.class);
                startActivity(intent);
            }
            }
        });
    }
}