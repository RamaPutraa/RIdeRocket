package com.example.riderocket;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DetailMotorActivity extends AppCompatActivity {

    private static final String TAG = "DetailMotorActivity";

    private ImageView imageMotor;
    private TextView namaMotor;
    private TextView deskripsi;
    private TextView tahunPembuatan;
    private TextView transmisi;
    private TextView hargaSewa;
    private Button buttonSewaSekarang;

    private int idMotor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_motor);

        imageMotor = findViewById(R.id.image_motor);
        namaMotor = findViewById(R.id.nama_motor);
        deskripsi = findViewById(R.id.deskripsi);
        tahunPembuatan = findViewById(R.id.tahun_pembuatan);
        transmisi = findViewById(R.id.transmisi);
        hargaSewa = findViewById(R.id.harga);
        buttonSewaSekarang = findViewById(R.id.sewa_sekarang);

        idMotor = getIntent().getIntExtra("id_motor", -1);

        if (idMotor != -1) {
            new FetchMotorDetailTask().execute();
        } else {
            Log.e(TAG, "Invalid motor id");
        }

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                finish();
            }
        });

        buttonSewaSekarang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(DetailMotorActivity.this, TransaksiActivity.class);
                inten.putExtra("nama_motor", namaMotor.getText().toString());
                inten.putExtra("harga_sewa_per_hari", Integer.parseInt(hargaSewa.getText().toString().replace(" Rp. ", "").replace(",00", "")));
                startActivity(inten);
            }
        });

    }


    private class FetchMotorDetailTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String urlApi = Db_connection.urlGetSpecMotor+idMotor;

            try {
                URL url = new URL(urlApi);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                return response.toString();

            } catch (Exception e) {
                Log.e(TAG, "Error fetching data: " + e.getMessage());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String namaMotorValue = jsonObject.getString("nama_motor");
                    String deskripsiValue = jsonObject.getString("deskripsi");
                    int tahunPembuatanValue = jsonObject.getInt("tahun_pembuatan");
                    String transmisiValue = jsonObject.getString("transmisi");
                    int hargaSewaValue = jsonObject.getInt("harga_sewa");

                    namaMotor.setText(namaMotorValue);
                    deskripsi.setText(deskripsiValue);
                    tahunPembuatan.setText("Tahun Pembuatan: " + tahunPembuatanValue);
                    transmisi.setText("Transmisi: " + transmisiValue);
                    hargaSewa.setText(" Rp. " + hargaSewaValue +",00");

                    // Set image using appropriate method (e.g., Picasso, Glide)

                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing JSON: " + e.getMessage());
                }
            } else {
                Log.e(TAG, "Failed to get data from API.");
            }
        }
    }
}