package com.example.riderocket;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class EditProfile extends AppCompatActivity {

    private String userId;
    private EditText etNama, etEmail, etNoTelp, etAlamat;
    private Button btnUpdate, btnHapusAkun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_profile);

        etNama = findViewById(R.id.nama);
        etEmail = findViewById(R.id.email);
        etNoTelp = findViewById(R.id.no_telp);
        etAlamat = findViewById(R.id.alamat);
        btnUpdate = findViewById(R.id.save_button);
        btnHapusAkun = findViewById(R.id.hapus_akun);

        // Load profile data
        userId = getIntent().getStringExtra("id_user");
        loadProfileData(userId);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });

        btnHapusAkun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDeleteAccount();
            }
        });

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                finish();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadProfileData(String userId) {
        String url = Db_connection.urlGetUser + userId;
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("error")) {
                        Toast.makeText(EditProfile.this, jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                    } else {
                        // Isi field form dengan data profil yang diterima
                        etNama.setText(jsonObject.getString("nama"));
                        etEmail.setText(jsonObject.getString("email"));
                        etNoTelp.setText(jsonObject.getString("no_telp"));
                        etAlamat.setText(jsonObject.getString("alamat"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(EditProfile.this, "Terjadi kesalahan saat memuat data", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditProfile.this, "Terjadi kesalahan koneksi", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequest);
    }

    private void updateProfile() {
        // Ambil nilai terbaru dari form
        final String nama = etNama.getText().toString();
        final String email = etEmail.getText().toString();
        final String noTelp = etNoTelp.getText().toString();
        final String alamat = etAlamat.getText().toString();

        // Buat request POST ke API untuk menyimpan perubahan profil
        String url = Db_connection.urlUpdateUser + userId;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("EditProfile", "Response: " + response);
                if (response.trim().equals("Update Berhasil")) {
                    Toast.makeText(EditProfile.this, "Profil berhasil diperbarui", Toast.LENGTH_SHORT).show();
                    // Kembalikan username baru ke HomeActivity
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("new_username", nama);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                } else {
                    Toast.makeText(EditProfile.this, response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("EditProfile", "Error: " + error.toString());
                Toast.makeText(EditProfile.this, "Terjadi kesalahan saat mengupdate data. Silakan coba lagi.", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Mengirimkan parameter POST ke API
                Map<String, String> params = new HashMap<>();
                params.put("nama", nama);
                params.put("email", email);
                params.put("no_telp", noTelp);
                params.put("alamat", alamat);
                params.put("id_user", userId); // ID pengguna untuk identifikasi
                return params;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void confirmDeleteAccount() {
        new AlertDialog.Builder(this)
                .setTitle("Hapus Akun")
                .setMessage("Apakah Anda yakin ingin menghapus akun ini?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteAccount();
                    }
                })
                .setNegativeButton("Tidak", null)
                .show();
    }

    private void deleteAccount() {
        String url = Db_connection.urlDeleteUser + userId;
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("EditProfile", "Response: " + response);
                if (response.isEmpty()) {
                    Toast.makeText(EditProfile.this, response, Toast.LENGTH_SHORT).show();
                } else {
                    // Hapus data sesi pengguna dari SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear(); // Hapus semua data dalam SharedPreferences
                    editor.apply();

                    // Arahkan ke halaman login
                    Intent intent = new Intent(EditProfile.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                    // Tampilkan notifikasi bahwa akun berhasil dihapus
                    Toast.makeText(EditProfile.this, "Akun berhasil dihapus", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("EditProfile", "Error: " + error.toString());
                Toast.makeText(EditProfile.this, "Terjadi kesalahan saat menghapus akun. Silakan coba lagi.", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(stringRequest);
    }

    private void logout() {
        // Clear shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Redirect to login activity
        Intent intent = new Intent(EditProfile.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the activity stack
        startActivity(intent);
        finish();
    }

}
