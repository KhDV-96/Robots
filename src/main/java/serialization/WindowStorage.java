package serialization;

import javax.swing.*;
import java.util.HashMap;

public class WindowStorage {

    private final String path;

    private HashMap<String, ComponentDescriber> frames;
    private boolean restored;

    @SuppressWarnings("unchecked")
    public WindowStorage(String path) {
        this.path = path;
        frames = (HashMap<String, ComponentDescriber>) Serializer.load(path);
        if (frames == null) {
            frames = new HashMap<>();
        } else {
            restored = true;
        }
    }

    public void store(String key, JFrame frame) {
        frames.put(key, new JFrameDescriber(frame));
    }

    public void store(String key, JInternalFrame frame) {
        frames.put(key, new JInternalFrameDescriber(frame));
    }

    public void restore(String key, JFrame frame) {
        var describer = (JFrameDescriber) frames.getOrDefault(key, null);
        if (describer != null) {
            describer.restoreState(frame);
        }
    }

    public void restore(String key, JInternalFrame frame) {
        var describer = (JInternalFrameDescriber) frames.getOrDefault(key, null);
        if (describer != null) {
            describer.restoreState(frame);
        }
    }

    public void save() {
        Serializer.save(frames, path);
    }

    public boolean isRestored() {
        return restored;
    }
}
