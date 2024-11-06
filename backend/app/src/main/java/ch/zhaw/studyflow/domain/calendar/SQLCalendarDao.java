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

    @Override
    public Calendar save(long userId, Calendar calendar) {
        String sql = "INSERT INTO calendars (id, user_id, name) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, calendar.getId());
            stmt.setLong(2, userId);
            stmt.setString(3, calendar.getName());
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

    @Override
    public Calendar update(long userId, Calendar calendar) {
        String sql = "UPDATE calendars SET name = ? WHERE id = ? AND user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, calendar.getName());
            stmt.setLong(2, calendar.getId());
            stmt.setLong(3, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return calendar;
    }
}