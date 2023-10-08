package fx.components.simulations.table.models;

import fx.Main;
import fx.components.requests.table.models.TransitionType;
import javafx.animation.ScaleTransition;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;

public interface SimulationControlButton {
    default ScaleTransition getTransition(Button controlButton, TransitionType type) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), controlButton);
        scaleTransition.setFromX(type.equals(TransitionType.ENTERED) ? 1.0 : 1.1);
        scaleTransition.setToX(type.equals(TransitionType.ENTERED) ? 1.1 : 1.0);
        scaleTransition.setFromY(type.equals(TransitionType.ENTERED) ? 1.0 : 1.1);
        scaleTransition.setToY(type.equals(TransitionType.ENTERED) ? 1.1 : 1.0);

        return scaleTransition;
    }

    default ImageView getImageView(String path) throws IOException {
        ImageView imageView = new ImageView(new Image(Objects.requireNonNull(Main.class.getResource(path)).openStream()));

        imageView.setFitHeight(20);
        imageView.setFitWidth(20);

        return imageView;
    }

    default void styleButton(Button controlButton, String imagePath) throws IOException {
        controlButton.setStyle("-fx-background-color: transparent");
        controlButton.setGraphic(getImageView(imagePath));
        controlButton.setOnMouseEntered(actionEvent -> getTransition(controlButton, TransitionType.ENTERED).play());
        controlButton.setOnMouseExited(actionEvent -> getTransition(controlButton, TransitionType.EXITED).play());

    }
}
