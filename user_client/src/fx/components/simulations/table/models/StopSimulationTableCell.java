package fx.components.simulations.table.models;

import api.simulation.HttpSimulation;
import consts.paths.IconPaths;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import okhttp3.Response;
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
            try {
                Response response = HttpSimulation.stopSimulation(getTableView().getSelectionModel().getSelectedItem().getUuid());

                //TODO: Show error details
                if (!response.isSuccessful())
                    response.close();
            } catch (Exception ignored) { }
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

            SingleSimulationDTO simulation = getTableView().getItems().get(getIndex());
            SimulationState simulationState = simulation.getSimulationState();
            boolean isDisabled = !simulation.getWorld().getTermination().isByUser() ||
                    (simulationState != SimulationState.PAUSED && simulationState != SimulationState.RUNNING);

            setDisable(isDisabled);
        } else
            setGraphic(null);
    }
}
