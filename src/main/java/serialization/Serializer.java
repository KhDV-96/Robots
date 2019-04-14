package serialization;

import java.io.*;

public class Serializer {

    public static void save(Serializable serializable, String file) {
        try (var out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
            out.writeObject(serializable);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static Object load(String file) {
        try (var in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {
            return in.readObject();
        } catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
            return null;
        }
    }
}
