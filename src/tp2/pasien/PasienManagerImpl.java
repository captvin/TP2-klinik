package tp2.pasien;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.stage.StageStyle;

import javafx.collections.ObservableList;
import tp2.connection.DB;

public class PasienManagerImpl implements PasienManager {

    @Override
    public ObservableList<Pasien> getAllPasien() {
        ObservableList<Pasien> daftarPasien = FXCollections.observableArrayList();
        Connection connection = DB.connect();
        String query = "SELECT * FROM pasien";
        try (PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nama = resultSet.getString("nama");
                String alamat = resultSet.getString("alamat");
                String nik = resultSet.getString("NIK");
                LocalDate tanggalLahir = resultSet.getDate("lahir").toLocalDate();
                Pasien pasien = new Pasien(id, nama, alamat, nik, tanggalLahir);
                daftarPasien.add(pasien);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Cannot retrieve data from the database.");

            alert.showAndWait();
        } finally {
            DB.close(connection);
        }
        return daftarPasien;
    }

    @Override
    public void tambahPasien(Pasien pasien) {
        Connection connection = DB.connect();
        String checkQuery = "SELECT * FROM pasien WHERE NIK = ?";
        try (PreparedStatement statement = connection.prepareStatement(checkQuery)) {
            statement.setString(1, pasien.getNik());
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.initStyle(StageStyle.UTILITY);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Data pasien dengan NIK yang sama sudah ada dalam database.");
                    alert.showAndWait();
                    return; 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Kesalahan dalam memeriksa data pasien.");
            alert.showAndWait();
            return;
        }

        String query = "INSERT INTO pasien (nama, alamat, NIK, lahir) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, pasien.getNama());
            statement.setString(2, pasien.getAlamat());
            statement.setString(3, pasien.getNik());
            statement.setDate(4, java.sql.Date.valueOf(pasien.getTanggalLahir()));
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Tangani kesalahan koneksi atau kueri di sini
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Kesalahan dalam memasukkan data pasien.");
            alert.showAndWait();
        } finally {
            DB.close(connection);
        }
    }

    @Override
    public void updatePasien(Pasien pasien) {
        Connection connection = DB.connect();
        String checkQuery = "SELECT * FROM pasien WHERE NIK = ? AND id != ?";
        try (PreparedStatement statement = connection.prepareStatement(checkQuery)){
            statement.setString(1, pasien.getNik());
            statement.setInt(2, pasien.getId());
            try (ResultSet resultSet = statement.executeQuery()){
                if (resultSet.next()){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.initStyle(StageStyle.UTILITY);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Data pasien dengan NIK yang sama sudah ada dalam database.");
                    alert.showAndWait();
                    return;
                }
            }
        }  catch (SQLException e) {
            e.printStackTrace();
            // Tangani kesalahan koneksi atau kueri di sini
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Kesalahan dalam memeriksa data pasien.");
            alert.showAndWait();
            return;
        }

        String query = "UPDATE pasien SET nama=?, alamat=?, NIK=?, lahir=? WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, pasien.getNama());
            statement.setString(2, pasien.getAlamat());
            statement.setString(3, pasien.getNik());
            statement.setDate(4, java.sql.Date.valueOf(pasien.getTanggalLahir()));
            statement.setInt(5, pasien.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Cannot update data pasien");
        } finally {
            DB.close(connection);
        }
    }

    @Override
    public void hapusPasien(int id) {
        Connection connection = DB.connect();
        String query = "DELETE FROM pasien WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            System.out.println(statement.toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initStyle(StageStyle.UTILITY);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Cannot delete data pasien");
        } finally {
            DB.close(connection);
        }
    }
}
