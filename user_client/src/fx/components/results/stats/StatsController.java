package fx.components.results.stats;


import fx.components.results.grid.WorldGridPaneController;
import fx.components.results.stats.finished.FinishedStatsController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class StatsController implements Initializable {

    @FXML private FinishedStatsController finishedStatsController;

    @FXML private WorldGridPaneController worldGridPaneController;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) { }
}

