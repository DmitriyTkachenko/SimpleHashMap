package app;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Random;

import static org.junit.Assert.assertEquals;

public class OpenAddressingHashMapTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    private Random rand = new Random();

    @Test
    public void testSize() throws Exception {
        OpenAddressingHashMap hashMap = new OpenAddressingHashMap();
        assertEquals(0, hashMap.size());
        hashMap.put(10, 66556543L);
        assertEquals(1, hashMap.size());
    }

    @Test
    public void testPutAndGet() throws Exception {
        OpenAddressingHashMap hashMap = new OpenAddressingHashMap();
        hashMap.put(42, -42L);
        hashMap.put(19, 4215412L);
        assertEquals((Long) (-42L), hashMap.get(42));
    }

    @Test
    public void testSearchMiss() throws Exception {
        OpenAddressingHashMap hashMap = new OpenAddressingHashMap();
        hashMap.put(5, 4214124L);
        hashMap.put(999, 32158153L);
        hashMap.put(48239, 215581L);
        assertEquals(null, hashMap.get(11));
    }

    @Test
    public void testReplaceValueForKey() throws Exception {
        OpenAddressingHashMap hashMap = new OpenAddressingHashMap();
        hashMap.put(1111, 10000L);
        hashMap.put(42, 0L);
        assertEquals((Long) 0L, hashMap.get(42));

        Long previousValue = hashMap.put(42, 666L);
        assertEquals((Long) 0L, previousValue); // previous value
        assertEquals((Long) 666L, hashMap.get(42)); // new value
    }

    @Test
    public void testPutValueForNullKey() throws Exception {
        OpenAddressingHashMap hashMap = new OpenAddressingHashMap();
        exception.expect(NullPointerException.class);
        hashMap.put(null, 11L);
    }

    @Test
    public void testGetValueForNullKey() throws Exception {
        OpenAddressingHashMap hashMap = new OpenAddressingHashMap();
        hashMap.put(13, 12L);
        hashMap.put(22, 30L);
        exception.expect(NullPointerException.class);
        hashMap.get(null);
    }

    @Test
    public void testPutNullValue() throws Exception {
        OpenAddressingHashMap hashMap = new OpenAddressingHashMap();
        hashMap.put(5, 13L);
        hashMap.put(42, null);
        assertEquals(null, hashMap.get(42));
    }

    @Test
    public void testResizing() throws Exception {
        OpenAddressingHashMap hashMap = new OpenAddressingHashMap(64);
        for (int i = 0; i < 100; ++i) {
            hashMap.put(rand.nextInt(), rand.nextLong());
        }
    }

    /* Constructor exceptions tests */
    @Test
    public void testLoadFactorGreaterThanOneConstructor() throws Exception {
        exception.expect(IllegalArgumentException.class);
        OpenAddressingHashMap hashMap = new OpenAddressingHashMap(64, 1.05);
    }

    @Test
    public void testZeroLoadFactorConstructor() throws Exception {
        exception.expect(IllegalArgumentException.class);
        OpenAddressingHashMap hashMap = new OpenAddressingHashMap(32, 0);
    }

    @Test
    public void testZeroInitialCapacityConstructor() throws Exception {
        exception.expect(IllegalArgumentException.class);
        OpenAddressingHashMap hashMap = new OpenAddressingHashMap(0);
    }

    @Test
    public void testNegativeInitialCapacityConstructor() throws Exception {
        exception.expect(IllegalArgumentException.class);
        OpenAddressingHashMap hashMap = new OpenAddressingHashMap(-1);
    }
}