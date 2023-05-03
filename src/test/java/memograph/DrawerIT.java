package memograph;

import org.junit.Test;
import java.util.Date;

public class DrawerIT extends BaseTest {
    @Test
    public void increaseGraphic() {
        graphicCheck("increaseLog");
    }

    @Test
    public void declineGraphic() {
        graphicCheck("declineLog");
    }

    private void graphicCheck(String fileName) {
        Pair<Date[], long[]> result = getData(fileName);
        new Drawer(result.getKey(), result.getValue()).setVisible(true);
        try {
            Thread.sleep(3_000);
        } catch (InterruptedException e) {
            System.err.println("Error while thread sleeping in drawer test: " + e.getMessage());
        }
    }
}
