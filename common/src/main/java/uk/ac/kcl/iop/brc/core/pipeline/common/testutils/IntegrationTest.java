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

package uk.ac.kcl.iop.brc.core.pipeline.common.testutils;

import org.hsqldb.Server;
import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.server.ServerAcl;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:testApplicationContext.xml"})
public class IntegrationTest {

    private Server server1;
    private Server server2;

    @Before
    public void init() throws IOException, ServerAcl.AclFormatException {
        HsqlProperties p1 = new HsqlProperties();
        p1.setProperty("server.database.0", "mem:hsqldb");
        p1.setProperty("server.dbname.0", "test");
        p1.setProperty("server.port", "9001");
        p1.setProperty("server.remote_open", "true");
        server1 = new Server();
        server1.setProperties(p1);
        server1.setLogWriter(null);
        server1.setErrWriter(null);
        server1.start();

        HsqlProperties p2 = new HsqlProperties();
        p2.setProperty("server.database.0", "mem:hsqldb");
        p2.setProperty("server.dbname.0", "test2");
        p2.setProperty("server.port", "9002");
        p2.setProperty("server.remote_open", "true");
        server2 = new Server();
        server2.setProperties(p2);
        server2.setLogWriter(null);
        server2.setErrWriter(null);
        server2.start();
    }

    @After
    public void end() {
        server1.stop();
        server2.stop();
    }

}
