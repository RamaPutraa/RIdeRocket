package com.example.riderocket;

public class Motor {
    private String namaMotor;
    private String deskripsi;
    private int tahunPembuatan;
    private String transmisi;
    private int hargaSewa;

    public Motor(String namaMotor, String deskripsi, int tahunPembuatan, String transmisi, int hargaSewa) {
        this.namaMotor = namaMotor;
        this.deskripsi = deskripsi;
        this.tahunPembuatan = tahunPembuatan;
        this.transmisi = transmisi;
        this.hargaSewa = hargaSewa;
    }

    public String getNamaMotor() {
        return namaMotor;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public int getTahunPembuatan() {
        return tahunPembuatan;
    }

    public String getTransmisi() {
        return transmisi;
    }

    public int getHargaSewa() {
        return hargaSewa;
    }
}


