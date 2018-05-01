package com.pazukdev;

import javax.persistence.*;
import java.io.Serializable;

@SuppressWarnings("serial")
@Entity
@Table(name = "CATEGORY")
public class HotelCategory implements Serializable, Cloneable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "NAME")
    private String name;

    @Override
    public String toString() {
        return name;
    }

    @Override
    protected HotelCategory clone() throws CloneNotSupportedException {
        return (HotelCategory)super.clone();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (this.id == null) {
            return false;
        }
        if (obj instanceof HotelCategory && obj.getClass().equals(getClass())) {
            return this.id.equals(((HotelCategory) obj).id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 43 * hash + (id == null ? 0 : id.hashCode());
        return hash;
    }

    public HotelCategory() {}

    public HotelCategory(Integer id) {
        super();
        this.id = id;
    }

    public HotelCategory(String name) {
        super();
        this.name = name;
    }

    public HotelCategory(Integer id, String name) {
        super();
        this.id = id;
        this.name = name;
    }

    public boolean isPersisted() {
        return id != null;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

}