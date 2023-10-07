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
import types.ByStep;
import types.SimulationState;

public class ResumeSimulationTableCell extends TableCell<SingleSimulationDTO, Boolean> implements SimulationControlButton {
    final Button resumeButton;
    final StackPane paddedButton;

    public ResumeSimulationTableCell(final TableView<SingleSimulationDTO> table) {
        resumeButton = new Button();
        paddedButton = new StackPane();
        paddedButton.setPadding(new Insets(3));
        paddedButton.getChildren().add(resumeButton);

        try {
            styleButton(resumeButton, IconPaths.RESUME_SIMULATION_ICON_PATH);
        } catch (Exception ignored) {
        }

        resumeButton.setOnAction(actionEvent -> {
            table.getSelectionModel().select(getTableRow().getIndex());

            try {
                String uuid = getTableView().getSelectionModel().getSelectedItem().getUuid();
                Response setByStepResponse = HttpSimulation.setByStep(uuid, ByStep.NOT_BY_STEP);
                Response resumeResponse = HttpSimulation.resumeSimulation(uuid);

                if (!setByStepResponse.isSuccessful())
                    setByStepResponse.close();

                if (!resumeResponse.isSuccessful())
                    resumeResponse.close();
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
            setDisable(getTableView().getItems().get(getIndex()).getSimulationState() != SimulationState.PAUSED);
        } else
            setGraphic(null);
    }
}
