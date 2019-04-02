package gui;

import log.LogWindowSource;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class LogWindowTest {

    @Test
    void registerWhenCreating() {
        var logSource = spy(new LogWindowSource(10));

        var window = new LogWindow(logSource);

        verify(logSource, times(1)).registerListener(window);
    }

    @Test
    void unregisterWhenClosing() {
        var logSource = spy(new LogWindowSource(10));
        var window = new LogWindow(logSource);

        window.doDefaultCloseAction();

        verify(logSource, times(1)).unregisterListener(window);
    }
}
