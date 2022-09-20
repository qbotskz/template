package qbots.mektep.util.components;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class DataRecRowMapper implements RowMapper<qbots.mektep.util.components.DataRec> {

    public DataRecRowMapper() {}

    @Override
    public qbots.mektep.util.components.DataRec mapRow(ResultSet resultSet, int i) throws SQLException {
        ResultSetMetaData meta  = resultSet.getMetaData();
        int columnCount         = meta.getColumnCount();
        qbots.mektep.util.components.DataRec rec = new DataRec();
        for (int column = 1; column <= columnCount; ++column) {
            String key          = meta.getColumnName(column);
            Object value        = resultSet.getObject(column);
            rec.put(key, value);
        }
        return rec;
    }
}
