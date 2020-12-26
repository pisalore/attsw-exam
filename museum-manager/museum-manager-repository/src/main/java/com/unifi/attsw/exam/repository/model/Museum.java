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
 * Model for Museum entity.
 * 
 *
 */
@Entity(name = "Museum")
@Table(name = "museums")
public class Museum {

	/**
	 * Museum ID, generated automatically by Hibernate
	 */
	@Id
	@GeneratedValue(generator = "UUID")
	private UUID id;

	/*
	 * Museum name
	 */
	@Column(name = "Museum_Name", unique = true)
	private String name;

	/*
	 * Museum total number of rooms (available or not)
	 */
	@Column(name = "Number_Of_Rooms")
	private int totalRooms;

	/**
	 * Museum number of occupied rooms (where there is an ongoing Exhibition)
	 */
	@Column(name = "Number_Of_Occupied_Rooms")
	private int occupiedRooms;

	public Museum(String name, int rooms) {
		this.name = name;
		this.totalRooms = rooms;
		this.occupiedRooms = 0;

	}

	public Museum() {

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

	public int getTotalRooms() {
		return totalRooms;
	}

	public void setRooms(int totalRooms) {
		this.totalRooms = totalRooms;
	}

	public int getOccupiedRooms() {
		return occupiedRooms;
	}

	public void setOccupiedRooms(int occupiedRooms) {
		this.occupiedRooms = occupiedRooms;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + occupiedRooms;
		result = prime * result + totalRooms;
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
		Museum other = (Museum) obj;
		return Objects.equals(id, other.id) && Objects.equals(name, other.name)
				&& Objects.equals(occupiedRooms, other.occupiedRooms) && Objects.equals(totalRooms, other.totalRooms);
	}

}
