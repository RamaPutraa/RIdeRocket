package com.example.riderocket;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class RiwayatActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RiwayatAdapter adapter;
    private List<Transaksi> transaksiList;
    private int idPenyewa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat);

        // Mendapatkan id_penyewa dari intent
        Intent intent = getIntent();
        idPenyewa = intent.getIntExtra("id_penyewa", -1); // Default -1 jika tidak ada data

        recyclerView = findViewById(R.id.recyclerView);
        transaksiList = new ArrayList<>();
        adapter = new RiwayatAdapter(transaksiList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fetchTransaksiData();

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                finish();
            }
        });
    }

    private void fetchTransaksiData() {
        String url = Db_connection.urlGetRiwayat + idPenyewa; // URL API sesuai dengan kebutuhan Anda

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject obj = response.getJSONObject(i);
                            Transaksi transaksi = new Transaksi(
                                    obj.getString("id_transaksi"),
                                    obj.getString("id_penyewa"),
                                    obj.getString("nama_penyewa"),
                                    obj.getString("nama_motor"),
                                    obj.getString("alamat"),
                                    obj.getString("tgl_sewa"),
                                    obj.getString("tgl_kembali"),
                                    obj.getString("harga_total")
                            );
                            transaksiList.add(transaksi);
                        }
                        adapter.notifyDataSetChanged(); // Memperbarui RecyclerView setelah data ditambahkan
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    // Handle error
                    Toast.makeText(RiwayatActivity.this, "Belum ada transaksi ", Toast.LENGTH_SHORT).show();
                });

        // Menambahkan request ke queue Volley
        Volley.newRequestQueue(this).add(request);
    }
}
