package log;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LogWindowSource {

    private final int queueLength;
    private final ArrayDeque<LogEntry> messages;
    private final List<LogChangeListener> listeners;

    public LogWindowSource(int iQueueLength) {
        queueLength = iQueueLength;
        messages = new ArrayDeque<>(iQueueLength);
        listeners = Collections.synchronizedList(new ArrayList<>());
    }

    public void registerListener(LogChangeListener listener) {
        listeners.add(listener);
    }

    public void unregisterListener(LogChangeListener listener) {
        listeners.remove(listener);
    }

    public void append(LogLevel logLevel, String strMessage) {
        addEntry(logLevel, strMessage);
        notifyListeners();
    }

    public int size() {
        synchronized (messages) {
            return messages.size();
        }
    }

    public Iterable<LogEntry> range(int startFrom, int count) {
        if (startFrom < 0) {
            return Collections.emptyList();
        }
        synchronized (messages) {
            return messages.stream()
                    .skip(startFrom)
                    .limit(count)
                    .collect(Collectors.toList());
        }
    }

    public Iterable<LogEntry> all() {
        synchronized (messages) {
            return messages.clone();
        }
    }

    private void addEntry(LogLevel logLevel, String strMessage) {
        synchronized (messages) {
            if (messages.size() == queueLength) {
                messages.poll();
            }
            messages.add(new LogEntry(logLevel, strMessage));
        }
    }

    private void notifyListeners() {
        synchronized (listeners) {
            for (var listener : listeners) {
                listener.onLogChanged();
            }
        }
    }
}
