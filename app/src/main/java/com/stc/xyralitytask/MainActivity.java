package com.stc.xyralitytask;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String EXTRA_WORLDS_LIST = "extra_worlds";
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView=findViewById(R.id.lv);
        parseIntent();
    }

    private void parseIntent(){
        if (getIntent()==null){
            Log.e(TAG, "parseIntent: null" );
            finish();
        }
        List<String>names=getIntent().getStringArrayListExtra(EXTRA_WORLDS_LIST);
        if(names==null || names.isEmpty()){
            Log.e(TAG, "parseIntent: null" );
            finish();
        }else {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, names);
            listView.setAdapter(adapter);
        }
    }

}
