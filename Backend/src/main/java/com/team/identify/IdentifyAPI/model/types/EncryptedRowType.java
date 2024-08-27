package com.team.identify.IdentifyAPI.model.types;

import com.team.identify.IdentifyAPI.apps.crypt.pojo.EncryptedRow;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.Objects;

public class EncryptedRowType implements UserType<EncryptedRow> {
    @Override
    public int getSqlType() {
        return Types.CLOB;
    }

    @Override
    public Class<EncryptedRow> returnedClass() {
        return EncryptedRow.class;
    }

    @Override
    public boolean equals(EncryptedRow x, EncryptedRow y) {
        if (Objects.isNull(x) || Objects.isNull(y)) {
            return Objects.isNull(x) && Objects.isNull(y);
        }
        if (x.getCypherText() == null || y.getCypherText() == null) {
            return x.getCypherText() == null && y.getCypherText() == null;
        }
        return Arrays.equals(x.getCypherText(), y.getCypherText());
    }

    @Override
    public int hashCode(EncryptedRow x) {
        return x.hashCode();
    }

    @Override
    public EncryptedRow nullSafeGet(ResultSet rs, int position, SharedSessionContractImplementor session, Object owner) throws SQLException {
        return new
                EncryptedRow(rs.getString(position));
    }

    @Override
    public void nullSafeSet(PreparedStatement st, EncryptedRow value, int index, SharedSessionContractImplementor session) throws SQLException {
        if (Objects.isNull(value)) {
            st.setNull(index, Types.CLOB);
        } else {
            st.setString(index, value.toString());
        }
    }

    @Override
    public EncryptedRow deepCopy(EncryptedRow value) {
        return new EncryptedRow(value);
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(EncryptedRow value) {
        return value;
    }

    @Override
    public EncryptedRow assemble(Serializable cached, Object owner) {
        return new EncryptedRow();
    }

    @Override
    public EncryptedRow replace(EncryptedRow detached, EncryptedRow managed, Object owner) {
        return new EncryptedRow(detached);
    }

}
