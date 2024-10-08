package com.WeatherManage.Helper;

import com.WeatherManage.data.WeatherManageData;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class DatabaseHelper {
    private static final String URL = "jdbc:sqlite:javabook.db";

    private static void initializeDatabase() {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS weather_data (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "date TEXT," +
                "place_name TEXT,"+
                "sunrise TEXT," +
                "timezone TEXT," +
                "temperature_max TEXT," +
                "precipitation TEXT," +
                "timezone_abbreviation TEXT," +
                "weather_code TEXT," +
                "temperature_min TEXT," +
                "sunset TEXT," +
                "temperature TEXT," +
                "apparent_temperature TEXT," +
                "humidity TEXT," +
                "time TEXT," +
                "uvIndex TEXT," +
                "windSpeed TEXT)";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sqlCreate);
            System.out.println("successfully created....");
        } catch (SQLException e) {
            System.err.println("Error during the database table initialization: " + e.getMessage());
        }
    }

    public static void storeDataInSQLite(WeatherManageData data) {
        // Method to insert data into the database
        initializeDatabase();
        String sqlInsert = "INSERT INTO weather_data (date, place_name, sunrise, timezone, temperature_max, precipitation, timezone_abbreviation, weather_code,temperature_min, sunset, temperature, apparent_temperature, humidity, time, uvIndex, windSpeed) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement statement = conn.prepareStatement(sqlInsert)) {
            statement.setString(1, data.getDate());
            statement.setString(2,data.getPlace_name());
            statement.setString(3, data.getSunrise());
            statement.setString(4, data.getTimezone());
            statement.setString(5, data.getTemperature_max());
            statement.setString(6, data.getPrecipitation());
            statement.setString(7, data.getTimezone_abbreviation());
            statement.setString(8, data.getWeathercode());
            statement.setString(9, data.getTemperature_min());
            statement.setString(10, data.getSunset());
            statement.setString(11,data.getTemperature());
            statement.setString(12,data.getApparent_temperature());
            statement.setString(13,data.getHumidity());
            statement.setString(14,data.getTime());
            statement.setString(15,data.getUvIndex());
            statement.setString(16,data.getWindSpeed());
            

            statement.executeUpdate();
            System.out.println("insertion done....");
        } catch (SQLException e) {
            System.err.println("Error inserting data into the database: " + e.getMessage());
        }

    }

    public static List<WeatherManageData> retrieveDataFromDB(){
        List<WeatherManageData> weatherDataList =new ArrayList<>();
        String sql = "SELECT * FROM weather_data";
        try(Connection conn = DriverManager.getConnection(URL);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                WeatherManageData weatherManageData = new WeatherManageData();
                weatherManageData.setDate(rs.getString("date"));
                weatherManageData.setPlace_name(rs.getString("place_name"));
                weatherManageData.setSunrise(rs.getString("sunrise"));
                weatherManageData.setTimezone(rs.getString("timezone"));
                weatherManageData.setTemperature_max(rs.getString("temperature_max"));
                weatherManageData.setPrecipitation(rs.getString("precipitation"));
                weatherManageData.setTimezone_abbreviation(rs.getString("timezone_abbreviation"));
                weatherManageData.setWeathercode(rs.getString("weather_code"));
                weatherManageData.setTemperature_min(rs.getString("temperature_min"));
                weatherManageData.setSunset(rs.getString("sunset"));
                weatherManageData.setTemperature(rs.getString("temperature"));
                weatherManageData.setApparent_temperature(rs.getString("apparent_temperature"));
                weatherManageData.setHumidity(rs.getString("humidity"));
                weatherManageData.setTime(rs.getString("time"));
                weatherManageData.setUvIndex(rs.getString("uvIndex"));
                weatherManageData.setWindSpeed(rs.getString("windSpeed"));
                

                weatherDataList.add(weatherManageData);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return weatherDataList;

    }

        public static void deleteWeatherDataByDate(String time) {
        String sqlDelete = "DELETE FROM weather_data WHERE time = ?" ;

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sqlDelete)) {

            pstmt.setString(1, time);// Set the date parameter in the SQL query
           int affectedRows = pstmt.executeUpdate();  // Execute the DELETE operation

            System.out.println(affectedRows + " rows deleted.");
        } catch (SQLException e) {
            System.err.println("Error deleting data from the database: " + e.getMessage());
        }
    }


}