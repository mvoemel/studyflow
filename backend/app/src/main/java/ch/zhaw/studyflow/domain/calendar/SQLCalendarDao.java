package ch.zhaw.studyflow.domain.calendar;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SQL implementation of the CalendarDao interface.
 * This class provides methods to perform CRUD operations on Calendar objects
 * stored in a SQL database.
 */
public class SQLCalendarDao implements CalendarDao {
    private final Connection connection;

    /**
     * Constructs a new SQLCalendarDao with the specified database connection.
     *
     * @param connection the database connection
     */
    public SQLCalendarDao(Connection connection) {
        this.connection = connection;
    }

    /**
     * Saves a calendar to the SQL database.
     *
     * @param calendar the calendar to save
     * @return the saved calendar
     */
    @Override
    public Calendar save(Calendar calendar) {
        String sql = "INSERT INTO calendars (id, name) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, calendar.getId());
            stmt.setString(2, calendar.getName());
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    calendar.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return calendar;
    }

    /**
     * Reads all calendars for a specific user from the SQL database.
     *
     * @param userId the ID of the user
     * @return a list of calendars for the specified user
     */
    @Override
    public List<Calendar> readAll(long userId) {
        List<Calendar> calendars = new ArrayList<>();
        String sql = "SELECT * FROM calendars WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Calendar calendar = new Calendar(rs.getLong("id"), rs.getString("name"));
                    calendars.add(calendar);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return calendars;
    }

    /**
     * Reads a specific calendar for a user from the SQL database.
     *
     * @param userId the ID of the user
     * @param calendarId the ID of the calendar
     * @return the calendar with the specified ID for the specified user, or null if not found
     */
    @Override
    public Calendar read(long userId, long calendarId) {
        Calendar calendar = null;
        String sql = "SELECT * FROM calendars WHERE user_id = ? AND id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, calendarId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    calendar = new Calendar(rs.getLong("id"), rs.getString("name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return calendar;
    }

    /**
     * Deletes a specific calendar for a user from the SQL database.
     *
     * @param userId the ID of the user
     * @param calendarId the ID of the calendar to delete
     */
    @Override
    public void delete(long userId, long calendarId) {
        String sql = "DELETE FROM calendars WHERE user_id = ? AND id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, calendarId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates a calendar in the SQL database.
     *
     * @param calendar the calendar to update
     * @return the updated calendar
     */
    @Override
    public Calendar update(Calendar calendar) {
        String sql = "UPDATE calendars SET name = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, calendar.getName());
            stmt.setLong(2, calendar.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return calendar;
    }
}