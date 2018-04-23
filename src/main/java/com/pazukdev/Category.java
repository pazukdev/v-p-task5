package com.pazukdev;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Category implements Serializable, Cloneable {

    private Long id;
    private String name;

    @Override
    public String toString() {
        return name;
    }

    @Override
    protected Category clone() throws CloneNotSupportedException {
        return (Category)super.clone();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (this.id == null) {
            return false;
        }
        if (obj instanceof Category && obj.getClass().equals(getClass())) {
            return this.id.equals(((Category) obj).id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + (id == null ? 0 : id.hashCode());
        return hash;
    }

    public Category() {}
    public Category(String name) {
        super();
        this.name = name;
    }


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

}