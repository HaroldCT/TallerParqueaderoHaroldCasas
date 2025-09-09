package com.edu.uptc.parkingSystem.view;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import com.edu.uptc.parkingSystem.control.Controller;
import com.edu.uptc.parkingSystem.enums.ETypeFileEnum;
import com.edu.uptc.parkingSystem.model.RecordParking;
import com.edu.uptc.parkingSystem.model.User;
import com.edu.uptc.parkingSystem.model.Vehicle;
import com.edu.uptc.parkingSystem.persistence.ParkingSystemPersistence;

public class View1 {
    Scanner sc = new Scanner(System.in);
	ParkingSystemPersistence parkingSystemPersistence = new ParkingSystemPersistence();
	Controller controller = new Controller();

    public void UserView() {
        // Cargar la información desde el archivo serializado (SER) al iniciar
        parkingSystemPersistence.loadFile(ETypeFileEnum.SER);
        parkingSystemPersistence.loadFile(ETypeFileEnum.CSV);
        parkingSystemPersistence.loadFile(ETypeFileEnum.XML);
        parkingSystemPersistence.loadFile(ETypeFileEnum.JSON);

        // Actualizar los ArrayList del controller principal con los datos cargados
        this.controller.setListUsers(controller.getListUsers());
        this.controller.setListVehicles(controller.getListVehicles());
        this.controller.setListRecordParkings(controller.getListRecordParkings());
        this.controller.setListVehicleRates(controller.getListVehicleRates());

        this.controller.addVehicleRate();
        System.out.println("Bienvenido al servicio del parqueadero");
        String opcion = "";
        Boolean salir = Boolean.FALSE;
        Boolean salir1 = Boolean.FALSE;
        Boolean salir2 = Boolean.FALSE;
        do {
            do {
                System.out.println("Ya esta registrado? (S/N)");
                opcion = sc.nextLine();
                if (opcion.equalsIgnoreCase("S")) {
                    System.out.println("Ingrese su nombre de usuario:");
                    String username = sc.next();
                    System.out.println("Ingrese su contraseña:");
                    String password = sc.next();
                    boolean usuarioValido = false;
                    for (User u : this.controller.getListUsers()) {
                        if (u.getUserName().equals(username) && u.getPassword().equals(password)) {
                            usuarioValido = true;
                            System.out.println("Bienvenido " + u.getUserName());
                            break;
                        }
                    }
                    if (usuarioValido) {
                        salir = Boolean.TRUE;
                    } else {
                        System.out.println("Nombre de usuario o contraseña incorrectos.");
                    }
                } else if (opcion.equalsIgnoreCase("N")) {
                    System.out.println("Ingrese su nombre de usuario:");
                    String newUsername = sc.next();
                    System.out.println("Ingrese su contraseña:");
                    String newPassword = sc.next();
                    User newUser = new User(newUsername, newPassword);
                    if (!this.controller.getListUsers().contains(newUser)) {
                        this.controller.getListUsers().add(newUser);
                        
                        parkingSystemPersistence.dumpFile(ETypeFileEnum.SER);

                        System.out.println("Usuario registrado exitosamente.");
                        salir = Boolean.TRUE;
                    } else {
                        System.out.println("El nombre de usuario ya está en uso.");
                    }
                } else {
                    System.out.println("Opción no válida.");
                }
            } while (!salir);
            do {
                System.out.println("""
                    Que desea hacer?
                    1. Ingresar vehiculo al parqueadero
                    2. Sacar vehiculo del parqueadero
                    3. CRUD de vehicle
                    4. Reportes por dia (Numero de vehiculos ingresados y total de dinero recaudado)
                    5. Volver al login
                    6. Salir del programa
                    """);
            int opcionMenu = sc.nextInt();
            sc.nextLine(); 

            switch (opcionMenu) {
                case 1: {
                    System.out.print("Ingrese la placa del vehículo: ");
                    String placa = sc.nextLine();
                    String tipoVehiculo = "";
                    int tipo;
                    do {
                        System.out.println("""
                                Ingrese el tipo de vehículo:
                                1. Carro
                                2. Moto
                                3. Camión
                                """);
                        tipo = sc.nextInt();
                        sc.nextLine();
                        if (tipo == 1) {
                            tipoVehiculo = "Carro";
                        } else if (tipo == 2) {
                            tipoVehiculo = "Moto";
                        } else if (tipo == 3) {
                            tipoVehiculo = "Bicicleta";
                        }else {
                            System.out.println("Tipo de vehículo no válido.");
                        }
                    } while (tipo < 1 || tipo > 3);

                    Vehicle vehicle = new Vehicle(placa, tipoVehiculo);
                    this.controller.getListVehicles().add(vehicle);

                    LocalDateTime entrada = LocalDateTime.now();
                    RecordParking record = new RecordParking(placa, entrada);
                    this.controller.getListRecordParkings().add(record);

                    this.parkingSystemPersistence.dumpFile(ETypeFileEnum.JSON);

                    System.out.println("Vehículo ingresado correctamente.");

                    break;
                }
                case 2: {
                    System.out.print("Ingrese la placa del vehículo a sacar: ");
                    String placa = sc.nextLine();
                    boolean encontrado = false;
                    for (RecordParking record : this.controller.getListRecordParkings()) {
                        if (record.getLicensePlate().equalsIgnoreCase(placa) && record.getDepartureTime() == null && record.getTotal() == 0) {
                            LocalDateTime departureTime = LocalDateTime.now();
                            record.setDepartureTime(departureTime);
                            record.setTotal(this.controller.calculateTotal(placa, departureTime));

                            this.parkingSystemPersistence.dumpFile(ETypeFileEnum.JSON);

                            System.out.println("Vehículo " + placa + " ha salido del parqueadero.");
                            encontrado = true;
                            break;
                        }
                    }
                    if (!encontrado) {
                        System.out.println("No se encontró el vehículo en el parqueadero.");
                    }
                    break;
                }
                case 3: {
                    System.out.println("CRUD de vehículos");
                    this.vehicleCRUD();
                    break;
                }
                case 4: {
                    System.out.println("Ingrese la fecha del dia que quiere ver los reportes: (YYYY-MM-DD)");
                    String fecha = sc.nextLine();
                    LocalDate fechaBuscada;
					try {
						fechaBuscada = LocalDate.parse(fecha, DateTimeFormatter.ISO_LOCAL_DATE);
					} catch (DateTimeParseException e) {
						System.out.println("Formato de fecha inválido. Use yyyy-MM-dd.");
						break;
					}
					int totalVehiculos = 0;
					int totalGanancias = 0;

                    for (RecordParking record : this.controller.getListRecordParkings()) {
                        if (record.getEntryTime().toLocalDate().equals(fechaBuscada)) {
                            totalVehiculos++;
                            totalGanancias += record.getTotal();
                        }
                    }
                    System.out.println("En la fecha " + fechaBuscada + " entraron " + totalVehiculos + " vehiculos y se recaudaron " + totalGanancias + " pesos.");
                    break;
                }
                case 5: {
                    salir2 = true;
                    salir = false;
                    break;
                }
                case 6: {
                    salir2 = true;
                    salir1 = true;
                    break;
                }
                default:
                    System.out.println("Opción no válida.");
                    break;
            }
            } while (!salir2);

        }while (!salir1);
        System.out.println("Gracias por usar el sistema de parqueadero.");
    } 

    public void vehicleCRUD() {
        Boolean salir = Boolean.FALSE;
        do {
            System.out.println("""
                Menú de CRUD de vehículos:
                1. Crear vehículo
                2. Leer vehículos
                3. Actualizar vehículo
                4. Eliminar vehículo
                5. Volver al menú principal
                """);
            int opcionMenu = sc.nextInt();
            sc.nextLine(); 

            switch (opcionMenu) {
                case 1: {
                    System.out.print("Ingrese la placa del vehículo: ");
                    String placa = sc.nextLine();
                    String tipoVehiculo = "";
                    int tipo;
                    do {
                        System.out.println("""
                                Ingrese el tipo de vehículo:
                                1. Carro
                                2. Moto
                                3. Camión
                                """);
                        tipo = sc.nextInt();
                        sc.nextLine();
                        if (tipo == 1) {
                            tipoVehiculo = "Carro";
                        } else if (tipo == 2) {
                            tipoVehiculo = "Moto";
                        } else if (tipo == 3) {
                            tipoVehiculo = "Bicicleta";
                        }else {
                            System.out.println("Tipo de vehículo no válido.");
                        }
                    } while (tipo < 1 || tipo > 3);
                    System.out.println("Ingrese el nombre del dueño del vehículo: ");
                    String owner = sc.nextLine();
                    System.out.println("Ingrese el modelo del vehículo: ");
                    String model = sc.nextLine();
                    System.out.println("Ingrese el color del vehículo: ");
                    String color = sc.nextLine();
                    Vehicle vehicle = new Vehicle(placa, tipoVehiculo, owner, model, color, this.controller.findVehicleRatebyType(tipoVehiculo));
                    if (this.controller.getListVehicles().contains(vehicle)) {
                        System.out.println("El vehículo ya existe.");
                    } else {
                        this.controller.getListVehicles().add(vehicle);

                        this.parkingSystemPersistence.dumpFile(ETypeFileEnum.XML);

                        System.out.println("Vehículo creado exitosamente.");
                    }
                    break;
                }
                case 2: {
                    System.out.println("Lista de vehículos:");
                    for (Vehicle v : this.controller.getListVehicles()) {
                        System.out.println(v);
                    }
                    break;
                }
                case 3: {
                    System.out.print("Ingrese la placa del vehículo a actualizar: ");
                    String placa = sc.nextLine();
                    Vehicle vehicleToUpdate = null;
                    for (Vehicle v : this.controller.getListVehicles()) {
                        if (v.getLicensePlate().equalsIgnoreCase(placa)) {
                            vehicleToUpdate = v;
                            break;
                        }
                    }
                    if (vehicleToUpdate != null) {
                        System.out.print("Ingrese la nueva placa del vehículo: ");
                        String newPlaca = sc.nextLine();
                        String newTipoVehiculo = "";
                        int newTipo;
                        do {
                            System.out.println("""
                                    Ingrese el nuevo tipo de vehículo:
                                    1. Carro
                                    2. Moto
                                    3. Camión
                                    """);
                            newTipo = sc.nextInt();
                            sc.nextLine();
                            if (newTipo == 1) {
                                newTipoVehiculo = "Carro";
                            } else if (newTipo == 2) {
                                newTipoVehiculo = "Moto";
                            } else if (newTipo == 3) {
                                newTipoVehiculo = "Bicicleta";
                            } else {
                                System.out.println("Tipo de vehículo no válido.");
                            }
                        } while (newTipo < 1 || newTipo > 3);

                        vehicleToUpdate.setLicensePlate(newPlaca);
                        vehicleToUpdate.setTypeVehicle(newTipoVehiculo);

                        this.parkingSystemPersistence.dumpFile(ETypeFileEnum.XML);

                        System.out.println("Vehículo actualizado exitosamente.");
                    } else {
                        System.out.println("Vehículo no encontrado.");
                    }
                    break;
                }
                case 4: {
                    System.out.print("Ingrese la placa del vehículo a eliminar: ");
                    String placa = sc.nextLine();
                    Vehicle vehicleToRemove = null;
                    for (Vehicle v : this.controller.getListVehicles()) {
                        if (v.getLicensePlate().equalsIgnoreCase(placa)) {
                            vehicleToRemove = v;
                            break;
                        }
                    }
                    if (vehicleToRemove != null) {
                        this.controller.getListVehicles().remove(vehicleToRemove);

                        this.parkingSystemPersistence.dumpFile(ETypeFileEnum.XML);

                        System.out.println("Vehículo eliminado exitosamente.");
                    } else {
                        System.out.println("Vehículo no encontrado.");
                    }
                    break;
                }
                case 5: {
                    salir = true;
                    break;
                }
                default:
                    System.out.println("Opción no válida.");
                    break;
            }
        } while (!salir);
    }
}
