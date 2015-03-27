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

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.util.StringUtils;

import javax.script.ScriptException;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

public class PythonServiceTest {

    private PythonService pythonService = new PythonService();

    @Test
    @Ignore
    public void shouldCallPython() throws IOException, ScriptException {
        String pythonFile = getClass().getClassLoader().getResource("pythonfiles/ignoretags.py").getPath();
        String htmlPath = getClass().getClassLoader().getResource("pythontestres/testInput.html").getPath();
        String jsonPath = getClass().getClassLoader().getResource("pythontestres/testJson.json").getPath();

        String result = pythonService.runFile(Arrays.asList(pythonFile, htmlPath, jsonPath));

        assertTrue(result.contains("XXXXX"));
    }

    @Test
    @Ignore
    public void shouldNotReturnEmptyWhenHTMLIsBad() throws IOException {
        File file = new File(getClass().getClassLoader().getResource("pythonfiles/ignoretags.py").getPath());
        String filePath = file.getAbsolutePath();
        String htmlPath = getClass().getClassLoader().getResource("pythontestres/testempty.html").getPath();
        String jsonPath = getClass().getClassLoader().getResource("pythontestres/testJson.json").getPath();

        String result = pythonService.runFile(Arrays.asList(filePath, htmlPath, jsonPath));

        assertFalse(StringUtils.isEmpty(result));
    }

}
