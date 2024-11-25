package ch.zhaw.studyflow.services.persistence.sql;

import ch.zhaw.studyflow.domain.curriculum.Module;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLModuleDao {

    private final Connection connection;

    public SQLModuleDao(Connection connection) {
        this.connection = connection;
    }

    public void create(Module module) {
        String sql = "INSERT INTO modules (name, description, ECTS, understandingValue, timeValue, importanceValue) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, module.getName());
            stmt.setString(2, module.getDescription());
            stmt.setLong(3, module.getECTS());
            stmt.setLong(4, module.getUnderstandingValue());
            stmt.setLong(5, module.getTimeValue());
            stmt.setLong(6, module.getImportanceValue());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    module.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Module read(long moduleId) {
        String sql = "SELECT * FROM modules WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, moduleId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Module(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getLong("ECTS"),
                            rs.getLong("understandingValue"),
                            rs.getLong("timeValue"),
                            rs.getLong("importanceValue")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void delete(long moduleId) {
        String sql = "DELETE FROM modules WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, moduleId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Module update(Module module) {
        String sql = "UPDATE modules SET name = ?, description = ?, ECTS = ?, understandingValue = ?, timeValue = ?, importanceValue = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, module.getName());
            stmt.setString(2, module.getDescription());
            stmt.setLong(3, module.getECTS());
            stmt.setLong(4, module.getUnderstandingValue());
            stmt.setLong(5, module.getTimeValue());
            stmt.setLong(6, module.getImportanceValue());
            stmt.setLong(7, module.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return module;
    }

    public Module getModule(long moduleId) {
        String sql = "SELECT * FROM modules WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, moduleId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Module(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getLong("ECTS"),
                            rs.getLong("understandingValue"),
                            rs.getLong("timeValue"),
                            rs.getLong("importanceValue")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Module> getModules(long semesterId) {
        List<Module> modules = new ArrayList<>();
        String sql = "SELECT * FROM modules WHERE semesterId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, semesterId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    modules.add(new Module(
                            rs.getLong("id"),
                            rs.getString("name"),
                            rs.getString("description"),
                            rs.getLong("ECTS"),
                            rs.getLong("understandingValue"),
                            rs.getLong("timeValue"),
                            rs.getLong("importanceValue")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return modules;
    }

}
