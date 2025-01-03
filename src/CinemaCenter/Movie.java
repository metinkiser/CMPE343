package CinemaCenter;

public class Movie {

    private int movieID;
    private String title;
    private String genre;
    private String summary;
    private String posterPath;

    // Constructor
    public Movie(int movieID, String title, String genre, String summary, String posterPath) {
        this.movieID = movieID;
        this.title = title;
        this.genre = genre;
        this.summary = summary;
        this.posterPath = posterPath;
    }

    // Getters and Setters
    public int getMovieID() {
        return movieID;
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    // toString method (optional, for debugging)
    @Override
    public String toString() {
        return "Movie{" +
                "movieID=" + movieID +
                ", title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", summary='" + summary + '\'' +
                ", posterPath='" + posterPath + '\'' +
                '}';
    }
}
