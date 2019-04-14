package serialization;

import javax.swing.*;
import java.beans.PropertyVetoException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class JFrameDescriber extends ComponentDescriber {

    private int extendedState;
    private Iterable<JInternalFrameDescriber> internalFrames;

    public JFrameDescriber(JFrame frame, Iterable<JInternalFrame> frames) {
        super(frame);
        extendedState = frame.getExtendedState();
        internalFrames = StreamSupport
                .stream(frames.spliterator(), false)
                .map(JInternalFrameDescriber::new)
                .collect(Collectors.toList());
    }

    public void restoreState(JFrame frame) {
        super.restoreState(frame);
        frame.setExtendedState(extendedState);
    }

    public void restoreInternalFrames(Iterable<JInternalFrame> frames) {
        var iterator1 = internalFrames.iterator();
        var iterator2 = frames.iterator();
        while (iterator1.hasNext() && iterator2.hasNext()) {
            iterator1.next().restoreState(iterator2.next());
        }
    }

    private static class JInternalFrameDescriber extends ComponentDescriber {

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
}
