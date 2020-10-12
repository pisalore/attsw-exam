package com.unifi.attws.exam.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "Museum")
@Table(name = "museums")
public final class Museum{
	
	@Id
	@GeneratedValue(generator = "UUID")
	private UUID id;

	@Column(name = "Museum_Name", unique=true)
	private String name;

	@Column(name = "Number_Of_Rooms")
	private int rooms;

	@Column(name = "Number_Of_Occupied_Rooms")
	private int occupiedRooms;
	
//	@OneToMany(cascade = CascadeType.ALL, mappedBy = "museum", orphanRemoval = true)
//	private List<Exhibition> exhibitions;

	public Museum(String name, int rooms) {
		this.name = name;
		this.rooms = rooms;
		this.occupiedRooms = 0;
//		this.exhibitions = new ArrayList<Exhibition>();

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

	public int getRooms() {
		return rooms;
	}

	public void setRooms(int rooms) {
		this.rooms = rooms;
	}

	public int getOccupiedRooms() {
		return occupiedRooms;
	}

	public void setOccupiedRooms(int availableRooms) {
		this.occupiedRooms = availableRooms;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + occupiedRooms;
		result = prime * result + rooms;
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
		if (rooms != other.rooms)
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
