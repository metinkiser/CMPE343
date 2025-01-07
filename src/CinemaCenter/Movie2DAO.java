 package CinemaCenter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Movie2DAO {

    /** ✅ Filmleri veritabanında arayan metod */
    public static List<Movie> searchMovies(String genre, String partialName, String fullName) {
        List<Movie> movies = new ArrayList<>();
        String query = "SELECT * FROM Movies WHERE 1=1";

        // Dinamik olarak SQL sorgusuna koşulları ekliyoruz
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
                movies.add(new Movie(
                        rs.getInt("MovieID"),
                        rs.getString("Title"),
                        rs.getString("Genre"),
                        rs.getString("Summary"),
                        rs.getString("PosterPath")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return movies;
    }

    /** ✅ Yeni film ekleme */
    public static boolean addMovie(String title, String genre, String summary, String posterPath) {
        // Önce filmin olup olmadığını kontrol edelim
        if (movieExists(title)) {
            System.out.println("Bu film zaten mevcut!");
            return false;
        }

        String query = "INSERT INTO Movies (Title, Genre, Summary, PosterPath) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, title);
            stmt.setString(2, genre);
            stmt.setString(3, summary);
            stmt.setString(4, posterPath);
            
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0; // Eğer satır eklendiyse true döner
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /** ✅ Film güncelleme */
    public static boolean updateMovie(int movieId, String newTitle, String newGenre, String newSummary, String newPosterPath) {
        String query = "UPDATE Movies SET Title = COALESCE(?, Title), Genre = COALESCE(?, Genre), Summary = COALESCE(?, Summary), PosterPath = COALESCE(?, PosterPath) WHERE MovieID = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, newTitle);
            stmt.setString(2, newGenre);
            stmt.setString(3, newSummary);
            stmt.setString(4, newPosterPath);
            stmt.setInt(5, movieId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /** ✅ Film var mı kontrolü */
    public static boolean movieExists(String title) {
        String query = "SELECT 1 FROM Movies WHERE Title = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, title);
            
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // Eğer sonuç varsa film zaten var demektir
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /** ✅ Film ID ile getir */
    public static Movie getMovieById(int movieId) {
        String query = "SELECT * FROM Movies WHERE MovieID = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, movieId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Movie(
                    rs.getInt("MovieID"),
                    rs.getString("Title"),
                    rs.getString("Genre"),
                    rs.getString("Summary"),
                    rs.getString("PosterPath")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // Film bulunamazsa null döndür
    }
}
