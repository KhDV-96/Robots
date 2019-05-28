package loaders;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

public class JarClassLoader extends URLClassLoader {

    public JarClassLoader(String... paths) {
        super(Arrays.stream(paths).map(JarClassLoader::convertPathToJarURL).toArray(URL[]::new));
    }

    private static URL convertPathToJarURL(String path) {
        try {
            return new URL("jar", "", "file:" + path + "!/");
        } catch (MalformedURLException exception) {
            throw new RuntimeException(exception);
        }
    }
}
