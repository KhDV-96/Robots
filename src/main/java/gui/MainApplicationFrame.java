package gui;

import game.Game;
import log.Logger;
import serialization.WindowStorage;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MainApplicationFrame extends JFrame implements Disposable {

    private static final int INSET = 50;

    private WindowStorage storage;
    private JInternalFrame logWindow, gameWindow, coordWindow;

    public MainApplicationFrame(WindowStorage storage) {
        this();
        this.storage = storage;
        if (storage != null && storage.isRestored()) {
            storage.restore(this.getClass().toString(), this);
            storage.restore(logWindow.getClass().toString(), logWindow);
            storage.restore(gameWindow.getClass().toString(), gameWindow);
            storage.restore(coordWindow.getClass().toString(), coordWindow);
        } else {
            setExtendedState(Frame.MAXIMIZED_BOTH);
            pack();
        }
    }

    public MainApplicationFrame() {
        var screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(INSET, INSET, screenSize.width - INSET * 2, screenSize.height - INSET * 2);
        setContentPane(new JDesktopPane());
        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onClose(MainApplicationFrame.this);
            }
        });

        logWindow = createLogWindow();
        addWindow(logWindow);
        setMinimumSize(logWindow.getSize());
        Logger.debug("Протокол работает");

        var game = new Game();

        gameWindow = createGameWindow(game);
        addWindow(gameWindow);

        coordWindow = createCoordinatesWindow(game);
        addWindow(coordWindow);
    }

    @Override
    public void onDispose() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        if (storage != null) {
            storage.store(this.getClass().toString(), this);
            storage.store(logWindow.getClass().toString(), logWindow);
            storage.store(gameWindow.getClass().toString(), gameWindow);
            storage.store(coordWindow.getClass().toString(), coordWindow);
            storage.save();
        }
    }

    private LogWindow createLogWindow() {
        var logWindow = new LogWindow(Logger.getDefaultLogSource());
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

    private GameWindow createGameWindow(Game game) {
        var gameWindow = new GameWindow(game);
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

    private ObservationWindow createCoordinatesWindow(Game game){
        var coordWindow = new ObservationWindow(game);
        coordWindow.setSize(200, 150);
        coordWindow.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        coordWindow.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                onClose(coordWindow);
            }
        });
        return coordWindow;
    }

    private void addWindow(JInternalFrame frame) {
        add(frame).setVisible(true);
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
        } catch (ReflectiveOperationException | UnsupportedLookAndFeelException ignored) {
        }
    }
}
