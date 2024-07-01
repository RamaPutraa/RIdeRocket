package com.example.riderocket;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RiwayatAdapter extends RecyclerView.Adapter<RiwayatAdapter.ViewHolder> {
    private List<Transaksi> data;

    public RiwayatAdapter(List<Transaksi> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.riwayat_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaksi transaksi = data.get(position);
        holder.namaMotor.setText(transaksi.getNamaMotor());
        holder.harga.setText("Rp. " + transaksi.getHargaTotal());
        holder.tanggal.setText(transaksi.getTglSewa() + " - " + transaksi.getTglKembali());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView namaMotor;
        TextView harga;
        TextView tanggal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            namaMotor = itemView.findViewById(R.id.namaMotor);
            harga = itemView.findViewById(R.id.harga);
            tanggal = itemView.findViewById(R.id.tanggal);
        }
    }
}
