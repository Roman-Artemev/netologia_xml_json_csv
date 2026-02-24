import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MainTest {
    public static final String TEST_CSV = "test_data.csv";

    @BeforeEach
    void setUp() throws Exception {
        String csvContent = "1,John,Smith,USA,25\n2,Ivan,Petrov,RU,23";
        Files.write(Paths.get(TEST_CSV), csvContent.getBytes());
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
}
