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

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.opencsv.CSVReader;

import es.unican.CIBEL.domain.Activo;
import es.unican.CIBEL.domain.Tipo;
import es.unican.CIBEL.domain.Vulnerabilidad;
import es.unican.CIBEL.repository.ActivoRepository;
import es.unican.CIBEL.repository.TipoRepository;
import es.unican.CIBEL.repository.VulnerabilidadRepository;

@Configuration
public class LoadDatabase {

	  @Bean
	  CommandLineRunner initDatabase(
			  TipoRepository tipoRepo,
			  ActivoRepository activoRepo,
			  VulnerabilidadRepository cvesRepo) {

	    return args -> {
	    	
	    	String apiKey = "AIzaSyAcxVPp7BKyASKhAaHKL5ElyZWM1LtGjOI";
            String idMotorBusqueda = "41b083a8d7b30426d";
            
            // Cargar tipos y dispositivos del archivo
	        try (BufferedReader br = new BufferedReader(new FileReader("/Users/alinasolonarubotnari/ALBAUC/CIBEL/Docs/Datos/Dispositivos.txt"))) {
	            String line;
	            Tipo tipoActual = null;

	            while ((line = br.readLine()) != null) {
	                if (line.startsWith("#")) {
	                    // Nuevo tipo de dispositivo
	                    String tipoNombre = line.substring(1).trim();
	                    tipoActual = new Tipo(tipoNombre);
	                    tipoRepo.save(tipoActual);
	                } else if (!line.isEmpty() && tipoActual != null) {
	                    // Nuevo dispositivo
	                    String nombreDispositivo = line;
	                    String urlImagen = getImageUrl(nombreDispositivo, apiKey, idMotorBusqueda);
	                    Activo activo = new Activo(nombreDispositivo, urlImagen, tipoActual);
	                    activoRepo.save(activo);
	                }
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    	
	    	
	    	// Cargar vulnerabiliades del csv
	    	CSVReader reader = new CSVReader(new FileReader("/Users/alinasolonarubotnari/ALBAUC/CIBEL/Docs/Datos/ListaLargaV2.csv"));
	    	List<String[]> rows = reader.readAll();
	    	
	    	for (String[] row : rows) {
	    		if (row[0].equals("Modelo")) {
	    	        continue; // Saltar la fila de encabezado
	    	    }
	    		Vulnerabilidad v = new Vulnerabilidad();
	    		v.setIdCVE(row[1]);
	    		v.setDescripcion(row[2]);
	    		v.setConfidentialityImpact(row[5]);
	    		v.setIntegrityImpact(row[6]);
	    		v.setAvailabilityImpact(row[7]);
	    		if (row[8] != null && !row[8].isEmpty()) {
	    		    v.setBaseScore(Double.parseDouble(row[8]));
	    		} else {
	    		    v.setBaseScore(0);
	    		}

	    		v.setBaseSeverity(row[9]);
	    		
	    		cvesRepo.save(v);
	    		
	    		Activo a = activoRepo.findByNombre(row[0]);
	    		a.getVulnerabilidades().add(v);
	    		activoRepo.save(a);
	    	}
	    	
	    };
	  }
	  
	    public static String getImageUrl(String query, String apiKey, String idMotorBusqueda) throws IOException {
	    	String googleSearchUrl = "https://www.googleapis.com/customsearch/v1?q=" 
	    								+ URLEncoder.encode(query, "UTF-8") 
	    								+ "&cx=" + idMotorBusqueda 
	    								+ "&num=1&searchType=image&key=" + apiKey;
	        URL url = URI.create(googleSearchUrl).toURL();
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
