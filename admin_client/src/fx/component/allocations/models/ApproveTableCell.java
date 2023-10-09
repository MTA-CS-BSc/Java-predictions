package fx.component.allocations.models;

import api.allocations.HttpAllocations;
import consts.paths.IconPaths;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.layout.StackPane;
import okhttp3.Response;
import other.AllocationRequestDTO;
import types.RequestState;

import java.io.IOException;

public class ApproveTableCell extends TableCell<AllocationRequestDTO, Boolean> implements RequestControlButton {
    final Button approveButton;
    final StackPane paddedButton;

    public ApproveTableCell(final TableView<AllocationRequestDTO> table) {
        approveButton = new Button();
        paddedButton = new StackPane();
        paddedButton.setPadding(new Insets(3));
        paddedButton.getChildren().add(approveButton);

        try {
            styleButton(approveButton, IconPaths.APPROVE_BUTTON_ICON_PATH);
        } catch (Exception ignored) {
        }

        approveButton.setOnAction(actionEvent -> {
            table.getSelectionModel().select(getTableRow().getIndex());
            try {
                Response response = HttpAllocations.approveRequest(getTableView().getSelectionModel().getSelectedItem().getUuid());

                //TODO: Show error details
                if (!response.isSuccessful())
                    response.close();
            } catch (IOException ignored) { }
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
            setDisable(getTableView().getItems().get(getIndex()).getState() != RequestState.CREATED);
        } else
            setGraphic(null);
    }
}
