package com.edu.uptc.parkingSystem.control;

import java.time.Duration;
import java.time.LocalDateTime;
import com.edu.uptc.parkingSystem.model.RecordParking;
import com.edu.uptc.parkingSystem.model.User;
import com.edu.uptc.parkingSystem.model.Vehicle;
import com.edu.uptc.parkingSystem.model.VehicleRate;
import com.edu.uptc.parkingSystem.persistence.ParkingSystemPersistence;

public class Controller {
    private ParkingSystemPersistence parkingSystemPersistence;

    public Controller() {
        parkingSystemPersistence = new ParkingSystemPersistence();
        parkingSystemPersistence.loadFileTotal();
    }
    public void addVehicleRate() {
        this.parkingSystemPersistence.getListVehicleRates().add(new VehicleRate("Carro", 1000));
        this.parkingSystemPersistence.getListVehicleRates().add(new VehicleRate("Moto", 500));
        this.parkingSystemPersistence.getListVehicleRates().add(new VehicleRate("Bicicleta", 200));
    }
    public void addUser(User user) {
        if (findUser(user) == null) {
            this.parkingSystemPersistence.getListUsers().add(user);
        }
    }
    public User findUser(User user){
        for (User u : parkingSystemPersistence.getListUsers()) {
            if (u.equals(user)) {
                return u;
            }
        }
        return null;
    }
    public Boolean addVehicle(Vehicle vehicle) {
        if (findVehicle(vehicle) == null) {
            this.parkingSystemPersistence.getListVehicles().add(vehicle);
            return true;
        }
        return false;
    }
    public Vehicle findVehicle(Vehicle vehicle){
        for (Vehicle v : this.parkingSystemPersistence.getListVehicles()) {
            if (v.equals(vehicle)) {
                return v;
            }
        }
        return null;
    }
    public void deleteVehicle(Vehicle vehicle) {
        if (findVehicle(vehicle) != null) {
            this.parkingSystemPersistence.getListVehicles().remove(vehicle);
        }
    }

    public Boolean updateVehicle(Vehicle oldVehicle, Vehicle newVehicle) {
        for (int i = 0; i < this.parkingSystemPersistence.getListVehicles().size(); i++) {
            if (this.parkingSystemPersistence.getListVehicles().get(i).equals(oldVehicle)) {
                this.parkingSystemPersistence.getListVehicles().set(i, newVehicle);
                return true;
            }
        }
        return false;
        
    }
    public String readVehicle(){
        StringBuilder sb = new StringBuilder();
        for (Vehicle v : this.parkingSystemPersistence.getListVehicles()) {
            sb.append(v.toString()).append("\n");
        }
        return sb.toString();
    }

    public Boolean addRecordParking(RecordParking recordParking) {
        if (this.parkingSystemPersistence.getListRecordParkings().size() < this.parkingSystemPersistence.getLimiteCupos()) {
            if (findRecordParking(recordParking) == null) {
                this.parkingSystemPersistence.getListRecordParkings().add(recordParking);
                return true;
            }
            return false;
        } else {
            return false;
        }
    }

    public RecordParking findRecordParking(RecordParking recordParking) {
        for (RecordParking r : this.parkingSystemPersistence.getListRecordParkings()) {
            if (r.equals(recordParking)) {
                return r;
            }
        }
        return null;
    }
    public void deleteRecordParking(RecordParking recordParking) {
        if (findRecordParking(recordParking) != null) {
            this.parkingSystemPersistence.getListRecordParkings().remove(recordParking);
        }
    }
    public Boolean updateRecordParking(RecordParking oldRecordParking, RecordParking newRecordParking) {
        for (int i = 0; i < this.parkingSystemPersistence.getListRecordParkings().size(); i++) {
            if (this.parkingSystemPersistence.getListRecordParkings().get(i).equals(oldRecordParking)) {
            	this.parkingSystemPersistence.getListRecordParkings().set(i, newRecordParking);
                return true;
            }
        }
        return false;
        
    }
    public String readRecordParking(){
        StringBuilder sb = new StringBuilder();
        for (RecordParking r : this.parkingSystemPersistence.getListRecordParkings()) {
            sb.append(r.toString()).append("\n");
        }
        return sb.toString();
    }

    public int findVehicleRatebyType (String type){
        for (VehicleRate rate : this.parkingSystemPersistence.getListVehicleRates()) {
            if (rate.getTypeVehicle().equalsIgnoreCase(type)) {
                return rate.getPrice();
            }
        }
        return -1;
    }
   
    public int calculateTotal(String placa, LocalDateTime departureTime) {
        RecordParking record = null;
        for (RecordParking r : this.parkingSystemPersistence.getListRecordParkings()) {
            if (r.getLicensePlate().equalsIgnoreCase(placa) && r.getEntryTime() != null) {
                record = r;
                break;
            }
        }
        if (record == null || departureTime == null) {
            return 0;
        }
        long horas = Duration.between(record.getEntryTime(), departureTime).toHours();
        String tipoVehiculo = null;
        for (Vehicle v : this.parkingSystemPersistence.getListVehicles()) {
            if (v.getLicensePlate().equalsIgnoreCase(placa)) {
                tipoVehiculo = v.getTypeVehicle();
                break;
            }
        }
        if (tipoVehiculo == null) {
            return 0;
        }
        int precioPorHora = findVehicleRatebyType(tipoVehiculo);
        return (int) (horas * precioPorHora);
    }

    

}
