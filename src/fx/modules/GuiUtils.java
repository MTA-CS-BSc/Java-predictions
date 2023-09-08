package fx.modules;

import javafx.scene.control.TreeItem;

import java.util.Objects;

public abstract class GuiUtils {
    public static TreeItem<String> findTreeItemByValue(TreeItem<String> root, String value) {
        if (root.getValue().equalsIgnoreCase(value))
            return root;

        if (root.getChildren().size() == 0)
            return null;

        return root.getChildren()
                        .stream()
                        .map(child -> findTreeItemByValue(child, value))
                        .filter(element -> !Objects.isNull(element))
                        .findFirst().orElse(null);
    }
}
