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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminUserActivity extends AppCompatActivity {
    private RecyclerView recyclerViewUsers;
    private UserAdapter userAdapter;
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user);

        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));

        userList = new ArrayList<>();
        userAdapter = new UserAdapter(userList, new UserAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(User user) {
                showEditDialog(user);
            }

            @Override
            public void onDeleteClick(User user) {
                showDeleteConfirmationDialog(user);
            }
        });
        recyclerViewUsers.setAdapter(userAdapter);

        fetchUsers();

        ImageButton addButton = findViewById(R.id.tambah);
        addButton.setOnClickListener(v -> showAddUserDialog());

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());
    }

    private void fetchUsers() {
        String url = Db_connection.urlGetAllUser; // Ganti dengan URL API Anda

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        userList.clear();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String idUser = jsonObject.getString("id_user");
                                String nama = jsonObject.getString("nama");
                                String email = jsonObject.getString("email");
                                String noTelp = jsonObject.getString("no_telp");
                                String alamat = jsonObject.getString("alamat");
                                String status = jsonObject.getString("status");
                                String password = jsonObject.getString("password");
                                userList.add(new User(idUser, nama, email, noTelp, alamat, status, password));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        userAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AdminUserActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    private void showEditDialog(User user) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.edit_user_dialog);

        EditText etNama = dialog.findViewById(R.id.etNama);
        EditText etEmail = dialog.findViewById(R.id.etEmail);
        EditText etNoTelp = dialog.findViewById(R.id.etNoTelp);
        EditText etPassword = dialog.findViewById(R.id.etPassword);
        EditText etStatus = dialog.findViewById(R.id.etStatus);
        EditText etAlamat = dialog.findViewById(R.id.etAlamat);

        Button btnUpdate = dialog.findViewById(R.id.btnUpdate);

        etNama.setText(user.getNama());
        etEmail.setText(user.getEmail());
        etNoTelp.setText(user.getNoTelp());
        etPassword.setText(user.getPassword());
        etStatus.setText(user.getStatus());
        etAlamat.setText(user.getAlamat());

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String updatedNama = etNama.getText().toString();
                String updatedEmail = etEmail.getText().toString();
                String updatedNoTelp = etNoTelp.getText().toString();
                String updatedPassword = etPassword.getText().toString();
                String updatedStatus = etStatus.getText().toString();
                String updatedAlamat = etAlamat.getText().toString();

                updateUser(user.getIdUser(), updatedNama, updatedEmail, updatedNoTelp, updatedPassword, updatedStatus, updatedAlamat);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void updateUser(String idUser, String nama, String email, String noTelp, String password, String status, String alamat) {
        String url = Db_connection.urlAdminUpdateUser + idUser;

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("API Response", response);
                        Toast.makeText(AdminUserActivity.this, "User updated successfully", Toast.LENGTH_SHORT).show();
                        fetchUsers(); // Refresh the user list
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("API Error", error.toString());
                        Toast.makeText(AdminUserActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nama", nama);
                params.put("email", email);
                params.put("no_telp", noTelp);
                params.put("password", password);
                params.put("status", status);
                params.put("alamat", alamat);

                Log.d("API Params", params.toString());

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showDeleteConfirmationDialog(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Hapus User");
        builder.setMessage("Apakah Anda yakin ingin menghapus user ini?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteUser(user.getIdUser());
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteUser(String idUser) {
        String url = Db_connection.urlDeleteUser + idUser;

        StringRequest stringRequest = new StringRequest(
                Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("API Response", response);
                        Toast.makeText(AdminUserActivity.this, "User deleted successfully", Toast.LENGTH_SHORT).show();
                        fetchUsers(); // Refresh the user list
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("API Error", error.toString());
                        Toast.makeText(AdminUserActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showAddUserDialog() {
        // Buat dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Inflate layout custom untuk dialog
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.tambah_user_dialog, null);
        builder.setView(dialogView);

        // Dapatkan referensi ke view di dialog
        EditText etNama = dialogView.findViewById(R.id.etNama);
        EditText etEmail = dialogView.findViewById(R.id.etEmail);
        EditText etNoTelp = dialogView.findViewById(R.id.etNoTelp);
        EditText etPassword = dialogView.findViewById(R.id.etPassword);
        EditText etAlamat = dialogView.findViewById(R.id.etAlamat);
        EditText etStatus = dialogView.findViewById(R.id.etStatus);
        Button btnSimpan = dialogView.findViewById(R.id.btnSimpan);

        // Buat dan tampilkan dialog
        AlertDialog dialog = builder.create();
        dialog.show();

        // Tombol simpan di dialog
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ambil data dari EditText
                String nama = etNama.getText().toString();
                String email = etEmail.getText().toString();
                String noTelp = etNoTelp.getText().toString();
                String password = etPassword.getText().toString();
                String alamat = etAlamat.getText().toString();
                String status = etStatus.getText().toString();

                // Validasi input
                if (!nama.isEmpty() && !email.isEmpty() && !noTelp.isEmpty() && !password.isEmpty() && !alamat.isEmpty() && !status.isEmpty()) {
                    // Panggil metode untuk menyimpan data
                    addUser(nama, email, noTelp, password, alamat, status);
                    dialog.dismiss(); // Tutup dialog setelah menyimpan
                } else {
                    Toast.makeText(AdminUserActivity.this, "Semua data harus diisi lengkap!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addUser(String nama, String email, String noTelp, String password, String alamat, String status) {
        String url = Db_connection.urlRegister; // Ganti dengan URL API Anda

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(AdminUserActivity.this, "User berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                        fetchUsers(); // Refresh list user setelah penambahan
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AdminUserActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nama", nama);
                params.put("email", email);
                params.put("no_telp", noTelp);
                params.put("password", password);
                params.put("alamat", alamat);
                params.put("status", status);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
