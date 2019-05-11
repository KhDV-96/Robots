package localization;

import java.util.*;
import java.util.function.Consumer;

public class LanguageManager {

    private static final ResourceBundle.Control CONTROL =
            ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_DEFAULT);

    private final String baseName;
    private final List<ResourceItem> items;
    private ResourceBundle resourceBundle;

    public LanguageManager(String baseName) {
        this.baseName = baseName;
        items = new LinkedList<>();
        resourceBundle = ResourceBundle.getBundle(baseName, Locale.getDefault(), CONTROL);
    }

    public void bindField(String key, Consumer<String> setter) {
        var item = new ResourceItem(key, setter);
        items.add(item);
        updateItem(item);
    }

    public String getString(String key) {
        // TODO: delete this try-catch after completion of the translation
        try {
            return resourceBundle.getString(key);
        } catch (MissingResourceException exception) {
            return "ERROR";
        }
    }

    public void changeLocale(Locale locale) {
        if (!resourceBundle.getLocale().equals(locale)) {
            resourceBundle = ResourceBundle.getBundle(baseName, locale, CONTROL);
            items.forEach(this::updateItem);
        }
    }

    private void updateItem(ResourceItem item) {
        item.setter.accept(getString(item.key));
    }

    private static class ResourceItem {

        private final String key;
        private final Consumer<String> setter;

        private ResourceItem(String key, Consumer<String> setter) {
            this.key = key;
            this.setter = setter;
        }
    }
}
