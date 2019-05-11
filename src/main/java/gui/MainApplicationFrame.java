package gui;

import localization.LanguageManager;
import log.Logger;
import serialization.WindowStorage;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Locale;

public class MainApplicationFrame extends JFrame implements Disposable {

    private static final Locale LOCALE_RU = new Locale("ru");
    private static final int INSET = 50;

    private WindowStorage storage;
    private JInternalFrame logWindow, gameWindow;

    public MainApplicationFrame(WindowStorage storage, LanguageManager languageManager) {
        this(languageManager);
        this.storage = storage;
        if (storage != null && storage.isRestored()) {
            storage.restore(this.getClass().toString(), this);
            storage.restore(logWindow.getClass().toString(), logWindow);
            storage.restore(gameWindow.getClass().toString(), gameWindow);
        } else {
            setExtendedState(Frame.MAXIMIZED_BOTH);
            pack();
        }
    }

    public MainApplicationFrame(LanguageManager languageManager) {
        var screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(INSET, INSET, screenSize.width - INSET * 2, screenSize.height - INSET * 2);
        setContentPane(new JDesktopPane());
        setJMenuBar(generateMenuBar(languageManager));
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onClose(languageManager, MainApplicationFrame.this);
            }
        });

        logWindow = createLogWindow(languageManager);
        addWindow(logWindow);
        setMinimumSize(logWindow.getSize());
        Logger.debug("Протокол работает");

        gameWindow = createGameWindow(languageManager);
        addWindow(gameWindow);
    }

    @Override
    public void onDispose() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        if (storage != null) {
            storage.store(this.getClass().toString(), this);
            storage.store(logWindow.getClass().toString(), logWindow);
            storage.store(gameWindow.getClass().toString(), gameWindow);
            storage.save();
        }
    }

    private LogWindow createLogWindow(LanguageManager languageManager) {
        var logWindow = new LogWindow(Logger.getDefaultLogSource(), languageManager);
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        logWindow.pack();
        logWindow.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        logWindow.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                onClose(languageManager, logWindow);
            }
        });
        return logWindow;
    }

    private GameWindow createGameWindow(LanguageManager languageManager) {
        var gameWindow = new GameWindow(languageManager);
        gameWindow.setSize(400, 400);
        gameWindow.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        gameWindow.addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                onClose(languageManager, gameWindow);
            }
        });
        return gameWindow;
    }

    private void addWindow(JInternalFrame frame) {
        add(frame).setVisible(true);
    }

    private JMenuBar generateMenuBar(LanguageManager languageManager) {
        var menuBar = new JMenuBar();

        var fileMenu = new MenuBuilder(languageManager)
                .setText("Файл")
                .setMnemonic(KeyEvent.VK_F)
                .addMenuItem("Выход", KeyEvent.VK_Q,
                        e -> dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING)))
                .build();
        menuBar.add(fileMenu);

        var lookAndFeelMenu = new MenuBuilder(languageManager)
                .setText("Режим отображения")
                .setMnemonic(KeyEvent.VK_S)
                .setDescription("Управление режимом отображения приложения")
                .addMenuItem("Системная схема", KeyEvent.VK_S,
                        e -> setLookAndFeel(UIManager.getSystemLookAndFeelClassName()))
                .addMenuItem("Универсальная схема", KeyEvent.VK_S,
                        e -> setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()))
                .build();
        menuBar.add(lookAndFeelMenu);

        var testMenu = new MenuBuilder(languageManager)
                .setText("Тесты")
                .setMnemonic(KeyEvent.VK_S)
                .setDescription("Тестовые команды")
                .addMenuItem("Сообщение в лог", KeyEvent.VK_S, e -> Logger.debug("Новая строка"))
                .build();
        menuBar.add(testMenu);

        var languageMenu = new MenuBuilder(languageManager)
                .setText("languageMenu.text")
                .addMenuItem("languageMenu.english", KeyEvent.VK_E,
                        e -> languageManager.changeLocale(Locale.ENGLISH))
                .addMenuItem("languageMenu.russian", KeyEvent.VK_R,
                        e -> languageManager.changeLocale(LOCALE_RU))
                .build();
        menuBar.add(languageMenu);

        return menuBar;
    }

    private void onClose(LanguageManager languageManager, Disposable disposable) {
        int confirmed = JOptionPane.showOptionDialog(this,
                languageManager.getString("confirmDialog.message"),
                languageManager.getString("confirmDialog.title"),
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{
                        languageManager.getString("confirmDialog.yes"),
                        languageManager.getString("confirmDialog.no")
                },
                JOptionPane.NO_OPTION);

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
