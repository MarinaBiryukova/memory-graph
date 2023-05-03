package memograph;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public abstract class BaseTest {
    private static final String RESOURCES_PATH = "." + File.separator + "src" + File.separator + "test" + File.separator + "resources";
    private final Reader reader;

    BaseTest() {
        this.reader = new Reader();
    }

    protected Pair<Date[], long[]> getData(String fileName) {
        Pair<Date[], long[]> result = null;
        try {
            result = reader.getUsedMemory(RESOURCES_PATH + File.separator + fileName);
        } catch (IOException e) {
            fail("Error while getting data in test. Path: " + RESOURCES_PATH + File.separator + fileName);
        }
        assertNotNull(result);
        return result;
    }
}
