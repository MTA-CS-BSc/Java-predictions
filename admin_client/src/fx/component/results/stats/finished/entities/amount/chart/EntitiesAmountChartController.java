package fx.component.results.stats.finished.entities.amount.chart;


import api.history.results.stats.entities.HttpEntitiesStats;
import com.fasterxml.jackson.core.type.TypeReference;
import fx.component.selected.SelectedProps;
import fx.modules.SingletonThreadpoolManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import json.JsonParser;
import okhttp3.Response;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class EntitiesAmountChartController implements Initializable {
    @FXML
    private LineChart<Integer, Integer> entitiesAmountChart;

    @FXML
    private NumberAxis ticksAxis;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        SelectedProps.SELECTED_SIMULATION.addListener((observableValue, singleSimulationDTO, t1) -> {
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

    private void fillChart() throws IOException {
        Response response = HttpEntitiesStats.getEntitiesAmountsPerTick(SelectedProps.SELECTED_SIMULATION.getValue().getUuid());

        //TODO: Show error details
        if (!response.isSuccessful()) {
            response.close();
            return;
        }

        if (!Objects.isNull(response.body())) {
            Map<String, List<Integer>> entitiesAmountsPerTick = JsonParser.objectMapper.readValue(
                    response.body().string(),
                    new TypeReference<Map<String, List<Integer>>>() {}
            );

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
    }

    public void toggleVisibility() {
        entitiesAmountChart.setVisible(!entitiesAmountChart.isVisible());
    }
}
