package fx.models.Results;

import com.fasterxml.jackson.core.JsonProcessingException;
import dtos.SingleSimulationDTO;
import fx.consts.FilePaths;
import fx.modules.Alerts;
import fx.modules.SingletonEngineAPI;
import helpers.modules.SingletonObjectMapper;
import helpers.types.SimulationState;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;

public class RestartSimulationTableCell extends TableCell<SingleSimulationDTO, Boolean> implements SimulationControlButton {
    final Button restartButton;
    final StackPane paddedButton;

    public RestartSimulationTableCell(final TableView<SingleSimulationDTO> table) {
        restartButton = new Button();
        paddedButton = new StackPane();
        paddedButton.setPadding(new Insets(3));
        paddedButton.getChildren().add(restartButton);

        try {
            styleButton(restartButton, FilePaths.RESTART_SIMULATION_ICON_PATH);
        } catch (Exception ignored) { }

        restartButton.setOnAction(actionEvent -> {
            table.getSelectionModel().select(getTableRow().getIndex());
            String fromUuid = getTableView().getSelectionModel().getSelectedItem().getUuid();

            try {
                String clonedUuid = SingletonObjectMapper.objectMapper.readValue(
                        SingletonEngineAPI.api.cloneSimulation(fromUuid).getData(),
                        String.class);

                //TODO: Not implemented
            } catch (JsonProcessingException e) {
                Alerts.showAlert("ERROR", "Simulation can not be restarted",
                        "Selected simulation could not be cloned", Alert.AlertType.ERROR);
            }
        });
    }

    /** places an add button in the row only if the row is not empty. */
    @Override protected void updateItem(Boolean item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty) {
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            setGraphic(paddedButton);
            setDisable(getTableView().getItems().get(getIndex()).getSimulationState() != SimulationState.FINISHED);
        }

        else
            setGraphic(null);
    }
}
