package fx.views.WorldGridPane;

import dtos.EntityDTO;
import dtos.SingleSimulationDTO;
import engine.modules.RandomGenerator;
import engine.prototypes.implemented.Coordinate;
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
import java.util.stream.Collectors;

public class WorldGridPaneController implements Initializable {
    @FXML
    private GridPane container;

    private ObjectProperty<SingleSimulationDTO> selectedSimulation;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectedSimulation = new SimpleObjectProperty<>();
        container.setVisible(false);

        selectedSimulation.addListener((observableValue, singleSimulationDTO, t1) -> {
            //TODO: Check if should by != ByStep.NOT_BY_STEP
            container.setVisible(!Objects.isNull(t1) && t1.getSimulationState() == SimulationState.PAUSED);
            container.getRowConstraints().clear();
            container.getColumnConstraints().clear();
            container.setGridLinesVisible(true);

            if (container.isVisible()) {
                SingletonThreadpoolManager.executeTask(() -> {
                    List<Pair<Coordinate, Rectangle>> spots = new ArrayList<>();
                    List<EntityDTO> entities = t1.getWorld().getEntities();
                    List<Color> randomColors = RandomGenerator.generateDistinctColors(entities.size());

                    for (int i = 0; i < entities.size(); i++) {
                        EntityDTO entity = t1.getWorld().getEntities().get(i);

                        for (Coordinate coordinate : entity.getTakenSpots()) {
                            Rectangle rectangle = new Rectangle(10, 10);
                            rectangle.setFill(randomColors.get(i));
                            spots.add(new Pair<>(coordinate, rectangle));
                        }
                    }

                    List<Coordinate> taken = spots.stream().map(Pair::getKey).collect(Collectors.toList());

                    for (int i = 0; i < t1.getWorld().getGridRows(); i++) {
                        for (int j = 0; j < t1.getWorld().getGridColumns(); j++) {
                            Coordinate coordinate = new Coordinate(i, j);
                            if (!taken.contains(coordinate)) {
                                Rectangle rectangle = new Rectangle(10, 10);
                                rectangle.setFill(Color.GREY);
                                spots.add(new Pair<>(coordinate, rectangle));
                            }
                        }
                    }

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
