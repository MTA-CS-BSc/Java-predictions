package fx.components.simulations.table.models;

import consts.paths.IconPaths;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import other.SingleSimulationDTO;
import types.SimulationState;

public class StopSimulationTableCell extends TableCell<SingleSimulationDTO, Boolean> implements SimulationControlButton {
    final Button stopButton;
    final StackPane paddedButton;

    public StopSimulationTableCell(final TableView<SingleSimulationDTO> table) {
        stopButton = new Button();
        paddedButton = new StackPane();
        paddedButton.setPadding(new Insets(3));
        paddedButton.getChildren().add(stopButton);

        try {
            styleButton(stopButton, IconPaths.STOP_SIMULATION_ICON_PATH);
        } catch (Exception ignored) {
        }

        stopButton.setOnAction(actionEvent -> {
            table.getSelectionModel().select(getTableRow().getIndex());
//            SingletonEngineAPI.api.stopSimulation(getTableView().getSelectionModel().getSelectedItem().getUuid());
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

            //TODO: Check if is button should be disabled if not By User
            SingleSimulationDTO simulation = getTableView().getItems().get(getIndex());
            SimulationState simulationState = simulation.getSimulationState();
            boolean isDisabled = !simulation.getWorld().getTermination().isByUser() ||
                    (simulationState != SimulationState.PAUSED && simulationState != SimulationState.RUNNING);

            setDisable(isDisabled);
        } else
            setGraphic(null);
    }
}
