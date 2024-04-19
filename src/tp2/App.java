package tp2;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.geometry.*;
import javafx.stage.Stage;
import tp2.pasien.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class App extends Application {
    
    private final static int rowsPerPage = 10;

    private PasienManager pasienManager = new PasienManagerImpl();
    private final TableView<Pasien> table = createTable();
    private ObservableList<Pasien> data = pasienManager.getAllPasien();
    private Pagination pagination = new Pagination((data.size() / rowsPerPage + 1), 0);


    @SuppressWarnings("unchecked")
    private TableView<Pasien> createTable() {

        TableView<Pasien> table = new TableView<>();

        // Table columns
        TableColumn<Pasien, String> namaCol = new TableColumn<>("Nama");
        namaCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNama()));

        TableColumn<Pasien, String> alamatCol = new TableColumn<>("Alamat");
        alamatCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAlamat()));

        TableColumn<Pasien, String> nikCol = new TableColumn<>("NIK");
        nikCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNik()));

        TableColumn<Pasien, String> tanggalLahirCol = new TableColumn<>("Tanggal Lahir");
        tanggalLahirCol.setCellValueFactory(cellData -> {
            LocalDate tanggalLahir = cellData.getValue().getTanggalLahir();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = tanggalLahir.format(formatter);
            return new SimpleStringProperty(formattedDate);
        });

        TableColumn<Pasien, String> aksiCol = new TableColumn<>("Aksi");
        aksiCol.setCellFactory(param -> new TableCell<Pasien, String>() {
            private final Button deleteButton = new Button("Hapus");
            private final Button editButton = new Button("Edit");

            {
                deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                deleteButton.setOnAction(event -> {
                    Pasien pasien = getTableView().getItems().get(getIndex());
                    pasienManager.hapusPasien(pasien.getId());
                    refreshTable();
                    // handleDataChange();
                });

                editButton.setStyle("-fx-background-color: blue; -fx-text-fill: white;");
                editButton.setOnAction(event -> {
                    Pasien pasien = getTableView().getItems().get(getIndex());
                    showEditForm(pasien);
                    
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox buttons = new HBox(deleteButton, editButton);
                    setGraphic(buttons);
                }
            }
        });

        table.getColumns().addAll(namaCol, alamatCol, nikCol, tanggalLahirCol, aksiCol);

        return table;
    }

    private Node createPage(int pageIndex) {

        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, data.size());
        ObservableList<Pasien> sublist = FXCollections.observableArrayList(data.subList(fromIndex, toIndex));
        // table.setItems(sublist);

        table.getItems().clear();
        table.setItems(sublist);

        return new BorderPane(table);
    }

    @Override
    public void start(final Stage stage) throws Exception {

        Button inputButton = new Button("Input Pasien");
        inputButton.setStyle("-fx-background-color: green; -fx-text-fill: white;");
        inputButton.setOnAction(e -> {
            InputForm inputForm = new InputForm(pasienManager);
            inputForm.display();
            refreshTable();
            // handleDataChange();
        });

        Button exitButton = new Button("Keluar");
        exitButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        exitButton.setOnAction(e -> {
            // Menutup aplikasi
            stage.close();
        });
        
        pagination.setPageFactory(this::createPage);

        HBox buttonBox = new HBox(10, inputButton, exitButton);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(pagination); // Atur pagination di tengah
        BorderPane.setAlignment(buttonBox, Pos.TOP_RIGHT);
        borderPane.setTop(buttonBox); // Atur inputButton di bagian bawah

        Scene scene = new Scene(borderPane, 760, 415);
        stage.setScene(scene);
        stage.setTitle("Klinik");
        stage.show();
    }

    private void showEditForm(Pasien pasien) {
        EditForm editForm = new EditForm(pasienManager, pasien);
        editForm.display();
        refreshTable();
    }

    private void refreshTable() {
        handleDataChange();
        data.clear();
        data.addAll(pasienManager.getAllPasien());

        pagination.setCurrentPageIndex(0); // Kembali ke halaman pertama
        pagination.setPageFactory(this::createPage);
    }

    private void handleDataChange() {
        int totalPageCount = (int) Math.ceil((double) data.size() / rowsPerPage);
        pagination.setPageCount(totalPageCount);

        if (pagination.getCurrentPageIndex() >= totalPageCount) {
            pagination.setCurrentPageIndex(totalPageCount - 1);
        }
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    
}