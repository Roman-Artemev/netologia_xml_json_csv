import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MainTest {
    public static final String TEST_CSV = "test_data.csv";
    private static final String TEST_XML = "test_data.xml";
    private static final String TEST_JSON = "test_new_data.json";

    @BeforeEach
    void setUp() throws Exception {
        String csvContent = "1,John,Smith,USA,25\n2,Ivan,Petrov,RU,23";
        Files.write(Paths.get(TEST_CSV), csvContent.getBytes());

        String xmlContent = "<staff>\n" +
                "    <employee>\n" +
                "        <id>1</id>\n" +
                "        <firstName>John</firstName>\n" +
                "        <lastName>Smith</lastName>\n" +
                "        <country>USA</country>\n" +
                "        <age>25</age>\n" +
                "    </employee>\n" +
                "    <employee>\n" +
                "        <id>2</id>\n" +
                "        <firstName>Ivan</firstName>\n" +
                "        <lastName>Petrov</lastName>\n" +
                "        <country>RU</country>\n" +
                "        <age>23</age>\n" +
                "    </employee>\n" +
                "</staff>";
        Files.write(Paths.get(TEST_XML), xmlContent.getBytes());

        String jsonContent = "[\n" +
                "  {\n" +
                "    \"id\": 1,\n" +
                "    \"firstName\": \"John\",\n" +
                "    \"lastName\": \"Smith\",\n" +
                "    \"country\": \"USA\",\n" +
                "    \"age\": 25\n" +
                "  },\n" +
                "  {\n" +
                "    \"id\": 2,\n" +
                "    \"firstName\": \"Ivan\",\n" +
                "    \"lastName\": \"Petrov\",\n" +
                "    \"country\": \"RU\",\n" +
                "    \"age\": 23\n" +
                "  }\n" +
                "]";
        Files.write(Paths.get(TEST_JSON), jsonContent.getBytes());
    }

    @Test
    void testParseCSV() {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        List<Employee> employees = Main.parseCSV(columnMapping, TEST_CSV);

        assertNotNull(employees);
        assertEquals(2, employees.size());

        Employee first = employees.get(0);
        assertEquals(1L, first.id);
        assertEquals("John", first.firstName);
        assertEquals("Smith", first.lastName);
        assertEquals("USA", first.country);
        assertEquals(25, first.age);

        Employee second = employees.get(1);
        assertEquals(2L, second.id);
        assertEquals("Ivan", second.firstName);
    }

    @Test
    void testReadString() {
        String result = Main.readString(TEST_JSON);
        assertNotNull(result);
        assertTrue(result.contains("John"));
        assertTrue(result.contains("Ivan"));
    }

    @Test
    void testParseXML() {
        List<Employee> employees = Main.parseXML(TEST_XML);

        assertNotNull(employees);
        assertEquals(2, employees.size());

        Employee first = employees.get(0);
        assertEquals(1L, first.id);
        assertEquals("John", first.firstName);
        assertEquals("Smith", first.lastName);
        assertEquals("USA", first.country);
        assertEquals(25, first.age);
    }

    @Test
    void testEmptyCSVFile() throws IOException {
        String emptyCSV = "empty_test.csv";
        Files.write(Paths.get(emptyCSV), "".getBytes());

        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        List<Employee> employees = Main.parseCSV(columnMapping, emptyCSV);

        assertNull(employees); // parseCSV возвращает null при ошибке
    }

}
