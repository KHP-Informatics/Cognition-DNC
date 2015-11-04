/*
        Cognition-DNC (Dynamic Name Concealer)         Developed by Ismail Kartoglu (https://github.com/iemre)
        Binary to text document converter and database pseudonymiser.

        Copyright (C) 2015 Biomedical Research Centre for Mental Health

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

import org.hibernate.engine.jdbc.SerializableBlobProxy;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;
import java.sql.Blob;
import java.sql.SQLException;

/**
 * BlobHelper is used for handling Blob (binary) data from different databases.
 */
@Component
public class BlobHelper {

    public byte[] getByteFromExpectedBlobObject(Object expectedBlob) {
        try {
            return (byte[]) expectedBlob;
        } catch (ClassCastException ex) {
            return getBytesFromSerializableBlob(expectedBlob);
        }
    }

    private byte[] getBytesFromSerializableBlob(Object serializableBlobProxy) {
        SerializableBlobProxy blobProxy = (SerializableBlobProxy) Proxy.getInvocationHandler(serializableBlobProxy);
        Blob wrappedBlob = blobProxy.getWrappedBlob();
        try {
            byte[] bytes = wrappedBlob.getBytes(1, (int) wrappedBlob.length());
            if (bytes != null) {
                if (bytes.length == 0) {
                    return null;
                }
            }
            return bytes;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
