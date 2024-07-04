package com.example.riderocket;

public class User {
    private String idUser;
    private String nama;
    private String email;
    private String noTelp;
    private String alamat;
    private String status;
    private String password;

    public User(String idUser, String nama, String email, String noTelp, String alamat, String status, String password) {
        this.idUser = idUser;
        this.nama = nama;
        this.email = email;
        this.noTelp = noTelp;
        this.alamat = alamat;
        this.status = status;
        this.password = password;
    }

    public String getIdUser() {
        return idUser;
    }

    public String getNama() {
        return nama;
    }

    public String getEmail() {
        return email;
    }

    public String getNoTelp() {
        return noTelp;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getStatus() {
        return status;
    }

    public String getPassword() {
        return password;
    }
}