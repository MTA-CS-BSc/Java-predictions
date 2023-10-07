package fx.component.results.population.table;

import fx.component.selected.SelectedProps;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import other.EntityDTO;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class PopulationTableController implements Initializable {
    @FXML private TableView<EntityDTO> populationTable;

    @FXML private TableColumn<EntityDTO, String> entityNameColumn;

    @FXML private TableColumn<EntityDTO, Integer> populationColumn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        entityNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        populationColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPopulation()).asObject());

        SelectedProps.SELECTED_SIMULATION.addListener((observableValue, singleSimulationDTO, t1) -> {
            if (Objects.isNull(t1))
                populationTable.getItems().clear();

            else {
                populationTable.getItems().clear();
                populationTable.getItems().addAll(t1.getWorld().getEntities());
                populationTable.refresh();
            }
        });
    }
}
