package gui;

import log.LogChangeListener;
import log.LogWindowSource;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;

public class LogWindow extends JInternalFrame implements LogChangeListener {

    private LogWindowSource logSource;
    private TextArea logContent;

    public LogWindow(LogWindowSource logSource) {
        super("Протокол работы", true, true, true, true);
        this.logSource = logSource;
        this.logSource.registerListener(this);

        logContent = new TextArea("");
        logContent.setSize(200, 500);

        addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                logSource.unregisterListener(LogWindow.this);
            }
        });

        var panel = new JPanel(new BorderLayout());
        panel.add(logContent, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        updateLogContent();
    }

    @Override
    public void onLogChanged() {
        EventQueue.invokeLater(this::updateLogContent);
    }

    private void updateLogContent() {
        var content = new StringBuilder();
        logSource.all().forEach(entry -> content.append(entry.getMessage()).append("\n"));
        logContent.setText(content.toString());
        logContent.invalidate();
    }
}
