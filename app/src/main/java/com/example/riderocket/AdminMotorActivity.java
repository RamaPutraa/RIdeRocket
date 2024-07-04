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

public class AdminMotorActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MotorAdapterAdmin motorAdapter;
    private List<Motor> motorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_motor);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        motorList = new ArrayList<>();
        motorAdapter = new MotorAdapterAdmin(motorList);
        recyclerView.setAdapter(motorAdapter);

        fetchMotors();

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());
    }

    private void fetchMotors() {
        String url = Db_connection.urlGetMotor; // Ganti dengan URL API Anda

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        motorList.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                int id = jsonObject.getInt("id_motor");
                                String namaMotor = jsonObject.getString("nama_motor");
                                String deskripsi = jsonObject.getString("deskripsi");
                                int tahunPembuatan = jsonObject.getInt("tahun_pembuatan");
                                String transmisi = jsonObject.getString("transmisi");
                                int hargaSewa = jsonObject.getInt("harga_sewa");
                                motorList.add(new Motor(id, namaMotor, deskripsi, tahunPembuatan, transmisi, hargaSewa));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        motorAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AdminMotorActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }
}
