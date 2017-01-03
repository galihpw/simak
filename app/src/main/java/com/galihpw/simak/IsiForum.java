package com.galihpw.simak;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import static com.galihpw.simak.ForumActivity.FORUM_MESSAGE1;
import static com.galihpw.simak.ForumActivity.FORUM_MESSAGE2;

/**
 * Created by GalihPW on 31/12/2016.
 */

public class IsiForum extends AppCompatActivity{

    String sJudulForum, sIsiForum;
    TextView tvJudulForum, tvIsiForum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listtopik);

        tvJudulForum = (TextView) findViewById(R.id.judulforum);
        tvIsiForum = (TextView) findViewById(R.id.isiforum);

        Intent intent = getIntent();
        sJudulForum = intent.getStringExtra(FORUM_MESSAGE1);
        sIsiForum = intent.getStringExtra(FORUM_MESSAGE2);

        tvJudulForum.setText("Cek " + sJudulForum);
        tvIsiForum.setText(sIsiForum);
    }

}
