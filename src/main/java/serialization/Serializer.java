package serialization;

import java.io.*;

class Serializer {

    static void save(Serializable serializable, String path) {
        try (var out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(path)))) {
            out.writeObject(serializable);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    static Object load(String path) {
        var file = new File(path);
        if (file.isFile()) {
            try (var in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {
                return in.readObject();
            } catch (IOException | ClassNotFoundException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }
}
