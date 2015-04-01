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
