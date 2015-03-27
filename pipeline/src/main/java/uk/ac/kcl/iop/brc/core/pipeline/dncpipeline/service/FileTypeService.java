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

package uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.service;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatch;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;
import org.springframework.stereotype.Service;

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
