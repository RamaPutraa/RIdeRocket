package com.example.riderocket;

import android.app.DatePickerDialog;
import android.content.Context;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TransaksiActivity extends AppCompatActivity {

    private EditText tanggalSewa, tanggalKembali, namaMotorField, namaPenyewaField, alamatField, hargaField;
    private Button simpanButton;
    private int hargaSewaPerHari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_transaksi);

        // EditText untuk tanggal
        tanggalSewa = findViewById(R.id.input_tanggal_sewa);
        tanggalKembali = findViewById(R.id.input_tanggal_kembali);
        namaMotorField = findViewById(R.id.input_nama_motor);
        namaPenyewaField = findViewById(R.id.input_nama_penyewa);
        alamatField = findViewById(R.id.input_alamat);
        hargaField = findViewById(R.id.input_harga);
        simpanButton = findViewById(R.id.button_simpan);

        tanggalSewa.setOnClickListener(v -> showDatePickerDialog(tanggalSewa));
        tanggalKembali.setOnClickListener(v -> showDatePickerDialog(tanggalKembali));

        Intent intent = getIntent();
        String namaMotor = intent.getStringExtra("nama_motor");
        hargaSewaPerHari = intent.getIntExtra("harga_sewa_per_hari", 0);

        // Debug atau cek log hargaSewaPerHari untuk memastikan nilainya
        Log.d("DEBUG", "Harga Sewa Per Hari: " + hargaSewaPerHari);

        if (namaMotor != null && hargaSewaPerHari > 0) {
            namaMotorField.setText(namaMotor);
        } else {
            // Handle jika hargaSewaPerHari tidak valid
            Toast.makeText(this, "Harga sewa per hari tidak valid", Toast.LENGTH_SHORT).show();
        }

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        simpanButton.setOnClickListener(v -> saveTransaction());
    }

    private void showDatePickerDialog(final EditText dateField) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, yearSelected, monthSelected, dayOfMonth) -> {
                    // Add +1 to monthSelected because months are zero-based
                    String date = yearSelected + "-" + (monthSelected + 1) + "-" + dayOfMonth;
                    dateField.setText(date);

                    // Update harga total when both dates are selected
                    if (!tanggalSewa.getText().toString().isEmpty() && !tanggalKembali.getText().toString().isEmpty()) {
                        updateHargaTotal();
                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    private void saveTransaction() {
        // Retrieve data from EditText fields
        String namaPenyewa = namaPenyewaField.getText().toString().trim();
        String namaMotor = namaMotorField.getText().toString().trim();
        String alamat = alamatField.getText().toString().trim();
        String tglSewa = tanggalSewa.getText().toString().trim();
        String tglKembali = tanggalKembali.getText().toString().trim();
        String hargaTotal = hargaField.getText().toString().trim();

        // Validate input fields
        if (namaPenyewa.isEmpty() || namaMotor.isEmpty() || alamat.isEmpty() || tglSewa.isEmpty() || tglKembali.isEmpty() || hargaTotal.isEmpty()) {
            Toast.makeText(this, "Semua data harus diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Calculate number of days
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date dateSewa = sdf.parse(tglSewa);
            Date dateKembali = sdf.parse(tglKembali);

            long diff = dateKembali.getTime() - dateSewa.getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000); // convert milliseconds to days

            // Calculate total price
            int hargaTotalInt = (int) (hargaSewaPerHari * diffDays);

            // Update EditText hargaField with total price
            hargaField.setText(String.valueOf(hargaTotalInt));

            // Get id_user from SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("UserSession", Context.MODE_PRIVATE);
            String idUser = sharedPreferences.getString("id_user", null);

            if (idUser == null) {
                Toast.makeText(this, "Gagal mendapatkan id penyewa. Harap login kembali.", Toast.LENGTH_SHORT).show();
                return;
            }

            // URL API for adding transaction
            String url = Db_connection.urlTransaksi;

            // Create request using Volley
            RequestQueue queue = Volley.newRequestQueue(this);
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Handle response from server
                            Toast.makeText(TransaksiActivity.this, "Transaksi berhasil disimpan", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(TransaksiActivity.this, HomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Handle error
                            Toast.makeText(TransaksiActivity.this, "Gagal menyimpan transaksi", Toast.LENGTH_SHORT).show();
                        }
                    }
            ) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("id_penyewa", idUser); // Send id_user as id_penyewa
                    params.put("nama_penyewa", namaPenyewa);
                    params.put("nama_motor", namaMotor);
                    params.put("alamat", alamat);
                    params.put("tgl_sewa", tglSewa);
                    params.put("tgl_kembali", tglKembali);
                    params.put("harga_total", String.valueOf(hargaTotalInt)); // Send total price to server
                    return params;
                }
            };

            // Add request to queue
            queue.add(postRequest);

        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Format tanggal salah", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateHargaTotal() {
        String tglSewaStr = tanggalSewa.getText().toString().trim();
        String tglKembaliStr = tanggalKembali.getText().toString().trim();

        if (tglSewaStr.isEmpty() || tglKembaliStr.isEmpty()) {
            // If either date is empty, exit the function
            return;
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date dateSewa = sdf.parse(tglSewaStr);
            Date dateKembali = sdf.parse(tglKembaliStr);

            long diff = dateKembali.getTime() - dateSewa.getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000); // convert milliseconds to days

            // Calculate total price
            int hargaTotalInt = (int) (hargaSewaPerHari * diffDays);

            // Update EditText hargaField with total price
            hargaField.setText(String.valueOf(hargaTotalInt));

        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, "Format tanggal salah", Toast.LENGTH_SHORT).show();
        }
    }
}
