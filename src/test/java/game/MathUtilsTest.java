package game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MathUtilsTest {

    @Test
    void asNormalizedRadiansZero() {
        assertEquals(0, MathUtils.asNormalizedRadians(0));
    }

    @Test
    void asNormalizedRadiansPI() {
        assertEquals(Math.PI, MathUtils.asNormalizedRadians(Math.PI));
    }

    @Test
    void asNormalizedRadiansValueInRange() {
        assertEquals(2 * Math.PI / 3, MathUtils.asNormalizedRadians(2 * Math.PI / 3));
    }

    @Test
    void asNormalizedRadiansTwoPI() {
        assertEquals(0, MathUtils.asNormalizedRadians(Math.PI * 2));
    }

    @Test
    void asNormalizedRadiansGreaterTwoPI() {
        assertEquals(Math.PI / 2, MathUtils.asNormalizedRadians(5 * Math.PI / 2));
    }

    @Test
    void asNormalizedRadiansLessZero() {
        assertEquals(3 * Math.PI / 2, MathUtils.asNormalizedRadians(-Math.PI / 2));
    }

    @Test
    void minByModulusZeros() {
        assertEquals(0.0, MathUtils.minByModulus(0.0, 0.0));
    }

    @Test
    void minByModulusPositive() {
        assertEquals(5.0, MathUtils.minByModulus(5.0, 10.0));
    }

    @Test
    void minByModulusNegative() {
        assertEquals(-5.0, MathUtils.minByModulus(-5.0, -10.0));
    }

    @Test
    void minByModulusMixed() {
        assertEquals(5.0, MathUtils.minByModulus(5.0, -10.0));
        assertEquals(-5.0, MathUtils.minByModulus(-5.0, 10.0));
    }
}
