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

package uk.ac.kcl.iop.brc.core.pipeline.common.testutils;

import org.hsqldb.Server;
import org.hsqldb.persist.HsqlProperties;
import org.hsqldb.server.ServerAcl;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
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
