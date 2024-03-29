package com.example.admindashboard.activity.student;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.admindashboard.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AddStudent extends AppCompatActivity {

    private ImageView addStudentImage;
    private EditText addStudentName, addStudentPhone, addStudentAddress;
    private Spinner addStudentCategory;
    private Button addStudentBtn;
    private final int REQ = 1;
    private Bitmap bitmap = null;
    private String category;
    private String name, phone, address, downloadUrl = "";
    private ProgressDialog pd;

    private StorageReference storageReference;
    private DatabaseReference reference, dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add Student");


        addStudentImage = findViewById(R.id.addStudentImage);
        addStudentName = findViewById(R.id.addStudnetName);
        addStudentPhone = findViewById(R.id.addStudnetPhone);
        addStudentAddress = findViewById(R.id.addStudnetAddress);
        addStudentCategory = findViewById(R.id.addStudentCategory);
        addStudentBtn = findViewById(R.id.addStudentBtn);

        pd = new ProgressDialog(this);

        reference = FirebaseDatabase.getInstance().getReference().child("student");
        storageReference = FirebaseStorage.getInstance().getReference();


        String[] items = new String[]{"Select Category", "Class 6", "Class 7", "Class 8", "Class 9", "Class 10"};
        addStudentCategory.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items));

        addStudentCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category = addStudentCategory.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addStudentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        addStudentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkValidation();
            }
        });
    }

    private void checkValidation() {
        name = addStudentName.getText().toString();
        phone = addStudentPhone.getText().toString();
        address = addStudentAddress.getText().toString();

        if (name.isEmpty()) {
            addStudentName.setError("Empty");
            addStudentName.requestFocus();
        } else if (phone.isEmpty()) {
            addStudentPhone.setError("Empty");
            addStudentPhone.requestFocus();
        } else if (address.isEmpty()) {
            addStudentAddress.setError("Empty");
            addStudentAddress.requestFocus();
        } else if (category.equals("Select Category")) {
            Toast.makeText(this, "Please provide student category", Toast.LENGTH_SHORT).show();
        } else if (bitmap == null) {
            insertData();
        } else {
            uploadImage();
        }
    }


    private void uploadImage() {
        pd.setMessage("Uploading......");
        pd.show();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] finalimg = baos.toByteArray();
        final StorageReference filePath;
        filePath = storageReference.child("Students").child(finalimg + "jpg");
        final UploadTask uploadTask = filePath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(AddStudent.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl = String.valueOf(uri);
                                    insertData();

                                }
                            });
                        }
                    });
                } else {
                    pd.dismiss();
                    Toast.makeText(AddStudent.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }

            }
        });


    }


    private void insertData() {

        dbRef = reference.child(category);
        final String uniqueKey = dbRef.push().getKey();

        StudentData studentData = new StudentData(name, phone, address, downloadUrl, uniqueKey);

        dbRef.child(uniqueKey).setValue(studentData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                pd.dismiss();
                Toast.makeText(AddStudent.this, "Student Added", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(AddStudent.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void openGallery() {
        Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage, REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            addStudentImage.setImageBitmap(bitmap);
        }
    }
}