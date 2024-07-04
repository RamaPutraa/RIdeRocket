package com.example.riderocket;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MotorAdapterAdmin extends RecyclerView.Adapter<MotorAdapterAdmin.MotorViewHolder> {

    private List<Motor> motorList;

    public MotorAdapterAdmin(List<Motor> motorList) {
        this.motorList = motorList;
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
//        holder.deskripsiMotor.setText(motor.getDeskripsi());
        holder.tahunPembuatanMotor.setText(String.valueOf(motor.getTahunPembuatan()));
        holder.transmisiMotor.setText(motor.getTransmisi());
//        holder.hargaSewaMotor.setText(String.valueOf(motor.getHargaSewa()));
    }

    @Override
    public int getItemCount() {
        return motorList.size();
    }

    public static class MotorViewHolder extends RecyclerView.ViewHolder {

        TextView namaMotor, deskripsiMotor, tahunPembuatanMotor, transmisiMotor, hargaSewaMotor;

        public MotorViewHolder(@NonNull View itemView) {
            super(itemView);
            namaMotor = itemView.findViewById(R.id.nama_motor);
//            deskripsiMotor = itemView.findViewById(R.id.deskripsi_motor);
            tahunPembuatanMotor = itemView.findViewById(R.id.tahun_pembuatan_motor);
            transmisiMotor = itemView.findViewById(R.id.transmisi_motor);
//            hargaSewaMotor = itemView.findViewById(R.id.harga_sewa_motor);
    }
    }
}
