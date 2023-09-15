package fx.models.Results;

import dtos.SingleSimulationDTO;
import engine.simulation.ByStep;
import fx.consts.FilePaths;
import fx.modules.SingletonEngineAPI;
import helpers.types.SimulationState;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;

public class ResumeSimulationTableCell extends TableCell<SingleSimulationDTO, Boolean> implements SimulationControlButton {
    final Button resumeButton;
    final StackPane paddedButton;

    public ResumeSimulationTableCell(final TableView<SingleSimulationDTO> table) {
        resumeButton = new Button();
        paddedButton = new StackPane();
        paddedButton.setPadding(new Insets(3));
        paddedButton.getChildren().add(resumeButton);

        try {
            styleButton(resumeButton, FilePaths.RESUME_SIMULATION_ICON_PATH);
        } catch (Exception ignored) { }

        resumeButton.setOnAction(actionEvent -> {
            table.getSelectionModel().select(getTableRow().getIndex());
            String uuid = getTableView().getSelectionModel().getSelectedItem().getUuid();
            SingletonEngineAPI.api.setByStep(uuid, ByStep.NOT_BY_STEP);
            SingletonEngineAPI.api.resumeSimulation(uuid);
        });
    }

    /** places an add button in the row only if the row is not empty. */
    @Override protected void updateItem(Boolean item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty) {
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            setGraphic(paddedButton);
            setDisable(getTableView().getItems().get(getIndex()).getSimulationState() != SimulationState.PAUSED);
        }

        else
            setGraphic(null);
    }
}
