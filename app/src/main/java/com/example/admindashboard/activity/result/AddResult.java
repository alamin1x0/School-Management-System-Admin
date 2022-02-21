package com.example.admindashboard.activity.result;

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
import com.example.admindashboard.activity.student.AddStudent;
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

import es.dmoral.toasty.Toasty;

public class AddResult extends AppCompatActivity {

    private ImageView addResultImage;
    private Button addResultBtn;

    private EditText inputresultBangla, inputresultEnglish, inputresultDateofBirth,
            inputresultfahterBangla, inputresultFatherEnglish, inputresultmotherBangla,
            inputreusltmotherEnglish, inputreusltRoll, inputreusltResultGPA;

    private String stBangla, stEnglish, stdateofbirth, fahterBangla,
            fahterEnglish, motherBangla, motherEnglish, division,
            year, roll, gpa, downloadUrl = "";

    private Spinner addClassCategory, addBoardCategory, addYearCategory;
    private ProgressDialog pd;
    private final int REQ = 1;
    private Bitmap bitmap = null;
    private String categoryClass, categoryBoard, categoryYear;

    private StorageReference storageReference;
    private DatabaseReference reference, dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_result);

        inputresultBangla = findViewById(R.id.inputresultBangla);
        inputresultEnglish = findViewById(R.id.inputresultEnglish);
        inputresultDateofBirth = findViewById(R.id.inputresultDateofBirth);
        inputresultfahterBangla = findViewById(R.id.inputresultfahterBangla);
        inputresultFatherEnglish = findViewById(R.id.inputresultFatherEnglish);
        inputresultmotherBangla = findViewById(R.id.inputresultmotherBangla);
        inputreusltmotherEnglish = findViewById(R.id.inputreusltmotherEnglish);
        inputreusltRoll = findViewById(R.id.inputreusltRoll);
        inputreusltResultGPA = findViewById(R.id.inputreusltResultGPA);

        addResultImage = findViewById(R.id.addResultImage);
        addResultBtn = findViewById(R.id.addResultBtn);

        addClassCategory = findViewById(R.id.addClassCategory);
        addBoardCategory = findViewById(R.id.addBoardCategory);
        addYearCategory = findViewById(R.id.addYearCategory);

        pd = new ProgressDialog(this);

        reference = FirebaseDatabase.getInstance().getReference().child("result");
        storageReference = FirebaseStorage.getInstance().getReference();


        //class spinner
        String[] items = new String[]{"ক্লাস নির্বাচন করুন", "ক্লাস ৬", "ক্লাস ৭", "ক্লাস ৮", "ক্লাস ৯", "ক্লাস ১০"};
        addClassCategory.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, items));

        addClassCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryClass = addClassCategory.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Division category
        String[] item = new String[]{"বিভাগ", "রাজশাহী বিভাগ", "রংপুর বিভাগ", "ঢাকা বিভাগ", "খুলনা বিভাগ", "বরিশাল বিভাগ", "সিলেট বিভাগ", "চট্টগ্রাম বিভাগ", "ময়মনসিংহ বিভাগ"};
        addBoardCategory.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, item));

        addBoardCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryBoard = addBoardCategory.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //years spinner
        String[] itemm = new String[]{"সাল", "২০২২", "২০২৪", "২০২৫", "২০২৬", "২০২৭"};
        addYearCategory.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, itemm));

        addYearCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryYear = addYearCategory.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        addResultImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        addResultBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });

    }

    private void openGallery() {
        Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickImage, REQ);
    }

    private void checkValidation() {
        stBangla = inputresultBangla.getText().toString();
        stEnglish = inputresultEnglish.getText().toString();
        stdateofbirth = inputresultDateofBirth.getText().toString();
        fahterBangla = inputresultfahterBangla.getText().toString();
        fahterEnglish = inputresultFatherEnglish.getText().toString();
        motherBangla = inputresultmotherBangla.getText().toString();
        motherEnglish = inputreusltmotherEnglish.getText().toString();


        roll = inputreusltRoll.getText().toString();
        gpa = inputreusltResultGPA.getText().toString();

        if (stBangla.isEmpty()) {
            inputresultBangla.setError("শিক্ষার্থীর নাম (বাংলা)");
            inputresultBangla.requestFocus();
            return;
        } else if (stEnglish.isEmpty()) {
            inputresultEnglish.setError("শিক্ষার্থীর নাম (ইংরেজি)");
            inputresultEnglish.requestFocus();
            return;
        } else if (stdateofbirth.isEmpty()) {
            inputresultDateofBirth.setError("জন্ম তারিখ");
            inputresultDateofBirth.requestFocus();
            return;
        } else if (fahterBangla.isEmpty()) {
            inputresultfahterBangla.setError("পিতার নাম (ইংরেজি)");
            inputresultfahterBangla.requestFocus();
            return;
        } else if (motherBangla.isEmpty()) {
            inputresultmotherBangla.setError("মাতার নাম (বাংলা)");
            inputresultmotherBangla.requestFocus();
            return;
        } else if (motherEnglish.isEmpty()) {
            inputresultFatherEnglish.setError("মাতার নাম (ইংরেজি)");
            inputresultFatherEnglish.requestFocus();
            return;
        } else if (categoryBoard.equals("বিভাগ")) {
            Toasty.error(this, "দয়া করে আপনারা বিভাগ সিলেক্ট করুন", Toasty.LENGTH_SHORT).show();
        } else if (categoryYear.equals("সাল")) {
            Toasty.warning(this, "দয়া করে আপনারা সাল সিলেক্ট করুন", Toasty.LENGTH_SHORT).show();
        } else if (roll.isEmpty()) {
            inputreusltRoll.setError("রোল");
            inputreusltRoll.requestFocus();
            return;
        } else if (gpa.isEmpty()) {
            inputreusltResultGPA.setError("ফলাফল (জিপিএ)");
            inputreusltResultGPA.requestFocus();
            return;
        } else if (categoryClass.equals("ক্লাস নির্বাচন করুন")) {
            Toasty.error(this, "দয়া করে আপনারা সাল সিলেক্ট করুন", Toasty.LENGTH_SHORT).show();
        } else if (bitmap == null) {
            insertData();
        } else {
            uploadImage();
        }
    }

    private void insertData() {

        pd.setMessage("ফলাফল যোগ করা হয়েছে......");
        pd.show();

        dbRef = reference.child(categoryClass);
        final String uniqueKey = dbRef.push().getKey();

        ResultData resultData = new ResultData(stBangla, stEnglish, stdateofbirth, fahterBangla,
                fahterEnglish, motherBangla, motherEnglish, categoryBoard,
                categoryYear, roll, gpa, downloadUrl, uniqueKey);

        dbRef.child(addYearCategory.getSelectedItem().toString()).child(uniqueKey).setValue(resultData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                pd.dismiss();
                Toasty.success(AddResult.this, "ফলাফল যোগ করা হয়েছে", Toasty.LENGTH_SHORT).show();
                startActivity(new Intent(AddResult.this, UploadResult.class));
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toasty.error(AddResult.this, "কিছু ভুল হয়েছে", Toasty.LENGTH_SHORT).show();
            }
        });
    }

    private void uploadImage() {
        pd.setMessage("Uploading......");
        pd.show();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] finalimg = baos.toByteArray();
        final StorageReference filePath;
        filePath = storageReference.child("results").child(finalimg + "jpg");
        final UploadTask uploadTask = filePath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(AddResult.this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
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
                    Toasty.error(AddResult.this, "কিছু ভুল হয়েছে", Toasty.LENGTH_LONG).show();
                }
            }
        });
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
            addResultImage.setImageBitmap(bitmap);
        }
    }
}