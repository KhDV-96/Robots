package serialization;

import javax.swing.*;
import java.beans.PropertyVetoException;
import java.io.Serializable;

class JInternalFrameDescriber extends ComponentDescriber implements Serializable {

    private boolean isIcon;

    JInternalFrameDescriber(JInternalFrame frame) {
        super(frame);
        isIcon = frame.isIcon() || frame.isClosed();
    }

    void restoreState(JInternalFrame frame) {
        super.restoreState(frame);
        try {
            frame.setIcon(isIcon);
        } catch (PropertyVetoException e) {
            e.printStackTrace();
        }
    }
}
