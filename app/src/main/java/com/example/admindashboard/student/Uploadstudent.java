package com.example.admindashboard.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.admindashboard.R;
import com.example.admindashboard.TeacherAdapter;
import com.example.admindashboard.TeacherData;
import com.example.admindashboard.Uploadfaculty;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Uploadstudent extends AppCompatActivity {

    FloatingActionButton fab;
    private RecyclerView class6, class7, class8, class9, class10;
    private LinearLayout class6NoData,class7NoData, class8NoData, class9NoData, class10NoData;
    private List<StudentData> list, list1, list2, list3,list4;
    private StudentAdapter adapter;

    private DatabaseReference reference, dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploadstudent);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Upload Student");


        class6 = findViewById(R.id.class6);
        class7 = findViewById(R.id.class7);
        class8 = findViewById(R.id.class8);
        class9 = findViewById(R.id.class9);
        class10 = findViewById(R.id.class10);

        class6NoData = findViewById(R.id.class6NoData);
        class7NoData = findViewById(R.id.class7NoData);
        class8NoData = findViewById(R.id.class8NoData);
        class9NoData = findViewById(R.id.class9NoData);
        class10NoData = findViewById(R.id.class10NoData);

        reference = FirebaseDatabase.getInstance().getReference().child("student");

       class6();
       class7();
       class8();
       class9();
       class10();

       fab = findViewById(R.id.fab);

       fab.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(Uploadstudent.this, AddStudent.class));
           }
       });
    }


    private void class6() {
        dbRef = reference.child("Class 6");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<>();
                if (!dataSnapshot.exists()){
                    class6NoData.setVisibility(View.VISIBLE);
                    class6.setVisibility(View.GONE);
                }else {


                    class6NoData.setVisibility(View.GONE);
                    class6.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        StudentData data = snapshot.getValue(StudentData.class);
                        list.add(data);
                    }
                    class6.setHasFixedSize(true);
                    class6.setLayoutManager(new LinearLayoutManager(Uploadstudent.this));
                    adapter = new StudentAdapter(list, Uploadstudent.this, "Class 6");
                    class6.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(Uploadstudent.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void class7() {
        dbRef = reference.child("Class 7");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list1 = new ArrayList<>();
                if (!dataSnapshot.exists()){
                    class7NoData.setVisibility(View.VISIBLE);
                    class7.setVisibility(View.GONE);
                }else {


                    class7NoData.setVisibility(View.GONE);
                    class7.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        StudentData data = snapshot.getValue(StudentData.class);
                        list1.add(data);
                    }
                    class7.setHasFixedSize(true);
                    class7.setLayoutManager(new LinearLayoutManager(Uploadstudent.this));
                    adapter = new StudentAdapter(list1, Uploadstudent.this, "Class 7");
                    class7.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(Uploadstudent.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void class8() {
        dbRef = reference.child("Class 8");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list2 = new ArrayList<>();
                if (!dataSnapshot.exists()){
                    class8NoData.setVisibility(View.VISIBLE);
                    class8.setVisibility(View.GONE);
                }else {


                    class8NoData.setVisibility(View.GONE);
                    class8.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        StudentData data = snapshot.getValue(StudentData.class);
                        list2.add(data);
                    }
                    class8.setHasFixedSize(true);
                    class8.setLayoutManager(new LinearLayoutManager(Uploadstudent.this));
                    adapter = new StudentAdapter(list2, Uploadstudent.this, "Class 8");
                    class8.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(Uploadstudent.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void class9() {
        dbRef = reference.child("Class 9");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list3 = new ArrayList<>();
                if (!dataSnapshot.exists()){
                    class9NoData.setVisibility(View.VISIBLE);
                    class9.setVisibility(View.GONE);
                }else {
                    class9NoData.setVisibility(View.GONE);
                    class9.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        StudentData data = snapshot.getValue(StudentData.class);
                        list3.add(data);
                    }
                    class9.setHasFixedSize(true);
                    class9.setLayoutManager(new LinearLayoutManager(Uploadstudent.this));
                    adapter = new StudentAdapter(list3, Uploadstudent.this, "Class 9");
                    class9.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(Uploadstudent.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void class10() {
        dbRef = reference.child("Class 10");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list4 = new ArrayList<>();
                if (!dataSnapshot.exists()){
                    class10NoData.setVisibility(View.VISIBLE);
                    class10.setVisibility(View.GONE);
                }else {
                    class10NoData.setVisibility(View.GONE);
                    class10.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                        StudentData data = snapshot.getValue(StudentData.class);
                        list4.add(data);
                    }
                    class10.setHasFixedSize(true);
                    class10.setLayoutManager(new LinearLayoutManager(Uploadstudent.this));
                    adapter = new StudentAdapter(list4, Uploadstudent.this, "Class 10");
                    class10.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(Uploadstudent.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
}