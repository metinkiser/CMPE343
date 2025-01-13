package CinemaCenter;

/**
 * An interface that defines a contract for objects that can be initialized with data.
 * Implementing classes should provide logic to initialize their state using the provided data.
 */
public interface DataInitializable {
    /**
     * Initializes the implementing object with the specified data.
     *
     * @param data The data object used to initialize this instance
     */
    void initData(Object data);
} 
