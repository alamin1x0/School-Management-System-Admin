package com.example.admindashboard.student;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

public class UpdateStudent extends AppCompatActivity {

    private ImageView updateStudentImage;
    private EditText updateStudentName, updateStudentPhone, updateStudentAddress;
    private Button updateStudentBtn, updateDeleteBtn;

    private String name, phone, image, address;
    private final int REQ = 1;
    private Bitmap bitmap = null;
    private ProgressDialog pd;

    private StorageReference storageReference;
    private DatabaseReference reference;

    private String downloadUrl, category, uniqueKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_student);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Update Student");


        name = getIntent().getStringExtra("name");
        phone = getIntent().getStringExtra("phone");
        image = getIntent().getStringExtra("image");
        address = getIntent().getStringExtra("address");

        uniqueKey = getIntent().getStringExtra("key");
        category = getIntent().getStringExtra("category");


        updateStudentImage = findViewById(R.id.updateStudentImage);
        updateStudentName = findViewById(R.id.updateStudentName);
        updateStudentPhone = findViewById(R.id.updateStudentPhone);
        updateStudentAddress = findViewById(R.id.updateStudentAddress);

        updateStudentBtn = findViewById(R.id.updateStudentBtn);
        updateDeleteBtn = findViewById(R.id.updateDeleteBtn);

        pd = new ProgressDialog(this);

        reference = FirebaseDatabase.getInstance().getReference().child("student");
        storageReference = FirebaseStorage.getInstance().getReference();

        try {
            Picasso.get().load(image).into(updateStudentImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        updateStudentPhone.setText(phone);
        updateStudentName.setText(name);
        updateStudentAddress.setText(address);


        updateStudentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        updateStudentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = updateStudentName.getText().toString();
                phone = updateStudentPhone.getText().toString();
                address = updateStudentAddress.getText().toString();
                checkValidation();
            }
        });

        updateDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();
            }
        });

    }


    private void deleteData() {
        reference.child(category).child(uniqueKey).removeValue()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(UpdateStudent.this, "Student Deleted Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UpdateStudent.this, Uploadstudent.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateStudent.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkValidation() {

        if (name.isEmpty()){
            updateStudentName.setError("Empty");
            updateStudentName.requestFocus();
        }else if (address.isEmpty()){
            updateStudentAddress.setError("Empty");
            updateStudentAddress.requestFocus();
        }else if (phone.isEmpty()){
            updateStudentPhone.setError("Empty");
            updateStudentPhone.requestFocus();
        }else if (bitmap == null){
            updateData(image);
        }else {
            uploadImage();
        }
    }



    private void updateData(String s) {

        HashMap hp = new HashMap();
        hp.put("name", name);
        hp.put("phone", phone);
        hp.put("address", address);
        hp.put("image", s);


        reference.child(category).child(uniqueKey).updateChildren(hp).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(UpdateStudent.this, "Student Updated Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UpdateStudent.this,Uploadstudent.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateStudent.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void uploadImage() {
        pd.setMessage("Uploading......");
        pd.show();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,baos);
        byte[] finalimg = baos.toByteArray();
        final StorageReference filePath;
        filePath = storageReference.child("Students").child(finalimg+"jpg");
        final UploadTask uploadTask = filePath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(UpdateStudent.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()){
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl = String.valueOf(uri);
                                    updateData(downloadUrl);

                                }
                            });
                        }
                    });
                }else {
//                    pd.dismiss();
                    Toast.makeText(UpdateStudent.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private void openGallery() {
        Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage,REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode == RESULT_OK){
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            updateStudentImage.setImageBitmap(bitmap);
        }
    }

}