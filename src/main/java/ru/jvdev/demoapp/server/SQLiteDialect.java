package ru.jvdev.demoapp.server;

import java.sql.Types;

import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.dialect.function.VarArgsSQLFunction;
import org.hibernate.type.StringType;

public class SQLiteDialect extends Dialect {

    private static final String INTEGER = "integer";
    private static final String BLOB = "blob";
    private static final String SUBSTR = "substr";

    public SQLiteDialect() {
        registerColumnType(Types.BIT, INTEGER);
        registerColumnType(Types.TINYINT, "tinyint");
        registerColumnType(Types.SMALLINT, "smallint");
        registerColumnType(Types.INTEGER, INTEGER);
        registerColumnType(Types.BIGINT, "bigint");
        registerColumnType(Types.FLOAT, "float");
        registerColumnType(Types.REAL, "real");
        registerColumnType(Types.DOUBLE, "double");
        registerColumnType(Types.NUMERIC, "numeric");
        registerColumnType(Types.DECIMAL, "decimal");
        registerColumnType(Types.CHAR, "char");
        registerColumnType(Types.VARCHAR, "varchar");
        registerColumnType(Types.LONGVARCHAR, "longvarchar");
        registerColumnType(Types.DATE, "date");
        registerColumnType(Types.TIME, "time");
        registerColumnType(Types.TIMESTAMP, "timestamp");
        registerColumnType(Types.BINARY, BLOB);
        registerColumnType(Types.VARBINARY, BLOB);
        registerColumnType(Types.LONGVARBINARY, BLOB);
        // registerColumnType(Types.NULL, "null");
        registerColumnType(Types.BLOB, BLOB);
        registerColumnType(Types.CLOB, "clob");
        registerColumnType(Types.BOOLEAN, INTEGER);

        registerFunction("concat", new VarArgsSQLFunction(StringType.INSTANCE, "", "||", ""));
        registerFunction("mod", new SQLFunctionTemplate(StringType.INSTANCE, "?1 % ?2"));
        registerFunction(SUBSTR, new StandardSQLFunction(SUBSTR, StringType.INSTANCE));
        registerFunction("substring", new StandardSQLFunction(SUBSTR, StringType.INSTANCE));
    }

    @SuppressWarnings("deprecation")
    public boolean supportsIdentityColumns() {
        return true;
    }

    /*
    public boolean supportsInsertSelectIdentity() {
    return true; // As specify in NHibernate dialect
    }
    */

    @SuppressWarnings("deprecation")
    public boolean hasDataTypeInIdentityColumn() {
        return false; // As specify in NHibernate dialect
    }

    /*
    public String appendIdentitySelectToInsert(String insertString) {
    return new StringBuffer(insertString.length()+30). // As specify in NHibernate dialect
      append(insertString).
      append("; ").append(getIdentitySelectString()).
      toString();
    }
    */

    @SuppressWarnings("deprecation")
    public String getIdentityColumnString() {
        // return "integer primary key autoincrement";
        return INTEGER;
    }

    @SuppressWarnings("deprecation")
    public String getIdentitySelectString() {
        return "select last_insert_rowid()";
    }

    @SuppressWarnings("deprecation")
    public boolean supportsLimit() {
        return true;
    }

    // CHECKSTYLE:OFF MagicNumberCheck
    @SuppressWarnings("deprecation")
    protected String getLimitString(String query, boolean hasOffset) {
        return new StringBuffer(query.length() + 20).
                append(query).
                append(hasOffset ? " limit ? offset ?" : " limit ?").
                toString();
    }
    // CHECKSTYLE:ON MagicNumberCheck

    public boolean supportsTemporaryTables() {
        return true;
    }

    public String getCreateTemporaryTableString() {
        return "create temporary table if not exists";
    }

    public boolean dropTemporaryTableAfterUse() {
        return false;
    }

    public boolean supportsCurrentTimestampSelection() {
        return true;
    }

    public boolean isCurrentTimestampSelectStringCallable() {
        return false;
    }

    public String getCurrentTimestampSelectString() {
        return "select current_timestamp";
    }

    public boolean supportsUnionAll() {
        return true;
    }

    public boolean hasAlterTable() {
        return false; // As specify in NHibernate dialect
    }

    public boolean dropConstraints() {
        return false;
    }

    public String getAddColumnString() {
        return "add column";
    }

    public String getForUpdateString() {
        return "";
    }

    public boolean supportsOuterJoinForUpdate() {
        return false;
    }

    public String getDropForeignKeyString() {
        throw new UnsupportedOperationException("No drop foreign key syntax supported by SQLiteDialect");
    }

    public String getAddForeignKeyConstraintString(String constraintName,
                                                   String[] foreignKey, String referencedTable, String[] primaryKey,
                                                   boolean referencesPrimaryKey) {
        throw new UnsupportedOperationException("No add foreign key syntax supported by SQLiteDialect");
    }

    public String getAddPrimaryKeyConstraintString(String constraintName) {
        throw new UnsupportedOperationException("No add primary key syntax supported by SQLiteDialect");
    }

    public boolean supportsIfExistsBeforeTableName() {
        return true;
    }

    public boolean supportsCascadeDelete() {
        return false;
    }
}