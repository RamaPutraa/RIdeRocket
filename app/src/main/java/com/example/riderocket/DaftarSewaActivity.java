package com.example.riderocket;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class DaftarSewaActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DaftarSewaAdapter daftarSewaAdapter;
    private List<Transaksi> transaksiList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_sewa);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        transaksiList = new ArrayList<>();
        daftarSewaAdapter = new DaftarSewaAdapter(transaksiList);
        recyclerView.setAdapter(daftarSewaAdapter);

        fetchTransaksiSewa();

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());
    }

    private void fetchTransaksiSewa() {
        String url = Db_connection.urlGetAllTransaksi; // Ganti dengan URL API Anda untuk mengambil data transaksi sewa motor

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
            Request.Method.GET, url, null,
            new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    transaksiList.clear();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            String idTransaksi = jsonObject.getString("id_transaksi");
                            String idPenyewa = jsonObject.getString("id_penyewa");
                            String namaPenyewa = jsonObject.getString("nama_penyewa");
                            String namaMotor = jsonObject.getString("nama_motor");
                            String alamat = jsonObject.getString("alamat");
                            String tglSewa = jsonObject.getString("tgl_sewa");
                            String tglKembali = jsonObject.getString("tgl_kembali");
                            String hargaTotal = jsonObject.getString("harga_total");

                            transaksiList.add(new Transaksi(idTransaksi, idPenyewa, namaPenyewa, namaMotor, alamat, tglSewa, tglKembali, hargaTotal));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    daftarSewaAdapter.notifyDataSetChanged();
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(DaftarSewaActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
}
