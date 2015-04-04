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

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;

@Deprecated
@Service
public class PythonService {

    private static Logger logger = Logger.getLogger(PythonService.class);

    /**
     * Runs the Python interpreter.
     * @param args List of arguments to be supplied to the python command.
     * @return Result from python string
     * @throws IOException
     */
    public String runFile(List<String> args) throws IOException {
        LinkedList<String> list = new LinkedList<>(args);
        list.addFirst("python");
        ProcessBuilder processBuilder = new ProcessBuilder(list);
        Process p = processBuilder.start();
        BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
        StringWriter stringWriter = new StringWriter();
        in.lines().forEach(stringWriter::append);
        p.destroy();
        return stringWriter.toString();
    }
}
