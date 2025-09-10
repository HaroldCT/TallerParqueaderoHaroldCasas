package com.edu.uptc.parkingSystem.persistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
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
import com.edu.uptc.parkingSystem.constants.CommonConstants;

public class ParkingSystemPersistence extends FilePlain implements IActionsFile{
	private List<User> listUsers;
    private List<Vehicle> listVehicles;
    private List<RecordParking> listRecordParkings;
    private List<VehicleRate> listVehicleRates;
    private int limiteCupos = 20;
	
	public ParkingSystemPersistence() {
		this.listRecordParkings= new ArrayList<>();
		this.listUsers = new ArrayList<User>();
		this.listVehicles = new ArrayList<Vehicle>();
		this.listVehicleRates = new ArrayList<>();
	}
	
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
	
	public void loadFileTotal() {
		loadFile(ETypeFileEnum.CSV);

		if (config.getNameFileJSON() != null) {
			File jsonFile = new File(config.getPathFile().concat(config.getNameFileJSON()));
			if (jsonFile.exists()) {
				loadFile(ETypeFileEnum.JSON);
			}
		}

		if (config.getNameFileSer() != null) {
			File serFile = new File(config.getPathFile().concat(config.getNameFileSer()));
			if (serFile.exists()) {
				loadFile(ETypeFileEnum.SER);
			}
		}

		if (config.getNameFileXML() != null) {
			File xmlFile = new File(config.getPathFile().concat(config.getNameFileXML()));
			if (xmlFile.exists()) {
				loadFile(ETypeFileEnum.XML);
			}
		}
	}
	
	public void loadFilePlain(String nameFile) {
		List <String> contentInLine = this.reader(config.getPathFile().concat(nameFile));
		contentInLine.forEach(row -> {
			StringTokenizer tokens = new StringTokenizer(row, CommonConstants.SEMICOLON);
			while(tokens.hasMoreElements()) {
				String typeVehicle = tokens.nextToken();
				int price = Integer.parseInt(tokens.nextToken());
				this.getListVehicleRates().add(new VehicleRate(typeVehicle, price));
			}
		});
	}
	public void dumpFilePlain(String nameFile) {
		StringBuilder rutaArchivo = new StringBuilder();
		rutaArchivo .append(config.getPathFile());
		rutaArchivo.append(nameFile);
		List<String> records = new ArrayList<>();
		this.getListVehicleRates().forEach(vehicleRate -> {
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
			this.setListUsers((List<User>) in.readObject());
		} catch (IOException i) {
			i.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void dumpFileSerializate() {
		try (FileOutputStream fileOut = new FileOutputStream(this.config.getPathFile().concat(this.config.getNameFileSer()));
				ObjectOutputStream out = new ObjectOutputStream(fileOut)){
				out.writeObject(this.getListUsers());
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
				this.getListVehicles().add(new Vehicle(licensePlate, typeVehicle, owner, model, color, pricePerHour));
			}
					
		} catch (Exception e) {
			System.out.println("Se presento un error en el cargue del archivo XML");
		}
	}
	
	public void dumpFileXML() {
		String rutaArchivo = config.getPathFile().concat(config.getNameFileXML());
		List<String> records = new ArrayList<String>();
		records.add("<XML version=\"1.0\" encoding=\"UTF-8\">");
		for (Vehicle vehicle : this.getListVehicles()) {
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
		List<String> contentInLine = this.reader(config.getPathFile().concat(this.config.getNameFileJSON())).stream()
				.filter(line -> !line.equals("[") && !line.equals("]") && !line.equals(CommonConstants.BREAK_LINE)
						&& !line.trim().isEmpty() && !line.trim().isBlank())
				.collect(Collectors.toList());

		for (String line : contentInLine) {
			line = line.replace("{", "").replace("},", "").replace("}", "");
			StringTokenizer tokens = new StringTokenizer(line, ",");

			while (tokens.hasMoreElements()) {
				String LicensePlate = this.escapeValue(tokens.nextToken().split(":", 2)[1]);
				String EntryTimeand = this.escapeValue(tokens.nextToken().split(":", 2)[1]);
				String DepartureTimeype = this.escapeValue(tokens.nextToken().split(":", 2)[1]);
				Long total = Long.parseLong(this.escapeValue(tokens.nextToken().split(":", 2)[1]));

				RecordParking record = new RecordParking(LicensePlate, EntryTimeand, DepartureTimeype);
				record.setTotal(total);
				this.listRecordParkings.add(record);
			}
		}
	}
	
	private String escapeValue(String value) {
		return value.replace("\"", "");
	}
	
	public void dumpFileJSON() {
		String rutaArchivo = config.getPathFile().concat(this.config.getNameFileJSON());
		StringBuilder json = null;
		List<String> content = new ArrayList<String>();
		content.add(CommonConstants.OPENING_BRACKET);
		int cont = 0;
		int total = listRecordParkings.size();

		for (RecordParking r : this.listRecordParkings) {
			json = new StringBuilder();
			json.append("{");
			json.append(" \"LicensePlate\":\"").append(escape(r.getLicensePlate())).append("\",");
			json.append(" \"EntryTime\":\"").append(escape(r.getEntryTimeAsLocalDateTime().format(r.getFormatter()))).append("\",");
			json.append(" \"DepartureTime\":\"").append(escape(String.valueOf(r.getDepartureTime()))).append("\",");
			json.append(" \"Total\":").append(r.getTotal());
			json.append("}");

			cont++;
			if (cont < total) {
				json.append(",");
			}
			content.add(json.toString());
		}

		content.add(CommonConstants.CLOSED_BRACKET);
		this.writer(rutaArchivo, content);
	}
	private String escape(String value) {
		if (value == null)
			return "";
		return value.replace("\\", "\\\\").replace("\"", "\\\"");
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
	
	
}
