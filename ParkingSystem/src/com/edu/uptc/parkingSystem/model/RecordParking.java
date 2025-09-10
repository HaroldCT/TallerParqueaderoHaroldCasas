package com.edu.uptc.parkingSystem.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.Temporal;
import java.util.Objects;

public class RecordParking {
	private String licensePlate;
	private LocalDateTime entryTime;
	private LocalDateTime departureTime;
	private long total; 

	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

	public RecordParking(String licensePlate, String entryTime, String departureTime) {
		this.licensePlate = licensePlate;
		// Intenta analizar con el formateador personalizado, si falla, intenta con el formato ISO por defecto.
		try {
			this.entryTime = LocalDateTime.parse(entryTime, formatter);
		} catch (DateTimeParseException e) {
			this.entryTime = LocalDateTime.parse(entryTime);
		}

		if (departureTime != null && !departureTime.isEmpty() && !"null".equalsIgnoreCase(departureTime)) {
			try {
				this.departureTime = LocalDateTime.parse(departureTime, formatter);
			} catch (DateTimeParseException e) {
				this.departureTime = LocalDateTime.parse(departureTime);
			}
		} else {
			this.departureTime = null;
		}
	}

	public RecordParking(String licensePlate, LocalDateTime entryTime) {
		this.licensePlate = licensePlate;
		this.entryTime = entryTime;
		this.departureTime = null;
	}
	
	

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public Temporal getEntryTime() { 
		return entryTime;
	}

	public void setEntryTime(LocalDateTime entryTime) {
		this.entryTime = entryTime;
	}

	public String getDepartureTime() {
		if(!Objects.isNull(departureTime)) {
			return departureTime.format(formatter);
		}
		return null;
	}

	public void setDepartureTime(LocalDateTime departureTime) {
		 if (departureTime == null) {
		        throw new IllegalArgumentException("La fecha de salida no puede ser nula.");
		    }
		    if (!departureTime.isAfter(entryTime)) {
		        throw new IllegalArgumentException("La fecha de salida debe ser posterior a la de entrada.");
		    }
	    
	    this.departureTime = departureTime;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public DateTimeFormatter getFormatter() {
		return formatter;
	}

	public void setFormatter(DateTimeFormatter formatter) {
		this.formatter = formatter;
	}
	
	public LocalDateTime getEntryTimeAsLocalDateTime() {
		return entryTime;
	}
	
	

}