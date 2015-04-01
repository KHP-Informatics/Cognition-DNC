/*
        Copyright (c) 2015 King's College London

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	    http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
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
