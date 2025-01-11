package CinemaCenter;

import javafx.beans.property.*;

public class Session {

    private final IntegerProperty sessionID;
    private final StringProperty hall;
    private final StringProperty date;
    private final StringProperty startTime;
    private final StringProperty endTime;
    private final IntegerProperty vacantSeats;
    
    private Movie movie;
    
    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Session(int sessionID, String hall, String date, String startTime, String endTime, int vacantSeats) {
        this.sessionID = new SimpleIntegerProperty(sessionID);
        this.hall = new SimpleStringProperty(hall);
        this.date = new SimpleStringProperty(date);
        this.startTime = new SimpleStringProperty(startTime);
        this.endTime = new SimpleStringProperty(endTime);
        this.vacantSeats = new SimpleIntegerProperty(vacantSeats);
    }

    // Getters and setters for TableView binding
    public int getSessionID() {
        return sessionID.get();
    }

    public IntegerProperty sessionIDProperty() {
        return sessionID;
    }

    public String getHall() {
        return hall.get();
    }

    public StringProperty hallProperty() {
        return hall;
    }

    public String getStartTime() {
        return startTime.get();
    }

    public StringProperty startTimeProperty() {
        return startTime;
    }

    public String getEndTime() {
        return endTime.get();
    }

    public StringProperty endTimeProperty() {
        return endTime;
    }

    public int getVacantSeats() {
        return vacantSeats.get();
    }

    public IntegerProperty vacantSeatsProperty() {
        return vacantSeats;
    }

    public String getDate() {
        return date.get();
    }

    public StringProperty dateProperty() {
        return date;
    }

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
