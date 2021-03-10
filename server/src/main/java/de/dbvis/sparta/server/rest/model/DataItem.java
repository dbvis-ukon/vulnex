package de.dbvis.sparta.server.rest.model;

import java.io.Serializable;

/**
 * Defines a data item as an object with an ID.
 */
public abstract class DataItem implements Serializable {

    /**
     * A unique identifier
     */
    private int id;

    public DataItem() {

    }

    public DataItem(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
