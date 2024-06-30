package com.example.riderocket;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MotorAdapter extends RecyclerView.Adapter<MotorAdapter.MotorViewHolder> {

    private List<Motor> motorList;

    public MotorAdapter(List<Motor> motorList) {
        this.motorList = motorList;
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
        holder.namaMotorTextView.setText(motor.getNamaMotor());
    }

    @Override
    public int getItemCount() {
        return motorList.size();
    }

    public static class MotorViewHolder extends RecyclerView.ViewHolder {
        TextView namaMotorTextView;

        public MotorViewHolder(View itemView) {
            super(itemView);
            namaMotorTextView = itemView.findViewById(R.id.nama_motor);
        }
    }
}
