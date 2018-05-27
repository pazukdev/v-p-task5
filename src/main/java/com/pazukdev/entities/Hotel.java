package com.pazukdev.entities;



import javax.persistence.*;


@SuppressWarnings("serial")
@Entity
@Table(name="HOTEL")
public class Hotel extends AbstractEntity {

	@Column(name="NAME")
	private String name;

    @Column(name="ADDRESS")
	private String address;

	@Column(name="RATING")
	private Integer rating;

	@Column(name="OPERATES_FROM")
	private Long operatesFromDay;

    @Column(name = "CATEGORY")
    private Long categoryId;

	@Column(name="URL")
	private String url="http://";

	@Column(name = "DESCRIPTION", columnDefinition = "text")
	private String description;

    @Embedded
    @Column(name="PAYMENT")
    private Payment payment;


	@Override
	public String toString() {
		return name + " hotel. Address: " + address + ". Rating: " + rating + " stars";
	}


	@Override
	protected Hotel clone() throws CloneNotSupportedException {
		return (Hotel)super.clone();
	}


	public Hotel() {}


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}


	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}


	public Integer getRating() {
		return rating;
	}
	public void setRating(Integer rating) {
		this.rating = rating;
	}


	public Long getOperatesFromDay() {
		return operatesFromDay;
	}
    public void setOperatesFromDay(Long operatesFromDay) {
        this.operatesFromDay = operatesFromDay;
    }


	public Long getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(Long categoryId) {
	    this.categoryId = categoryId;
	}


	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
	    this.url=url;
	}


	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description=description;
	}


	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}
}


















