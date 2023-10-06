package fx.results.population.table;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import other.EntityDTO;
import other.SingleSimulationDTO;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class PopulationTableController implements Initializable {
    @FXML private TableView<EntityDTO> populationTable;

    @FXML private TableColumn<EntityDTO, String> entityNameColumn;

    @FXML private TableColumn<EntityDTO, Integer> populationColumn;

    private ObjectProperty<SingleSimulationDTO> selectedSimulation;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        entityNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        populationColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPopulation()).asObject());

        selectedSimulation = new SimpleObjectProperty<>();
        selectedSimulation.addListener((observableValue, singleSimulationDTO, t1) -> {
            if (Objects.isNull(t1))
                populationTable.getItems().clear();

            else {
                populationTable.getItems().clear();
                populationTable.getItems().addAll(t1.getWorld().getEntities());
                populationTable.refresh();
            }
        });
    }

    public void setSelectedSimulation(SingleSimulationDTO simulation) {
        selectedSimulation.setValue(simulation);
    }
}
