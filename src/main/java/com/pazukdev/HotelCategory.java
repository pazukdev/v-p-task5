package com.pazukdev;

import javax.persistence.*;
import java.io.Serializable;

@SuppressWarnings("serial")
@Entity
@Table(name = "CATEGORY")
public class HotelCategory extends AbstractEntity {

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


    public HotelCategory() {}


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

}