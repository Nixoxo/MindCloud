package persistence;

import de.pm.mindcloud.MindCloudApplication;
import de.pm.mindcloud.persistence.domain.Node;
import de.pm.mindcloud.persistence.repository.NodeAccess;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created on 18/06/15
 * This class is responsible
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MindCloudApplication.class)
@WebAppConfiguration   // 3
@IntegrationTest("server.port:0")
public class NodeDAOTest {

    @Autowired
    private NodeAccess nodeAccess;

    @Test
    public void createChild() throws Exception {
//        Node one = new Node("Der erste Streich");
//        Node two = new Node("Der zweite Streich");
//        one.addNode(two);
//        nodeAccess.save(one);
        Node delete = nodeAccess.find(2);
        nodeAccess.delete(delete);
//        Child three = new Child("third child");
//        databaseService.insert(three);
//        two.addNode(three);
//        databaseService.update(two);
    }
}
