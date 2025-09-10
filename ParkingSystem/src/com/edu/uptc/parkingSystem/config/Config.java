package com.edu.uptc.parkingSystem.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class Config {

	private static Config config;
	private String pathFile;
	private String nameFileCSV;
	private String nameFileSer;
	private String nameFileXML;
	private String nameFileJSON;
	private Properties properties;
	
	public Config() {
		this.properties = new Properties();
		try (FileInputStream entrada = new FileInputStream("resources/config/appConfig.properties")){
			properties.load(entrada);
			this.pathFile = properties.getProperty("app.config.path.files");
			this.nameFileCSV = properties.getProperty("app.config.file.name.csv");
			this.nameFileSer = properties.getProperty("app.config.file.name.ser");
			this.nameFileXML = properties.getProperty("app.config.file.name.xml");
			this.nameFileJSON = properties.getProperty("app.config.file.name.json");
		} catch (IOException ex) {
			System.err.println("Error al cargar el archivo properties de configuracion: "+ ex.getMessage());
			
		}
		
	}
	
	public static Config getInstance() {
		if (Objects.isNull(config)) {
			config = new Config();
		}
		return config;
	}

	public String getPathFile() {
		return pathFile;
	}

	public void setPathFile(String pathFile) {
		this.pathFile = pathFile;
	}

	public String getNameFileCSV() {
		return nameFileCSV;
	}

	public void setNameFileCSV(String nameFileCSV) {
		this.nameFileCSV = nameFileCSV;
	}

	public String getNameFileSer() {
		return nameFileSer;
	}

	public void setNameFileSer(String nameFileSer) {
		this.nameFileSer = nameFileSer;
	}

	public String getNameFileXML() {
		return nameFileXML;
	}

	public void setNameFileXML(String nameFileXML) {
		this.nameFileXML = nameFileXML;
	}

	public String getNameFileJSON() {
		return nameFileJSON;
	}

	public void setNameFileJSON(String nameFileJSON) {
		this.nameFileJSON = nameFileJSON;
	}
	
	
	
	
}
