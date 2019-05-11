package gui;

import localization.LanguageManager;
import log.LogChangeListener;
import log.LogWindowSource;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;

public class LogWindow extends JInternalFrame implements LogChangeListener, Disposable {

    private LogWindowSource logSource;
    private TextArea logContent;

    public LogWindow(LogWindowSource logSource, LanguageManager languageManager) {
        super(null, true, true, true, true);
        languageManager.bindField("logWindow.title", this::setTitle);

        this.logSource = logSource;
        this.logSource.registerListener(this);

        addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosed(InternalFrameEvent e) {
                logSource.unregisterListener(LogWindow.this);
            }
        });

        logContent = new TextArea();
        logContent.setSize(200, 500);

        getContentPane().add(logContent, BorderLayout.CENTER);
        pack();
        updateLogContent();
    }

    @Override
    public void onLogChanged() {
        EventQueue.invokeLater(this::updateLogContent);
    }

    @Override
    public void onDispose() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void updateLogContent() {
        var content = new StringBuilder();
        logSource.all().forEach(entry -> content.append(entry.getMessage()).append("\n"));
        logContent.setText(content.toString());
        logContent.invalidate();
    }
}
