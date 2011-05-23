/**
 * Copyright 2010 Gitana Software, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * For more information, please contact Gitana Software, Inc. at this
 * address:
 *
 *   info@gitanasoftware.com
 */

package org.gitana.repo.client;

import org.gitana.repo.client.nodes.Node;
import org.gitana.util.ClasspathUtil;
import org.junit.Test;

/**
 * @author uzi
 */
public class NodeTest extends AbstractTestCase
{
    @Test
    public void testCRUD()
    {
        Gitana gitana = new Gitana();

        // authenticate
        Server server = gitana.authenticate("admin", "admin");

        // create a repository
        Repository repository = server.createRepository();

        // get the master branch
        Branch master = repository.readBranch("master");

        // create three nodes
        Node node1 = master.createNode();
        Node node2 = master.createNode();
        Node node3 = master.createNode();

        // read nodes back (verify)
        Node verify1 = master.readNode(node1.getId());
        assertNotNull(verify1);
        Node verify2 = master.readNode(node2.getId());
        assertNotNull(verify2);
        Node verify3 = master.readNode(node3.getId());
        assertNotNull(verify3);

        // update a node
        node2.set("axl", "rose");
        node2.update();

        // read node back to verify
        Node verify4 = master.readNode(node2.getId());
        assertEquals("rose", verify4.getString("axl"));

        // delete the second node
        node2.delete();

        // verify that we can't read it
        Node verify5 = master.readNode(node2.getId());
        assertNull(verify5);
    }

    @Test
    public void testAttachments()
        throws Exception
    {
        Gitana gitana = new Gitana();

        // authenticate
        Server server = gitana.authenticate("admin", "admin");

        // create a repository
        Repository repository = server.createRepository();

        // get the master branch
        Branch master = repository.readBranch("master");

        // create a node
        Node node = master.createNode();

        // upload
        byte[] bytes = ClasspathUtil.bytesFromClasspath("org/gitana/repo/client/daffy.jpeg");
        node.uploadAttachment("thumb", bytes, "image/jpeg");

        // download and verify
        byte[] verify = node.downloadAttachment("thumb");
        assertEquals(bytes.length, verify.length);

        // update a node
        node.set("axl", "rose");
        node.update();

        // read node back to verify
        Node verify2 = master.readNode(node.getId());
        assertEquals("rose", verify2.getString("axl"));

        verify = node.downloadAttachment("thumb");
        assertEquals(bytes.length, verify.length);
    }

}
