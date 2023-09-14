package fx.controllers;

import dtos.EntityDTO;
import dtos.ResponseDTO;
import dtos.SingleSimulationDTO;
import fx.modules.Alerts;
import fx.modules.SingletonEngineAPI;
import helpers.Constants;
import helpers.types.SimulationState;
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

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class PopulationTableController implements Initializable {
    @FXML
    private TableView<EntityDTO> populationTable;

    @FXML
    private TableColumn<EntityDTO, String> entityNameColumn;

    @FXML
    private TableColumn<EntityDTO, Integer> populationColumn;

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

    public void setSelectedSimulation(SingleSimulationDTO simulation) { selectedSimulation.setValue(simulation); }

    public void addPopulationEditCommit() {
        if (Objects.isNull(selectedSimulation.getValue()))
            return;

        populationColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        populationColumn.setOnEditCommit(event -> {
            EntityDTO editedEntity = event.getRowValue();

            try {
                ResponseDTO response = SingletonEngineAPI.api
                        .setEntityInitialPopulation(selectedSimulation.getValue().getUuid(),
                                editedEntity, event.getNewValue());

                if (response.getStatus() != Constants.API_RESPONSE_OK) {
                    Alerts.showAlert("Validation failed", "Population is invalid",
                            response.getErrorDescription().getCause(), Alert.AlertType.ERROR);

                    editedEntity.setPopulation(event.getOldValue());
                    populationTable.refresh();
                }

                else
                    editedEntity.setPopulation(event.getNewValue());

            } catch (Exception ignored) { }
        });
    }

    public boolean validateAllInitialized() {
        return populationTable.getItems()
                .stream()
                .anyMatch(entity -> entity.getPopulation() > 0);
    }

    public void clearPopulationTable() {
        if (Objects.isNull(selectedSimulation.getValue()))
            return;

        for (EntityDTO entity : populationTable.getItems())
            SingletonEngineAPI.api.setEntityInitialPopulation(selectedSimulation.getValue().getUuid(),
                    entity, 0);
    }

    public void refreshTable() {
        populationTable.refresh();
    }
}
