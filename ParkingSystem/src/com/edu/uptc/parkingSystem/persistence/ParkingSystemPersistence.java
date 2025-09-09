package com.edu.uptc.parkingSystem.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import com.edu.uptc.parkingSystem.enums.ETypeFileEnum;
import com.edu.uptc.parkingSystem.interfaces.IActionsFile;
import com.edu.uptc.parkingSystem.model.RecordParking;
import com.edu.uptc.parkingSystem.model.User;
import com.edu.uptc.parkingSystem.model.Vehicle;
import com.edu.uptc.parkingSystem.model.VehicleRate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.edu.uptc.parkingSystem.constants.CommonConstants;
import com.edu.uptc.parkingSystem.control.Controller;

public class ParkingSystemPersistence extends FilePlain implements IActionsFile{
	public Controller controller = new Controller();
	
	@Override
	public void loadFile(ETypeFileEnum eTypeFileEnum) {
		if (ETypeFileEnum.CSV.equals(eTypeFileEnum)) {
			String nameFileCsv = config.getNameFileCSV();
			loadFilePlain(nameFileCsv);
		}
		if (ETypeFileEnum.SER.equals(eTypeFileEnum)) {
			loadFileSerializate();
		}
		if (ETypeFileEnum.XML.equals(eTypeFileEnum)) {
			loadFileXML();
		}
		if(ETypeFileEnum.JSON.equals(eTypeFileEnum)) {
			loadFileJSON();
		}
		
	}

	@Override
	public void dumpFile(ETypeFileEnum eTypeFileEnum) {
		if (ETypeFileEnum.CSV.equals(eTypeFileEnum)) {
			String nameFile = config.getNameFileCSV();
			this.dumpFilePlain(nameFile);
		}
		if (ETypeFileEnum.SER.equals(eTypeFileEnum)) {
			dumpFileSerializate();
		}
		if (ETypeFileEnum.XML.equals(eTypeFileEnum)) {
			dumpFileXML();
		}
		if (ETypeFileEnum.JSON.equals(eTypeFileEnum)) {
			dumpFileJSON();
		}
		
	}
	
	public void loadFilePlain(String nameFile) {
		List <String> contentInLine = this.reader(config.getPathFile().concat(nameFile));
		contentInLine.forEach(row -> {
			StringTokenizer tokens = new StringTokenizer(row, CommonConstants.SEMICOLON);
			while(tokens.hasMoreElements()) {
				String typeVehicle = tokens.nextToken();
				int price = Integer.parseInt(tokens.nextToken());
				this.controller.getListVehicleRates().add(new VehicleRate(typeVehicle, price));
			}
		});
	}
	public void dumpFilePlain(String nameFile) {
		StringBuilder rutaArchivo = new StringBuilder();
		rutaArchivo .append(config.getPathFile());
		rutaArchivo.append(nameFile);
		List<String> records = new ArrayList<>();
		this.controller.getListVehicleRates().forEach(vehicleRate -> {
			StringBuilder contentPrices = new StringBuilder();
			contentPrices.append(vehicleRate.getTypeVehicle()).append(CommonConstants.SEMICOLON);
			contentPrices.append(String.valueOf(vehicleRate.getPrice()));
			records.add(contentPrices.toString());
		});
		
		this.writer(rutaArchivo.toString(), records);
	}
	
	@SuppressWarnings("unchecked")
	public void loadFileSerializate() {
		try (FileInputStream fileIn = new FileInputStream(this.config.getPathFile().concat(this.config.getNameFileSer()));
				ObjectInputStream in = new ObjectInputStream(fileIn)){
			this.controller.setListUsers((List<User>) in.readObject());
		} catch (IOException i) {
			i.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void dumpFileSerializate() {
		try (FileOutputStream fileOut = new FileOutputStream(this.config.getPathFile().concat(this.config.getNameFileSer()));
				ObjectOutputStream out = new ObjectOutputStream(fileOut)){
				out.writeObject(this.controller.getListUsers());
			} catch (IOException i) {
				i.printStackTrace();
			}
	}
	
	public void loadFileXML() {
		try {
			File file  = new File(config.getPathFile().concat(config.getNameFileXML()));
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(file);
			NodeList list = document.getElementsByTagName(CommonConstants.NAME_TAG_VEHICLE);
			for(int i=0; i<list.getLength();i++) {
				String licensePlate = document.getElementsByTagName("licensePlate").item(i).getTextContent();
				String typeVehicle  = document.getElementsByTagName("typeVehicle").item(i).getTextContent();
				String owner = document.getElementsByTagName("owner").item(i).getTextContent();
				String model = document.getElementsByTagName("model").item(i).getTextContent();
				String color = document.getElementsByTagName("color").item(i).getTextContent();
				int pricePerHour = Integer.parseInt(document.getElementsByTagName("pricePerHour").item(i).getTextContent());
				this.controller.getListVehicles().add(new Vehicle(licensePlate, typeVehicle, owner, model, color, pricePerHour));
			}
					
		} catch (Exception e) {
			System.out.println("Se presento un error en el cargue del archivo XML");
		}
	}
	
	public void dumpFileXML() {
		String rutaArchivo = config.getPathFile().concat(config.getNameFileXML());
		List<String> records = new ArrayList<String>();
		records.add("<XML version=\"1.0\" encoding=\"UTF-8\">");
		for (Vehicle vehicle : this.controller.getListVehicles()) {
			records.add("<vehicle>\n");
			records.add("\t<licensePlate>"+ vehicle.getLicensePlate()+"</licensePlate>");
			records.add("\t<typeVehicle>"+vehicle.getTypeVehicle()+"</typeVehicle>");
			records.add("\t<owner>"+vehicle.getOwner()+"</owner>");
			records.add("\t<model>"+vehicle.getModel()+"</model>");
			records.add("\t<color>"+vehicle.getColor()+"</color>");
			records.add("\t<pricePerHour>"+vehicle.getPricePerHour()+"</pricePerHour>");
			records.add("</vehicle>");
			
			
		}
		records.add("</XML>");
		this.writer(rutaArchivo, records);
	}
	
	public void loadFileJSON() {
		 String rutaArchivo = config.getPathFile().concat(config.getNameFileJSON());
		    try (FileReader reader = new FileReader(rutaArchivo)){
		    	Gson gson = new Gson();
		    	RecordParking[] recordParkingArray = gson.fromJson(reader, RecordParking[].class);
		    	this.controller.setListRecordParkings(new ArrayList<> (Arrays.asList(recordParkingArray)));

			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public void dumpFileJSON() {
		String rutaArchivo = config.getPathFile().concat(config.getNameFileJSON());
	    try (FileWriter writer = new FileWriter(rutaArchivo)) {
	        Gson gson = new GsonBuilder().setPrettyPrinting().create(); // formato bonito
	        gson.toJson(this.controller.getListRecordParkings(), writer);  // Serializa toda la lista
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
}
