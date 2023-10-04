package fx.xml.valid.worlds.controllers;

import api.xml.valid.worlds.HttpValidWorlds;
import com.fasterxml.jackson.core.type.TypeReference;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import json.SingletonObjectMapper;
import modules.Constants;
import okhttp3.Response;
import other.WorldDTO;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class XmlValidWorldController implements Initializable {
    @FXML private TableView<WorldDTO> validWorldsTableView;
    @FXML private TableColumn<WorldDTO, String> nameColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

        Executors.newScheduledThreadPool(1)
                .scheduleAtFixedRate(this::fetchValidWorlds, 0,
                        Constants.API_REFETCH_INTERVAL_MILLIS, TimeUnit.MILLISECONDS);
    }

    public void fetchValidWorlds() {
        try {
            Response response = HttpValidWorlds.getValidWorlds();

            if (response.isSuccessful() && !Objects.isNull(response.body())) {
                List<WorldDTO> validWorlds = SingletonObjectMapper.objectMapper.readValue(response.body().string(), new TypeReference<List<WorldDTO>>() {});

                Platform.runLater(() -> {
                   validWorldsTableView.getItems().clear();
                   validWorldsTableView.getItems().addAll(validWorlds);
                });
            }
        } catch (Exception ignored) { }

    }
}
