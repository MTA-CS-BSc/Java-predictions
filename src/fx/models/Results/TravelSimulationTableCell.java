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

public class TravelSimulationTableCell extends TableCell<SingleSimulationDTO, Boolean> implements SimulationControlButton {
    final Button travelPastButton;
    final StackPane paddedButton;

    public TravelSimulationTableCell(final TableView<SingleSimulationDTO> table, ByStep byStep) {
        travelPastButton = new Button();
        paddedButton = new StackPane();
        paddedButton.setPadding(new Insets(3));
        paddedButton.getChildren().add(travelPastButton);

        try {
            styleButton(travelPastButton, byStep == ByStep.PAST ? FilePaths.PAST_TRAVEL_ICON_PATH : FilePaths.FUTURE_TRAVEL_ICON_PATH);
        } catch (Exception ignored) { }

        travelPastButton.setOnAction(actionEvent -> {
            table.getSelectionModel().select(getTableRow().getIndex());
            SingletonEngineAPI.api.setByStep(getTableView().getSelectionModel().getSelectedItem().getUuid(), byStep);
        });
    }

    /** places an add button in the row only if the row is not empty. */
    @Override protected void updateItem(Boolean item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty) {
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            setGraphic(paddedButton);

            SingleSimulationDTO simulation = getTableView().getItems().get(getIndex());
            setDisable(simulation.getSimulationState() != SimulationState.PAUSED);
        }

        else
            setGraphic(null);
    }
}