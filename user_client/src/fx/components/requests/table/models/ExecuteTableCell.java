package fx.components.requests.table.models;

import consts.paths.IconPaths;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import other.AllocationRequestDTO;

public class ExecuteTableCell extends TableCell<AllocationRequestDTO, Boolean> implements RequestControlButton {
    final Button executeButton;
    final StackPane paddedButton;

    public ExecuteTableCell(final TableView<AllocationRequestDTO> table, Runnable navigateToExecution) {
        executeButton = new Button();
        paddedButton = new StackPane();
        paddedButton.setPadding(new Insets(3));
        paddedButton.getChildren().add(executeButton);

        try {
            styleButton(executeButton, IconPaths.EXECUTE_SIMULATION_ICON);
        } catch (Exception ignored) {
        }

        executeButton.setOnAction(actionEvent -> {
            table.getSelectionModel().select(getTableRow().getIndex());
            navigateToExecution.run();
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
            setDisable(!getTableView().getItems().get(getIndex()).isCanExecute());
        } else
            setGraphic(null);
    }
}
