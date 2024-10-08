package com.WeatherManage.GUI;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class WeatherAppGui extends JFrame {
    private StringBuilder lastFetchedJson; // Store the last fetched JSON
    public WeatherAppGui() {
        super("Weather App");
        System.out.println("Initializing WeatherAppGui");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(600, 800); // Increased size
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);
        addGuiComponents();
    }

    private void addGuiComponents() {
        // Using a JScrollPane to allow more components
        JPanel contentPane = new JPanel(null);
        contentPane.setPreferredSize(new Dimension(580, 1500)); // Larger content area to fit all details
        JScrollPane scrollPane = new JScrollPane(contentPane,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBounds(0, 0, 600, 800);
        scrollPane.setBorder(null);
        add(scrollPane);

        JTextField searchTextField = new JTextField();
        searchTextField.setBounds(15, 15, 451, 45);
        searchTextField.setFont(new Font("Dialog", Font.PLAIN, 24));
        contentPane.add(searchTextField);

        JLabel weatherConditionImage = new JLabel(loadImage("src/assets/weather-forecast.png"));
        weatherConditionImage.setBounds(75, 75, 450, 217);
        contentPane.add(weatherConditionImage);

        JLabel temperatureText = new JLabel("");
        temperatureText.setBounds(0, 300, 580, 54);
        temperatureText.setFont(new Font("Dialog", Font.BOLD, 48));
        temperatureText.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(temperatureText);

        JLabel weatherConditionDesc = new JLabel("Please enter a location to proceed.");
        weatherConditionDesc.setBounds(0, 360, 580, 36);
        weatherConditionDesc.setFont(new Font("Dialog", Font.PLAIN, 32));
        weatherConditionDesc.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(weatherConditionDesc);

        JLabel apparentTempImage = new JLabel(loadImage("src/assets/thermometer.png"));
        apparentTempImage.setBounds(10, 500, 65, 65);
        contentPane.add(apparentTempImage);

        JLabel apparentTempLabel = new JLabel("Feels Like: ");
        apparentTempLabel.setBounds(90, 500, 700, 60);
        apparentTempLabel.setFont(new Font("Dialog", Font.PLAIN, 24));
        contentPane.add(apparentTempLabel);

        // Humidity
        JLabel humidityImage = new JLabel(loadImage("src/assets/humidity.png"));
        humidityImage.setBounds(10, 600, 65, 65);
        contentPane.add(humidityImage);

        JLabel humidityLabel = new JLabel("Humidity: ");
        humidityLabel.setBounds(90, 600, 700, 70);
        humidityLabel.setFont(new Font("Dialog", Font.PLAIN, 24));
        contentPane.add(humidityLabel);

        // Wind Speed
        JLabel windSpeedImage = new JLabel(loadImage("src/assets/wind.png"));
        windSpeedImage.setBounds(10, 700, 65, 65);
        contentPane.add(windSpeedImage);

        JLabel windSpeedLabel = new JLabel("Wind Speed: ");
        windSpeedLabel.setBounds(90, 700, 700, 60);
        windSpeedLabel.setFont(new Font("Dialog", Font.PLAIN, 24));
        contentPane.add(windSpeedLabel);

        // Max Temperature
        JLabel maxTempImage = new JLabel(loadImage("src/assets/maxTemp.png"));
        maxTempImage.setBounds(10, 800, 65, 65);
        contentPane.add(maxTempImage);

        JLabel maxTempLabel = new JLabel("Max Temp: ");
        maxTempLabel.setBounds(90, 800, 700, 60);
        maxTempLabel.setFont(new Font("Dialog", Font.PLAIN, 24));
        contentPane.add(maxTempLabel);

        // Min Temperature
        JLabel minTempImage = new JLabel(loadImage("src/assets/minTemp.png"));
        minTempImage.setBounds(10, 900, 65, 65);
        contentPane.add(minTempImage);

        JLabel minTempLabel = new JLabel("Min Temp: ");
        minTempLabel.setBounds(90, 900, 700, 60);
        minTempLabel.setFont(new Font("Dialog", Font.PLAIN, 24));
        contentPane.add(minTempLabel);

        // Sunrise
        JLabel SunriseImage = new JLabel(loadImage("src/assets/sunrise.png"));
        SunriseImage.setBounds(10, 1000, 65, 65);
        contentPane.add(SunriseImage);

        JLabel SunriseLabel = new JLabel("Sunrise Time: ");
        SunriseLabel.setBounds(90, 1000, 700, 80);
        SunriseLabel.setFont(new Font("Dialog", Font.PLAIN, 24));
        contentPane.add(SunriseLabel);

        // Sunset
        JLabel SunsetImage = new JLabel(loadImage("src/assets/sunset.png"));
        SunsetImage.setBounds(10, 1100, 65, 65);
        contentPane.add(SunsetImage);

        JLabel SunsetLabel = new JLabel("Sunset Time: ");
        SunsetLabel.setBounds(90, 1100, 700, 80);
        SunsetLabel.setFont(new Font("Dialog", Font.PLAIN, 24));
        contentPane.add(SunsetLabel);

        // UV Index
        JLabel uvIndexImage = new JLabel(loadImage("src/assets/uv-index.png"));
        uvIndexImage.setBounds(10, 1200, 65, 65);
        contentPane.add(uvIndexImage);

        JLabel uvIndexLabel = new JLabel("UV Index: ");
        uvIndexLabel.setBounds(90, 1200, 700, 60);
        uvIndexLabel.setFont(new Font("Dialog", Font.PLAIN, 24));
        contentPane.add(uvIndexLabel);

        // Precipitation probability
        JLabel rainProbImage = new JLabel(loadImage("src/assets/rain-prob.png"));
        rainProbImage.setBounds(10, 1300, 65, 65);
        contentPane.add(rainProbImage);

        JLabel rainProbLabel = new JLabel("Precipitation: ");
        rainProbLabel.setBounds(90, 1300, 700, 50);
        rainProbLabel.setFont(new Font("Dialog", Font.PLAIN, 24));
        contentPane.add(rainProbLabel);

        JButton searchButton = new JButton(loadImage("src/assets/search.png"));
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBounds(480, 13, 47, 45);
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // get location from user
                String userInput = searchTextField.getText();

                // validate input - remove whitespace to ensure non-empty text
                if (userInput.replaceAll("\\s", "").length() <= 0) {
                    return;
                }

                String urlString = "http://localhost:8081/api/v1/getWeatherData/" + userInput;
                System.out.println(urlString);
                lastFetchedJson = getjsonNode(urlString); // Save the fetched JSON
                System.out.println("Result: " + lastFetchedJson);

                if (lastFetchedJson == null) {
                    weatherConditionDesc.setText("Please verify the location entered.");
                    weatherConditionDesc.setForeground(Color.RED);
                    temperatureText.setText("");
                    apparentTempLabel.setText("Feels Like: ");
                    humidityLabel.setText("Humidity: ");
                    windSpeedLabel.setText("Wind Speed: x mph");
                    maxTempLabel.setText("Max Temp: ");
                    minTempLabel.setText("Min Temp: ");
                    SunriseLabel.setText("Sunrise Time: ");
                    SunsetLabel.setText("Sunset Time: ");
                    uvIndexLabel.setText("UV Index: ");
                    rainProbLabel.setText("Probability of Rain: X%");
                    return;
                }

                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = null;
                try {
                    jsonNode = mapper.readTree(lastFetchedJson.toString());
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                String apparentTemp = jsonNode.get("apparent_temperature").toString().replace("\"", "");
                String humidity = jsonNode.get("humidity").toString().replace("\"", "");
                String maxTemp = jsonNode.get("temperature_max").toString().replace("\"", "");
                String minTemp = jsonNode.get("temperature_min").toString().replace("\"", "");
                String sunrise = jsonNode.get("sunrise").toString().replace("\"", "");
                String sunset = jsonNode.get("sunset").toString().replace("\"", "");
                String uvIndex = jsonNode.get("uvIndex").toString().replace("\"", "");
                String temp = jsonNode.get("temperature").toString().replace("\"", "");
                String weatherCondition = jsonNode.get("weathercode").toString().replace("\"", "");
                String windSpeed = jsonNode.get("windSpeed").toString().replace("\"", "");
                String precipitation = jsonNode.get("precipitation").toString().replace("\"", "");

                temperatureText.setText(temp);
                apparentTempLabel.setText("Feels Like: " + apparentTemp);
                humidityLabel.setText("Humidity: " + humidity);
                windSpeedLabel.setText("Wind Speed: " + windSpeed);
                maxTempLabel.setText("Max Temp: " + maxTemp);
                minTempLabel.setText("Min Temp: " + minTemp);
                SunriseLabel.setText("Sunrise Time: " + sunrise);
                SunsetLabel.setText("Sunset Time: " + sunset);
                uvIndexLabel.setText("UV Index: " + uvIndex);
                rainProbLabel.setText("Precipitation: " + precipitation );

                switch (weatherCondition) {
                    case "Clear":
                        weatherConditionImage.setIcon(loadImage("src/assets/sun.png"));
                        break;
                    case "Cloudy":
                        weatherConditionImage.setIcon(loadImage("src/assets/clouds.png"));
                        break;
                    case "Rain":
                        weatherConditionImage.setIcon(loadImage("src/assets/rain.png"));
                        break;
                    case "Snow":
                        weatherConditionImage.setIcon(loadImage("src/assets/snowflake.png"));
                        break;
                }

                weatherConditionDesc.setForeground(Color.BLACK);
                weatherConditionDesc.setText(weatherCondition);
            }
        });
        contentPane.add(searchButton);

        // Save Button
        JButton saveButton = new JButton(loadImage("src/assets/save.png"));
        saveButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        saveButton.setBounds(540, 13, 47, 45);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (lastFetchedJson != null) {
                    String response = storeWeatherData(lastFetchedJson.toString());
                    System.out.println("Save Response: " + response);
                    JOptionPane.showMessageDialog(null, response, "Save Result", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "No data to save. Please search first.", "Save Result", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        contentPane.add(saveButton);
    }

    private static HttpURLConnection fetchApiResponse(String urlString) {
        try {
            // attempt to create connection
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // set request method to get
            conn.setRequestMethod("GET");

            // connect to our API
            conn.connect();
            return conn;
        } catch (IOException e) {
            e.printStackTrace();
        }

        // could not make connection
        return null;
    }

    private static StringBuilder getjsonNode(String urlString) {
        StringBuilder resultJson = new StringBuilder();
        try {
            HttpURLConnection conn = fetchApiResponse(urlString);
            if (conn.getResponseCode() != 200) {
                System.out.println("Error: Could not connect to API");
                return null;
            }

            Scanner scanner = new Scanner(conn.getInputStream());
            while (scanner.hasNext()) {
                resultJson.append(scanner.nextLine());
            }

            scanner.close();
            conn.disconnect();

            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(resultJson.toString());
            if (rootNode.has("statusCode") && "INTERNAL_SERVER_ERROR".equals(rootNode.get("statusCode").asText())) {
                System.out.println("API Error: " + rootNode.get("body").asText());
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultJson;
    }

    private static String storeWeatherData(String weatherData) {
    	System.out.println("Sending to DB:" + weatherData);
        String responseMessage = "Failed to store weather data.";
        try {
            URL url = new URL("http://localhost:8081/api/v1/storeWeatherData");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = weatherData.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    responseMessage = response.toString();
                }
            } else {
                System.out.println("HTTP Error Code: " + responseCode);
            }

            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseMessage;
    }

    private ImageIcon loadImage(String resourcePath) {
        try {
            BufferedImage image = ImageIO.read(new File(resourcePath));
            return new ImageIcon(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Could not find resource");
        return null;
    }
}
