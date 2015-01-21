package app;

/**
 * Hash map implementation for Integer keys and Long values.
 * Uses open addressing (linear probing) collision resolution strategy.
 * This class permits null values, but does not allow null to be used as a key.
 * Provides constant-time average case performance for put and get, which may degrade to logarithmic in worst case.
 * With the default load factor (0.5) the average number of probes is 3/2 for search hit and 5/2 for search miss.
 */
public class OpenAddressingHashMap {
    private static final int DEFAULT_INITIAL_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.5;
    private int size;
    private int capacity;
    private final double loadFactor;
    private Integer[] keys;
    private Long[] values;

    /**
     * Constructs an empty OpenAddressingHashMap with the default initial capacity (16) and load factor (0.5).
     */
    public OpenAddressingHashMap() {
        this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Constructs an empty OpenAddressingHashMap with the specified initial capacity and load factor (0.5).
     *
     * @param capacity the initial capacity
     * @throws IllegalArgumentException if the specified initial capacity is nonpositive
     */
    public OpenAddressingHashMap(int capacity) {
        this(capacity, DEFAULT_LOAD_FACTOR);
    }

    /**
     * Constructs an empty OpenAddressingHashMap with the specified initial capacity and load factor.
     *
     * @param capacity   the initial capacity
     * @param loadFactor the load factor
     * @throws IllegalArgumentException if the specified initial capacity is nonpositive or load factor is nonpositive or greater than one
     */
    public OpenAddressingHashMap(int capacity, double loadFactor) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Initial capacity cannot be zero or negative.");
        }

        if (Double.compare(loadFactor, 0) <= 0 || Double.compare(loadFactor, 1) > 0) {
            throw new IllegalArgumentException("Load factor must greater than 0 and less than or equal to 1.");
        }

        this.capacity = capacity;
        this.loadFactor = loadFactor;
        keys = new Integer[capacity];
        values = new Long[capacity];
    }

    /**
     * Computes hash for key as a remainder after division of lowest 31 bits of key.hashCode() by map's capacity.
     * For Integer, key.hashCode() is the primitive value. If the specified key is null, 0 is returned.
     *
     * @param key key to compute the hash for
     * @return hash for specified key, 0 if the specified key is null
     */
    private int hash(Integer key) {
        int keyHashCode = (key == null) ? 0 : key.hashCode();
        return (keyHashCode & Integer.MAX_VALUE) % capacity;
    }

    /**
     * Resizes this map to the specified capacity by recreating the underlying keys and values arrays and rehashing all keys.
     * @param newCapacity capacity to resize this map to
     */
    private void resize(int newCapacity) {
        OpenAddressingHashMap resizedMap = new OpenAddressingHashMap(newCapacity);

        /* Rehash all keys */
        for (int i = 0; i < capacity; ++i) {
            if (keys[i] != null) {
                resizedMap.put(keys[i], values[i]);
            }
        }

        this.keys = resizedMap.keys;
        this.values = resizedMap.values;
        this.capacity = resizedMap.capacity;
    }

    /**
     * Maps the specified value to the specified key. If the map previously contained a value for this key, the old value is replaced.
     * If the size of map is greater than or equal to (loadFactor * capacity) before insertion of the new entry, the map capacity is doubled.
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous value mapped to the key, or null if there was none
     * @throws NullPointerException if the specified key is null
     */
    public Long put(Integer key, Long value) {
        if (key == null) {
            throw new NullPointerException("Null is not allowed to be used as a key.");
        }

        /* Double the capacity when the load factor exceeds the desired one */
        if (size >= loadFactor * capacity) {
            resize(2 * capacity);
        }

        int i = hash(key); // "hashed-to" slot

        /* Loop through the array of keys until unoccupied slot is found */
        for (; keys[i] != null; i = (i + 1) % capacity) {

            /* If the same key is found, reset the value and return the previous one */
            if (keys[i].equals(key)) {
                Long previous = values[i];
                values[i] = value;
                return previous;
            }
        }

        /* Unoccupied slot is found. Put the new entry in */
        keys[i] = key;
        values[i] = value;
        ++size;

        /* No previous value */
        return null;
    }

    /**
     * Returns the value mapped to the specified key or null if no value is associated with this key.
     * @param key the key whose associated value is to be returned
     * @return the value associated with the key, or null if there is no value for the key
     * @throws NullPointerException if the specified key is null
     */
    public Long get(Integer key) {
        if (key == null) {
            throw new NullPointerException("Null is not allowed to be used as a key.");
        }

        /* Loop through the array of keys until unoccupied slot is found */
        for (int i = hash(key); keys[i] != null; i = (i + 1) % size) {
            /* The key we are looking for is found, return the corresponding value */
            if (keys[i].equals(key)) {
                return values[i];
            }
        }

        /* Search miss */
        return null;
    }

    /**
     * Returns the number of key-value pair in this map.
     * @return the number of key-value pairs in map
     */
    public int size() {
        return size;
    }
}
