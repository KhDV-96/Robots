package log;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class LogWindowSourceTest {

    @Test
    void addMoreThanLength() {
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

    @Test
    void rangeNegativeStartFromValue() {
        var lws = new LogWindowSource(10);
        for (var i = 0; i < 5; i++)
            lws.append(null, Integer.toString(i));

        assertIterableEquals(Collections.emptyList(), lws.range(-1, 3));
    }

    @Test
    void rangeSubset() {
        var entries = new ArrayList<LogEntry>();
        var lws = new LogWindowSource(10);
        for (var i = 0; i < 5; i++) {
            var message = Integer.toString(i);
            entries.add(new LogEntry(null, message));
            lws.append(null, message);
        }
        var expected = entries.subList(1, 4);

        var actual = lws.range(1, 3);

        var i = 0;
        for (var entry : actual)
            assertEquals(expected.get(i++).getMessage(), entry.getMessage());
    }

    @Test
    void rangeLargeCount() {
        var entries = new ArrayList<LogEntry>();
        var lws = new LogWindowSource(10);
        for (var i = 0; i < 5; i++) {
            var message = Integer.toString(i);
            entries.add(new LogEntry(null, message));
            lws.append(null, message);
        }

        var actual = lws.range(0, 20);

        var i = 0;
        for (var entry : actual) {
            assertTrue(i < entries.size());
            assertEquals(entries.get(i++).getMessage(), entry.getMessage());
        }
    }
}
