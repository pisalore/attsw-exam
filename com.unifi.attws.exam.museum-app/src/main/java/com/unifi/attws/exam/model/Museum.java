package com.unifi.attws.exam.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "Museum")
@Table(name = "museums")
public final class Museum {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "Museum_Name", unique=true)
	private String name;

	@Column(name = "Number_Of_Rooms")
	private int rooms;

	@Column(name = "Number_Of_Occupied_Rooms")
	private int occupiedRooms;

	public Museum(String name, int rooms) {
		this.name = name;
		this.rooms = rooms;
		this.occupiedRooms = 0;

	}
	
	public Museum() {
		
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
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

}
