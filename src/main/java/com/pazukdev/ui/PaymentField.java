package com.pazukdev.ui;


import com.pazukdev.converters.StringToIntegerPaymentConverter;
import com.pazukdev.entities.Payment;
import com.vaadin.data.Binder;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SuppressWarnings("serial")
public class PaymentField extends CustomField<Payment> {
    private Payment payment;
    private Binder<Payment> binder;

    private RadioButtonGroup<String> radioButton;
    private TextField valueField;
    private Label label;
    private String caption;

    private Integer oldValue;
    private Integer newValue;
    private boolean valueProcessed = true;


    public PaymentField(String caption) {
        super();
        this.caption = caption;
        getContent();
    }


    @Override
    protected Component initContent() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setMargin(false);
        super.setCaption(caption);

        valueField = new TextField("Value");
        valueField.setId("paymentValueTextField");
        valueField.setPlaceholder("1-100%");
        valueField.addValueChangeListener(event -> {
            oldValue = newValue;
            Payment notOperablePayment = new Payment();
            notOperablePayment.setOperable(false);
            try {
                newValue = Integer.valueOf(valueField.getValue());
            } catch (NumberFormatException ex) {
                newValue = -1;
            }

            valueProcessed = false;
            setValue(notOperablePayment);
        });

        binder = new Binder<>(Payment.class);
        binder.forField(valueField)
                .asRequired("The field shouldn't be empty")
                .withNullRepresentation("")
                .withConverter(new StringToIntegerPaymentConverter())
                .withValidator(value -> radioButton.getSelectedItem().get() == "Cash" || (value > 0 && value <= 100),
                        "Wrong value. Valid values integer numbers from 1 to 100")
                .bind(Payment::getPaymentValue, Payment::setPaymentValue);

        label = new Label("Payment will be made directly in the hotel");



        setDescription("Payment method: Credit Card for prepayment or Cash for payment on arrival" );

        addValueChangeListener(event -> {
            if (!valueProcessed) {
                Notification.show("Payment changed", reportBuilder(), Notification.Type.TRAY_NOTIFICATION);
                valueProcessed = true;
            }
        });

        radioButton = new RadioButtonGroup<>();
        radioButton.setItems("Credit Card", "Cash");
        radioButton.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
        radioButton.setSelectedItem("Cash");
        radioButton.addSelectionListener(e -> {
            Payment notOperablePayment = new Payment();
            notOperablePayment.setOperable(false);

            if(radioButton.getSelectedItem().get() == "Cash") {
                valueField.setVisible(false);
                label.setVisible(true);
                try {
                    oldValue = Integer.valueOf(valueField.getValue());
                } catch (NumberFormatException ex) {
                    oldValue = -1;
                }
                newValue = null;
            }
            if(radioButton.getSelectedItem().get() == "Credit Card") {
                valueField.setVisible(true);
                label.setVisible(false);
                oldValue = null;
                try {
                    newValue = Integer.valueOf(valueField.getValue());
                } catch (NumberFormatException ex) {
                    newValue = -1;
                }
            }

            valueProcessed = false;
            setValue(notOperablePayment);
        });

        verticalLayout.addComponents(radioButton, valueField, label);
        return verticalLayout;
    }


    private String reportBuilder() {
        String firstValue;
        String secondValue;
        if (oldValue == null) {
            firstValue = "Cash";
        } else if (oldValue < 1 || oldValue > 100) {
            firstValue = "Invalid value";
        } else {
            firstValue = oldValue.toString();
        }
        if (newValue == null) {
            secondValue = "Cash";
        } else if (newValue < 1 || newValue > 100) {
            secondValue = "Invalid value";
        } else {
            secondValue = newValue.toString();
        }
        return "from " + firstValue + " to " + secondValue;
    }

    @Override
    public Payment getValue() {
        binder.validate();
        if(radioButton.getSelectedItem().get() == "Cash") {
            Payment payment = new Payment();
            payment.setPaymentValue(null);
            return payment;
        }
        if (binder.isValid()) {
            return payment;
        }
        Payment payment = new Payment();
        payment.setPaymentValue(-1);
        return payment;
    }


    @Override
    protected void doSetValue(Payment payment) {
        if (payment != null && !payment.isOperable()) {
            return;
        } else {
            this.payment = new Payment();
            try {
                this.payment.setPaymentValue(payment.getPaymentValue());
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            binder.removeBean();
            binder.setBean(this.payment);
            if (this.payment.getPaymentValue() == null) {
                radioButton.setSelectedItem("Cash");
                valueField.setVisible(false);
                label.setVisible(true);
            } else {
                radioButton.setSelectedItem("Credit Card");
                valueField.setVisible(true);
                label.setVisible(false);
            }
        }
    }

}