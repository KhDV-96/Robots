package gui;

import localization.LanguageManager;
import log.LogWindowSource;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.mockito.Mockito.*;

class LogWindowTest {

    @Test
    void registerWhenCreating() {
        var logSource = spy(new LogWindowSource(10));

        var window = new LogWindow(logSource, mock(LanguageManager.class));

        verify(logSource, times(1)).registerListener(window);
    }

    @Test
    void unregisterWhenClosing() {
        var logSource = spy(new LogWindowSource(10));
        var window = new LogWindow(logSource, mock(LanguageManager.class));

        window.doDefaultCloseAction();

        verify(logSource, times(1)).unregisterListener(window);
    }

    @Test
    void unregisterCanceled() {
        var logSource = spy(new LogWindowSource(10));
        var window = new LogWindow(logSource, mock(LanguageManager.class));
        window.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        window.doDefaultCloseAction();

        verify(logSource, never()).unregisterListener(window);
    }
}
