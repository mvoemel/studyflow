package ch.zhaw.studyflow.domain.appointment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * SQL implementation of the AppointmentDao interface.
 */
public class SQLAppointmentDao implements AppointmentDao {
    private final Connection connection;

    public SQLAppointmentDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Appointment create(Appointment appointment) {
        String sql = "INSERT INTO appointments (id, start_time, end_time, calendar_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, appointment.getId());
            stmt.setTimestamp(2, java.sql.Timestamp.valueOf(appointment.getStartTime()));
            stmt.setTimestamp(3, java.sql.Timestamp.valueOf(appointment.getEndTime()));
            stmt.setLong(4, appointment.getCalendarId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointment;
    }

    @Override
    public Appointment read(long calendarId, long id) {
        String sql = "SELECT * FROM appointments WHERE calendar_id = ? AND id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, calendarId);
            stmt.setLong(2, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Appointment(
                            rs.getLong("id"),
                            rs.getTimestamp("start_time").toLocalDateTime(),
                            rs.getTimestamp("end_time").toLocalDateTime(),
                            rs.getLong("calendar_id")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Appointment> readAllBy(long calendarId, Date from, Date to) {
        String sql = "SELECT * FROM appointments WHERE calendar_id = ? AND start_time >= ? AND end_time <= ?";
        List<Appointment> appointments = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, calendarId);
            stmt.setTimestamp(2, new java.sql.Timestamp(from.getTime()));
            stmt.setTimestamp(3, new java.sql.Timestamp(to.getTime()));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    appointments.add(new Appointment(
                            rs.getLong("id"),
                            rs.getTimestamp("start_time").toLocalDateTime(),
                            rs.getTimestamp("end_time").toLocalDateTime(),
                            rs.getLong("calendar_id")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    @Override
    public void delete(long id) {
        String sql = "DELETE FROM appointments WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Appointment update(Appointment appointment) {
        String sql = "UPDATE appointments SET start_time = ?, end_time = ?, calendar_id = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, java.sql.Timestamp.valueOf(appointment.getStartTime()));
            stmt.setTimestamp(2, java.sql.Timestamp.valueOf(appointment.getEndTime()));
            stmt.setLong(3, appointment.getCalendarId());
            stmt.setLong(4, appointment.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointment;
    }
}