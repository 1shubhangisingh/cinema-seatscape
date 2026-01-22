package com.seatscape.seatscape.dao;

import com.seatscape.seatscape.model.Show;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
@Transactional
public interface ShowDAO extends JpaRepository<Show, Integer> {

    @Query(value = "SELECT * FROM shows s WHERE s.cinemaid = :cinemaId", nativeQuery = true)
    List<Show> findByCinemaId(@Param("cinemaId") Integer cinemaId);

    @Query(value = "SELECT * FROM shows s WHERE s.movieid = :movieId", nativeQuery = true)
    List<Show> getByMovieId(@Param("movieId") Integer movieId);

    @Query(value = "SELECT * FROM shows s WHERE s.starttime > :timestamp", nativeQuery = true)
    List<Show> getFutureShows(@Param("timestamp") Timestamp timestamp);

    @Query(value = "SELECT * FROM shows s WHERE s.starttime > :timestamp AND s.cinemaid = :cinemaId", nativeQuery = true)
    List<Show> getFutureShowsByCinemaId(
            @Param("timestamp") Timestamp timestamp,
            @Param("cinemaId") Integer cinemaId
    );

    @Query(value = "SELECT * FROM shows s WHERE s.starttime > :timestamp AND s.movieid = :movieId", nativeQuery = true)
    List<Show> getFutureShowsByMovieId(
            @Param("timestamp") Timestamp timestamp,
            @Param("movieId") Integer movieId
    );

    @Query(value = """
        SELECT s.*
        FROM shows s
        JOIN cinema c ON s.cinemaid = c.cinemaid
        WHERE c.city = :cityName
        """, nativeQuery = true)
    List<Show> getShowsByCityName(@Param("cityName") String cityName);

    @Query(value = """
        SELECT s.*
        FROM shows s
        JOIN cinema c ON s.cinemaid = c.cinemaid
        JOIN movie m ON s.movieid = m.movieid
        WHERE c.city = :cityName
          AND m.title = :movieName
        """, nativeQuery = true)
    List<Show> getShowsByCityAndMovieName(
            @Param("cityName") String cityName,
            @Param("movieName") String movieName
    );

    @Query(value = "SELECT availableseats FROM shows WHERE showid = :showId", nativeQuery = true)
    Integer getAvailableSeatsFromShowId(@Param("showId") Integer showId);

    @Modifying
    @Query(value = "UPDATE shows SET availableseats = :finalValue WHERE showid = :showId", nativeQuery = true)
    void updateSeats(
            @Param("showId") Integer showId,
            @Param("finalValue") Integer finalValue
    );

    @Query(value = "SELECT bookedtickets FROM shows WHERE showid = :showId", nativeQuery = true)
    String getBookedSeatsByShowId(@Param("showId") Integer showId);

    @Modifying
    @Query(value = "UPDATE shows SET bookedtickets = :bookedSeats WHERE showid = :showId", nativeQuery = true)
    void setBookedSeats(
            @Param("bookedSeats") String bookedSeats,
            @Param("showId") Integer showId
    );
}