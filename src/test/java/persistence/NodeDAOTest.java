package persistence;

import de.pm.mindcloud.MindCloudApplication;
import de.pm.mindcloud.persistence.domain.Mindmap;
import de.pm.mindcloud.persistence.domain.MindmapData;
import de.pm.mindcloud.persistence.repository.MindmapAccess;
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
    private MindmapAccess mindMapAccess;

    @Test
    public void createMindMap() throws Exception {

        Mindmap mindMap = new Mindmap("Die yolo map");
        MindmapData first = new MindmapData();
        first.putData("id", "123");
        first.putData("title", "Title");

        mindMapAccess.save(mindMap);
    }

}
