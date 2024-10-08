package fx.component.mgmt.valid.worlds;

import api.ApiConstants;
import api.mgmt.xml.valid.worlds.HttpValidWorlds;
import com.fasterxml.jackson.core.type.TypeReference;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import json.JsonParser;
import okhttp3.Response;
import other.WorldDTO;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class XmlValidWorldsController implements Initializable {
    @FXML private TableView<WorldDTO> validWorldsTableView;
    @FXML private TableColumn<WorldDTO, String> nameColumn;

    private ObjectProperty<WorldDTO> selectedWorld;

    private ReadOnlyBooleanProperty isParentVisible;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isParentVisible = new SimpleBooleanProperty(true);
        selectedWorld = new SimpleObjectProperty<>();
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

        Executors.newScheduledThreadPool(1)
                .scheduleAtFixedRate(this::fetchValidWorlds, 0,
                        ApiConstants.API_REFETCH_INTERVAL_MILLIS, TimeUnit.MILLISECONDS);
    }

    public void setIsParentVisibleProperty(ReadOnlyBooleanProperty value) {
        isParentVisible = value;
    }

    public void fetchValidWorlds() {
        if (isParentVisible.getValue()) {
            try {
                Response response = HttpValidWorlds.getValidWorlds();

                //TODO: Show error details
                if (!response.isSuccessful())
                    response.close();

                if (response.isSuccessful() && !Objects.isNull(response.body())) {
                    List<WorldDTO> validWorlds = JsonParser.objectMapper.readValue(response.body().string(), new TypeReference<List<WorldDTO>>() {
                    });

                    Platform.runLater(() -> {
                        validWorldsTableView.getItems().clear();
                        validWorldsTableView.getItems().addAll(validWorlds);
                        validWorldsTableView.refresh();

                        if (!Objects.isNull(selectedWorld))
                            selectPreviouslySelected();
                    });
                }
            } catch (Exception ignored) {
            }
        }
    }

    private void selectPreviouslySelected() {
        if (Objects.isNull(selectedWorld.getValue()))
            return;

        WorldDTO newlySelected = validWorldsTableView.getItems()
                .stream()
                .filter(element -> element.getName().equals(selectedWorld.getValue().getName()))
                .findFirst().orElse(null);

        validWorldsTableView.getSelectionModel().select(newlySelected);
        setSelectedWorld(newlySelected);
    }

    @FXML
    private void handleSelectedWorld(MouseEvent event) {
        if (event.getClickCount() == 1) {
            WorldDTO selectedWorld = validWorldsTableView.getSelectionModel().getSelectedItem();

            if (selectedWorld != null)
                setSelectedWorld(selectedWorld);
        }
    }

    public void setSelectedWorld(WorldDTO value) {
        selectedWorld.setValue(value);
    }

    public WorldDTO getSelectedWorld() {
        return selectedWorld.getValue();
    }

    public ObjectProperty<WorldDTO> selectedWorldProperty() {
        return selectedWorld;
    }

    public void clearWorlds() {
        setSelectedWorld(null);

        Platform.runLater(() -> {
            validWorldsTableView.getItems().clear();
            validWorldsTableView.refresh();
        });
    }
}
