package CinemaCenter;

/**
 * Represents a movie in the cinema system.
 * Contains basic information about a movie including its ID, title, genre,
 * summary and poster image path.
 */
public class Movie {

    private int movieID;
    private String title;
    private String genre;
    private String summary;
    private String posterPath;

    /**
     * Creates a new Movie instance with the specified details.
     *
     * @param movieID    The unique identifier for the movie
     * @param title      The title of the movie
     * @param genre      The genre of the movie
     * @param summary    A brief summary or description of the movie
     * @param posterPath The file path to the movie's poster image
     */
    public Movie(int movieID, String title, String genre, String summary, String posterPath) {
        this.movieID = movieID;
        this.title = title;
        this.genre = genre;
        this.summary = summary;
        this.posterPath = posterPath;
    }

    /**
     * @return The movie's unique identifier
     */
    public int getMovieID() {
        return movieID;
    }

    /**
     * @param movieID The movie's unique identifier to set
     */
    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    /**
     * @return The title of the movie
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title The movie title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return The genre of the movie
     */
    public String getGenre() {
        return genre;
    }

    /**
     * @param genre The movie genre to set
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * @return The summary of the movie
     */
    public String getSummary() {
        return summary;
    }

    /**
     * @param summary The movie summary to set
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * @return The file path to the movie's poster image
     */
    public String getPosterPath() {
        return posterPath;
    }

    /**
     * @param posterPath The file path to set for the movie's poster image
     */
    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    /**
     * Returns a string representation of the Movie object.
     *
     * @return A string containing all movie details
     */
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
