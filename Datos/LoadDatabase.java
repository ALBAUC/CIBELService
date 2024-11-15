package es.unican.CIBEL;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import es.unican.CIBEL.domain.*;
import es.unican.CIBEL.repository.*;

@Configuration
public class LoadDatabase {

	@Bean
	CommandLineRunner initDatabase(
			DispositivoRepository dispositivoRepository, AppRepository appRepository, TipoRepository tipoRepository, VulnerabilidadRepository vulnerabilidadRepository, VulnerabilidadAppRepository vulnerabilidadAppRepository, DebilidadRepository debilidadRepository) {

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

			// Asignar puntuaciones estimadas de sostenibilidad
			try (CSVReader reader = new CSVReaderBuilder(new FileReader("/Users/alinasolonarubotnari/ALBAUC/CIBELService/Datos/DevicesEcoPredicted.csv"))
					.withCSVParser(new CSVParserBuilder().withSeparator(';').build()) // Mi CSV usa el separador ';'
					.build()) {
				List<String[]> rows = reader.readAll();
				for (String[] row : rows) {
					try {
						if (row.length < 3 || row[0].equalsIgnoreCase("dispositivo")) {
							System.err.println("row.length < 3 || row[0].equalsIgnoreCase(\"dispositivo\") --> " + row.length);
							continue; // Skip invalid or header rows
						}

						Dispositivo dispositivo = dispositivoRepository.findByNombre(row[0]);
						if (dispositivo == null) {
							System.err.println("Dispositivo not found: " + row[0]);
							continue;
						}

						boolean ecoPredicted = "1".equals(row[1]);
						dispositivo.setEcoPredicted(ecoPredicted);
						dispositivo.setEcoPuntuacion(Integer.parseInt(row[2]));
						dispositivoRepository.save(dispositivo);

					} catch (Exception e) {
						System.err.println("Error processing row (DevicesEcoPredicted): " + String.join(", ", row));
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				System.err.println("Error reading DevicesEcoPredicted.csv: " + e.getMessage());
				e.printStackTrace();
			}

			// Cargar CWES del csv
			try (CSVReader reader = new CSVReaderBuilder(new FileReader("/Users/alinasolonarubotnari/ALBAUC/CIBELService/Datos/CWE_List.csv"))
					.withCSVParser(new CSVParserBuilder().withSeparator(';').build()) // Mi CSV usa el separador ';'
					.build()) {
				List<String[]> rows = reader.readAll();
				for (String[] row : rows) {
					try {
						if (row.length < 5 || row[0].equalsIgnoreCase("idCWE")) {
							continue; // Skip invalid or header rows
						}

						Debilidad cwe = new Debilidad("CWE-" + row[0], row[3], row[4]);
						cwe.setNombre_en(row[1]);
						cwe.setDescripcion_en(row[2]);
						debilidadRepository.save(cwe);

					} catch (Exception e) {
						System.err.println("Error processing row (CWE_List): " + String.join(", ", row));
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				System.err.println("Error reading CWE_List.csv: " + e.getMessage());
				e.printStackTrace();
			}

			// Cargar vulnerabiliades del csv
			try (CSVReader reader = new CSVReaderBuilder(new FileReader("/Users/alinasolonarubotnari/ALBAUC/CIBELService/Datos/DispositivosCVES.csv"))
					.withCSVParser(new CSVParserBuilder().withSeparator(';').build()) // Mi CSV usa el separador ';'
					.build()) {
				List<String[]> rows = reader.readAll();
				for (String[] row : rows) {
					try {
						if (row.length < 12 || row[0].equalsIgnoreCase("Modelo")) {
							continue; // Skip invalid or header rows
						}

						// Obtener o crear vulnerabilidad
						Vulnerabilidad vulnerabilidad = vulnerabilidadRepository.findById(row[1]).orElse(new Vulnerabilidad());
						vulnerabilidad.setIdCVE(row[1]);
						vulnerabilidad.setDescripcion(row[3]);
						vulnerabilidad.setDescripcion_en(row[4]);
						vulnerabilidad.setConfidentialityImpact(row[7]);
						vulnerabilidad.setIntegrityImpact(row[8]);
						vulnerabilidad.setAvailabilityImpact(row[9]);
						if (row[10] != null && !row[10].isEmpty() && !row[10].equalsIgnoreCase("NULL")) {
							vulnerabilidad.setBaseScore(Double.parseDouble(row[10]));
						} else {
							vulnerabilidad.setBaseScore(0);
						}
						vulnerabilidad.setBaseSeverity(row[11]);

						// Asignar CWEs si existen
						if (vulnerabilidad.getCwes() == null) {
							vulnerabilidad.setCwes(new LinkedHashSet<>());
						}

						if (row[2] != null && !row[2].isBlank() && !row[2].equalsIgnoreCase("NULL")) {
							debilidadRepository.findById(row[2]).ifPresent(cwe -> vulnerabilidad.getCwes().add(cwe));
						}

						// Actualizar repo
						vulnerabilidadRepository.save(vulnerabilidad);

						// Asociar cve al dispositivo
						Dispositivo dispositivo = dispositivoRepository.findByNombre(row[0]);
						if (dispositivo != null) {
							dispositivo.getVulnerabilidades().add(vulnerabilidad);
							dispositivoRepository.save(dispositivo);
						} else {
							System.err.println("Dispositivo not found for vulnerability: " + row[0]);
						}

					} catch (Exception e) {
						System.err.println("Error processing row (DispositivosCVES): " + String.join(", ", row));
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				System.err.println("Error reading DispositivosCVES.csv: " + e.getMessage());
				e.printStackTrace();
			}

			// Asignar puntuación de seguridad
			try (BufferedReader reader = new BufferedReader(new FileReader("/Users/alinasolonarubotnari/ALBAUC/CIBELService/Datos/Dispositivos.txt"))) {
				String line;
				while ((line = reader.readLine()) != null) {
					try {
						if (line.isEmpty() || line.startsWith("#") || line.startsWith("*")) {
							continue; // Skip types or empty lines
						}

						Dispositivo dispositivo = dispositivoRepository.findByNombre(line);
						if (dispositivo == null) {
							System.err.println("Dispositivo not found: " + line);
							continue;
						}

						int securityScore = calcularPuntuacionSeguridad(dispositivo);
						dispositivo.setSecurityScore(securityScore);
						dispositivoRepository.save(dispositivo);

					} catch (Exception e) {
						System.err.println("Error processing dispositivo: " + line);
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				System.err.println("Error reading Dispositivos.txt: " + e.getMessage());
				e.printStackTrace();
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
			CSVReader reader = new CSVReader(new FileReader("/Users/alinasolonarubotnari/ALBAUC/CIBELService/Datos/Datos/AppsCVES.csv"));
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

	public int calcularPuntuacionSeguridad(Dispositivo d) {
		// Considero un límite de 700 como la máxima gravedad posible
		int puntuacionSeguridad = (int) Math.round(100 - (calcularTotalGravedad(d) / 7));
		return Math.max(0, Math.min(100, puntuacionSeguridad));
	}

	public double calcularTotalGravedad(Dispositivo d) {
		List<Vulnerabilidad> vulnerabilidades = d.getVulnerabilidades();
		int totalGravedad = 0;

		for (Vulnerabilidad v : vulnerabilidades) {
			totalGravedad += v.getBaseScore() * 10; // baseScore de 0 a 100
		}

		return totalGravedad;
	}

}
