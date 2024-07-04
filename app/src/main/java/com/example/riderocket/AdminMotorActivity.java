package com.example.riderocket;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        motorAdapter = new MotorAdapterAdmin(motorList, new MotorAdapterAdmin.OnItemClickListener() {
            @Override
            public void onEditClick(Motor motor) {
                showEditDialog(motor);
            }

            @Override
            public void onDeleteClick(Motor motor) {
                showDeleteConfirmationDialog(motor);
            }
        });
        recyclerView.setAdapter(motorAdapter);

        fetchMotors();

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        ImageButton addButton = findViewById(R.id.tambah);
        addButton.setOnClickListener(v -> showAddMotorDialog());
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

    private void showEditDialog(Motor motor) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.edit_motor_dialog);

        EditText etNamaMotor = dialog.findViewById(R.id.etNamaMotor);
        EditText etDeskripsi = dialog.findViewById(R.id.etDeskripsi);
        EditText etTahunPembuatan = dialog.findViewById(R.id.etTahunPembuatan);
        EditText etTransmisi = dialog.findViewById(R.id.etTransmisi);
        EditText etHargaSewa = dialog.findViewById(R.id.etHargaSewa);
        Button btnUpdateMotor = dialog.findViewById(R.id.btnUpdateMotor);

        etNamaMotor.setText(motor.getNamaMotor());
        etDeskripsi.setText(motor.getDeskripsi());
        etTahunPembuatan.setText(String.valueOf(motor.getTahunPembuatan()));
        etTransmisi.setText(motor.getTransmisi());
        etHargaSewa.setText(String.valueOf(motor.getHargaSewa()));

        btnUpdateMotor.setOnClickListener(v -> {
            String updatedNamaMotor = etNamaMotor.getText().toString();
            String updatedDeskripsi = etDeskripsi.getText().toString();
            int updatedTahunPembuatan = Integer.parseInt(etTahunPembuatan.getText().toString());
            String updatedTransmisi = etTransmisi.getText().toString();
            int updatedHargaSewa = Integer.parseInt(etHargaSewa.getText().toString());

            updateMotor(motor.getId(), updatedNamaMotor, updatedDeskripsi, updatedTahunPembuatan, updatedTransmisi, updatedHargaSewa);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void showDeleteConfirmationDialog(Motor motor) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Hapus Motor");
        builder.setMessage("Apakah Anda yakin ingin menghapus motor ini?");
        builder.setPositiveButton("Ya", (dialog, which) -> {
            deleteMotor(motor.getId());
        });
        builder.setNegativeButton("Tidak", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void updateMotor(int id, String namaMotor, String deskripsi, int tahunPembuatan, String transmisi, int hargaSewa) {
        Log.d("AdminMotorActivity", "Mengirim permintaan update untuk motor dengan ID: " + id);

        String url = Db_connection.urlGetSetMotor + id;

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("AdminMotorActivity", "Respon dari server: " + response);
                        Toast.makeText(AdminMotorActivity.this, "Motor berhasil diperbarui", Toast.LENGTH_SHORT).show();
                        fetchMotors();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("AdminMotorActivity", "Error saat update motor: " + error.getMessage());
                        Toast.makeText(AdminMotorActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_motor", String.valueOf(id));
                params.put("nama_motor", namaMotor);
                params.put("deskripsi", deskripsi);
                params.put("tahun_pembuatan", String.valueOf(tahunPembuatan));
                params.put("transmisi", transmisi);
                params.put("harga_sewa", String.valueOf(hargaSewa));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void deleteMotor(int id) {
        String url = Db_connection.urlDeleteMotor + id; // Sesuaikan dengan URL API Anda

        StringRequest stringRequest = new StringRequest(
                Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(AdminMotorActivity.this, "Motor berhasil dihapus", Toast.LENGTH_SHORT).show();
                        fetchMotors();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AdminMotorActivity.this, "Error deleting motor: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("Volley Delete Error", "Error deleting motor", error);
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void addMotor(String namaMotor, String deskripsi, String tahunPembuatan, String transmisi, String hargaSewa) {
        String url = Db_connection.urlTambahMotor; // Ganti dengan URL API Anda

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(AdminMotorActivity.this, "Motor berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                        fetchMotors(); // Refresh list motor setelah penambahan
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AdminMotorActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nama_motor", namaMotor);
                params.put("deskripsi", deskripsi);
                params.put("tahun_pembuatan", tahunPembuatan);
                params.put("transmisi", transmisi);
                params.put("harga_sewa", hargaSewa);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        }

    private void showAddMotorDialog() {
        // Buat dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Inflate layout custom untuk dialog
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.tambah_motor_dialog, null);
        builder.setView(dialogView);

        // Dapatkan referensi ke view di dialog
        EditText etNamaMotor = dialogView.findViewById(R.id.etNamaMotor);
        EditText etDeskripsi = dialogView.findViewById(R.id.etDeskripsi);
        EditText etTahunPembuatan = dialogView.findViewById(R.id.etTahunPembuatan);
        EditText etTransmisi = dialogView.findViewById(R.id.etTransmisi);
        EditText etHargaSewa = dialogView.findViewById(R.id.etHargaSewa);
        Button btnSimpan = dialogView.findViewById(R.id.btnSimpan);

        // Buat dan tampilkan dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        // Tombol simpan di dialog
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ambil data dari EditText
                String namaMotor = etNamaMotor.getText().toString();
                String deskripsi = etDeskripsi.getText().toString();
                String tahunPembuatan = etTahunPembuatan.getText().toString();
                String transmisi = etTransmisi.getText().toString();
                String hargaSewa = etHargaSewa.getText().toString();

                // Validasi input
                if (!namaMotor.isEmpty() && !deskripsi.isEmpty() && !tahunPembuatan.isEmpty() && !transmisi.isEmpty() && !hargaSewa.isEmpty()) {
                    // Panggil metode untuk menyimpan data
                    addMotor(namaMotor, deskripsi, tahunPembuatan, transmisi, hargaSewa);
                    dialog.dismiss(); // Tutup dialog setelah menyimpan
                } else {
                    Toast.makeText(AdminMotorActivity.this, "Semua data harus diisi lengkap!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
