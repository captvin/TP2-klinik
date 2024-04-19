package tp2.pasien;

import java.time.LocalDate;

public class Pasien {
    private int id;
    private String nama;
    private String alamat;
    private String nik;
    private LocalDate tanggalLahir;

    public Pasien(int id, String nama, String alamat, String nik, LocalDate tanggalLahir) {
        this.id = id;
        this.nama = nama;
        this.alamat = alamat;
        this.nik = nik;
        this.tanggalLahir = tanggalLahir;
    }

    public Pasien(String nama, String alamat, String nik, LocalDate tanggalLahir) {
        this.nama = nama;
        this.alamat = alamat;
        this.nik = nik;
        this.tanggalLahir = tanggalLahir;
    }

    // Getter methods
    public String getNama() {
        return nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getNik() {
        return nik;
    }

    public int getId() {
        return id;
    }

    public LocalDate getTanggalLahir() {
        return tanggalLahir;
    }
}
