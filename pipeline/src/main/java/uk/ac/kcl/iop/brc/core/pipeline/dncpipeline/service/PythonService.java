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

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;

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
