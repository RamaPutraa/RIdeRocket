package com.example.riderocket;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;


public class HomeActivity extends AppCompatActivity {

    private static final int REQUEST_EDIT_PROFILE = 1;
    private TextView welcomeText;
    private String id_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

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
}
