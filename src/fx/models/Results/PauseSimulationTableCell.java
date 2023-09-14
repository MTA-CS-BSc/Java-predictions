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

public class PauseSimulationTableCell extends TableCell<SingleSimulationDTO, Boolean> implements SimulationControlButton {
    final Button pauseButton;
    final StackPane paddedButton;

    public PauseSimulationTableCell(final TableView<SingleSimulationDTO> table) {
        pauseButton = new Button();
        paddedButton = new StackPane();
        paddedButton.setPadding(new Insets(3));
        paddedButton.getChildren().add(pauseButton);

        try {
            styleButton(pauseButton, FilePaths.PAUSE_SIMULATION_ICON_PATH);
        } catch (Exception ignored) { }

        pauseButton.setOnAction(actionEvent -> {
            table.getSelectionModel().select(getTableRow().getIndex());
            SingletonEngineAPI.api.pauseSimulation(getTableView().getSelectionModel().getSelectedItem().getUuid());
        });
    }

    /** places an add button in the row only if the row is not empty. */
    @Override protected void updateItem(Boolean item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty) {
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            setGraphic(paddedButton);
            setDisable(getTableView().getItems().get(getIndex()).getSimulationState() != SimulationState.RUNNING);
        }

        else
            setGraphic(null);
    }
}
