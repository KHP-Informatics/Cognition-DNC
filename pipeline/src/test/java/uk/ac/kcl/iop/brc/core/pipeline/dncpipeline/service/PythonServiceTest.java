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
