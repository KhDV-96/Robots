package log;

import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

public class LogWindowSource {

    private final Queue<LogEntry> messages;
    private final List<LogChangeListener> listeners;

    public LogWindowSource(int queueLength) {
        messages = new LinkedBlockingDeque<>(queueLength);
        listeners = new CopyOnWriteArrayList<>();
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
        return messages.size();
    }

    public Iterable<LogEntry> range(int startFrom, int count) {
        if (startFrom < 0) {
            return Collections.emptyList();
        }
        return messages.stream()
                .skip(startFrom)
                .limit(count)
                .collect(Collectors.toList());
    }

    public Iterable<LogEntry> all() {
        return messages;
    }

    private void addEntry(LogLevel logLevel, String strMessage) {
        var entry = new LogEntry(logLevel, strMessage);
        while (!messages.offer(entry)) {
            messages.poll();
        }
    }

    private void notifyListeners() {
        listeners.forEach(LogChangeListener::onLogChanged);
    }
}
