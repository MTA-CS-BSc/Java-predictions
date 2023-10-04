package fx.themes;

import consts.ThemePaths;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;

import java.util.ArrayList;
import java.util.List;

public abstract class ScenesStore {
    private static final List<Scene> SCENES = new ArrayList<>();
    public static final ObservableList<Scene> SCENES_PROPERTY = FXCollections.observableList(SCENES);
    public static final StringProperty SELECTED_THEME_PATH = new SimpleStringProperty(ThemePaths.DEFAULT_THEME_CSS);
}
