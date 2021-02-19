package com.example.admin_school;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Uploadfaculty extends AppCompatActivity {

    FloatingActionButton fab;
    private RecyclerView csDepartment, mechanicalDepartment, physicsDepartment, chmesityDepartment;
    private LinearLayout csNoData, mecNoData, phyNoData, chNoData;
    private List<TeacherData> list1, list2, list3,list4;
    private TeacherAdapter adapter;

    private DatabaseReference reference, dbRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadfaculty);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Upload Teacher");

        csDepartment = findViewById(R.id.csDepartment);
        mechanicalDepartment = findViewById(R.id.mechanicalDepartment);
        physicsDepartment = findViewById(R.id.physicsDepartment);
        chmesityDepartment = findViewById(R.id.chmesityDepartment);

        csNoData = findViewById(R.id.csNoData);
        mecNoData = findViewById(R.id.mecNoData);
        phyNoData = findViewById(R.id.phyNoData);
        chNoData = findViewById(R.id.chNoData);

        reference = FirebaseDatabase.getInstance().getReference().child("teacher");

        csDepartment();
        mechanicalDepartment();
        physicsDepartment();
        chmesityDepartment();

        fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Uploadfaculty.this,AddTeacher.class));
            }
        });
    }

    private void csDepartment() {

        dbRef = reference.child("Science");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list1 = new ArrayList<>();
                if (!dataSnapshot.exists()){
                    csNoData.setVisibility(View.VISIBLE);
                    csDepartment.setVisibility(View.GONE);
                }else {


                    csNoData.setVisibility(View.GONE);
                    csDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list1.add(data);
                    }
                    csDepartment.setHasFixedSize(true);
                    csDepartment.setLayoutManager(new LinearLayoutManager(Uploadfaculty.this));
                    adapter = new TeacherAdapter(list1, Uploadfaculty.this, "Science");
                    csDepartment.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(Uploadfaculty.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void mechanicalDepartment() {

        dbRef = reference.child("Humanities");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list2 = new ArrayList<>();
                if (!dataSnapshot.exists()){
                    mecNoData.setVisibility(View.VISIBLE);
                    mechanicalDepartment.setVisibility(View.GONE);
                }else {


                    mecNoData.setVisibility(View.GONE);
                    mechanicalDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list2.add(data);
                    }
                    mechanicalDepartment.setHasFixedSize(true);
                    mechanicalDepartment.setLayoutManager(new LinearLayoutManager(Uploadfaculty.this));
                    adapter = new TeacherAdapter(list2, Uploadfaculty.this,"Humanities");
                    mechanicalDepartment.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(Uploadfaculty.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void physicsDepartment() {

        dbRef = reference.child("Commerce");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list3 = new ArrayList<>();
                if (!dataSnapshot.exists()){
                    phyNoData.setVisibility(View.VISIBLE);
                    physicsDepartment.setVisibility(View.GONE);
                }else {


                    phyNoData.setVisibility(View.GONE);
                    physicsDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list3.add(data);
                    }
                    physicsDepartment.setHasFixedSize(true);
                    physicsDepartment.setLayoutManager(new LinearLayoutManager(Uploadfaculty.this));
                    adapter = new TeacherAdapter(list3, Uploadfaculty.this,"Commerce");
                    physicsDepartment.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(Uploadfaculty.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }



    private void chmesityDepartment() {

        dbRef = reference.child("ICT");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list4 = new ArrayList<>();
                if (!dataSnapshot.exists()){
                    chNoData.setVisibility(View.VISIBLE);
                    chmesityDepartment.setVisibility(View.GONE);
                }else {
                    chNoData.setVisibility(View.GONE);
                    chmesityDepartment.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        TeacherData data = snapshot.getValue(TeacherData.class);
                        list4.add(data);
                    }
                    chmesityDepartment.setHasFixedSize(true);
                    chmesityDepartment.setLayoutManager(new LinearLayoutManager(Uploadfaculty.this));
                    adapter = new TeacherAdapter(list4, Uploadfaculty.this,"ICT");
                    chmesityDepartment.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(Uploadfaculty.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}