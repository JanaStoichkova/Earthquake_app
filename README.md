# Earthquake Assignment

This is a Spring Boot application that displays live earthquake data.There is a table view that shows all of the important fields and also a map view that shows the exact epicenter where the earthquake happened.
Along with that the user has the ability to filter the entries by the magnitude and the time of their occurrence. The data is fetched from USGS GeoJSON API and stored in a PostgreSQL database.

![App Screenshot 1](<images/Screenshot 1.png>)
![App Screenshot 2](<images/Screenshot 2.png>)
![App Screenshot 3](<images/Screenshot 3.png>)
![App Screenshot 4](<images/Screenshot 4.png>)

## Project setup instructions

### Requirements
- Java 17
- PostgreSQL

### Clone the project
```bash
git clone https://github.com/JanaStoichkova/Earthquake_app.git
cd earthquake-assignment
```

### Install dependencies
```bash
mvn clean install
```

## Database configuration steps

1. Start PostgreSQL
```bash
sudo systemctl start postgresql
```

2. Open the PostgreSQL shell
```bash
sudo -u postgres psql
```

3. Create the database
```sql
CREATE DATABASE earthquake_db;
```

4. Exit PostgreSQL
```sql
\q
```

5. Set the database connection in `src/main/resources/application.properties`.
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/earthquake_db
spring.datasource.username=postgres
spring.datasource.password=your_password
```

### Run the backend

```bash
mvn spring-boot:run
```

### Run the frontend

```bash
cd frontend
npm install
npm run start
```

### Then you can access the app at

```bash
http://localhost:8080
```

### Useful backend endpoints
- `POST /api/earthquakes/fetch` - fetch and save fresh earthquake data
- `GET /api/earthquakes` - list all earthquakes
- `GET /api/earthquakes/filter-mag/{mag}` - filter by magnitude
- `GET /api/earthquakes/filter-time/{time}` - filter by time
- `DELETE /api/earthquakes/delete/{id}` - delete one earthquake

### Important Note

Since you can't run a POST request through your browser, I would suggest using the following curl command to accomplish that:

```bash
curl -X POST http://localhost:8080/api/earthquakes/fetch
```

### Running tests

```bash
mvn test
```

## Futute Improvements

As future improvements, I would like to do the following 2 things first:
- Use a Docker Container so the backend and the PostgreSQL can run together without the need of manual setup on the host machine.
- Add the ability to fetch the data from the USGS GeoJSON API without the need to run commands in the terminal.

