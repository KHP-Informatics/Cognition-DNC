/*
        The MIT License (MIT)
        Copyright (c) 2015 King's College London

        Permission is hereby granted, free of charge, to any person obtaining a copy
        of this software and associated documentation files (the "Software"), to deal
        in the Software without restriction, including without limitation the rights
        to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
        copies of the Software, and to permit persons to whom the Software is
        furnished to do so, subject to the following conditions:

        The above copyright notice and this permission notice shall be included in
        all copies or substantial portions of the Software.

        THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
        IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
        FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
        AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
        LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
        OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
        THE SOFTWARE.
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
