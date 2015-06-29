package persistence;

import de.pm.mindcloud.MindCloudApplication;
import de.pm.mindcloud.persistence.domain.MindMap;
import de.pm.mindcloud.persistence.domain.Node;
import de.pm.mindcloud.persistence.repository.MindMapAccess;
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
    private MindMapAccess mindMapAccess;

    @Test
    public void createMindMap() throws Exception {

        MindMap mindMap = new MindMap("Die yolo map");
        Node first = new Node("0", "First child");
        Node second = new Node("1", "Second child");
        Node third = new Node("2", "Third child");
        Node fourth = new Node("3", "Fourth child");

        mindMap.getNodes().add(first);
        mindMap.getNodes().add(second);
        mindMap.getNodes().add(third);
        mindMap.getNodes().add(fourth);

        mindMapAccess.save(mindMap);
    }

}
