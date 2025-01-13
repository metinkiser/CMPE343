package cinemaCenter;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents a session, including how many tickets have been sold (ticketsSold).
 */
public class SessionData {
    private int sessionID;
    private String hall;
    private LocalDate sessionDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private int ticketsSold;

    public SessionData(int sessionID, String hall, LocalDate sessionDate, LocalTime startTime, LocalTime endTime) {
        this.sessionID = sessionID;
        this.hall = hall;
        this.sessionDate = sessionDate;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getSessionID() { return sessionID; }
    public String getHall() { return hall; }
    public LocalDate getSessionDate() { return sessionDate; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }

    /**
     * Number of tickets sold for this session (used in the table).
     */
    public int getTicketsSold() {
        return ticketsSold;
    }

    public void setTicketsSold(int ticketsSold) {
        this.ticketsSold = ticketsSold;
    }
}
