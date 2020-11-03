package com.unifi.attsw.exam.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "Museum")
@Table(name = "museums")
public class Museum {

	@Id
	@GeneratedValue(generator = "UUID")
	private UUID id;

	@Column(name = "Museum_Name", unique = true)
	private String name;

	@Column(name = "Number_Of_Rooms")
	private int totalRooms;

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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (occupiedRooms != other.occupiedRooms)
			return false;
		if (totalRooms != other.totalRooms)
			return false;
		return true;
	}

//	public List<Exhibition> getExhibitions() {
//		return exhibitions;
//	}
//
//	public void setExhibitions(List<Exhibition> exhibitions) {
//		this.exhibitions = exhibitions;
//	}

}
