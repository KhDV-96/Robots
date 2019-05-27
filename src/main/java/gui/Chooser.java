package gui;

import localization.LanguageManager;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

class Chooser extends JFileChooser{

//    Chooser(LanguageManager languageManager){
//        languageManager.bindField("chooser.tile", this::setDialogTitle);
//    }

    static JFileChooser getChooser() {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setDialogTitle("Choose File");
        jfc.setMultiSelectionEnabled(true);
        jfc.setFileFilter(
                new FileNameExtensionFilter("Jar", "jar")
        );
        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        return jfc;
    }
}

