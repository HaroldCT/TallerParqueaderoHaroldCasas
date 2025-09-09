package com.edu.uptc.parkingSystem.control;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.edu.uptc.parkingSystem.model.RecordParking;
import com.edu.uptc.parkingSystem.model.User;
import com.edu.uptc.parkingSystem.model.Vehicle;
import com.edu.uptc.parkingSystem.model.VehicleRate;

public class Controller {

    private List<User> listUsers;
    private List<Vehicle> listVehicles;
    private List<RecordParking> listRecordParkings;
    private List<VehicleRate> listVehicleRates;
    private int limiteCupos = 20;

    public Controller() {
        this.listUsers = new ArrayList<>();
        this.listVehicles = new ArrayList<>();
        this.listRecordParkings = new ArrayList<>();
        this.listVehicleRates = new ArrayList<>();
    }
    public void addVehicleRate() {
        this.listVehicleRates.add(new VehicleRate("Carro", 1000));
        this.listVehicleRates.add(new VehicleRate("Moto", 500));
        this.listVehicleRates.add(new VehicleRate("Bicicleta", 200));
    }
    public void addUser(User user) {
        if (findUser(user) == null) {
            this.listUsers.add(user);
        }
    }
    public User findUser(User user){
        for (User u : listUsers) {
            if (u.equals(user)) {
                return u;
            }
        }
        return null;
    }
    public Boolean addVehicle(Vehicle vehicle) {
        if (findVehicle(vehicle) == null) {
            this.listVehicles.add(vehicle);
            return true;
        }
        return false;
    }
    public Vehicle findVehicle(Vehicle vehicle){
        for (Vehicle v : listVehicles) {
            if (v.equals(vehicle)) {
                return v;
            }
        }
        return null;
    }
    public void deleteVehicle(Vehicle vehicle) {
        if (findVehicle(vehicle) != null) {
            this.listVehicles.remove(vehicle);
        }
    }

    public Boolean updateVehicle(Vehicle oldVehicle, Vehicle newVehicle) {
        for (int i = 0; i < listVehicles.size(); i++) {
            if (listVehicles.get(i).equals(oldVehicle)) {
                listVehicles.set(i, newVehicle);
                return true;
            }
        }
        return false;
        
    }
    public String readVehicle(){
        StringBuilder sb = new StringBuilder();
        for (Vehicle v : listVehicles) {
            sb.append(v.toString()).append("\n");
        }
        return sb.toString();
    }

    public Boolean addRecordParking(RecordParking recordParking) {
        if (listRecordParkings.size() < limiteCupos) {
            if (findRecordParking(recordParking) == null) {
                this.listRecordParkings.add(recordParking);
                return true;
            }
            return false;
        } else {
            return false;
        }
    }

    public RecordParking findRecordParking(RecordParking recordParking) {
        for (RecordParking r : listRecordParkings) {
            if (r.equals(recordParking)) {
                return r;
            }
        }
        return null;
    }
    public void deleteRecordParking(RecordParking recordParking) {
        if (findRecordParking(recordParking) != null) {
            this.listRecordParkings.remove(recordParking);
        }
    }
    public Boolean updateRecordParking(RecordParking oldRecordParking, RecordParking newRecordParking) {
        for (int i = 0; i < listRecordParkings.size(); i++) {
            if (listRecordParkings.get(i).equals(oldRecordParking)) {
                listRecordParkings.set(i, newRecordParking);
                return true;
            }
        }
        return false;
        
    }
    public String readRecordParking(){
        StringBuilder sb = new StringBuilder();
        for (RecordParking r : listRecordParkings) {
            sb.append(r.toString()).append("\n");
        }
        return sb.toString();
    }

    public int findVehicleRatebyType (String type){
        for (VehicleRate rate : listVehicleRates) {
            if (rate.getTypeVehicle().equalsIgnoreCase(type)) {
                return rate.getPrice();
            }
        }
        return -1;
    }
    public List<User> getListUsers() {
        return listUsers;
    }
    public void setListUsers(List<User> listUsers) {
        this.listUsers = listUsers;
    }
    public List<Vehicle> getListVehicles() {
        return listVehicles;
    }
    public void setListVehicles(List<Vehicle> listVehicles) {
        this.listVehicles = listVehicles;
    }
    public List<RecordParking> getListRecordParkings() {
        return listRecordParkings;
    }
    public void setListRecordParkings(List<RecordParking> listRecordParkings) {
        this.listRecordParkings = listRecordParkings;
    }
    public List<VehicleRate> getListVehicleRates() {
        return listVehicleRates;
    }
    public void setListVehicleRates(List<VehicleRate> listVehicleRates) {
        this.listVehicleRates = listVehicleRates;
    }
    public int getLimiteCupos() {
        return limiteCupos;
    }
    public void setLimiteCupos(int limiteCupos) {
        this.limiteCupos = limiteCupos;
    }
    public int calculateTotal(String placa, LocalDateTime departureTime) {
        RecordParking record = null;
        for (RecordParking r : getListRecordParkings()) {
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
        for (Vehicle v : getListVehicles()) {
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
