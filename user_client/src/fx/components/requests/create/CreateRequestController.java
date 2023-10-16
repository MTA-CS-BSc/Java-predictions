package fx.components.requests.create;

import api.ApiConstants;
import api.details.valid.worlds.HttpValidWorlds;
import com.fasterxml.jackson.core.type.TypeReference;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.util.Callback;
import json.JsonParser;
import okhttp3.Response;
import other.WorldDTO;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CreateRequestController implements Initializable {
    @FXML private ComboBox<WorldDTO> worldsComboBox;
    @FXML private TextArea executionsAmount;

    private ObjectProperty<WorldDTO> selectedWorld;
    private ReadOnlyBooleanProperty isParentVisible;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectedWorld = new SimpleObjectProperty<>();
        isParentVisible = new SimpleBooleanProperty(false);
        worldsComboBox.setCellFactory(getWorldCellFactory());
        addNumericFilter(executionsAmount);

        worldsComboBox.getSelectionModel().selectedItemProperty().addListener((observableValue, worldDTO, t1) -> {
            if (Objects.isNull(t1))
                worldsComboBox.getItems().clear();

            else
                selectedWorld.setValue(t1);
        });

        Executors.newScheduledThreadPool(1)
                .scheduleAtFixedRate(this::fetchValidWorlds, 0,
                        ApiConstants.API_REFETCH_INTERVAL_MILLIS, TimeUnit.MILLISECONDS);
    }

    public void setIsParentVisibleProperty(ReadOnlyBooleanProperty property) {
        isParentVisible = property;
    }

    public void fetchValidWorlds() {
        if (isParentVisible.getValue()) {
            try {
                Response response = HttpValidWorlds.getValidWorlds();

                //TODO: Show error details
                if (!response.isSuccessful())
                    response.close();

                if (response.isSuccessful() && !Objects.isNull(response.body())) {
                    List<WorldDTO> validWorlds = JsonParser.objectMapper.readValue(response.body().string(), new TypeReference<List<WorldDTO>>() {});

                    Platform.runLater(() -> {
                        worldsComboBox.getItems().clear();
                        worldsComboBox.getItems().addAll(validWorlds);

                        if (!Objects.isNull(selectedWorld))
                            selectPreviouslySelected();
                    });
                }
            } catch (Exception ignored) { }
        }
    }

    private void selectPreviouslySelected() {
        if (Objects.isNull(selectedWorld.getValue()))
            return;

        WorldDTO newlySelectedWorld = worldsComboBox.getItems()
                .stream()
                .filter(element -> element.getName().equals(selectedWorld.getValue().getName()))
                .findFirst().orElse(null);

        worldsComboBox.getSelectionModel().select(newlySelectedWorld);
        selectedWorld.setValue(newlySelectedWorld);
    }

    private Callback<ListView<WorldDTO>, ListCell<WorldDTO>> getWorldCellFactory() {
        return new Callback<ListView<WorldDTO>, ListCell<WorldDTO>>() {
            @Override
            public ListCell<WorldDTO> call(ListView<WorldDTO> l) {
                return new ListCell<WorldDTO>() {
                    @Override
                    protected void updateItem(WorldDTO item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item == null || empty)
                            setGraphic(null);

                        else
                            setText(item.getName());
                    }
                };
            }
        };
    }

    private void addNumericFilter(TextArea textArea) {
        textArea.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.matches("\\d*")) return;

            textArea.setText(newValue.replaceAll("[^\\d]", ""));
        });
    }
}
