/*
        Cognition-DNC (Dynamic Name Concealer)
        Binary to text document converter and database pseudonymiser.

        Copyright (C) 2015 Ismail E. Kartoglu

        This program is free software: you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.

        This program is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        GNU General Public License for more details.

        You should have received a copy of the GNU General Public License
        along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/


package uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.data.helper;

import org.hibernate.engine.jdbc.SerializableClobProxy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;
import java.sql.Clob;
import java.sql.SQLException;

/**
 * Clob helper is used to handle Clob (large text) types of different databases.
 */
@Component
public class ClobHelper {

    public String getStringFromExpectedClob(Object expectedBlob) {
        try {
            return (String) expectedBlob;
        } catch (ClassCastException ex) {
            return getString(expectedBlob);
        }
    }

    private String getString(Object expectedClob) {
        SerializableClobProxy clobProxy = (SerializableClobProxy) Proxy.getInvocationHandler(expectedClob);
        Clob wrappedClob = clobProxy.getWrappedClob();
        try {
            return  wrappedClob.getSubString(1, (int) wrappedClob.length());
        } catch (SQLException e) {
            e.printStackTrace();
            return "error: could not retrieve text";
        }
    }
}
