package ch.zhaw.studyflow.services.persistence.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ch.zhaw.studyflow.domain.calendar.Appointment;
import ch.zhaw.studyflow.services.persistence.AppointmentDao;

/**
 * SQL implementation of the AppointmentDao interface.
 */
public class SQLAppointmentDao implements AppointmentDao {
    private final Connection connection;

    public SQLAppointmentDao(Connection connection) {
        this.connection = connection;
    }

    public void create(Appointment appointment) {
        String sql = "INSERT INTO appointments (start_time, end_time, calendar_id) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setTimestamp(1, java.sql.Timestamp.valueOf(appointment.getStartTime()));
            stmt.setTimestamp(2, java.sql.Timestamp.valueOf(appointment.getEndTime()));
            stmt.setLong(3, appointment.getCalendarId());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    appointment.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Appointment read(long calendarId, long appointmentId) {
        String sql = "SELECT * FROM appointments WHERE calendar_id = ? AND id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, calendarId);
            stmt.setLong(2, appointmentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Appointment(
                            rs.getLong("id"),
                            rs.getTimestamp("start_time").toLocalDateTime(),
                            rs.getTimestamp("end_time").toLocalDateTime(),
                            rs.getLong("calendar_id"),
                            rs.getString("title"),
                            rs.getString("description")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Appointment> readAllBy(long calendarId) {
        String sql = "SELECT * FROM appointments WHERE calendar_id = ?";
        List<Appointment> appointments = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, calendarId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    appointments.add(new Appointment(
                            rs.getLong("id"),
                            rs.getTimestamp("start_time").toLocalDateTime(),
                            rs.getTimestamp("end_time").toLocalDateTime(),
                            rs.getLong("calendar_id"),
                            rs.getString("title"),
                            rs.getString("description")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    @Override
    public void delete(long appointmentId) {
        String sql = "DELETE FROM appointments WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, appointmentId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Appointment appointment) {
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
    }

    @Override
    public List<Appointment> readAllBy(long calendarId, LocalDate start, LocalDate end) {
        String sql = "SELECT * FROM appointments WHERE calendar_id = ? AND start_time >= ? AND end_time <= ?";
        List<Appointment> appointments = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, calendarId);
            stmt.setTimestamp(2, java.sql.Timestamp.valueOf(start.atStartOfDay()));
            stmt.setTimestamp(3, java.sql.Timestamp.valueOf(end.plusDays(1).atStartOfDay()));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    appointments.add(new Appointment(
                            rs.getLong("id"),
                            rs.getTimestamp("start_time").toLocalDateTime(),
                            rs.getTimestamp("end_time").toLocalDateTime(),
                            rs.getLong("calendar_id"),
                            rs.getString("title"),
                            rs.getString("description")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }
}