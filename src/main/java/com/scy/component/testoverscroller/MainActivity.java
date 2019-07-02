package com.scy.component.testoverscroller;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private MyTextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.tv_customer);
        final View view1 = findViewById(R.id.view1);
        final View view2 = findViewById(R.id.view2);
        textView.post(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                int top = view1.getBottom();
                int bottom = view2.getTop();
                textView.setBoundary(top, bottom);

            }
        });
    }
}