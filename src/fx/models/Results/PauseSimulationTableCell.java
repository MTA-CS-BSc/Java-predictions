package fx.models.Results;

import dtos.SingleSimulationDTO;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class PauseSimulationTableCell extends TableCell<SingleSimulationDTO, Boolean> implements SimulationControlButton {
    final Button pauseButton;
    final StackPane paddedButton;

    public PauseSimulationTableCell(final TableView<SingleSimulationDTO> table) throws IOException {
        pauseButton = new Button();
        paddedButton = new StackPane();
        paddedButton.setPadding(new Insets(3));
        paddedButton.getChildren().add(pauseButton);

        styleButton(pauseButton, "../../views/Resources/pause-button.png");

        pauseButton.setOnAction(actionEvent -> {
            table.getSelectionModel().select(getTableRow().getIndex());
        });
    }

    /** places an add button in the row only if the row is not empty. */
    @Override protected void updateItem(Boolean item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty) {
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            setGraphic(paddedButton);
        }

        else
            setGraphic(null);
    }
}
