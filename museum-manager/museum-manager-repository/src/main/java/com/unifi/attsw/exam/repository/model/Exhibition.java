package com.unifi.attsw.exam.repository.model;

import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * Model for Exhibition entity.
 * 
 */
@Entity(name = "Exhibition")
@Table(name = "exhibitions")
public class Exhibition {
	/**
	 * Exhibition ID, generated automatically by Hibernate
	 */
	@Id
	@GeneratedValue(generator = "UUID")
	private UUID id;

	/**
	 * Exhibition name
	 */
	@Column(name = "Exhibition_Name", unique = true)
	private String name;

	/**
	 * Exhibition total number of seats (available or not)
	 */
	@Column(name = "Total_seats")
	private int totalSeats;

	/**
	 * Exhibition number of booked seats
	 */
	@Column(name = "Booked_seats")
	private int bookedSeats;

	/**
	 * Museum ID to which the exhibition belongs (this implements a many to one
	 * relation)
	 */
	@Column(name = "Museum_id", nullable = false)
	UUID museumId;

	public Exhibition(String name, int totalSeats) {
		this.name = name;
		this.totalSeats = totalSeats;
	}

	public Exhibition() {

	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTotalSeats() {
		return totalSeats;
	}

	public void setTotalSeats(int totalSeats) {
		this.totalSeats = totalSeats;
	}

	public int getBookedSeats() {
		return bookedSeats;
	}

	public void setBookedSeats(int bookedSeats) {
		this.bookedSeats = bookedSeats;
	}

	public UUID getMuseumId() {
		return museumId;
	}

	public void setMuseumId(UUID museumId) {
		this.museumId = museumId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bookedSeats;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((museumId == null) ? 0 : museumId.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + totalSeats;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Exhibition other = (Exhibition) obj;
		return Objects.equals(id, other.id) && Objects.equals(name, other.name)
				&& Objects.equals(totalSeats, other.totalSeats) && Objects.equals(bookedSeats, other.bookedSeats);
	}

}
