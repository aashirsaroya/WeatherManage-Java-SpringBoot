package com.WeatherManage;

import com.WeatherManage.GUI.WeatherAppGui;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class WeatherManageApplication {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	public static void main(String[] args) {
		new WeatherAppGui().setVisible(true);
		System.out.println("Starting WeatherManageApplication...");
		SpringApplication.run(WeatherManageApplication.class, args);
	}

}
