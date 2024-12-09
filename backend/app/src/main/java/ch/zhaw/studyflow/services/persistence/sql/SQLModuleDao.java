package ch.zhaw.studyflow.services.persistence.sql;

import ch.zhaw.studyflow.domain.curriculum.Module;
import ch.zhaw.studyflow.services.persistence.ModuleDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SQLModuleDao implements ModuleDao {

    private final Connection connection;

    public SQLModuleDao(Connection connection) {
        this.connection = connection;
    }

    public void create(long studentId, long semesterId, long degreeId, Module module) {
        String sql = "INSERT INTO modules (name, description, ECTS, understanding, time, complexity) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, module.getName());
            stmt.setString(2, module.getDescription());
            stmt.setLong(3, module.getECTS());
            stmt.setLong(4, module.getUnderstanding());
            stmt.setLong(5, module.getTime());
            stmt.setLong(6, module.getComplexity());
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
                    Module module = new Module();
                    module.setId(rs.getLong("id"));
                    module.setName(rs.getString("name"));
                    module.setDescription(rs.getString("description"));
                    module.setECTS(rs.getLong("ECTS"));
                    module.setUnderstanding(rs.getLong("understanding"));
                    module.setTime(rs.getLong("time"));
                    module.setComplexity(rs.getLong("complexity"));
                    return module;
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
        String sql = "UPDATE modules SET name = ?, description = ?, ECTS = ?, understanding = ?, time = ?, complexity = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, module.getName());
            stmt.setString(2, module.getDescription());
            stmt.setLong(3, module.getECTS());
            stmt.setLong(4, module.getUnderstanding());
            stmt.setLong(5, module.getTime());
            stmt.setLong(6, module.getComplexity());
            stmt.setLong(7, module.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return module;
    }

    @Override
    public List<Module> readAllByStudent(long studentId) {
        List<Module> modules = new ArrayList<>();
        String sql = "SELECT semester.* FROM modules JOIN semesters on modules.id = semester.moduleId JOIN degree ON degree.id = semester.degreeId WHERE degree.ownerId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Module module = new Module();
                    module.setId(rs.getLong("id"));
                    module.setName(rs.getString("name"));
                    module.setDescription(rs.getString("description"));
                    module.setECTS(rs.getLong("ECTS"));
                    module.setUnderstanding(rs.getLong("understanding"));
                    module.setTime(rs.getLong("time"));
                    module.setComplexity(rs.getLong("complexity"));
                    modules.add(module);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return modules;
    }

    @Override
    public List<Module> readAllBySemester(long semesterId) {
        List<Module> modules = new ArrayList<>();
        String sql = "SELECT * FROM modules WHERE semesterId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, semesterId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Module module = new Module();
                    module.setId(rs.getLong("id"));
                    module.setName(rs.getString("name"));
                    module.setDescription(rs.getString("description"));
                    module.setECTS(rs.getLong("ECTS"));
                    module.setUnderstanding(rs.getLong("understanding"));
                    module.setTime(rs.getLong("time"));
                    module.setComplexity(rs.getLong("complexity"));
                    modules.add(module);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return modules;
    }
}
