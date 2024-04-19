package tp2;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.control.DatePicker;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.time.LocalDate;

import tp2.pasien.*;

public class EditForm {

    private PasienManager pasienManager;
    private Pasien pasienToEdit;
    private Pasien edited;

    public EditForm(PasienManager pasienManager, Pasien pasienToEdit) {
        this.pasienManager = pasienManager;
        this.pasienToEdit = pasienToEdit;
        // System.out.println(pasienManager.getData().indexOf(pasienToEdit));
    }

    public void display() {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Edit Pasien");

        // Form elements
        Label nameLabel = new Label("Nama:");
        Label alamatLabel = new Label("Alamat:");
        Label nikLabel = new Label("NIK:");
        Label tanggalLahirLabel = new Label("Tanggal Lahir:");

        TextField namaField = new TextField(pasienToEdit.getNama());
        TextField alamatField = new TextField(pasienToEdit.getAlamat());
        TextField nikField = new TextField(pasienToEdit.getNik());
        DatePicker tanggalLahirPicker = new DatePicker(pasienToEdit.getTanggalLahir());

        nikField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                nikField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });

        Button submitButton = new Button("Submit");

        submitButton.disableProperty().bind(
                namaField.textProperty().isEmpty()
                        .or(alamatField.textProperty().isEmpty())
                        .or(nikField.textProperty().isEmpty())
                        .or(tanggalLahirPicker.valueProperty().isNull()));

        submitButton.setOnAction(e -> {
            LocalDate tanggalLahir = tanggalLahirPicker.getValue();

            this.edited = new Pasien(pasienToEdit.getId(), namaField.getText(), alamatField.getText(),
                    nikField.getText(), tanggalLahir);
            pasienManager.updatePasien(edited);
            stage.close();
        });

        // Form layout
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.addRow(0, nameLabel, namaField);
        gridPane.addRow(1, alamatLabel, alamatField);
        gridPane.addRow(2, nikLabel, nikField);
        gridPane.addRow(3, tanggalLahirLabel, tanggalLahirPicker);
        gridPane.addRow(4, submitButton);

        // Scene
        Scene scene = new Scene(gridPane, 300, 200);
        stage.setScene(scene);
        stage.showAndWait();
    }
}
