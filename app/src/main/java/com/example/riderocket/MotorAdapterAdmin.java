package com.example.riderocket;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MotorAdapterAdmin extends RecyclerView.Adapter<MotorAdapterAdmin.MotorViewHolder> {

    private List<Motor> motorList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(Motor motor);
        void onDeleteClick(Motor motor);
    }

    public MotorAdapterAdmin(List<Motor> motorList, OnItemClickListener listener) {
        this.motorList = motorList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MotorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_motor_admin, parent, false);
        return new MotorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MotorViewHolder holder, int position) {
        Motor motor = motorList.get(position);
        holder.namaMotor.setText(motor.getNamaMotor());
        holder.tahunPembuatanMotor.setText(String.valueOf(motor.getTahunPembuatan()));
        holder.transmisiMotor.setText(motor.getTransmisi());

        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(motor));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(motor));
    }

    @Override
    public int getItemCount() {
        return motorList.size();
    }

    public static class MotorViewHolder extends RecyclerView.ViewHolder {
        TextView namaMotor, tahunPembuatanMotor, transmisiMotor;
        ImageView btnEdit, btnDelete;

        public MotorViewHolder(@NonNull View itemView) {
            super(itemView);
            namaMotor = itemView.findViewById(R.id.nama_motor);
            tahunPembuatanMotor = itemView.findViewById(R.id.tahun_pembuatan_motor);
            transmisiMotor = itemView.findViewById(R.id.transmisi_motor);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
