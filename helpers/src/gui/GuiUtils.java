package gui;

import javafx.animation.FadeTransition;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public abstract class GuiUtils {
    public static void fadeInAnimation(Pane root) {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(1800), root);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        fadeIn.setOnFinished(event -> root.setVisible(true));
        fadeIn.play();
    }

    public static void fadeOutAnimation(Pane root) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(1200), root);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        fadeOut.setOnFinished(event -> root.setVisible(false));
        fadeOut.play();
    }

}
