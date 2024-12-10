package Pojos;

/**
 * The `Symptoms` class represents a symptom with a unique identifier and a name.
 */
public class Symptoms {
    // Fields

    /** Unique identifier for the symptom. */
    private int id;

    /** Name of the symptom. */
    private String name;

    // Constructor

    /**
     * Constructs a new `Symptoms` object with a specified ID and name.
     *
     * @param id   the unique identifier for the symptom.
     * @param name the name of the symptom.
     */
    public Symptoms(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and Setters

    /**
     * Gets the unique identifier for the symptom.
     *
     * @return the symptom ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the unique identifier for the symptom.
     *
     * @param id the symptom ID to set.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the name of the symptom.
     *
     * @return the symptom name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the symptom.
     *
     * @param name the symptom name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    // Override Methods

    /**
     * Returns a string representation of the `Symptoms` object.
     *
     * <p>The representation includes the symptom's ID and name.</p>
     *
     * @return a string representation of the `Symptoms` object.
     */
    @Override
    public String toString() {
        return "Symptoms{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
