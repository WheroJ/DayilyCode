package com.github.wheroj.goover2017_03_15.motionevent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.github.wheroj.goover2017_03_15.R;

import java.util.ArrayList;

/**
 * Created by shopping on 2017/6/13 11:37.
 *
 * @description
 */

public class TestStickLayoutActivity extends FragmentActivity {

    private ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teststick);

        listView = (ListView) findViewById(R.id.listview);

        ArrayList<String> data = new ArrayList<>();
        int size = 100;
        for (int i = 0; i < size; i++){
            if (i % 3 == 0) {
                data.add("hahahah" + i);
            } else if (i % 3 == 1){
                data.add("heheheheh" + i);
            } else {
                data.add("oooooooo" + i);
            }
        }

        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.test_list_item, data));


    }
}
