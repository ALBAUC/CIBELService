package es.unican.CIBEL;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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

			String apiKey = "AIzaSyAcxVPp7BKyASKhAaHKL5ElyZWM1LtGjOI";
			String idMotorBusqueda = "41b083a8d7b30426d";

			// Cargar tipos y dispositivos del archivo
			try (BufferedReader br = new BufferedReader(new FileReader("/Users/alinasolonarubotnari/ALBAUC/CIBELService/Datos/Datos/Dispositivos.txt"))) {
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
						//String urlImagen = getImageUrl(nombreDispositivo, apiKey, idMotorBusqueda);
						String urlImagen = "";
						Dispositivo activo = new Dispositivo(nombreDispositivo, urlImagen, tipoActual);
						dispositivoRepository.save(activo);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}


			// Cargar vulnerabiliades del csv
			CSVReader reader = new CSVReader(new FileReader("/Users/alinasolonarubotnari/ALBAUC/CIBELService/Datos/Datos/DispositivosCVES.csv"));
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

	public static String getImageUrl(String query, String apiKey, String idMotorBusqueda) throws IOException {
		String googleSearchUrl = "https://www.googleapis.com/customsearch/v1?q=" 
				+ URLEncoder.encode(query, "UTF-8") 
				+ "&cx=" + idMotorBusqueda 
				+ "&num=1&searchType=image&key=" + apiKey;
		URI uri = URI.create(googleSearchUrl);
		URL url = uri.create(googleSearchUrl).toURL();
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.connect();

		InputStream stream = connection.getInputStream();
		InputStreamReader reader = new InputStreamReader(stream);
		char[] buffer = new char[1024];
		StringBuilder response = new StringBuilder();

		int bytesRead;
		while ((bytesRead = reader.read(buffer)) != -1) {
			response.append(buffer, 0, bytesRead);
		}

		String jsonResponse = response.toString();

		// Analizar la respuesta JSON para obtener la URL de la imagen
		String imageUrl = parseJsonResponse(jsonResponse);

		return imageUrl;
	}

	private static String parseJsonResponse(String jsonResponse) {
		JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();
		JsonArray items = jsonObject.getAsJsonArray("items");

		if (items.size() > 0) {
			JsonObject item = items.get(0).getAsJsonObject();
			String link = item.get("link").getAsString();
			return link;
		}

		return "";
	}

}
