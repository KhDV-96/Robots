package log;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LogWindowSourceTest {

    @Test
    void addMoreThanLenght() {
        var length = 5;
        var lws = new LogWindowSource(length);

        for (var i = 0; i < length * 2; i++)
            lws.append(null, null);

        assertEquals(length, lws.size());
    }

    @Test
    void sizeZero() {
        var lws = new LogWindowSource(10);

        assertEquals(0, lws.size());
    }

    @Test
    void sizeNonZero() {
        var lws = new LogWindowSource(10);
        var expectedSize = 5;

        for (var i = 0; i < expectedSize; i++)
            lws.append(null, null);

        assertEquals(expectedSize, lws.size());
    }
}
