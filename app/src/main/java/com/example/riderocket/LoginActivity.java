package com.example.riderocket;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText etNama, etPass;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        etNama = findViewById(R.id.namaEditText);
        etPass = findViewById(R.id.passwordEditText);
        btnLogin = findViewById(R.id.loginButton);

        // cek user sudah login atau belum
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        String savedUsername = sharedPreferences.getString("username", null);
        if (savedUsername != null) {
            String userStatus = sharedPreferences.getString("userStatus", "customer");
            Intent intent;
            if ("admin".equalsIgnoreCase(userStatus)) {
                intent = new Intent(LoginActivity.this, AdminActivity.class);
            } else {
                intent = new Intent(LoginActivity.this, HomeActivity.class);
            }
            intent.putExtra("username", savedUsername);
            intent.putExtra("id_user", sharedPreferences.getString("id_user", null));
            startActivity(intent);
            finish();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String nama = etNama.getText().toString();
                String pass = etPass.getText().toString();

                if (!(nama.isEmpty() || pass.isEmpty())) {
                    String url = Db_connection.urlLogin + "?nama=" + nama + "&password=" + pass;
                    Log.d(TAG, "URL: " + url); // Log URL untuk debugging

                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            Log.d(TAG, "Response: " + s); // Log response untuk debugging
                            try {
                                JSONObject jsonResponse = new JSONObject(s);
                                String status = jsonResponse.getString("status");
                                String message = jsonResponse.getString("message");

                                if (status.equals("success")) {
                                    String userId = jsonResponse.getString("userId");
                                    String email = jsonResponse.getString("email");
                                    String no_telp = jsonResponse.getString("no_telp");
                                    String alamat = jsonResponse.getString("alamat");
                                    String userStatus = jsonResponse.getString("userStatus");

                                    // Simpan username dan id_user ke SharedPreferences
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("username", nama);
                                    editor.putString("id_user", userId);
                                    editor.putString("userStatus", userStatus);
                                    editor.apply();

                                    Toast.makeText(getApplicationContext(), "Login berhasil", Toast.LENGTH_SHORT).show();

                                    Intent intent;
                                    if ("admin".equalsIgnoreCase(userStatus)) {
                                        intent = new Intent(getApplicationContext(), AdminActivity.class);
                                    } else {
                                        intent = new Intent(getApplicationContext(), HomeActivity.class);
                                    }

                                    // kirim data user
                                    intent.putExtra("username", nama);
                                    intent.putExtra("password", pass);
                                    intent.putExtra("id_user", userId);
                                    intent.putExtra("email", email);
                                    intent.putExtra("no_telp", no_telp);
                                    intent.putExtra("alamat", alamat);
                                    intent.putExtra("status", userStatus);
                                    startActivity(intent);
                                    finish(); // Tutup LoginActivity setelah login berhasil
                                } else {
                                    Toast.makeText(getApplicationContext(), "Login gagal: " + message, Toast.LENGTH_SHORT).show(); // Tampilkan respon server
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Log.e(TAG, "Error: " + volleyError.toString()); // Log error untuk debugging
                            Toast.makeText(getApplicationContext(), "Error: " + volleyError.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    requestQueue.add(stringRequest);
                } else {
                    Toast.makeText(getApplicationContext(), "Nama atau Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView buttonDaftar = findViewById(R.id.buttonDaftar);
        buttonDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
