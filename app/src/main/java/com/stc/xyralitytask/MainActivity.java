package com.stc.xyralitytask;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.stc.xyralitytask.data.AllAvailableWorld;
import com.stc.xyralitytask.data.MyResponce;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String EXTRA_WORLDS_LIST = "extra_worlds";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        processIntent();
    }

    private void processIntent(){
        if(getIntent()==null){
            Log.e(TAG, "processIntent: null" );
            finish();
        }
        MyResponce myResponce = (MyResponce) getIntent().getSerializableExtra(MainActivity.EXTRA_WORLDS_LIST);
        for (AllAvailableWorld w :
                myResponce.getAllAvailableWorlds()) {

            Log.w(TAG, "processIntent: "+w.toString() );
        }
    }

}
