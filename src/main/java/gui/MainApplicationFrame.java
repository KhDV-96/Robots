package gui;

import log.Logger;
import serialization.JFrameDescriber;
import serialization.Serializer;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

public class MainApplicationFrame extends JFrame implements Disposable, Externalizable {

    static final String SERIALIZATION_FILE = "main_frame.ser";
    private static final int INSET = 50;

    private final JDesktopPane desktopPane = new JDesktopPane();

    private List<JInternalFrame> internalFrames = new ArrayList<>();

    public MainApplicationFrame() {
        var screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(INSET, INSET, screenSize.width - INSET * 2, screenSize.height - INSET * 2);

        setContentPane(desktopPane);
        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onClose(MainApplicationFrame.this);
            }
        });

        var logWindow = createLogWindow();
        addWindow(logWindow);
        setMinimumSize(logWindow.getSize());
        Logger.debug("Протокол работает");

        var gameWindow = createGameWindow();
        addWindow(gameWindow);
    }

    @Override
    public void onDispose() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Serializer.save(this, SERIALIZATION_FILE);
    }

    private LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        logWindow.pack();
        logWindow.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        logWindow.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                onClose(logWindow);
            }
        });
        return logWindow;
    }

    private GameWindow createGameWindow() {
        var gameWindow = new GameWindow();
        gameWindow.setSize(400, 400);
        gameWindow.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        gameWindow.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                onClose(gameWindow);
            }
        });
        return gameWindow;
    }

    private void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        internalFrames.add(frame);
        frame.setVisible(true);
    }

    private JMenuBar generateMenuBar() {
        var menuBar = new JMenuBar();

        var lookAndFeelMenu = new MenuBuilder("Режим отображения")
                .setMnemonic(KeyEvent.VK_S)
                .setDescription("Управление режимом отображения приложения")
                .addMenuItem("Системная схема", KeyEvent.VK_S,
                        e -> setLookAndFeel(UIManager.getSystemLookAndFeelClassName()))
                .addMenuItem("Универсальная схема", KeyEvent.VK_S,
                        e -> setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()))
                .build();

        var testMenu = new MenuBuilder("Тесты")
                .setMnemonic(KeyEvent.VK_S)
                .setDescription("Тестовые команды")
                .addMenuItem("Сообщение в лог", KeyEvent.VK_S,
                        e -> Logger.debug("Новая строка"))
                .build();

        var fileMenu = new MenuBuilder("Файл")
                .setMnemonic(KeyEvent.VK_F)
                .addMenuItem("Выход", KeyEvent.VK_Q,
                        e -> dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)))
                .build();

        menuBar.add(fileMenu);
        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        return menuBar;
    }

    private void onClose(Disposable disposable) {
        int confirmed = JOptionPane.showConfirmDialog(this,
                "Вы точно хотите выйти?",
                "Выход", JOptionPane.YES_NO_OPTION);

        if (confirmed == JOptionPane.YES_OPTION) {
            disposable.onDispose();
        }
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
            invalidate();
        } catch (ReflectiveOperationException | UnsupportedLookAndFeelException e) {
            // just ignore
        }
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(new JFrameDescriber(this, internalFrames));
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        var describer = (JFrameDescriber) in.readObject();
        describer.restoreState(this);
        describer.restoreInternalFrames(internalFrames);
    }
}
