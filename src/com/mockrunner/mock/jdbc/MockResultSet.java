package com.mockrunner.mock.jdbc;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mockobjects.sql.MockResultSetMetaData;

/**
 * Mock implementation of <code>ResultSet</code>.
 */
public class MockResultSet implements ResultSet
{
    private Statement statement;
    private Map columnMap;
    private List columnNameList;
    private int cursor;
    private boolean wasNull;
    private String cursorName;
    private int fetchSize = 0;
    private int fetchDirection = ResultSet.FETCH_FORWARD;
    private int resultSetType = ResultSet.TYPE_FORWARD_ONLY;
    private int resultSetConcurrency = ResultSet.CONCUR_READ_ONLY;
    
    public MockResultSet(Statement statement)
    {
        this(statement, "");
    }
    
    public MockResultSet(Statement statement, String cursorName)
    {
        this.statement = statement;
        columnMap = new HashMap();
        columnNameList = new ArrayList();
        cursor = -1;
        wasNull = false;
        try
        {
            fetchDirection = statement.getFetchDirection();
            resultSetType = statement.getResultSetType();
            resultSetConcurrency = statement.getResultSetConcurrency();
            fetchSize = statement.getFetchSize();
        }
        catch(SQLException exc)
        {
        
        }
        this.cursorName = cursorName;
    }
    
    public void addRow(List values)
    {
        int rowCount = getRowCount();
        for(int ii = 0; ii < columnNameList.size(); ii++)
        {   
            Object nextValue = null;
            if(ii < values.size())
            {
                nextValue = values.get(ii);
            }
            List nextColumnList = (List)columnNameList.get(ii);
            nextColumnList.add(rowCount + 1, nextValue);
        }
    }
    
    public void addColumn(String columnName, Object value)
    {
        List columnValues = new ArrayList();
        columnValues.add(value);
        addColumn(columnName, columnValues);
    }
    
    public void addColumn(String columnName, List values)
    {
        List column = new ArrayList(values);
        columnMap.put(columnName, column);
        columnNameList.add(columnName);
        adjustColumns();
    }
    
    public int getRowCount()
    {
        if(columnMap.size() == 0) return 0;
        List column = (List)columnMap.keySet().iterator().next();
        return column.size();
    }
    
    public void close() throws SQLException
    {
        
    }

    public boolean wasNull() throws SQLException
    {
        return wasNull;
    }
    
    public Object getObject(int columnIndex) throws SQLException
    {
        checkColumnBounds(columnIndex);
        checkRowBounds();
        Object value = null;
        String columnName = (String)columnNameList.get(columnIndex - 1);
        return getObject(columnName);
    }
    
    public Object getObject(String colName, Map map) throws SQLException
    {
        return getObject(colName);
    }

    public Object getObject(String columnName) throws SQLException
    {
        checkColumnName(columnName);
        checkRowBounds();
        List column = (List)columnMap.get(columnName);
        Object value = column.get(cursor);
        wasNull = (null == value);
        return value;
    }

    public String getString(int columnIndex) throws SQLException
    {
        Object value = getObject(columnIndex);
        if(null != value) return value.toString();
        return null;
    }
    
    public String getString(String columnName) throws SQLException
    {
        Object value = getObject(columnName);
        if(null != value) return value.toString();
        return null;
    }


    public boolean getBoolean(int columnIndex) throws SQLException
    {
        Object value = getObject(columnIndex);
        if(null != value)
        {
            if(value instanceof Boolean) return ((Boolean)value).booleanValue();
            return new Boolean(value.toString()).booleanValue();
        }
        return false;
    }
    
    public boolean getBoolean(String columnName) throws SQLException
    {
        Object value = getObject(columnName);
        if(null != value)
        {
            if(value instanceof Boolean) return ((Boolean)value).booleanValue();
            return new Boolean(value.toString()).booleanValue();
        }
        return false;
    }

    public byte getByte(int columnIndex) throws SQLException
    {
        Object value = getObject(columnIndex);
        if(null != value)
        {
            if(value instanceof Number) return ((Number)value).byteValue();
            return new Byte(value.toString()).byteValue();
        }
        return 0;
    }
    
    public byte getByte(String columnName) throws SQLException
    {
        Object value = getObject(columnName);
        if(null != value)
        {
            if(value instanceof Number) return ((Number)value).byteValue();
            return new Byte(value.toString()).byteValue();
        }
        return 0;
    }

    public short getShort(int columnIndex) throws SQLException
    {
        Object value = getObject(columnIndex);
        if(null != value)
        {
            if(value instanceof Number) return ((Number)value).shortValue();
            return new Short(value.toString()).shortValue();
        }
        return 0;
    }
    
    public short getShort(String columnName) throws SQLException
    {
        Object value = getObject(columnName);
        if(null != value)
        {
            if(value instanceof Number) return ((Number)value).shortValue();
            return new Short(value.toString()).shortValue();
        }
        return 0;
    }

    public int getInt(int columnIndex) throws SQLException
    {
        Object value = getObject(columnIndex);
        if(null != value)
        {
            if(value instanceof Number) return ((Number)value).intValue();
            return new Integer(value.toString()).intValue();
        }
        return 0;
    }
    
    public int getInt(String columnName) throws SQLException
    {
        Object value = getObject(columnName);
        if(null != value)
        {
            if(value instanceof Number) return ((Number)value).intValue();
            return new Integer(value.toString()).intValue();
        }
        return 0;
    }

    public long getLong(int columnIndex) throws SQLException
    {
        Object value = getObject(columnIndex);
        if(null != value)
        {
            if(value instanceof Number) return ((Number)value).longValue();
            return new Long(value.toString()).longValue();
        }
        return 0;
    }
    
    public long getLong(String columnName) throws SQLException
    {
        Object value = getObject(columnName);
        if(null != value)
        {
            if(value instanceof Number) return ((Number)value).longValue();
            return new Long(value.toString()).longValue();
        }
        return 0;
    }

    public float getFloat(int columnIndex) throws SQLException
    {
        Object value = getObject(columnIndex);
        if(null != value)
        {
            if(value instanceof Number) return ((Number)value).floatValue();
            return new Float(value.toString()).floatValue();
        }
        return 0;
    }
    
    public float getFloat(String columnName) throws SQLException
    {
        Object value = getObject(columnName);
        if(null != value)
        {
            if(value instanceof Number) return ((Number)value).floatValue();
            return new Float(value.toString()).floatValue();
        }
        return 0;
    }
    
    public double getDouble(int columnIndex) throws SQLException
    {
        Object value = getObject(columnIndex);
        if(null != value)
        {
            if(value instanceof Number) return ((Number)value).doubleValue();
            return new Double(value.toString()).doubleValue();
        }
        return 0;
    }
    
    public double getDouble(String columnName) throws SQLException
    {
        Object value = getObject(columnName);
        if(null != value)
        {
            if(value instanceof Number) return ((Number)value).doubleValue();
            return new Double(value.toString()).doubleValue();
        }
        return 0;
    }

    public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException
    {
        BigDecimal value = getBigDecimal(columnIndex);
        if(null != value)
        {
            value.setScale(scale);
        }
        return null;
    }
    
    public BigDecimal getBigDecimal(String columnName, int scale) throws SQLException
    {
        BigDecimal value = getBigDecimal(columnName);
        if(null != value)
        {
            value.setScale(scale);
        }
        return null;
    }
    
    public BigDecimal getBigDecimal(int columnIndex) throws SQLException
    {
        Object value = getObject(columnIndex);
        if(null != value)
        {
            if(value instanceof Number) return new BigDecimal(((Number)value).doubleValue());
            return new BigDecimal(value.toString());
        }
        return null;
    }

    public BigDecimal getBigDecimal(String columnName) throws SQLException
    {
        Object value = getObject(columnName);
        if(null != value)
        {
            if(value instanceof Number) return new BigDecimal(((Number)value).doubleValue());
            return new BigDecimal(value.toString());
        }
        return null;
    }

    public byte[] getBytes(int columnIndex) throws SQLException
    {
        Object value = getObject(columnIndex);
        if(null != value)
        {
            if(value instanceof byte[]) return (byte[])value;
            return value.toString().getBytes();
        }
        return null;
    }
    
    public byte[] getBytes(String columnName) throws SQLException
    {
        Object value = getObject(columnName);
        if(null != value)
        {
            if(value instanceof byte[]) return (byte[])value;
            return value.toString().getBytes();
        }
        return null;
    }

    public Date getDate(int columnIndex) throws SQLException
    {
        Object value = getObject(columnIndex);
        if(null != value)
        {
            if(value instanceof Date) return (Date)value;
            return Date.valueOf(value.toString());
        }
        return null;
    }
    
    public Date getDate(String columnName) throws SQLException
    {
        Object value = getObject(columnName);
        if(null != value)
        {
            if(value instanceof Date) return (Date)value;
            return Date.valueOf(value.toString());
        }
        return null;
    }

    public Time getTime(int columnIndex) throws SQLException
    {
        Object value = getObject(columnIndex);
        if(null != value)
        {
            if(value instanceof Time) return (Time)value;
            return Time.valueOf(value.toString());
        }
        return null;
    }
    
    public Time getTime(String columnName) throws SQLException
    {
        Object value = getObject(columnName);
        if(null != value)
        {
            if(value instanceof Time) return (Time)value;
            return Time.valueOf(value.toString());
        }
        return null;
    }

    public Timestamp getTimestamp(int columnIndex) throws SQLException
    {
        Object value = getObject(columnIndex);
        if(null != value)
        {
            if(value instanceof Timestamp) return (Timestamp)value;
            return Timestamp.valueOf(value.toString());
        }
        return null;
    }
    
    public Timestamp getTimestamp(String columnName) throws SQLException
    {
        Object value = getObject(columnName);
        if(null != value)
        {
            if(value instanceof Timestamp) return (Timestamp)value;
            return Timestamp.valueOf(value.toString());
        }
        return null;
    }

    public InputStream getAsciiStream(int columnIndex) throws SQLException
    {
        return getBinaryStream(columnIndex);
    }
    
    public InputStream getAsciiStream(String columnName) throws SQLException
    {
        return getBinaryStream(columnName);
    }

    public InputStream getUnicodeStream(int columnIndex) throws SQLException
    {
        return getBinaryStream(columnIndex);
    }
    
    public InputStream getUnicodeStream(String columnName) throws SQLException
    {
        return getBinaryStream(columnName);
    }

    public InputStream getBinaryStream(int columnIndex) throws SQLException
    {
        Object value = getObject(columnIndex);
        if(null != value)
        {
            if(value instanceof InputStream) return (InputStream)value;
            return new ByteArrayInputStream(getBytes(columnIndex));
        }
        return null;
    }

    public InputStream getBinaryStream(String columnName) throws SQLException
    {
        Object value = getObject(columnName);
        if(null != value)
        {
            if(value instanceof InputStream) return (InputStream)value;
            return new ByteArrayInputStream(getBytes(columnName));
        }
        return null;
    }
    
    public Reader getCharacterStream(int columnIndex) throws SQLException
    {
        Object value = getObject(columnIndex);
        if(null != value)
        {
            if(value instanceof Reader) return (Reader)value;
            return new StringReader(getString(columnIndex));
        }
        return null;
    }

    public Reader getCharacterStream(String columnName) throws SQLException
    {
        Object value = getObject(columnName);
        if(null != value)
        {
            if(value instanceof Reader) return (Reader)value;
            return new StringReader(getString(columnName));
        }
        return null;
    }


    public SQLWarning getWarnings() throws SQLException
    {
        return null;
    }

    public void clearWarnings() throws SQLException
    {

    }

    public String getCursorName() throws SQLException
    {
        return cursorName;
    }

    public ResultSetMetaData getMetaData() throws SQLException
    {
        return new MockResultSetMetaData();
    }

    public boolean isBeforeFirst() throws SQLException
    {
        return cursor == -1;
    }

    public boolean isAfterLast() throws SQLException
    {    
        return cursor >= getRowCount();
    }

    public boolean isFirst() throws SQLException
    {
        return cursor == 0;
    }

    public boolean isLast() throws SQLException
    {
        return cursor == getRowCount() - 1;
    }

    public void beforeFirst() throws SQLException
    {
        checkResultSetType();
        cursor = -1;
    }

    public void afterLast() throws SQLException
    {
        checkResultSetType();
        cursor = getRowCount();
    }
    
    public boolean next() throws SQLException
    {
        cursor++;
        adjustCursor();
        return false;
    }


    public boolean first() throws SQLException
    {
        checkResultSetType();
        cursor = 0;
        return getRowCount() >= 0;
    }

    public boolean last() throws SQLException
    {
        checkResultSetType();
        cursor = getRowCount() - 1;
        return getRowCount() >= 0;
    }
    
    public boolean absolute(int row) throws SQLException
    {
        checkResultSetType();
        if(row > 0) cursor = row - 1;
        if(row < 0) cursor = getRowCount() + row;
        adjustCursor();
        return cursor < getRowCount();
    }

    public boolean relative(int rows) throws SQLException
    {
        checkResultSetType();
        cursor += rows;
        adjustCursor();
        return cursor < getRowCount();
    }

    public int getRow() throws SQLException
    {
        return cursor + 1;
    }

    public boolean previous() throws SQLException
    {
        checkResultSetType();
        cursor--;
        adjustCursor();
        return cursor < getRowCount();
    }
    
    public void setFetchDirection(int fetchDirection) throws SQLException
    {
        this.fetchDirection = fetchDirection;
    }

    public int getFetchDirection() throws SQLException
    {
        return fetchDirection;
    }

    public void setFetchSize(int fetchSize) throws SQLException
    {
        this.fetchSize = fetchSize;
    }

    public int getFetchSize() throws SQLException
    {
        return fetchSize;
    }

    public int getType() throws SQLException
    {
        return resultSetType;
    }

    public int getConcurrency() throws SQLException
    {
        return resultSetConcurrency;
    }
    
    public int findColumn(String columnName) throws SQLException
    {
        for(int ii = 0; ii < columnNameList.size(); ii++)
        {
            if(columnName.equals(columnNameList.get(ii))) return ii;
        }
        throw new SQLException("No column with name " + columnName + " found");
    }

    public boolean rowUpdated() throws SQLException
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean rowInserted() throws SQLException
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean rowDeleted() throws SQLException
    {
        // TODO Auto-generated method stub
        return false;
    }
    
    public void updateObject(int columnIndex, Object x) throws SQLException
    {
        
    }
    
    public void updateObject(int columnIndex, Object x, int scale) throws SQLException
    {
        

    }
    
    public void updateObject(String columnName, Object value, int scale) throws SQLException
    {
        checkColumnName(columnName);
        checkRowBounds();
        List column = (List)columnMap.get(columnName);
        if(null != column && cursor < column.size())
        {
            value = column.set(cursor, value);
        }
    }

    public void updateObject(String columnName, Object x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void updateNull(int columnIndex) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void updateBoolean(int columnIndex, boolean x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void updateByte(int columnIndex, byte x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void updateShort(int columnIndex, short x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void updateInt(int columnIndex, int x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void updateLong(int columnIndex, long x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void updateFloat(int columnIndex, float x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void updateDouble(int columnIndex, double x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void updateString(int columnIndex, String x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void updateBytes(int columnIndex, byte[] x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void updateDate(int columnIndex, Date x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void updateTime(int columnIndex, Time x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void updateNull(String columnName) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void updateBoolean(String columnName, boolean x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void updateByte(String columnName, byte x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void updateShort(String columnName, short x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void updateInt(String columnName, int x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void updateLong(String columnName, long x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void updateFloat(String columnName, float x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void updateDouble(String columnName, double x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void updateBigDecimal(String columnName, BigDecimal x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void updateString(String columnName, String x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void updateBytes(String columnName, byte[] x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void updateDate(String columnName, Date x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void updateTime(String columnName, Time x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void updateTimestamp(String columnName, Timestamp x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void updateAsciiStream(String columnName, InputStream x, int length) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void updateBinaryStream(String columnName, InputStream x, int length) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void updateCharacterStream(String columnName, Reader reader, int length) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void insertRow() throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void updateRow() throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void deleteRow() throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void refreshRow() throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void cancelRowUpdates() throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void moveToInsertRow() throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void moveToCurrentRow() throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public Statement getStatement() throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getObject(int i, Map map) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Ref getRef(int i) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Blob getBlob(int i) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Clob getClob(int i) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Array getArray(int i) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Ref getRef(String colName) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Blob getBlob(String colName) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Clob getClob(String colName) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Array getArray(String colName) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Date getDate(int columnIndex, Calendar cal) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Date getDate(String columnName, Calendar cal) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Time getTime(int columnIndex, Calendar cal) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Time getTime(String columnName, Calendar cal) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Timestamp getTimestamp(String columnName, Calendar cal) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public URL getURL(int columnIndex) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public URL getURL(String columnName) throws SQLException
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void updateRef(int columnIndex, Ref x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void updateRef(String columnName, Ref x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void updateBlob(int columnIndex, Blob x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void updateBlob(String columnName, Blob x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void updateClob(int columnIndex, Clob x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void updateClob(String columnName, Clob x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void updateArray(int columnIndex, Array x) throws SQLException
    {
        // TODO Auto-generated method stub

    }

    public void updateArray(String columnName, Array x) throws SQLException
    {
        // TODO Auto-generated method stub

    }
    
    private void checkColumnName(String columnName) throws SQLException
    {
        if(!columnMap.containsKey(columnName))
        {
            throw new SQLException("No column " + columnName);
        }
    }
    
    private void checkColumnBounds(int columnIndex) throws SQLException
    {
        if(!(columnIndex - 1 < columnNameList.size()))
        {
            throw new SQLException("Index " + columnIndex + " out of bounds");
        }
    }
    
    private void checkRowBounds() throws SQLException
    {
        if(!(cursor < getRowCount()))
        {
            throw new SQLException("Current row invalid");
        }
    }
    
    private void checkResultSetType() throws SQLException
    {
        if(resultSetType == ResultSet.TYPE_FORWARD_ONLY)
        {
            throw new SQLException("ResultSet is TYPE_FORWARD_ONLY");
        }
    }
    
    private void adjustCursor()
    {
        if(cursor < 0) cursor = -1;
        if(cursor >= getRowCount()) cursor = getRowCount();
    }
    
    private void adjustColumns()
    {
        //TODO Implement me
    }
}