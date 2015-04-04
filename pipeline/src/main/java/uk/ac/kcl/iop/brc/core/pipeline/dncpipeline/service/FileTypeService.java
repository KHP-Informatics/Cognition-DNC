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

package uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.service;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatch;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;
import org.springframework.stereotype.Service;

@Deprecated
@Service
public class FileTypeService {

    public String getType(byte[] data) throws MagicParseException, MagicException, MagicMatchNotFoundException {
        MagicMatch match = Magic.getMagicMatch(data);
        return match.getMimeType();
    }

    public boolean isDoc(byte[] data) {
        return isDataTypeEqualTo(data, "application/msword");
    }

    public boolean isPDF(byte[] data) {
        return isDataTypeEqualTo(data, "application/pdf");
    }

    public boolean isDocx(byte[] data){
        return isDataTypeEqualTo(data, "application/zip");
    }

    private boolean isDataTypeEqualTo(byte[] data, String type) {
        try {
            return getType(data).equalsIgnoreCase(type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



}
