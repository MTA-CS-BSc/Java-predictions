package fx.mgmt.threadpool.controllers;

import api.threadpool.HttpThreadpool;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import json.SingletonObjectMapper;
import modules.Constants;
import okhttp3.Response;
import other.QueueMgmtDTO;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadpoolQueueController implements Initializable {
    @FXML private TableView<QueueMgmtDTO> queueMgmtTableView;
    @FXML private TableColumn<QueueMgmtDTO, Integer> threadsAmountColumn;
    @FXML private TableColumn<QueueMgmtDTO, Integer> runningColumn;
    @FXML private TableColumn<QueueMgmtDTO, Integer> finishedColumn;
    @FXML private TableColumn<QueueMgmtDTO, Integer> pendingColumn;

    private BooleanProperty isParentVisible;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        isParentVisible = new SimpleBooleanProperty(false);

        threadsAmountColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getThreadsAmount()).asObject());
        finishedColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getFinished()).asObject());
        pendingColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPending()).asObject());
        runningColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getRunning()).asObject());

        Executors.newScheduledThreadPool(1)
                .scheduleAtFixedRate(this::fetchQueueMgmt, 0,
                        Constants.API_REFETCH_INTERVAL_MILLIS, TimeUnit.MILLISECONDS);
    }

    private void fetchQueueMgmt() {
        if (isParentVisible.getValue()) {
            try {
                Response response = HttpThreadpool.getThreadpoolQueueData();

                if (!response.isSuccessful()) {
                    response.close();
                    return;
                }

                if (!Objects.isNull(response.body())) {
                    QueueMgmtDTO queueMgmtDTO = SingletonObjectMapper.objectMapper.readValue(response.body().string(), QueueMgmtDTO.class);

                    Platform.runLater(() -> {
                        queueMgmtTableView.getItems().clear();
                        queueMgmtTableView.getItems().add(queueMgmtDTO);
                        queueMgmtTableView.refresh();
                    });
                }
            } catch (Exception ignored) { }
        }
    }

    public void clearTableView() {
        Platform.runLater(() -> {
            queueMgmtTableView.getItems().clear();
            queueMgmtTableView.refresh();
        });
    }

    public void setIsParentVisibleProperty(BooleanProperty value) {
        isParentVisible = value;
    }
}
