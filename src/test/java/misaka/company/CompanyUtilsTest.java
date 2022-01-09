package misaka.company;

import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class CompanyUtilsTest {
    private static Logger log = LoggerFactory.getLogger(CompanyUtilsTest.class);

    @Test
    public void test1() {
        CompanyUtils.info("Apple");
        CompanyUtils.search("Apple");
    }

}
