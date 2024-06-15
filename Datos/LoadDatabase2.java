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

			String apiKey = "AIzaSyAcxVPp7BKyASKhAaHKL5ElyZWM1LtGjOI";
			String idMotorBusqueda = "41b083a8d7b30426d";

			// Actualizar imagenes de dispositivos
			try (BufferedReader br = new BufferedReader(new FileReader("/Users/alinasolonarubotnari/ALBAUC/CIBELService/Datos/Dispositivos.txt"))) {
				String line;

				while ((line = br.readLine()) != null) {
					if (line.startsWith("#")) {
						// Saltar tipo de dispositivo
						continue;
					} else if (line.startsWith("*")) {
						// Saltar tipo de dispositivo en ingles
						continue;
					}  else if (!line.isEmpty()) {
						// Leer dispositivo
						String nombreDispositivo = line;
						Dispositivo d = dispositivoRepository.findByNombre(nombreDispositivo);
						if (d != null && d.getIcono().isEmpty()) {
							String urlImagen = getImageUrl(nombreDispositivo, apiKey, idMotorBusqueda);
							d.setIcono(urlImagen);
							dispositivoRepository.save(d);
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
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
