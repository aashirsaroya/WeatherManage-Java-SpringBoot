# **WeatherManage-Java-SpringBoot**

A comprehensive weather management application built with **Java**, **Spring Boot**, and **SQLite**, designed to fetch and manage real-time weather information through a user-friendly GUI. It integrates multiple RESTful APIs and uses multithreading for efficient data retrieval and processing.

## **Features**
- **Real-time Weather Data**: Fetch accurate weather data for any location using integrated RESTful APIs.
- **SQLite Database Integration**: Store and retrieve weather data locally using SQLite.
- **Multithreading**: Ensures smooth and responsive data fetching without freezing the UI.
- **User Interface**: Built with **Swing** to provide a clean and interactive graphical interface for users.
  
## **Technologies Used**
- **Java (Spring Boot)**: Backend logic and API handling.
- **Swing**: GUI for interacting with users.
- **SQLite**: Local database for storing weather data.
- **RESTful APIs**: Integration of public APIs to fetch weather information.
- **JDBC**: Java Database Connectivity for managing SQLite database operations.
- **Multithreading**: For concurrent data fetching and smooth application flow.

## **System Architecture**
1. **GUI Layer**: 
   - `WeatherAppGui`: Handles user input, displays weather data, and allows data storage.
2. **Controller Layer**: 
   - `WeatherManageController`: Coordinates between the user interface and the service layer.
3. **Service Layer**:
   - `WeatherManagementServices`: Processes data from APIs and interacts with the controller.
4. **Data Layer**:
   - `DatabaseHelper`: Manages SQLite operations.
   - `WeatherManageData`: Represents weather data structure.

## **API Endpoints**
- `/api/v1/getWeatherData/{name}`: Retrieves real-time weather for the specified location.
- `/api/v1/storeWeatherData`: Stores fetched weather data into the SQLite database.
- `/api/v1/getAllWeatherData`: Retrieves all stored weather data.
- `/api/v1/deleteWeatherData/{time}`: Deletes specific weather data by time.

## **Installation Steps**
1. **Java Setup**:
   - Install **Java 17** from the [Oracle JDK 17 archive](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html).
2. **Import Project**:
   - In **Eclipse**, navigate to `File -> Import -> Maven -> Existing Maven Projects`.
   - Browse to the directory where the project is located.
3. **Configure JDK**:
   - Right-click on the project and navigate to `Properties -> Java Build Path -> Libraries -> JRE System Libraries -> Edit -> Installed JREs`.
   - Select **Java SE 17** and apply changes.
4. **Run Application**:
   - Right-click the project and select `Run As -> Java Application -> WeatherManageApplication`.

## **Screenshots**
_Add relevant screenshots here, showcasing the UI and database interactions._

## **Usage**
- **Search Weather**: Use the search functionality in the GUI to fetch and display weather data.
- **Store Data**: Click on the save button to store fetched data into the SQLite database.
- **View Stored Data**: Access stored weather information using the database interface.

## **Database Commands**
1. Navigate to the project directory: `cd /path/to/project/WeatherManage_3`.
2. Open SQLite database: `sqlite3 javabook.db`.
3. Query weather data: `select * from weather_data;`.

## **Contributing**
Feel free to open issues or submit pull requests to enhance the functionality of this project.

## **License**
This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
