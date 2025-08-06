package com.ticket.generator;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

public class SharedSequenceGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        String sequenceName = "ticket_subticket_seq"; // shared sequence name

        try (Connection connection = session.getJdbcConnectionAccess().obtainConnection()) {

            long nextVal = 1;

            // Step 1: SELECT next_val
            PreparedStatement ps1 = connection.prepareStatement(
                "SELECT next_val FROM custom_sequence WHERE sequence_name = ? FOR UPDATE"
            );
            ps1.setString(1, sequenceName);
            ResultSet rs = ps1.executeQuery();

            if (rs.next()) {
                nextVal = rs.getLong("next_val");
            }
            rs.close();
            ps1.close();

            // Step 2: UPDATE next_val
            PreparedStatement ps2 = connection.prepareStatement(
                "UPDATE custom_sequence SET next_val = ? WHERE sequence_name = ?"
            );
            ps2.setLong(1, nextVal + 1);
            ps2.setString(2, sequenceName);
            ps2.executeUpdate();
            ps2.close();

            return nextVal;

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate ID using custom sequence", e);
        }
    }
}
