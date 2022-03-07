package com.example.admindashboard.activity.pdf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.admindashboard.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import es.dmoral.toasty.Toasty;

public class InvoiceActivity extends AppCompatActivity {

    Button saveAndPrintButton, printbutton;
    EditText editTextName, editTextQty;
    Spinner spinner;
    String[] itemList;
    double[] itemPrice;

    ArrayAdapter<String> adapter;
    long invoiceNo = 0;

    SimpleDateFormat datePatternformat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");

    DecimalFormat decimalFormat = new DecimalFormat("#.##");

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Invoice");

    PdfModel pdfModel = new PdfModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);

        callFindViewById();
        callOnClickLisiner();

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                invoiceNo = snapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void callOnClickLisiner() {
        saveAndPrintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = editTextName.getText().toString();
                String price = editTextQty.getText().toString();

                if (name.isEmpty()) {
                    editTextName.setError("গ্রাহকের নাম");
                    editTextName.requestFocus();
                    Toasty.error(InvoiceActivity.this, "গ্রাহকের নাম", Toasty.LENGTH_SHORT).show();
                    return;
                }
                if (price.isEmpty()) {
                    editTextQty.setError("পরিমান");
                    editTextQty.requestFocus();
                    Toasty.error(InvoiceActivity.this, "পরিমান", Toasty.LENGTH_SHORT).show();
                    return;
                } else {

                }


                pdfModel.invoiceNo = invoiceNo + 1;
                pdfModel.customerName = String.valueOf(editTextName.getText());
                pdfModel.date = new Date().getTime();
                pdfModel.fuelType = spinner.getSelectedItem().toString();
                pdfModel.fuelQty = Double.parseDouble(String.valueOf(editTextQty.getText()));
                pdfModel.amount = Double.valueOf(decimalFormat.format(pdfModel.getFuelQty() * itemPrice[spinner.getSelectedItemPosition()]));

                myRef.child(String.valueOf(invoiceNo + 1)).setValue(pdfModel);

                printPDF();

                Toasty.success(InvoiceActivity.this, "ইনভয়েস সেভ এবং প্রিন্ট সফল হয়েছে", Toasty.LENGTH_SHORT).show();
            }
        });

        printbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InvoiceActivity.this, OldPdfPrintActivity.class);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("NewApi")
    private void printPDF() {

        @SuppressLint({"NewApi", "LocalSuppress"}) PdfDocument mypdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint forLinePaint = new Paint();
        forLinePaint.setColor(Color.rgb(0, 50, 250));
        @SuppressLint({"NewApi", "LocalSuppress"}) PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(250, 350, 1).create();
        @SuppressLint({"NewApi", "LocalSuppress"}) PdfDocument.Page myPage = mypdfDocument.startPage(myPageInfo);
        @SuppressLint({"NewApi", "LocalSuppress"}) Canvas canvas = myPage.getCanvas();

        paint.setTextSize(15.5f);
        paint.setColor(Color.rgb(0, 50, 250));

        canvas.drawText("শ্যামপুর উচ্চ বিদ্যালয়", 20, 20, paint);
        paint.setTextSize(8.5f);
        canvas.drawText("স্থাপন: ১৯৬৪ সাল  ", 20, 40, paint);
        canvas.drawText("পুরানাপইল, জয়পুরহাট সাদর", 20, 55, paint);
        forLinePaint.setStyle(Paint.Style.STROKE);
        forLinePaint.setPathEffect(new DashPathEffect(new float[]{5, 5}, 0));
        forLinePaint.setStrokeWidth(2);
        canvas.drawLine(20, 65, 230, 65, forLinePaint);

        canvas.drawText("শিক্ষার্থীর নাম: " + editTextName.getText(), 20, 80, paint);
        canvas.drawLine(20, 90, 230, 90, forLinePaint);

        canvas.drawText("ক্রয়: ", 20, 105, paint);

        canvas.drawText(spinner.getSelectedItem().toString(), 20, 135, paint);
        canvas.drawText(editTextQty.getText() + "পিচ ", 120, 135, paint);

        double amount = itemPrice[spinner.getSelectedItemPosition()] * Double.parseDouble(editTextQty.getText().toString());
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(String.valueOf(decimalFormat.format(amount)), 230, 135, paint);
        paint.setTextAlign(Paint.Align.LEFT);

        canvas.drawText("+%", 20, 175, paint);
        canvas.drawText("ভ্যাট 5%", 120, 175, paint);
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(decimalFormat.format(amount * 0 / 100), 230, 175, paint);
        paint.setTextAlign(Paint.Align.LEFT);

        canvas.drawLine(20, 210, 230, 210, forLinePaint);

        paint.setTextSize(10f);
        canvas.drawText("মোট ", 120, 225, paint);
        paint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(decimalFormat.format((amount * 5 / 100) + amount), 230, 225, paint);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(8.5f);

        canvas.drawText("তারিখ: " + datePatternformat.format(new Date().getTime()), 20, 260, paint);
        canvas.drawText(String.valueOf(invoiceNo + 1), 20, 275, paint);
        canvas.drawText("পেমেন্ট পদ্ধতি নগদ", 20, 290, paint);


        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(12f);
        canvas.drawText("ধন্যবাদ!", canvas.getWidth() / 2, 320, paint);

        mypdfDocument.finishPage(myPage);
        File file = new File(this.getExternalFilesDir("/"), "শ্যামপুর উচ্চ বিদ্যালয়.pdf");

        try {
            mypdfDocument.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        mypdfDocument.close();


    }

    private void callFindViewById() {
        saveAndPrintButton = findViewById(R.id.btnSaveAndPrint);
        printbutton = findViewById(R.id.btnPrint);
        editTextName = findViewById(R.id.editTextName);
        editTextQty = findViewById(R.id.editTextQty);
        spinner = findViewById(R.id.spinner);

        itemList = new String[]{"ফোন ", "ল্যাপটপ "};
        itemPrice = new double[]{1200.00, 38000.00};
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, itemList);
        spinner.setAdapter(adapter);
    }
}