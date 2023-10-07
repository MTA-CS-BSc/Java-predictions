package fx.component.selected;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import other.AllocationRequestDTO;
import other.SingleSimulationDTO;

public abstract class SelectedProps {
    public static final ObjectProperty<AllocationRequestDTO> SELECTED_REQUEST = new SimpleObjectProperty<>();
    public static final ObjectProperty<SingleSimulationDTO> SELECTED_SIMULATION = new SimpleObjectProperty<>();
}
