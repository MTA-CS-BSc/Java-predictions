package fx.components.population.table;

import api.simulation.set.HttpSimulationSetters;
import consts.Alerts;
import fx.components.selected.SelectedProps;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;
import json.JsonParser;
import json.Keys;
import okhttp3.Response;
import other.EntityDTO;
import other.SingleSimulationDTO;
import types.SimulationState;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class PopulationTableController implements Initializable {
    @FXML
    private TableView<EntityDTO> populationTable;

    @FXML private TableColumn<EntityDTO, String> entityNameColumn;

    @FXML private TableColumn<EntityDTO, Integer> populationColumn;

    private ObjectProperty<SingleSimulationDTO> selectedSimulation;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        entityNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        populationColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPopulation()).asObject());

        selectedSimulation = new SimpleObjectProperty<>();
        selectedSimulation.addListener((observableValue, singleSimulationDTO, t1) -> {
            if (Objects.isNull(t1))
                populationTable.getItems().clear();

            else {
                if (t1.getSimulationState() == SimulationState.CREATED)
                    addPopulationEditCommit();

                populationTable.getItems().clear();
                populationTable.getItems().addAll(t1.getWorld().getEntities());
                populationTable.refresh();
            }
        });
    }

    public void addPopulationEditCommit() {
        if (Objects.isNull(selectedSimulation.getValue()))
            return;

        populationColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        populationColumn.setOnEditCommit(event -> {
            EntityDTO editedEntity = event.getRowValue();

            try {
                Response response = HttpSimulationSetters.setEntityInitialPopulation(selectedSimulation.getValue().getUuid(),
                        editedEntity.getName(), event.getNewValue());

                //TODO: Show error details
                if (!response.isSuccessful()) {
                    if (!Objects.isNull(response.body()))
                        Alerts.showAlert("Validation failed", JsonParser.getMapFromJsonString(response.body().string()).get(Keys.INVALID_RESPONSE_KEY).toString(), Alert.AlertType.ERROR);

                    response.close();

                    editedEntity.setPopulation(event.getOldValue());
                    populationTable.refresh();
                } else
                    editedEntity.setPopulation(event.getNewValue());
            } catch (Exception ignored) {
            }
        });
    }

    public void setSelectedSimulation(SingleSimulationDTO simulation) {
        selectedSimulation.setValue(simulation);
    }

    public boolean validateAllInitialized() {
        return populationTable.getItems()
                .stream()
                .anyMatch(entity -> entity.getPopulation() > 0);
    }

    public void clearPopulationTable() {
        populationTable.getItems().forEach(entity -> {
            try {
                Response response = HttpSimulationSetters.setEntityInitialPopulation(SelectedProps.CREATING_SIMULATION.getValue().getUuid(), entity.getName(), 0);

                //TODO: Show error details
                if (!response.isSuccessful())
                    response.close();

            } catch (Exception ignored) { }
        });

        populationTable.refresh();
    }
}
