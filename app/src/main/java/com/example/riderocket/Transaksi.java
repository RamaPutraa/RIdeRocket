package com.example.riderocket;

public class Transaksi {
    private String idTransaksi;
    private String idPenyewa;
    private String namaPenyewa;
    private String namaMotor;
    private String alamat;
    private String tglSewa;
    private String tglKembali;
    private String hargaTotal;

    public Transaksi(String idTransaksi, String idPenyewa, String namaPenyewa, String namaMotor, String alamat, String tglSewa, String tglKembali, String hargaTotal) {
        this.idTransaksi = idTransaksi;
        this.idPenyewa = idPenyewa;
        this.namaPenyewa = namaPenyewa;
        this.namaMotor = namaMotor;
        this.alamat = alamat;
        this.tglSewa = tglSewa;
        this.tglKembali = tglKembali;
        this.hargaTotal = hargaTotal;
    }

    public String getIdTransaksi() {
        return idTransaksi;
    }

    public String getIdPenyewa() {
        return idPenyewa;
    }

    public String getNamaPenyewa() {
        return namaPenyewa;
    }

    public String getNamaMotor() {
        return namaMotor;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getTglSewa() {
        return tglSewa;
    }

    public String getTglKembali() {
        return tglKembali;
    }

    public String getHargaTotal() {
        return hargaTotal;
    }
}
