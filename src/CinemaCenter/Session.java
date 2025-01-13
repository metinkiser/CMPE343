package CinemaCenter;

import javafx.beans.property.*;

/**
 * Represents a movie session in the cinema system.
 * This class manages all the details related to a specific movie screening,
 * including session ID, hall information, timing details, and seat availability.
 */
public class Session {

    private final IntegerProperty sessionID;
    private final StringProperty hall;
    private final StringProperty date;
    private final StringProperty startTime;
    private final StringProperty endTime;
    private final IntegerProperty vacantSeats;
    
    private Movie movie;
    
    /**
     * Gets the movie associated with this session.
     * @return The movie object associated with the session
     */
    public Movie getMovie() {
        return movie;
    }

    /**
     * Sets the movie for this session.
     * @param movie The movie object to be associated with the session
     */
    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    /**
     * Constructs a new Session with the specified parameters.
     * @param sessionID Unique identifier for the session
     * @param hall Name or identifier of the cinema hall
     * @param date Date of the session
     * @param startTime Starting time of the movie
     * @param endTime Ending time of the movie
     * @param vacantSeats Number of available seats
     */
    public Session(int sessionID, String hall, String date, String startTime, String endTime, int vacantSeats) {
        this.sessionID = new SimpleIntegerProperty(sessionID);
        this.hall = new SimpleStringProperty(hall);
        this.date = new SimpleStringProperty(date);
        this.startTime = new SimpleStringProperty(startTime);
        this.endTime = new SimpleStringProperty(endTime);
        this.vacantSeats = new SimpleIntegerProperty(vacantSeats);
    }

    /**
     * @return The session's unique identifier
     */
    public int getSessionID() {
        return sessionID.get();
    }

    /**
     * @return The session ID property for JavaFX binding
     */
    public IntegerProperty sessionIDProperty() {
        return sessionID;
    }

    /**
     * @return The hall name or identifier
     */
    public String getHall() {
        return hall.get();
    }

    /**
     * @return The hall property for JavaFX binding
     */
    public StringProperty hallProperty() {
        return hall;
    }

    /**
     * @return The session's start time
     */
    public String getStartTime() {
        return startTime.get();
    }

    /**
     * @return The start time property for JavaFX binding
     */
    public StringProperty startTimeProperty() {
        return startTime;
    }

    /**
     * @return The session's end time
     */
    public String getEndTime() {
        return endTime.get();
    }

    /**
     * @return The end time property for JavaFX binding
     */
    public StringProperty endTimeProperty() {
        return endTime;
    }

    /**
     * @return The number of vacant seats
     */
    public int getVacantSeats() {
        return vacantSeats.get();
    }

    /**
     * @return The vacant seats property for JavaFX binding
     */
    public IntegerProperty vacantSeatsProperty() {
        return vacantSeats;
    }

    /**
     * @return The session date
     */
    public String getDate() {
        return date.get();
    }

    /**
     * @return The date property for JavaFX binding
     */
    public StringProperty dateProperty() {
        return date;
    }

    /**
     * Returns a string representation of the Session object.
     * @return A string containing the session details
     */
    @Override
    public String toString() {
        return "Session{" +
                "sessionID=" + sessionID.get() +
                ", hall='" + hall.get() + '\'' +
                ", startTime='" + startTime.get() + '\'' +
                ", endTime='" + endTime.get() + '\'' +
                ", vacantSeats=" + vacantSeats.get() +
                '}';
    }
}
