package fx.results.stats.finished.entities.amount.chart.controllers;


import com.fasterxml.jackson.core.JsonProcessingException;
import fx.modules.SingletonThreadpoolManager;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import other.SingleSimulationDTO;

import java.net.URL;
import java.util.*;

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
            } else {
                try {
                    ticksAxis.setUpperBound(t1.getTicks());
                    fillChart();
                } catch (Exception ignored) {
                }
            }

        });
    }

    private void fillChart() throws JsonProcessingException {
        //TODO: Re-write
        // Map<String, List<Integer>> entitiesAmountsPerTick = SingletonObjectMapper.objectMapper.readValue(
//                SingletonEngineAPI.api.getEntitiesAmountsPerTick(selectedSimulation.getValue().getUuid()).getData(),
//                new TypeReference<Map<String, List<Integer>>>() {
//                }
//        );

        Map<String, List<Integer>> entitiesAmountsPerTick = Collections.emptyMap();

        SingletonThreadpoolManager.executeTask(() -> {
            List<XYChart.Series<Integer, Integer>> seriesList = new ArrayList<>();

            entitiesAmountsPerTick.forEach((key, amountsListPerTick) -> {
                XYChart.Series<Integer, Integer> series = new XYChart.Series<>();
                series.setName(key);

                for (int i = 0; i < amountsListPerTick.size(); i++)
                    series.getData().add(new XYChart.Data<>(i, amountsListPerTick.get(i)));

                seriesList.add(series);
            });

            Platform.runLater(() -> {
                if (!seriesList.isEmpty()) {
                    entitiesAmountChart.getData().clear();
                    entitiesAmountChart.getData().addAll(seriesList);
                }
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
