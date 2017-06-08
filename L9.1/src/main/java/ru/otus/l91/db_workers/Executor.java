package ru.otus.l91.db_workers;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Execute create, query, update and delete operations over DB.
 */
public class Executor {
    private final Connection connection;

    public Executor(Connection connection) {
        this.connection = connection;
    }

    /**
     * Persist an entity in DB storage.
     * @param entity - entity to store
     * @return index of the entity in the DB
     * @throws Exception if a problem occurred with persisting the entity
     */
    public <T> long create(T entity) throws Exception {
        try (PreparedStatement stm = connection.prepareStatement(formInsertStatement(entity), Statement.RETURN_GENERATED_KEYS)) {
            int affectedRows = stm.executeUpdate();
            if(affectedRows == 0) throw new SQLException("Creating entity failed.");
            try (ResultSet generatedKeys = stm.getGeneratedKeys()) {
                if(generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                }
                throw new SQLException("Creating entity failed, no id was generated.");
            }
        }
    }

    private <T> String formInsertStatement(T entity) throws Exception {
        String tableName;
        List<String> attributes = new ArrayList<>();
        List<String> values = new ArrayList<>();

        Class<?> clazz = entity.getClass();

        Table tableAnno = clazz.getAnnotation(Table.class);
        tableName = (tableAnno != null && !tableAnno.name().equals("")) ? tableAnno.name() : clazz.getSimpleName();

        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (!field.isAnnotationPresent(Transient.class)) {
                Column columnAnno = field.getAnnotation(Column.class);
                String fieldName = (columnAnno != null && !columnAnno.name().equals("")) ? columnAnno.name() : field.getName();
                attributes.add(fieldName);
                if (field.getType().isPrimitive()) {
                    values.add(field.get(entity).toString());
                } else if (field.getType() == String.class) {
                    values.add("'" + field.get(entity).toString() + "'");
                }
            }
        }

        return String.format("insert into %s (%s) values (%s)",
                    tableName,
                    String.join(",", attributes),
                    String.join(",", values));
    }

    public <T> T find(Class<T> clazz, long id, Function<ResultSet, T> handler) throws Exception {
        Table tableAnno = clazz.getAnnotation(Table.class);
        String tableName = (tableAnno != null && !tableAnno.name().equals("")) ? tableAnno.name() : clazz.getSimpleName();
        String query = String.format("select * from %s where id=%d;", tableName, id);

        return execute(query, handler);
    }

    public int update(String update) throws SQLException {
        try (Statement st = connection.createStatement()) {
            st.execute(update);
            return st.getUpdateCount();
        }
    }

    public <T> T execute(String query, Function<ResultSet, T> handler) throws SQLException {
        try (Statement st = connection.createStatement()) {
            st.execute(query);
            return handler.apply(st.getResultSet());
        }
    }
}
