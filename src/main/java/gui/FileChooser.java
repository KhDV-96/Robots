package gui;

import localization.LanguageManager;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;

class FileChooser extends JFileChooser {

    FileChooser(LanguageManager languageManager, FileFilter filter, int mode) {
        super(FileSystemView.getFileSystemView().getHomeDirectory());
        languageManager.bindField("chooser.title", this::setDialogTitle);
        setFileFilter(filter);
        setFileSelectionMode(mode);
        setMultiSelectionEnabled(true);
    }
}

