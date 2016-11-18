package com.galihpw.simak;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by GalihPW on 07/11/2016.
 */

public class IsiTopik extends AppCompatActivity {

    String position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.isi_topik);

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        position = intent.getStringExtra(ForumActivity.EXTRA_MESSAGE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(IsiTopik.this, "Tambah Komentar", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
