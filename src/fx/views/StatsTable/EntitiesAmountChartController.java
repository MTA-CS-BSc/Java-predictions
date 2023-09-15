package fx.views.StatsTable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import dtos.SingleSimulationDTO;
import fx.modules.SingletonEngineAPI;
import helpers.modules.SingletonObjectMapper;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

public class EntitiesAmountChartController implements Initializable {
    @FXML
    private LineChart<Integer, Integer> entitiesAmountChart;

    @FXML
    private NumberAxis ticksAxis;

    private ObjectProperty<SingleSimulationDTO> selectedSimulation;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectedSimulation = new SimpleObjectProperty<>();

        selectedSimulation.addListener((observableValue, singleSimulationDTO, t1) -> {
            if (Objects.isNull(t1))
                entitiesAmountChart.getData().clear();

            else if (!Objects.isNull(singleSimulationDTO) &&
                    singleSimulationDTO.getUuid().equals(t1.getUuid())) {
                // Skip
            }

            else {
                try {
                    ticksAxis.setUpperBound(t1.getTicks());
                    fillChart();
                } catch (Exception ignored) { }
            }

        });
    }

    private void fillChart() throws JsonProcessingException {
        Map<String, List<Integer>> entitiesAmountsPerTick = SingletonObjectMapper.objectMapper.readValue(
                SingletonEngineAPI.api.getEntitiesAmountsPerTick(selectedSimulation.getValue().getUuid()).getData(),
                new TypeReference<Map<String, List<Integer>>>() {}
        );

        Platform.runLater(() -> entitiesAmountChart.getData().clear());

        entitiesAmountsPerTick.forEach((key, amountsListPerTick) -> {
            XYChart.Series<Integer, Integer> series =  new XYChart.Series<>();
            series.setName(key);

            for (int i = 0; i < amountsListPerTick.size(); i++)
                series.getData().add(new XYChart.Data<>(i, amountsListPerTick.get(i)));

            Platform.runLater(() -> {
                entitiesAmountChart.getData().add(series);
            });
        });
    }

    public void setSelectedSimulation(SingleSimulationDTO simulation) {
        selectedSimulation.setValue(simulation);
    }

    public void toggleVisibility() {
        entitiesAmountChart.setVisible(!entitiesAmountChart.isVisible());
    }
}
