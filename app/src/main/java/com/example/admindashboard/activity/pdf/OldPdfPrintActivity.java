package com.example.admindashboard.activity.pdf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
import java.util.ArrayList;
import java.util.Date;

import ir.androidexception.datatable.DataTable;
import ir.androidexception.datatable.model.DataTableHeader;
import ir.androidexception.datatable.model.DataTableRow;

public class OldPdfPrintActivity extends AppCompatActivity {

    Button oldPrintBtn;
    EditText oldPrintEditText;
    DataTable dataTable;
    DataTableHeader header;
    SimpleDateFormat datePattenFormat = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat timePatternFormat = new SimpleDateFormat("hh:mm a");

    DecimalFormat decimalFormat = new DecimalFormat("#.##");

    ArrayList<DataTableRow> rows = new ArrayList<>();
    long invoiceNo;
    String customerName;
    long databaseDate;
    String fuelType;
    double fuelQty;
    double amount;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child("Invoice");
    DatabaseReference retriveRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_old_pdf_print);

        dataTable = findViewById(R.id.data_table);
        oldPrintBtn = findViewById(R.id.oldPrintBtn);
        oldPrintEditText = findViewById(R.id.oldPrintEditText);

        header = new DataTableHeader.Builder()
                .item("Invoice No.", 5)
                .item("Customer name", 5)
                .item("Date", 5)
                .item("Time", 5)
                .item("Amount", 5)
                .build();
        loadTable();
        listeners();
    }

    private void listeners() {
        oldPrintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printPdf(oldPrintEditText.getText().toString());
            }
        });
    }

    private void printPdf(String invoice) {
        retriveRef = FirebaseDatabase.getInstance().getReference().child("Invoice").child(invoice);
        retriveRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                invoiceNo = (long) snapshot.child("invoiceNo").getValue();
                customerName = (String) snapshot.child("customerName").getValue();
                databaseDate = (long) snapshot.child("date").getValue();
                fuelType = String.valueOf(snapshot.child("fuelType").getValue());
                fuelQty = Double.parseDouble(String.valueOf(snapshot.child("fuelQty").getValue()));
                amount = Double.parseDouble(String.valueOf(snapshot.child("amount").getValue()));


                @SuppressLint({"NewApi", "LocalSuppress"}) PdfDocument mypdfDocument = new PdfDocument();
                Paint paint = new Paint();
                Paint forLinePaint = new Paint();
                forLinePaint.setColor(Color.rgb(0, 50, 250));
                @SuppressLint({"NewApi", "LocalSuppress"}) PdfDocument.PageInfo myPageInfo = new PdfDocument.PageInfo.Builder(250, 350, 1).create();
                @SuppressLint({"NewApi", "LocalSuppress"}) PdfDocument.Page myPage = mypdfDocument.startPage(myPageInfo);
                @SuppressLint({"NewApi", "LocalSuppress"}) Canvas canvas = myPage.getCanvas();

                paint.setTextSize(15.5f);
                paint.setColor(Color.rgb(0, 50, 250));

                canvas.drawText("Developer Al-Amin", 20, 20, paint);
                paint.setTextSize(8.5f);
                canvas.drawText("MR. No. 2, Joypurhat", 20, 40, paint);
                canvas.drawText("Sakib 901253", 20, 55, paint);
                forLinePaint.setStyle(Paint.Style.STROKE);
                forLinePaint.setPathEffect(new DashPathEffect(new float[]{5, 5}, 0));
                forLinePaint.setStrokeWidth(2);
                canvas.drawLine(20, 65, 230, 65, forLinePaint);

                canvas.drawText("Customer Name: " + customerName, 20, 80, paint);
                canvas.drawLine(20, 90, 230, 90, forLinePaint);

                canvas.drawText("Purchase: ", 20, 105, paint);

                canvas.drawText(fuelType, 20, 135, paint);
                canvas.drawText(fuelQty + " Pice", 120, 135, paint);

                paint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(String.valueOf(decimalFormat.format(amount)), 230, 135, paint);
                paint.setTextAlign(Paint.Align.LEFT);

                canvas.drawText("+%", 20, 175, paint);
                canvas.drawText("Tax 5%", 120, 175, paint);
                paint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(decimalFormat.format(amount * 0 / 100), 230, 175, paint);
                paint.setTextAlign(Paint.Align.LEFT);

                canvas.drawLine(20, 210, 230, 210, forLinePaint);

                paint.setTextSize(10f);
                canvas.drawText("Total", 120, 225, paint);
                paint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(decimalFormat.format((amount * 5 / 100) + amount), 230, 225, paint);
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setTextSize(8.5f);

                canvas.drawText("Date: " + datePattenFormat.format(new Date(databaseDate).getTime()), 20, 260, paint);
                canvas.drawText(String.valueOf(invoiceNo), 20, 275, paint);
                canvas.drawText("Payment Method Cash", 20, 290, paint);


                paint.setTextAlign(Paint.Align.CENTER);
                paint.setTextSize(12f);
                canvas.drawText("Thank you!", canvas.getWidth() / 2, 320, paint);

                mypdfDocument.finishPage(myPage);
                File file = new File(OldPdfPrintActivity.this.getExternalFilesDir("/"), invoiceNo + "Developer alamin.pdf");

                try {
                    mypdfDocument.writeTo(new FileOutputStream(file));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mypdfDocument.close();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadTable() {

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot myDataSnapshot : snapshot.getChildren()) {
                    DataTableRow row = new DataTableRow.Builder()
                            .value(String.valueOf(myDataSnapshot.child("invoiceNo").getValue()))
                            .value(String.valueOf(myDataSnapshot.child("customerName").getValue()))
                            .value(datePattenFormat.format(myDataSnapshot.child("date").getValue()))
                            .value(timePatternFormat.format(myDataSnapshot.child("date").getValue()))
                            .value(String.valueOf(myDataSnapshot.child("amount").getValue()))
                            .build();

                    rows.add(row);
                }
                dataTable.setHeader(header);
                dataTable.setRows(rows);
                dataTable.inflate(OldPdfPrintActivity.this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}