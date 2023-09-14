package fx.models.Results;

import dtos.SingleSimulationDTO;
import fx.consts.FilePaths;
import fx.modules.SingletonEngineAPI;
import helpers.types.SimulationState;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;

public class StopSimulationTableCell extends TableCell<SingleSimulationDTO, Boolean> implements SimulationControlButton {
    final Button stopButton;
    final StackPane paddedButton;

    public StopSimulationTableCell(final TableView<SingleSimulationDTO> table) {
        stopButton = new Button();
        paddedButton = new StackPane();
        paddedButton.setPadding(new Insets(3));
        paddedButton.getChildren().add(stopButton);

        try {
            styleButton(stopButton, FilePaths.STOP_SIMULATION_ICON_PATH);
        } catch (Exception ignored) { }

        stopButton.setOnAction(actionEvent -> {
            table.getSelectionModel().select(getTableRow().getIndex());
            SingletonEngineAPI.api.stopSimulation(getTableView().getSelectionModel().getSelectedItem().getUuid());
        });
    }

    /** places an add button in the row only if the row is not empty. */
    @Override protected void updateItem(Boolean item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty) {
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            setGraphic(paddedButton);

            SimulationState simulationState = getTableView().getItems().get(getIndex()).getSimulationState();
            setDisable(simulationState != SimulationState.PAUSED && simulationState != SimulationState.RUNNING);
        }

        else
            setGraphic(null);
    }
}
