package loaders;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

public class JarClassLoader extends URLClassLoader {

    public JarClassLoader(File... jars) {
        super(Arrays.stream(jars).map(JarClassLoader::convertPathToJarURL).toArray(URL[]::new));
    }

    private static URL convertPathToJarURL(File jar) {
        try {
            return new URL("jar", "", jar.toURI().toURL() + "!/");
        } catch (MalformedURLException exception) {
            throw new RuntimeException(exception);
        }
    }
}
