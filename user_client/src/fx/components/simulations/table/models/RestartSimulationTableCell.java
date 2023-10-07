package fx.components.simulations.table.models;

import api.simulation.HttpSimulation;
import consts.Alerts;
import consts.paths.IconPaths;
import fx.components.simulations.table.SimulationsTableController;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import json.JsonParser;
import json.Keys;
import okhttp3.Response;
import other.SingleSimulationDTO;
import types.SimulationState;

import java.util.Objects;

public class RestartSimulationTableCell extends TableCell<SingleSimulationDTO, Boolean> implements SimulationControlButton {
    final Button restartButton;
    final StackPane paddedButton;
    final SimulationsTableController controller;

    public RestartSimulationTableCell(final TableView<SingleSimulationDTO> table, SimulationsTableController controller) {
        this.controller = controller;
        restartButton = new Button();
        paddedButton = new StackPane();
        paddedButton.setPadding(new Insets(3));
        paddedButton.getChildren().add(restartButton);

        try {
            styleButton(restartButton, IconPaths.RESTART_SIMULATION_ICON_PATH);
        } catch (Exception ignored) {}

        restartButton.setOnAction(actionEvent -> {
            table.getSelectionModel().select(getTableRow().getIndex());
            try {
                String fromUuid = getTableView().getSelectionModel().getSelectedItem().getUuid();
                Response response = HttpSimulation.cloneSimulation(fromUuid);

                if (!response.isSuccessful()) {
                    if (!Objects.isNull(response.body()))
                        Alerts.showAlert("Selected simulation could not be cloned", JsonParser.getMapFromJsonString(response.body().string()).get(Keys.INVALID_RESPONSE_KEY).toString(), Alert.AlertType.ERROR);
                    response.close();
                }

                else {
                    //TODO: Navigate to new execution screen
                }
            } catch (Exception e) {
                Alerts.showAlert("Selected simulation could not be cloned", e.getMessage() , Alert.AlertType.ERROR);
            }
        });
    }

    /**
     * places an add button in the row only if the row is not empty.
     */
    @Override
    protected void updateItem(Boolean item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty) {
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            setGraphic(paddedButton);
            setDisable(getTableView().getItems().get(getIndex()).getSimulationState() != SimulationState.FINISHED);
        } else
            setGraphic(null);
    }
}
