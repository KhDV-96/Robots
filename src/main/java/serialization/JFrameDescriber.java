package serialization;

import javax.swing.*;

class JFrameDescriber extends ComponentDescriber {

    private int extendedState;

    JFrameDescriber(JFrame frame) {
        super(frame);
        extendedState = frame.getExtendedState();
    }

    void restoreState(JFrame frame) {
        super.restoreState(frame);
        frame.setExtendedState(extendedState);
    }
}
