package com.example.riderocket;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity {

    private static final int REQUEST_EDIT_PROFILE = 1;
    private TextView welcomeText;
    private String id_user;
    private RecyclerView recyclerView;
    private MotorAdapter motorAdapter;
    private List<Motor> motorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        motorList = new ArrayList<>();
        motorAdapter = new MotorAdapter(motorList);
        recyclerView.setAdapter(motorAdapter);

        new FetchMotorsTask().execute();

        String username = getIntent().getStringExtra("username");
        id_user = getIntent().getStringExtra("id_user");

        welcomeText = findViewById(R.id.welcome);
        welcomeText.setText("Hallo " + username + ", Selamat Datang!");

        ImageView buttonEdit = findViewById(R.id.edit_icon);
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, EditProfile.class);
                intent.putExtra("id_user", id_user);
                startActivityForResult(intent, REQUEST_EDIT_PROFILE);
            }
        });

        Button buttonLogout = findViewById(R.id.button_logout);
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogoutConfirmationDialog();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EDIT_PROFILE && resultCode == RESULT_OK) {
            if (data != null && data.hasExtra("new_username")) {
                String newUsername = data.getStringExtra("new_username");
                welcomeText.setText("Hallo " + newUsername + ", Selamat Datang!");
            }
        }
    }

    private void showLogoutConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi Logout")
                .setMessage("Apakah Anda yakin ingin logout?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                })
                .setNegativeButton("Tidak", null)
                .show();
    }

    private void logout() {
        // Clear shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Redirect to login activity
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear the activity stack
        startActivity(intent);
        finish();
    }

    private class FetchMotorsTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String urlApi = Db_connection.urlGetMotor;

            try {
                URL url = new URL(urlApi);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                // Baca response dari server
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                return response.toString();

            } catch (Exception e) {
                Log.e("FetchMotorsTask", "Error fetching data: " + e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String namaMotor = jsonObject.getString("nama_motor");
                        String deskripsi = jsonObject.getString("deskripsi");
                        int tahunPembuatan = jsonObject.getInt("tahun_pembuatan");
                        String transmisi = jsonObject.getString("transmisi");
                        int hargaSewa = jsonObject.getInt("harga_sewa");

                        Motor motor = new Motor(namaMotor, deskripsi, tahunPembuatan, transmisi, hargaSewa);
                        motorList.add(motor);
                    }

                    // Refresh RecyclerView
                    motorAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    Log.e("FetchMotorsTask", "Error parsing JSON: " + e.getMessage());
                }
            } else {
                Log.e("FetchMotorsTask", "Failed to get data from API.");
            }
        }
    }
}
