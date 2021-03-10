package de.dbvis.sparta.server.rest.model.intersection;

import de.dbvis.sparta.server.rest.model.DataItem;
import de.dbvis.sparta.server.rest.model.ItemType;

import java.io.Serializable;

public class ReferencedItem extends DataItem implements Serializable {

    private static final long serialVersionUID = 14L;

    private ItemType type;
    private Object data;
    private IdReferences refs;

    public ReferencedItem() {

    }

    public ReferencedItem(int id, ItemType type, Object data, IdReferences refs) {
        super(id);
        this.type = type;
        this.data = data;
        this.refs = refs;
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public IdReferences getRefs() {
        return refs;
    }

    public void setRefs(IdReferences refs) {
        this.refs = refs;
    }

}
