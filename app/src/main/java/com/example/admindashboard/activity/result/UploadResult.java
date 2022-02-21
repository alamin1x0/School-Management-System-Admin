package com.example.admindashboard.activity.result;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.admindashboard.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UploadResult extends AppCompatActivity {

    FloatingActionButton fab;

    private DatabaseReference reference, dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_result);
        getSupportActionBar().setTitle("আপলোড রেজাল্ট");


        fab = findViewById(R.id.fab);

        reference = FirebaseDatabase.getInstance().getReference().child("result");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UploadResult.this, AddResult.class));
            }
        });

    }
}