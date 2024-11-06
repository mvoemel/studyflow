package ch.zhaw.studyflow.domain.calendar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * SQL implementation of the CalendarDao interface.
 * This class provides methods to perform CRUD operations on Calendar objects
 * stored in a SQL database.
 */
public class SQLCalendarDao implements CalendarDao {
    private final Connection connection;

    public SQLCalendarDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Calendar create(Calendar calendar) {
        String sql = "INSERT INTO calendars (id, name, owner_id) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, calendar.getId());
            stmt.setString(2, calendar.getName());
            stmt.setLong(3, calendar.getOwnerId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return calendar;
    }

    @Override
    public Calendar read(long userId, long calendarId) {
        String sql = "SELECT * FROM calendars WHERE owner_id = ? AND id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, calendarId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Calendar(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getLong("owner_id")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void delete(long userId, long calendarId) {
        String sql = "DELETE FROM calendars WHERE owner_id = ? AND id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, calendarId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Calendar update(Calendar calendar) {
        String sql = "UPDATE calendars SET name = ? WHERE id = ? AND owner_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, calendar.getName());
            stmt.setLong(2, calendar.getId());
            stmt.setLong(3, calendar.getOwnerId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return calendar;
    }

    @Override
    public long getCalendarId(Calendar calendar) {
        return calendar.getId();
    }

    @Override
    public void setCalendarId(Calendar calendar, long calendarId) {
        calendar.setId(calendarId);
    }
}