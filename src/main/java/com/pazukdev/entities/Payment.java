package com.pazukdev.entities;


import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

@SuppressWarnings("serial")
@Embeddable
public class Payment implements Serializable {

    @Column(name = "PAYMENT")
    private Integer paymentValue;

    @Transient
    private boolean operable = true;


    public Payment() {}


    @Override
    public String toString() {
        return paymentValue.toString()+"%";
    }


    public Integer getPaymentValue() {
        return paymentValue;
    }

    public void setPaymentValue(Integer paymentValue) {
        this.paymentValue = paymentValue;
    }


    public boolean isOperable() {
        return operable;
    }

    public void setOperable(boolean operable) {
        this.operable = operable;
    }

}