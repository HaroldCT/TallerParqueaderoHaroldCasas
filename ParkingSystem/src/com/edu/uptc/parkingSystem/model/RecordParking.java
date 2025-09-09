package com.edu.uptc.parkingSystem.model;

import java.time.LocalDateTime;

public class RecordParking {

	private String licensePlate;
	private LocalDateTime entryTime;
	private LocalDateTime departureTime;
	private int total;
	
	
	public RecordParking(String licensePlate, LocalDateTime entryTime) {
		this.licensePlate = licensePlate;
		this.entryTime = entryTime;
	}
	
	
	public RecordParking(String licensePlate, LocalDateTime entryTime, LocalDateTime departureTime, int total) {
		this.licensePlate = licensePlate;
		this.entryTime = entryTime;
		this.departureTime = departureTime;
		this.total = total;
	}


	public RecordParking() {
	}

	public String getLicensePlate() {
		return licensePlate;
	}
	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}
	public LocalDateTime getEntryTime() {
		return entryTime;
	}
	public void setEntryTime(LocalDateTime entryTime) {
		this.entryTime = entryTime;
	}
	public LocalDateTime getDepartureTime() {
		return departureTime;
	}
	public void setDepartureTime(LocalDateTime departureTime) {
		this.departureTime = departureTime;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	
	
}
