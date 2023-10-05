package fx.allocations.controllers;

import api.history.allocations.HttpAllocations;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import okhttp3.Response;
import other.TerminationDTO;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;

public class AllocationsController implements Initializable {
    @FXML private VBox container;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        TerminationDTO termination = new TerminationDTO(Collections.emptySet(), true);

        try {
            Response response = HttpAllocations.createAllocationRequest("maya", "master", 5, termination);
            if (response.isSuccessful())
                System.out.println("Horray!");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public VBox getContainer() {
        return container;
    }

    public void clearAllocations() {
        //TODO: Implement
    }
}
