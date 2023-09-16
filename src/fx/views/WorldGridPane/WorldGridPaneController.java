package fx.views.WorldGridPane;

import dtos.SingleSimulationDTO;
import engine.prototypes.implemented.Coordinate;
import engine.simulation.ByStep;
import fx.modules.SingletonThreadpoolManager;
import helpers.types.SimulationState;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class WorldGridPaneController implements Initializable {
    @FXML
    private GridPane container;

    private ObjectProperty<SingleSimulationDTO> selectedSimulation;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectedSimulation = new SimpleObjectProperty<>();
        container.setVisible(false);

        selectedSimulation.addListener((observableValue, singleSimulationDTO, t1) -> {
            container.setVisible(!Objects.isNull(t1) && t1.getSimulationState() == SimulationState.PAUSED && t1.getByStep() != ByStep.NOT_BY_STEP);
            container.getRowConstraints().clear();
            container.getColumnConstraints().clear();

            if (container.isVisible()) {
                SingletonThreadpoolManager.executeTask(() -> {
                    List<Pair<Coordinate, Rectangle>> spots = new ArrayList<>();

                    t1.getWorld().getEntities().forEach(entity -> {
                        entity.getTakenSpots().forEach(coordinate -> {
                            Rectangle rectangle = new Rectangle(10, 10);
                            rectangle.setFill(Color.BLUE);
                            spots.add(new Pair<>(coordinate, rectangle));
                        });
                    });

                    Platform.runLater(() -> {
                        container.getChildren().clear();

                        spots.forEach(pair -> {
                            container.add(pair.getValue(), pair.getKey().getX(), pair.getKey().getY());
                        });
                    });

                    //TODO: Fix alignment & add random colors to entities
                });
            }
        });
    }

    public GridPane getContainer() { return container; }

    public void setSelectedSimulation(SingleSimulationDTO simulation) {
        selectedSimulation.setValue(simulation);
    }
}
