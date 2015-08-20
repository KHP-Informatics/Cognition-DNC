/*
        Cognition-DNC (Dynamic Name Concealer)
        Binary to text document converter and database pseudonymiser.

        Copyright (C) 2015 Ismail E. Kartoglu

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
