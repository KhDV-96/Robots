package serialization;

import java.awt.*;
import java.io.Serializable;

class ComponentDescriber implements Serializable {

    private Point location;
    private Dimension size;

    ComponentDescriber(Component component) {
        location = component.getLocation();
        size = component.getSize();
    }

    void restoreState(Component component) {
        component.setLocation(location);
        component.setSize(size);
    }
}
