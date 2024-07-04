package com.example.riderocket;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DaftarSewaAdapter extends RecyclerView.Adapter<DaftarSewaAdapter.DaftarSewaViewHolder> {

    private List<Transaksi> transaksiList;

    public DaftarSewaAdapter(List<Transaksi> transaksiList) {
        this.transaksiList = transaksiList;
    }

    @NonNull
    @Override
    public DaftarSewaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_motor_sewa, parent, false);
        return new DaftarSewaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DaftarSewaViewHolder holder, int position) {
        Transaksi transaksi = transaksiList.get(position);
        holder.tvNamaPenyewa.setText(transaksi.getNamaPenyewa());
        holder.tvNamaMotor.setText(transaksi.getNamaMotor());
//        holder.tvAlamat.setText(transaksi.getAlamat());
//        holder.tvTglSewa.setText(transaksi.getTglSewa());
        holder.tvTglKembali.setText("Tanggal Kembali : " +transaksi.getTglKembali());
//        holder.tvHargaTotal.setText(transaksi.getHargaTotal());
    }

    @Override
    public int getItemCount() {
        return transaksiList.size();
    }

    public static class DaftarSewaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNamaPenyewa, tvNamaMotor, tvAlamat, tvTglSewa, tvTglKembali, tvHargaTotal;

        public DaftarSewaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNamaPenyewa = itemView.findViewById(R.id.tvNamaPenyewa);
            tvNamaMotor = itemView.findViewById(R.id.tvNamaMotor);
//            tvAlamat = itemView.findViewById(R.id.tvAlamat);
//            tvTglSewa = itemView.findViewById(R.id.tvTglSewa);
            tvTglKembali = itemView.findViewById(R.id.tvTglKembali);
//            tvHargaTotal = itemView.findViewById(R.id.tvHargaTotal);
        }
    }
}
