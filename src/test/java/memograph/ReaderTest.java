package memograph;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Date;

public class ReaderTest extends BaseTest {
    @Test
    public void increaseGraphic() {
        dataCheck("increaseLog");
    }

    @Test
    public void declineGraphic() {
        dataCheck("declineLog");
    }

    private void dataCheck(String fileName) {
        Pair<Date[], long[]> result = getData(fileName);
        for (int i = 0; i < result.getValue().length - 2; i++) {
            if (fileName.equals("declineLog")) {
                assertTrue(result.getValue()[i] > result.getValue()[i + 1]);
            }
            else if (fileName.equals("increaseLog")) {
                assertTrue(result.getValue()[i] < result.getValue()[i + 1]);
            }
        }
    }
}