import javaCore.homeworks.Employee;
import org.junit.jupiter.api.*;

import java.util.List;

import static javaCore.homeworks.Main.*;

public class MainTest {
    private static long suiteStartTime;
    private long testStartTime;

    @BeforeAll
    public static void initSuite() {
        System.out.println("Running tests");
        suiteStartTime = System.nanoTime();
    }

    @AfterAll
    public static void completeSuite() {
        System.out.println("All tests complete: " + (System.nanoTime() - suiteStartTime));
    }

    @BeforeEach
    public void initTest() {
        System.out.println("Starting new test");
        testStartTime = System.nanoTime();
    }

    @AfterEach
    public void finalizeTest() {
        System.out.println("Test complete: " + (System.nanoTime() - testStartTime));
    }

    @Test
    public void testParseCSV() {
        //given:
        final String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        final String fileName = "data.csv";

        //when:
        List<Employee> result = parseCSV(columnMapping, fileName);

        //then:
        Assertions.assertNotNull(result);
    }

    @Test
    public void testParseXML() {
        //when:
        List<Employee> listXml = parseXML("data.xml");

        //then:
        Assertions.assertNotNull(listXml);
    }

    @Test
    public void testReadString() {
        //given:
        String s = "[  {    \"id\": 1,    \"firstName\": \"John\",    \"lastName\": \"Smith\",    \"country\": \"USA\",    \"age\": 25  },  {    \"id\": 2,    \"firstName\": \"Ivan\",    \"lastName\": \"Petrov\",    \"country\": \"RU\",    \"age\": 23  }]";

        //when:
        String json = readString("new_data.json");

        //then:
        Assertions.assertEquals(s, json);
    }

    @Test
    public void testJsonToList() {
        //given:
        String json = readString("new_data.json");

        //when:
        List<Employee> list = jsonToList(json);

        //then:
        Assertions.assertNotNull(list);
    }
}
