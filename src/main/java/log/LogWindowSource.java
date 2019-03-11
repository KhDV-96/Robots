package log;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

public class LogWindowSource {

    private int m_iQueueLength;
    private ArrayDeque<LogEntry> m_messages;
    private final ArrayList<LogChangeListener> m_listeners;

    public LogWindowSource(int iQueueLength) {
        m_iQueueLength = iQueueLength;
        m_messages = new ArrayDeque<>(iQueueLength);
        m_listeners = new ArrayList<>();
    }

    public void registerListener(LogChangeListener listener) {
        synchronized (m_listeners) {
            m_listeners.add(listener);
        }
    }

    public void unregisterListener(LogChangeListener listener) {
        synchronized (m_listeners) {
            m_listeners.remove(listener);
        }
    }

    public void append(LogLevel logLevel, String strMessage) {
        addEntry(logLevel, strMessage);
        notifyListeners();
    }

    public int size() {
        return m_messages.size();
    }

    public Iterable<LogEntry> range(int startFrom, int count) {
        if (startFrom < 0) {
            return Collections.emptyList();
        }
        return m_messages.stream()
                .skip(startFrom)
                .limit(count)
                .collect(Collectors.toList());
    }

    public Iterable<LogEntry> all() {
        return m_messages.clone();
    }

    private void addEntry(LogLevel logLevel, String strMessage) {
        if (m_messages.size() == m_iQueueLength)
            m_messages.poll();
        m_messages.add(new LogEntry(logLevel, strMessage));
    }

    private void notifyListeners() {
        synchronized (m_listeners) {
            for (var listener : m_listeners) {
                listener.onLogChanged();
            }
        }
    }
}
