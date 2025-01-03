package CinemaCenter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO {

    /**
     * Searches movies by genre, partial name, or full name.
     *
     * @param genre       The genre to filter by (can be null or empty).
     * @param partialName The partial name to search for (can be null or empty).
     * @param fullName    The full name to search for (can be null or empty).
     * @return A list of movies matching the search criteria.
     */
    public static List<Movie> searchMovies(String genre, String partialName, String fullName) {
        List<Movie> movies = new ArrayList<>();

        String query = "SELECT * FROM Movies WHERE 1=1";

        // Dynamically build the query based on parameters
        if (genre != null && !genre.isEmpty()) {
            query += " AND Genre = ?";
        }
        if (partialName != null && !partialName.isEmpty()) {
            query += " AND Title LIKE ?";
        }
        if (fullName != null && !fullName.isEmpty()) {
            query += " AND Title = ?";
        }

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            int paramIndex = 1;

            if (genre != null && !genre.isEmpty()) {
                stmt.setString(paramIndex++, genre);
            }
            if (partialName != null && !partialName.isEmpty()) {
                stmt.setString(paramIndex++, "%" + partialName + "%");
            }
            if (fullName != null && !fullName.isEmpty()) {
                stmt.setString(paramIndex++, fullName);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Movie movie = new Movie(
                        rs.getInt("MovieID"),
                        rs.getString("Title"),
                        rs.getString("Genre"),
                        rs.getString("Summary"),
                        rs.getString("PosterPath")
                );
                movies.add(movie);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return movies;
    }
}
