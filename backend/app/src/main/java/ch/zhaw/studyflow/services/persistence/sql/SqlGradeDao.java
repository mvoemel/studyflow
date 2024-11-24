package ch.zhaw.studyflow.domain.grade;

import ch.zhaw.studyflow.services.persistence.GradeDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Field;

/**
 * SQL implementation of GradeDao.
 */
public class SqlGradeDao implements GradeDao {
    private final Connection connection;

    public SqlGradeDao(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Grade grade) {
        String sql = "INSERT INTO grades (module_id, student_id, value) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setLong(1, getFieldValue(grade, "belongsTo"));
            stmt.setLong(2, getFieldValue(grade, "studentId"));
            stmt.setDouble(3, grade.getMark());
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
        String sql = "SELECT * FROM grades WHERE module_id = ?";
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
    public List<Grade> readByStudent(long studentId) {
        String sql = "SELECT * FROM grades WHERE student_id = ?";
        List<Grade> grades = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, studentId);
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
    public void update(Grade grade) {
        String sql = "UPDATE grades SET module_id = ?, student_id = ?, value = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, getFieldValue(grade, "belongsTo"));
            stmt.setLong(2, getFieldValue(grade, "studentId"));
            stmt.setDouble(3, grade.getMark());
            stmt.setLong(4, grade.getId());
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
        grade.setBelongsTo(rs.getLong("module_id"));
        grade.setMark((long) rs.getDouble("value")); // Cast double to long
        setFieldValue(grade, "studentId", rs.getLong("student_id"));
        return grade;
    }

    private long getFieldValue(Grade grade, String fieldName) {
        try {
            Field field = Grade.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.getLong(grade);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void setFieldValue(Grade grade, String fieldName, long value) {
        try {
            Field field = Grade.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.setLong(grade, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
