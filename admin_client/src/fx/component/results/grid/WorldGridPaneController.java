package fx.component.results.grid;


import fx.component.selected.SelectedProps;
import fx.modules.SingletonThreadpoolManager;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Pair;
import modules.RandomGenerator;
import other.EntityDTO;
import other.SingleSimulationDTO;
import types.Coordinate;
import types.SimulationState;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class WorldGridPaneController implements Initializable {
    @FXML private HBox container;

    @FXML private GridPane grid;

    @FXML private GridPane legend;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        container.setVisible(false);

        SelectedProps.SELECTED_SIMULATION.addListener((observableValue, singleSimulationDTO, t1) -> {
            container.setVisible(!Objects.isNull(t1) && t1.getSimulationState() == SimulationState.PAUSED);

            if (container.isVisible() && (Objects.isNull(singleSimulationDTO)
                    || !singleSimulationDTO.getUuid().equals(t1.getUuid())
                    || singleSimulationDTO.getTicks() != t1.getTicks())) {
                SingletonThreadpoolManager.executeTask(() -> {
                    List<Pair<Coordinate, Rectangle>> spots = new ArrayList<>();
                    List<Pair<String, Color>> legendItems = new ArrayList<>();
                    List<EntityDTO> entities = t1.getWorld().getEntities();
                    List<Color> randomColors = RandomGenerator.generateDistinctColors(entities.size());
                    List<Color> legendColors = RandomGenerator.generateDistinctColors(entities.size());

                    for (int i = 0; i < entities.size(); i++) {
                        EntityDTO entity = t1.getWorld().getEntities().get(i);
                        legendItems.add(new Pair<>(entity.getName(), legendColors.get(i)));
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
                        grid.getChildren().clear();
                        legend.getChildren().clear();

                        for (int i = 0; i < legendItems.size(); i++) {
                            Pair<String, Color> pair = legendItems.get(i);
                            Label entityNameLabel = new Label(pair.getKey());
                            Rectangle rectangle = new Rectangle(5, 5);
                            rectangle.setFill(pair.getValue());

                            legend.add(rectangle, 0, i);
                            legend.add(entityNameLabel, 1, i);
                        }

                        spots.forEach(pair -> {
                            grid.add(pair.getValue(), pair.getKey().getX(), pair.getKey().getY());
                        });
                    });
                });
            }
        });
    }

    public GridPane getGrid() { return grid; }
}
