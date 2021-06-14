package com.example.admindashboard;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.admindashboard.student.Uploadstudent;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener{

    CardView uploadNotice, addGalleryImage, addEbook, faculty, news, student,  delete, logout;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();


        sharedPreferences = this.getSharedPreferences("login", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (sharedPreferences.getString("isLogin", "false").equals("false")){
            openLogin();

        }


        if (!isConnected())
        {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Internet Connection Please")
                    .setMessage("Please Check your Internet Connection")
                    .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();
        }

//        else {
//            Toast.makeText(MainActivity.this,"", Toast.LENGTH_LONG).show();
//        }


        uploadNotice = findViewById(R.id.addNotice);
        addGalleryImage = findViewById(R.id.addGalleryImage);
        addEbook = findViewById(R.id.addEbook);
        faculty = findViewById(R.id.faculty);
        news = findViewById(R.id.news);
        student = findViewById(R.id.student);
        delete = findViewById(R.id.delete);
        logout = findViewById(R.id.logout);

        uploadNotice.setOnClickListener(this);
        addGalleryImage.setOnClickListener(this);
        addEbook.setOnClickListener(this);
        faculty.setOnClickListener(this);
        student.setOnClickListener(this);
        delete.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    private void openLogin() {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()){
            case R.id.addNotice:
                intent = new Intent(MainActivity.this,UploadNotice.class);
                startActivity(intent);
                break;

            case R.id.addGalleryImage:
                intent = new Intent(MainActivity.this,UploadImage.class);
                startActivity(intent);
                break;


            case R.id.addEbook:
                intent = new Intent(MainActivity.this,UploadPdfActivity.class);
                startActivity(intent);
                break;

            case R.id.faculty:
                intent = new Intent(MainActivity.this,Uploadfaculty.class);
                startActivity(intent);
                break;


            case R.id.student:
                intent = new Intent(MainActivity.this, Uploadstudent.class);
                startActivity(intent);
                break;



            case R.id.delete:
                intent = new Intent(MainActivity.this,DeleteNoticeActivity.class);
                startActivity(intent);
                break;

            case R.id.logout:
                editor.putString("isLogin", "false");
                editor.commit();
                openLogin();
                break;


        }
    }


    private boolean isConnected()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }
}