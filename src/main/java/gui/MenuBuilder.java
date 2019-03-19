package gui;

import javax.swing.*;
import java.awt.event.ActionListener;

class MenuBuilder {

    private JMenu menu;

    MenuBuilder(String name) {
        menu = new JMenu(name);
    }

    MenuBuilder setMnemonic(int mnemonic) {
        menu.setMnemonic(mnemonic);
        return this;
    }

    MenuBuilder setDescription(String description) {
        menu.getAccessibleContext().setAccessibleDescription(description);
        return this;
    }

    MenuBuilder addMenuItem(String name, int mnemonic, ActionListener listener) {
        var item = new JMenuItem(name, mnemonic);
        item.addActionListener(listener);
        menu.add(item);
        return this;
    }

    JMenu build() {
        return menu;
    }
}
