# Seatscape API Documentation

Welcome to the Seatscape API documentation. This API provides endpoints to manage cinemas, movies, shows, tickets, food items, and food orders.

## Seatscape Functionality

### Cinema Management

Add Cinema: Add details of a cinema, including its name, address, and halls.
Retrieve Cinemas: Get information on cinemas by city, state, or cinema ID.
Delete Cinemas: Remove cinema entries from the system based on the cinema ID.

### Movie Management

Add Movie: Insert new movie details (title, genre, language, etc.).
Retrieve Movies: Get all movies or specific movies based on different criteria such as duration, language, genre, or title.
Delete Movies: Delete movie records by their unique ID.

### Show Management
 
Schedule Shows: Admin can add show timings for movies in different cinemas.
Retrieve Shows: Retrieve shows based on the cinema ID, movie ID, or city.
View Future Shows: Get upcoming movie shows and manage the show schedule.

### Ticketing System

Book Tickets: Customers can book tickets for any available show.
View Tickets: Users can view their booked tickets and cancel them if needed.
Check Available Seats: Get information about available seats for a specific show.

### Food Ordering System

Add Food Items: Admin can add new food items (popcorn, snacks, drinks, etc.) for sale during the booking process.
Order Food: Customers can order food along with their movie tickets.
Track Payment: Food orders are tracked, and customers can check if their order has been paid or not

## Tech Stack

### Backend Framework:

Spring Boot (Java-based framework for building APIs)
Spring Data JPA (For database operations and ORM mapping)
Hibernate ORM (For object-relational mapping)
MySQL (For data storage)

### Frontend:

Postman (For API testing)

### API Testing:

Postman Collections (For easy API testing and integration)

### Deployment:

Render (For cloud hosting and deployment)

## Features

Cinema Management: Easily manage cinemas, including adding, deleting, and viewing cinema details by city, state, or cinema ID.
Movie Management: Add new movies, retrieve movie details based on criteria such as genre, language, and duration, and delete movies.
Show Management: Admin can add shows for a particular movie, and users can retrieve shows by cinema, movie, or city.
Ticketing: Book, view, and cancel tickets; also check available seats for any movie show.
Food Ordering: Customers can add food items to their ticket booking, track food orders, and pay for them.

## Controllers

### Cinema Controller

#### Endpoints

*- POST /cinema/put:* Add or update a cinema.
JSON Example:
```json
{
  "cinemaid": 101,
  "name": "Regal Cinema",
  "totalhalls": 1,
  "address": "384, Chinhpokli Gali, Rampur",
  "city": "Rampur",
  "state": "Karnataka"
}
```
**- GET /cinema/all:** Get all cinemas.
**- GET /cinema/{id}:**  Get cinema by ID.
**- GET /cinema/city/{cityname}:** Get cinemas by city.
*- GET /cinema/state/{statename}:* Get cinemas by state.
*- GET /cinema/statecity/{statename}/{cityname}:* Get cinemas by state and city.
*- DELETE /cinema/delete/{id}:* Delete cinema by ID.

### Hall Controller

#### Endpoints

*-PUT /hall/create/{cinemaid}:* Create a hall for a cinema.
JSON Example:
```json
{
  "hallid": 1,
  "hallname": "Hall 1",
  "totalSeats": 50
}
```
*-GET /hall/getall:* Get all halls.
*-GET /hall/get/{cinemaid}:* Get halls by cinema ID.

### Movie Controller

#### Endpoints

*-PUT /movie/put:* Add or update a movie.
JSON Example:
```json
{
  "movieid": 1,
  "title": "Inception",
  "description": "Sci-fi thriller",
  "duration": 148,
  "genre": "Sci-fi",
  "lang": "English",
  "releasedate": "2010-07-16"
}
```
*-GET /movie/getAll:* Get all movies.
*-GET /movie/get/{id}:* Get movie by ID.
*-GET /movie/get/duration/greater/{duration}:* Get movies with duration greater than a value.
*-GET /movie/get/duration/lesser/{duration}:* Get movies with duration lesser than a value.
*-GET /movie/get/language/{lang}:* Get movies by language.
*-GET /movie/get/genre/{genre}:* Get movies by genre.
*-GET /movie/get/title/{keyword}:* Find movies by title keyword.
*-DELETE /movie/delete/{id}:* Delete a movie by ID.

### Food Controller

#### Endpoints

*-PUT /food/add:* Add a new food item.
JSON Example:
```json
{
  "id": 1,
  "name": "Popcorn",
  "description": "Salted",
  "price": 150,
  "itemType": "Solid"
}
```
*-GET /food/all:* Get all food items.
*-GET /food/id/{id}:* Get food item by ID.

### Food Order Controller

#### Endpoints

*-GET /foodorder/getall:* Get all food orders.
*-GET /foodorder/ticketid/{ticketid}:* Get food order by ticket ID.
*-GET /foodorder/id/{id}:* Get food order by ID.
*-GET /foodorder/showid/{showid}:* Get food orders by show ID.
*-PUT /foodorder/placeorder:* Place a food order.
JSON Example:
```json
{
  "ticketId": 1,
  "items": [1],
  "totalPrice": 150,
  "paid": true
}
```
*-DELETE /foodorder/cancel/{id}:* Cancel a food order by ID.
JSON Example:
```json
{
  "id": 1,
  "ticketId": 1,
  "items": [1],
  "totalPrice": 150,
  "paid": false,
  "status": "Cancelled"
}
```
### Show Controller

#### Endpoints

*-PUT /show/add:* Add a new show.
JSON Example:
```json
{
  "cinemaId": 101,
  "hallId": 1011,
  "movieId": 9,
  "availableSeats": 8,
  "startYear": 2010,
  "startMonth": 7,
  "startDay": 16,
  "startHour": 18,
  "startMin": 30,
  "bookedTickets": 5
}
```
*-GET /show/all:* Get all shows.
*-GET /show/id/{id}:* Get show by ID.
*-GET /show/cid/{cinemaid}:* Get shows by cinema ID.
*-GET /show/mid/{movieid}:* Get shows by movie ID.
*-GET /show/future:* Get future shows.
*-GET /show/future/{cinemaid}:* Get future shows by cinema ID.
*-GET /show/city/{cityname}:* Get shows by city name.
*-GET /show/citymovie/{cityname}/{moviename}:* Get shows by city and movie name.

### Ticket Controller

#### Endpoints

*=PUT /ticket/create:* Create a ticket.
JSON Example:
```json
{
  "showId": 19,
  "numberOfSeats": 1,
  "bookedBy": "anki",
  "bookedSeats": 2
}
```
*-GET /ticket/all:* Get all tickets.
*-GET /ticket/show/{showid}:* Get tickets by show ID.
*-GET /ticket/id/{ticketid}:* Get ticket by ticket ID.
*-GET /ticket/user/{username}:* Get tickets by username.
*-GET /ticket/show/{showid}/availableseats:* Get available seats by show ID.
*-DELETE /ticket/cancel/{id}:* Cancel a ticket by ID.
JSON Example:
```json
{
  "ticket_id": 2,
  "booked_by": "anku",
  "bookedseats": "3",
  "number_of_seats": 1,
  "show_id": 25
}
```
## Installation & Setup

### Prerequisites
- Java 11 or later
- MySQL (For storing data)

### Steps to Run the Application

1. Clone the repository:

git clone https://github.com/yourusername/yourprojectname.git

2. Set up MySQL:

- Create a database in MySQL (e.g., seatscape).
- Configure the application.properties file for database connection:

spring.datasource.url=jdbc:mysql://localhost:3306/seatscape
spring.datasource.username=root
spring.datasource.password=yourpassword

3. Run the application using Maven:

mvn spring-boot:run

The application will be available on http://localhost:8084

## Usage

### Postman Collection

You can use Postman to test the API endpoints. Import the following Postman collection to interact with the API:

Postman Collection

## Contributor

- Shubhangi Singh

## License

This project is for learning purposes.


