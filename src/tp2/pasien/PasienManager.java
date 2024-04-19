package tp2.pasien;

import javafx.collections.ObservableList;

public interface PasienManager {
    ObservableList<Pasien> getAllPasien();
    void tambahPasien(Pasien pasien);
    void updatePasien(Pasien pasien);
    void hapusPasien(int id);
}
