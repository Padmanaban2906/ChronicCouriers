import org.example.ChronosCouriers;
import org.example.exception.PackageNotFoundException;
import org.example.exception.RiderNotFoundException;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ChronosCouriersTest {

    @Test
    @Order(3)
    void testPackageEmptyJson()  {
        String[] args = {
                "src/test/resources/Packages_dummy.json",
                "src/test/resources/Riders.json"
        };

        assertThrows(PackageNotFoundException.class, () -> ChronosCouriers.main(args));
    }

    @Test
    @Order(4)
    void testRiderEmptyJson()  {
        String[] args = {
                "src/test/resources/Packages.json",
                "src/test/resources/Riders_dummy.json"
        };

        assertThrows(RiderNotFoundException.class, () -> ChronosCouriers.main(args));
    }

    @Test
    @Order(1)
    void testWithOneArguments()  {
        String[] args = {
                "src/test/resources/Packages.json"
        };

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ChronosCouriers.main(args);
        });

        String expectedMessage = "Usage: java ChronosCouriers <packages_file_path> <riders_file_path>";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @Order(2)
    void testWithEmptyArguments()  {
        String[] args = {
                "",
                ""
        };

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            ChronosCouriers.main(args);
        });

        String expectedMessage = "Usage: java ChronosCouriers <packages_file_path> <riders_file_path>";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @Order(5)
    void testAllRidersEmptyLocationJson() {
        String[] args = {
                "src/test/resources/Packages.json",
                "src/test/resources/Riders_Location_Empty.json"
        };

        assertThrows(RiderNotFoundException.class, () -> ChronosCouriers.main(args));
    }

    @Test
    @Order(6)
    void testAllPackagesEmptyLocationJson() {
        String[] args = {
                "src/test/resources/Packages_Location_Empty.json",
                "src/test/resources/Riders.json"
        };

        assertThrows(PackageNotFoundException.class, () -> ChronosCouriers.main(args));
    }

    @Test
    @Order(7)
    void testSomeRidersEmptyLocationJson() throws Exception {
        String[] args = {
                "src/test/resources/Packages_Location_Empty_Some.json",
                "src/test/resources/Riders_Location_Empty_Some.json"
        };

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        ChronosCouriers.main(args);

        String output = outContent.toString();
        assertTrue(output.contains("Rider location is empty for the ID :"));
        assertFalse(output.contains("All riders have empty locations"));
    }

    @Test
    @Order(8)
    void testSomePackagesEmptyLocationJson() throws Exception {
        String[] args = {
                "src/test/resources/Packages_Location_Empty_Some.json",
                "src/test/resources/Riders.json"
        };

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        ChronosCouriers.main(args);

        String output = outContent.toString();
        assertTrue(output.contains("Package location is empty for the ID :"));
        assertFalse(output.contains("All packages have empty locations"));
    }

    @Test
    @Order(9)
    void testRiderPackagesJson()  {
        String[] args = {
                "src/test/resources/Packages.json",
                "src/test/resources/Riders.json"
        };

        assertDoesNotThrow(() -> ChronosCouriers.main(args));
    }
}
