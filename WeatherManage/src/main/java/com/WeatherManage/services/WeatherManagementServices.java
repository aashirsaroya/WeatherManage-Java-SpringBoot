package com.WeatherManage.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;



@Service
public class WeatherManagementServices {
    @Value("${weatherUrl}")
    private String weatherUrl;
    @Value("${geoCodingApi}")
    private String geoCodingURL;

    @Autowired
    private RestTemplate restTemplate;
    public Object getAllWeatherData(String name) throws JSONException, JsonProcessingException {
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("name", name);
        ResponseEntity<String> geoCodingJSON;
        String place_name = "";
        HttpHeaders headers =new HttpHeaders();
        try {
            geoCodingJSON = restTemplate.exchange(geoCodingURL,HttpMethod.GET,new HttpEntity<>(headers),String.class,uriVariables);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Issue with the Geo Coding API");
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode rootNode = mapper.readTree(geoCodingJSON.getBody());
            JsonNode resultsNode = rootNode.get("results");
            Map<String,Double> coordinates =new HashMap<>();

            if (resultsNode.isArray() && !resultsNode.isEmpty()) {

//                System.out.println("resultof api 1"+resultsNode.toString());
                JsonNode firstResult = resultsNode.get(0);

                double latitude = firstResult.path("latitude").asDouble();
                double longitude = firstResult.path("longitude").asDouble();

                coordinates.put("lat",latitude);
                coordinates.put("lon",longitude);
                place_name = firstResult.get("name").toString();
//                System.out.println("Latitude: " + latitude);
//                System.out.println("Longitude: " + longitude);

            } else {
                System.out.println("No results found.Failed to fetch data : ");
            }

//        System.out.println("geoCodingAPI-->"+geoCodingJSON.getBody());
            ResponseEntity<String> response = null;
            try {
                response =restTemplate.exchange(weatherUrl, HttpMethod.GET,new HttpEntity<>(headers),String.class,coordinates);
            } catch (NullPointerException e){
                return ResponseEntity.status(500).body("Error fetching data from weather api");
            }

            ResponseEntity<String> customized = weatherJSONcustomised(response,place_name);
//            JsonNode responseNode = mapper.readTree(response.getBody());
            return customized.getBody();

        } catch (Exception e) {
//            System.out.println("Issue with the Geo Coding API"+e.getMessage());
            return ResponseEntity.status(500).body("Error processing JSON data");
        }
    }
    public ResponseEntity<String> weatherJSONcustomised(ResponseEntity<String> response, String place_name) throws JsonProcessingException {
        ObjectMapper mapper =new ObjectMapper();
        JsonNode rootNode = mapper.readTree(response.getBody());
        Map<String, Object> jsonMap =new HashMap<>();

        // extracting the data from weather API and  returning the customized JSON object
        String temperature=String.valueOf(rootNode.get("current").get("temperature_2m")).replace("\"", "");
        String temperature_units = String.valueOf(rootNode.get("current_units").get("temperature_2m"));
        temperature = temperature.concat(temperature_units).replace("\"", "");;

        String humidity=String.valueOf(rootNode.get("current").get("relative_humidity_2m"));
        String humidity_units = String.valueOf(rootNode.get("current_units").get("relative_humidity_2m"));
        humidity = humidity.concat(humidity_units).replace('"',' ').trim();
        String apparentTemperature=String.valueOf(rootNode.get("current").get("apparent_temperature"));
        String apparentTemperature_units = String.valueOf(rootNode.get("current_units").get("apparent_temperature"));
        apparentTemperature = apparentTemperature.concat(apparentTemperature_units).replace('"',' ').trim();

        String dateTimeString = String.valueOf(rootNode.get("current").get("time"));
        dateTimeString=dateTimeString.replace("\"", "");
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeString, inputFormatter);

        String date = dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String time = dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));

        long weathercode = rootNode.get("current").get("weather_code").asLong();
        String weatherType = convertWeatherCode(weathercode);
        JsonNode sunriseArray = rootNode.get("daily").get("sunrise");
        JsonNode sunsetArray = rootNode.get("daily").get("sunset");
        String uvIndex = rootNode.get("daily").get("uv_index_max").get(0).toString();
        String temp_units = rootNode.get("daily_units").get("temperature_2m_max").toString().replace('"',' ');
        String temperature_max =rootNode.get("daily").get("temperature_2m_max").get(0).toString();
        temperature_max = temperature_max.concat(temp_units).trim();
        String temperature_min =rootNode.get("daily").get("temperature_2m_min").get(0).toString();
        temperature_min =temperature_min.concat(temp_units).trim();

        String sunrise ="";
        String sunset = "";

        if (sunriseArray.isArray() && sunriseArray.has(0)) {
            sunrise = sunriseArray.get(0).toString().replace("\"", "");
            sunset =sunsetArray.get(0).toString().replace("\"", "");
            dateTime = LocalDateTime.parse(sunset, inputFormatter);
            sunset=dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
            LocalDateTime dateTime2 = LocalDateTime.parse(sunrise, inputFormatter);
            sunrise=dateTime2.format(DateTimeFormatter.ofPattern("HH:mm"));
        } else {
            System.out.println("Sunrise and sunset array is empty or not found.");
        }

        String windSpeed = rootNode.get("current").get("wind_speed_10m").toString();
        windSpeed = windSpeed.concat(" mph");
        String precipitation = rootNode.get("current").get("precipitation").toString();
        precipitation = precipitation.concat(" inches");
        
        jsonMap.put("date",date);
        jsonMap.put("place_name",place_name);
        jsonMap.put("sunrise",sunrise);
        jsonMap.put("timezone", String.valueOf(rootNode.get("timezone")));
        jsonMap.put("temperature_max",temperature_max);
        jsonMap.put("precipitation", precipitation);
        jsonMap.put("timezone_abbreviation", String.valueOf(rootNode.get("timezone_abbreviation")).replace("\"", ""));
        jsonMap.put("weathercode",weatherType);
        jsonMap.put("temperature_min",temperature_min);
        jsonMap.put("sunset",sunset);
        jsonMap.put("temperature",temperature);
        jsonMap.put("apparent_temperature",apparentTemperature);
        jsonMap.put("humidity",humidity);
        jsonMap.put("time", time);       
        jsonMap.put("uvIndex",uvIndex);
        jsonMap.put("windSpeed", windSpeed);
      

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse;
        try {
            jsonResponse = objectMapper.writeValueAsString(jsonMap);
        } catch (JsonProcessingException e) {
            return ResponseEntity.internalServerError().body("{\"error\":\"Unable to process JSON\"}");
        }
        return ResponseEntity.ok(jsonResponse);
    }
    private static String convertWeatherCode(long weathercode) {
        String weatherCondition = "";
        if(weathercode == 0L){

            weatherCondition = "Clear";
        }else if(weathercode > 0L && weathercode <= 3L){

            weatherCondition = "Cloudy";
        }else if((weathercode >= 51L && weathercode <= 67L) || (weathercode >= 80L && weathercode <= 99L)){

            weatherCondition = "Rain";
        }else if(weathercode >= 71L && weathercode <= 77L){

            weatherCondition = "Snow";
        }

        return weatherCondition;
    }

}