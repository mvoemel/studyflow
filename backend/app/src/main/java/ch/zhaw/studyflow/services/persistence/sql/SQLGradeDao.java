package ch.zhaw.studyflow.services.persistence.sql;

import ch.zhaw.studyflow.domain.grade.Grade;
import ch.zhaw.studyflow.services.persistence.GradeDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SQL implementation of GradeDao.
 */
public class SQLGradeDao implements GradeDao {
    private final Connection connection;

    public SQLGradeDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Grade grade) {
        String sql = "INSERT INTO grades (name, percentage, value, belongs_to_module) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, grade.getName());
            stmt.setDouble(2, grade.getPercentage());
            stmt.setDouble(3, grade.getValue());
            stmt.setLong(4, grade.getBelongsTo());
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    grade.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Grade read(long gradeId) {
        String sql = "SELECT * FROM grades WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, gradeId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToGrade(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<Grade> readByModule(long moduleId) {
        String sql = "SELECT * FROM grades WHERE belongs_to_module = ?";
        List<Grade> grades = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, moduleId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    grades.add(mapRowToGrade(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return grades;
    }

    @Override
    public List<Grade> readByDegree(long degreeId) {
        String sql = "SELECT * FROM grades WHERE degree_id = ?";
        List<Grade> grades = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, degreeId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    grades.add(mapRowToGrade(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return grades;
    }

    @Override
    public void updateByDegree(long degreeId, List<Grade> grades) {
        String sql = "UPDATE grades SET name = ?, percentage = ?, value = ?, belongs_to_module = ? WHERE degree_id = ? AND id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (Grade grade : grades) {
                stmt.setString(1, grade.getName());
                stmt.setDouble(2, grade.getPercentage());
                stmt.setDouble(3, grade.getValue());
                stmt.setLong(4, grade.getBelongsTo());
                stmt.setLong(5, degreeId);
                stmt.setLong(6, grade.getId());
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(Grade grade) {
        String sql = "UPDATE grades SET name = ?, percentage = ?, value = ?, belongs_to_module = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, grade.getName());
            stmt.setDouble(2, grade.getPercentage());
            stmt.setDouble(3, grade.getValue());
            stmt.setLong(4, grade.getBelongsTo());
            stmt.setLong(5, grade.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(long gradeId) {
        String sql = "DELETE FROM grades WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, gradeId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Grade mapRowToGrade(ResultSet rs) throws SQLException {
        Grade grade = new Grade();
        grade.setId(rs.getLong("id"));
        grade.setName(rs.getString("name"));
        grade.setPercentage(rs.getDouble("percentage"));
        grade.setValue(rs.getDouble("value"));
        grade.setBelongsTo(rs.getLong("belongs_to_module"));
        return grade;
    }
}