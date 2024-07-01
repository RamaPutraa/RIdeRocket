package com.example.riderocket;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MotorAdapter extends RecyclerView.Adapter<MotorAdapter.MotorViewHolder> {

    private List<Motor> motorList;
    private Context context;

    public MotorAdapter(List<Motor> motorList, Context context) {
        this.motorList = motorList;
        this.context = context;
    }

    @NonNull
    @Override
    public MotorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_motor, parent, false);
        return new MotorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MotorViewHolder holder, int position) {
        Motor motor = motorList.get(position);
        holder.namaMotor.setText(motor.getNamaMotor());
        // Set other fields if necessary

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailMotorActivity.class);
                intent.putExtra("id_motor", motor.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return motorList.size();
    }

    public class MotorViewHolder extends RecyclerView.ViewHolder {
        public TextView namaMotor;
        // Define other views

        public MotorViewHolder(@NonNull View itemView) {
            super(itemView);
            namaMotor = itemView.findViewById(R.id.nama_motor);
            // Initialize other views
        }
    }
}
