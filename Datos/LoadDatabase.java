package es.unican.CIBEL;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.opencsv.CSVReader;

import es.unican.CIBEL.domain.*;
import es.unican.CIBEL.repository.*;

@Configuration
public class LoadDatabase {

	@Bean
	CommandLineRunner initDatabase(
			DispositivoRepository dispositivoRepository, AppRepository appRepository, TipoRepository tipoRepository, VulnerabilidadRepository vulnerabilidadRepository, VulnerabilidadAppRepository vulnerabilidadAppRepository) {

		return args -> {

			// DISPOSITIVOS

			// Cargar tipos y dispositivos del archivo
			try (BufferedReader br = new BufferedReader(new FileReader("/Users/alinasolonarubotnari/ALBAUC/CIBELService/Datos/Dispositivos.txt"))) {
				String line;
				Tipo tipoActual = null;

				while ((line = br.readLine()) != null) {
					if (line.startsWith("#")) {
						// Nuevo tipo de dispositivo
						String tipoNombre = line.substring(1).trim();
						tipoActual = new Tipo(tipoNombre);
						tipoRepository.save(tipoActual);
					} else if (line.startsWith("*") && tipoActual != null) {
						// Tipo de dispositivo en ingles
						String tipoNombreEN = line.substring(1).trim();
						tipoActual.setNombre_en(tipoNombreEN);
						tipoRepository.save(tipoActual);
					}  else if (!line.isEmpty() && tipoActual != null) {
						// Nuevo dispositivo
						String nombreDispositivo = line;
						String urlImagen = "";
						Dispositivo activo = new Dispositivo(nombreDispositivo, urlImagen, tipoActual);
						dispositivoRepository.save(activo);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}


			// Cargar vulnerabiliades del csv
			CSVReader reader = new CSVReader(new FileReader("/Users/alinasolonarubotnari/ALBAUC/CIBELService/Datos/DispositivosCVES.csv"));
			List<String[]> rows = reader.readAll();

			for (String[] row : rows) {
				if (row[0].equals("Modelo")) {
					continue; // Saltar la fila de encabezado
				}
				Vulnerabilidad v = new Vulnerabilidad();
				v.setIdCVE(row[1]);
				v.setDescripcion(row[2]);
				v.setDescripcion_en(row[3]);
				v.setConfidentialityImpact(row[6]);
				v.setIntegrityImpact(row[7]);
				v.setAvailabilityImpact(row[8]);
				if (row[9] != null && !row[9].isEmpty()) {
					v.setBaseScore(Double.parseDouble(row[9]));
				} else {
					v.setBaseScore(0);
				}

				v.setBaseSeverity(row[10]);

				vulnerabilidadRepository.save(v);

				Dispositivo d = dispositivoRepository.findByNombre(row[0]);
				d.getVulnerabilidades().add(v);
				dispositivoRepository.save(d);
			}

			// APLICACIONES

			// Cargar tipos y aplicaciones del archivo
			try (BufferedReader br = new BufferedReader(new FileReader("/Users/alinasolonarubotnari/ALBAUC/CIBELService/Datos/Datos/Aplicaciones.txt"))) {
				String line;
				Tipo tipoActual = null;

				while ((line = br.readLine()) != null) {
					if (line.startsWith("#")) {
						// Nuevo tipo de aplicacion
						String tipoNombre = line.substring(1).trim();
						tipoActual = new Tipo(tipoNombre);
						tipoRepository.save(tipoActual);
					} else if (line.startsWith("*") && tipoActual != null) {
						// Tipo de aplicacion en ingles
						String tipoNombreEN = line.substring(1).trim();
						tipoActual.setNombre_en(tipoNombreEN);
						tipoRepository.save(tipoActual);
					} else if (!line.isEmpty() && tipoActual != null) {
						// Nueva aplicacion
						String[] aplicacionInfo = line.split(", ");
						String nombreApp = aplicacionInfo[0];
						String icono = aplicacionInfo[1];
						Aplicacion activo = new Aplicacion(nombreApp, icono, tipoActual);
						appRepository.save(activo);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}


			// Cargar vulnerabiliades del csv
			reader = new CSVReader(new FileReader("/Users/alinasolonarubotnari/ALBAUC/CIBELService/Datos/Datos/AppsCVES.csv"));
			List<String[]> rowsApps = reader.readAll();

			for (String[] row : rowsApps) {
				if (row[0].equals("APP")) {
					continue; // Saltar la fila de encabezado
				}
				VulnerabilidadApp v = new VulnerabilidadApp();
				v.setIdCVE(row[1]);
				v.setDescripcion(row[2]);
				v.setDescripcion_en(row[3]);
				v.setConfidentialityImpact(row[6]);
				v.setIntegrityImpact(row[7]);
				v.setAvailabilityImpact(row[8]);
				if (row[9] != null && !row[9].isEmpty()) {
					v.setBaseScore(Double.parseDouble(row[9]));
				} else {
					v.setBaseScore(0);
				}

				v.setBaseSeverity(row[10]);
				
				if (row[13].isEmpty()) {
					v.setVersionEndExcluding(null);
				} else {
					v.setVersionEndExcluding(row[13]);
				}
				
				if (row[14].isEmpty()) {
					v.setVersionEndIncluding(null);
				} else {
					v.setVersionEndIncluding(row[14]);
				}

				vulnerabilidadAppRepository.save(v);

				Aplicacion a = appRepository.findByNombre(row[0]);
				a.getVulnerabilidades().add(v);
				appRepository.save(a);

			}

		};
	}

}
