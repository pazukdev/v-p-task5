package com.pazukdev.entities;

import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
@Table(name = "CATEGORY")
public class Category extends AbstractEntity {

    @Column(name = "NAME")
    private String name;


    @Override
    public String toString() {
        return name;
    }


    @Override
    protected Category clone() throws CloneNotSupportedException {
        return (Category)super.clone();
    }


    public Category() {}


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

}