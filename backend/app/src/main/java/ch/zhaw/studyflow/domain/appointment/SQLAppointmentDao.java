package ch.zhaw.studyflow.domain.appointment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SQL implementation of the AppointmentDao interface.
 * This class provides methods to perform CRUD operations on Appointment objects
 * stored in a SQL database.
 */
public class SQLAppointmentDao implements AppointmentDao {
    private final Connection connection;

    /**
     * Constructs a new SQLAppointmentDao with the specified database connection.
     *
     * @param connection the database connection
     */
    public SQLAppointmentDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Appointment save(long userId, Appointment appointment) {
        String sql = "INSERT INTO appointments (id, user_id, start_time, end_time) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, appointment.getId());
            stmt.setLong(2, userId);
            stmt.setTimestamp(3, Timestamp.valueOf(appointment.getStartTime()));
            stmt.setTimestamp(4, Timestamp.valueOf(appointment.getEndTime()));
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    appointment.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointment;
    }

    @Override
    public List<Appointment> readAll(long userId) {
        List<Appointment> appointments = new ArrayList<>();
        String sql = "SELECT * FROM appointments WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Appointment appointment = new Appointment(
                            rs.getLong("id"),
                            rs.getTimestamp("start_time").toLocalDateTime(),
                            rs.getTimestamp("end_time").toLocalDateTime()
                    );
                    appointments.add(appointment);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointments;
    }

    @Override
    public Appointment read(long userId, long appointmentId) {
        Appointment appointment = null;
        String sql = "SELECT * FROM appointments WHERE user_id = ? AND id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, appointmentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    appointment = new Appointment(
                            rs.getLong("id"),
                            rs.getTimestamp("start_time").toLocalDateTime(),
                            rs.getTimestamp("end_time").toLocalDateTime()
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointment;
    }

    @Override
    public Appointment update(long userId, Appointment appointment) {
        String sql = "UPDATE appointments SET start_time = ?, end_time = ? WHERE id = ? AND user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(appointment.getStartTime()));
            stmt.setTimestamp(2, Timestamp.valueOf(appointment.getEndTime()));
            stmt.setLong(3, appointment.getId());
            stmt.setLong(4, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return appointment;
    }

    @Override
    public void delete(long userId, long appointmentId) {
        String sql = "DELETE FROM appointments WHERE user_id = ? AND id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            stmt.setLong(2, appointmentId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}