package com.example.admindashboard.student;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.admindashboard.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewAdapter> {
    private List<StudentData> list;
    private Context context;
    private String category;

    public StudentAdapter(List<StudentData> list, Context context, String category) {
        this.list = list;
        this.context = context;
        this.category = category;

    }

    //StudentViewAdapter

    @NonNull
    @Override
    public StudentViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.stuent_item_layout, parent, false);
        return new StudentViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewAdapter holder, int position) {

        StudentData item = list.get(position);
        holder.name.setText(item.getName());
        holder.phone.setText(item.getPhone());
        holder.address.setText(item.getAddress());

        try {
            Picasso.get().load(item.getImage()).into(holder.imageView);
        }catch (Exception e){
            e.printStackTrace();
        }



        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateStudent.class);
                intent.putExtra("name",item.getName());
                intent.putExtra("phone",item.getPhone());
                intent.putExtra("address",item.getAddress());
                intent.putExtra("image",item.getImage());
                intent.putExtra("key",item.getKey());
                intent.putExtra("category", category);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class StudentViewAdapter extends RecyclerView.ViewHolder {

        private TextView name, phone, address;
        private Button update;
        private ImageView imageView;

        public StudentViewAdapter(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.studentName);
            phone = itemView.findViewById(R.id.studentPhone);
            address = itemView.findViewById(R.id.studentAddress);
            imageView = itemView.findViewById(R.id.studentImage);
            update = itemView.findViewById(R.id.studentUpdate);
        }
    }
}
