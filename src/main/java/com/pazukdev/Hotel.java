package com.pazukdev;



import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;


@SuppressWarnings("serial")
@Entity
@Table(name="HOTEL")
public class Hotel implements Serializable, Cloneable {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @Column(name="NAME")
	private String name;

    @Column(name="ADDRESS")
	private String address;

	@Column(name="RATING")
	private Integer rating;

	@Column(name="OPERATES_FROM")
	private Long operatesFromDay;

	@Column(name="CATEGORY")
	private Integer categoryID;

	@Column(name="URL")
	private String url="http://";

	@Column(name = "DESCRIPTION", columnDefinition = "text")
	private String description;

	public boolean isPersisted() {
		return id != null;
	}

	@Override
	public String toString() {
		return name + " hotel. Address: " + address + ". Rating: " + rating + " stars";
	}

	@Override
	protected Hotel clone() throws CloneNotSupportedException {
		return (Hotel)super.clone();
	}

	@Override
	public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (this.id == null) {
            return false;
        }

        if (obj instanceof Hotel && obj.getClass().equals(getClass())) {
            return this.id.equals(((Hotel) obj).id);
        }

        return false;
	}

	@Override
	public int hashCode() {
        int hash = 5;
        hash = 43 * hash + (id == null ? 0 : id.hashCode());
        return hash;
	}

    public Hotel() {}

	/*public Hotel(LocalDate yesterday) {
	    this.operatesFromDay = yesterday.toEpochDay();
    }*/

    public Hotel(Long id, String name, String address, Integer rating, Long operatesFromDay, Integer categoryID,
				 String url, String description) {
        super();
        this.id = id;
        this.name = name;
        this.address = address;
        this.rating = rating;
        this.operatesFromDay = operatesFromDay;
        this.categoryID = categoryID;
        this.url = url;
        this.description=description;
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


	public Integer getCategoryID() {
		return categoryID;
	}
	public void setCategoryID(Integer categoryID) {
	    this.categoryID = categoryID;
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



}