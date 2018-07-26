package com.codefutures.tpcc.load;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

/**
 * Copyright (C) 2011 CodeFutures Corporation. All rights reserved.
 */
public class JdbcStatementLoader implements RecordLoader {

    Connection conn;

    Statement stmt;

    String tableName;

    String columnName[];

    boolean ignore;

    int maxBatchSize;

    int currentBatchSize;

    StringBuilder b = new StringBuilder();

    public JdbcStatementLoader(Connection conn, String tableName, String columnName[], boolean ignore, int maxBatchSize) {
        this.conn = conn;
        this.tableName = tableName;
        this.columnName = columnName;
        this.ignore = ignore;
        this.maxBatchSize = maxBatchSize;
    }

    public void load(Record r) throws Exception {
        final Object[] field = r.getField();
        Object fieldValue = field[0];

        b.append("INSERT INTO default VALUES (");
        if (tableName.equals("history")) {
            b.append("uuid()");
        }
        else {
            b.append("'").append(fieldValue).append("'");
        }
        b.append(", {'type': '").append(tableName).append("'");
        /*if (ignore) {
            b.append("IGNORE ");
        }
        b.append("INTO test_").append(tableName).append(" (");*/
        for (int i = 0; i < columnName.length; i++) {
            fieldValue = field[i+1];

            b.append(", '");
            b.append(columnName[i].trim());
            b.append("': ");

            if (fieldValue instanceof Date) {
//                b.append("'").append(dateTimeFormat.format((Date)field[i])).append("'");
                b.append("'").append((Date) fieldValue).append("'");
            } else if (fieldValue instanceof String) {
                b.append("'").append(fieldValue).append("'");
            } else {
                b.append(fieldValue);
            }
        }
        b.append("})");

        executeBulkInsert();
    }

    private void executeBulkInsert() throws SQLException {
        if (stmt == null) {
            stmt = conn.createStatement();
        }
        final String sql = b.toString();
        b.setLength(0);
        try {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException("Error loading into table '" + tableName + "' with SQL: " + sql, e);
        }
        currentBatchSize = 0;
    }

    public void commit() throws Exception {
        if (!conn.getAutoCommit()) {
            conn.commit();
        }
    }

    public void close() throws Exception {
        if (currentBatchSize > 0) {
            executeBulkInsert();
        }
        stmt.close();
        if (!conn.getAutoCommit()) {
            conn.commit();
        }
    }
}
