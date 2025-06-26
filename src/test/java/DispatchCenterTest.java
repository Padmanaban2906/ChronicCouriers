
import org.example.ChronosCouriers;
import org.example.exception.PackageNotFoundException;
import org.example.models.Package;
import org.example.models.PackagePriority;
import org.example.models.Rider;
import org.example.models.RiderStatus;
import org.example.service.DispatchCenter;
import org.junit.jupiter.api.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DispatchCenterTest {
    private DispatchCenter dsp;
    private static List<Package> testPackages;
    private static List<Rider> testRiders;
    private static ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void setupTestData() throws IOException {
        testPackages = objectMapper.readValue(
                new File("src/test/resources/Packages.json"),
                new TypeReference<List<Package>>() {}
        );
        testRiders = objectMapper.readValue(
                new File("src/test/resources/Riders.json"),
                new TypeReference<List<Rider>>() {}
        );
    }

    @BeforeEach
    void setUp() {
        dsp = new DispatchCenter();
    }

    @Test
    @Order(1)
    void testAddRiderDetails() {
        Rider rider = testRiders.get(0);
        Package pkg = testPackages.get(0);
        dsp.addRiderDetails(rider);
        dsp.setPackages(pkg);
        assertTrue(dsp.availabilityChk());
    }

    @Test
    @Order(2)
    void testSetPackages() {
        Package pkg = testPackages.get(0);
        dsp.setPackages(pkg);
        assertFalse(dsp.assignPackageToRider().isEmpty());
    }

    @Test
    @Order(3)
    void testAssignPackageToRider() {
        testRiders.forEach(rider -> dsp.addRiderDetails(rider));
        testPackages.forEach(pkg -> dsp.setPackages(pkg));
        PriorityQueue<Package> result = dsp.assignPackageToRider();
        Package firstUnassigned = result.peek();
        if (firstUnassigned != null) {
            assertEquals(PackagePriority.STANDARD, firstUnassigned.getPriority());
        }
    }

    @Test
    @Order(4)
    void testDeliveryCompletionStatus() {
        Rider rider = testRiders.get(0);
        Package pkg = testPackages.get(0);
        rider.setStatus(RiderStatus.AVAILABLE);
        dsp.addRiderDetails(rider);
        dsp.setPackages(pkg);
        dsp.assignPackageToRider();
        dsp.deliveryCompletionStatus();
        assertFalse(dsp.availabilityChk());
    }

    @Test
    @Order(5)
    void testMultipleAssign() {
        testRiders.forEach(rider -> dsp.addRiderDetails(rider));
        testPackages.forEach(pkg -> dsp.setPackages(pkg));

        PriorityQueue<Package> remainingPackages = dsp.assignPackageToRider();
        dsp.deliveryCompletionStatus();

        assertNotNull(remainingPackages);
    }

    @Test
    @Order(6)
    void testLocationBased() {
        testRiders.forEach(rider -> dsp.addRiderDetails(rider));

        Package pkg = new Package();
        pkg.setId(1);
        pkg.setLocation("Chennai");
        pkg.setPriority(PackagePriority.EXPRESS);
        pkg.setOrderTime(System.currentTimeMillis());
        pkg.setDeadline(System.currentTimeMillis() + 3600000);
        dsp.setPackages(pkg);

        PriorityQueue<Package> result = dsp.assignPackageToRider();
        assertTrue(result.isEmpty());
    }

    @Test
    @Order(7)
    void testRiderReliability() {
        Rider highRating = new Rider();
        Rider lowRating = new Rider();
        highRating.setId(1);
        highRating.setLocation("Chennai");
        highRating.setReliabilityRating(4.9);
        highRating.setStatus(RiderStatus.AVAILABLE);
        lowRating.setId(2);
        lowRating.setLocation("Chennai");
        lowRating.setReliabilityRating(4.2);
        lowRating.setStatus(RiderStatus.AVAILABLE);
        dsp.addRiderDetails(highRating);
        dsp.addRiderDetails(lowRating);

        Package pkg = testPackages.get(0);
        dsp.setPackages(pkg);

        dsp.assignPackageToRider();

        assertEquals(RiderStatus.BUSY, highRating.getStatus());
        assertEquals(RiderStatus.AVAILABLE, lowRating.getStatus());
    }

    @Test
    @Order(8)
    void testPackagePriorities() {
        Package expressPkg = new Package();
        expressPkg.setId(1);
        expressPkg.setLocation("Chennai");
        expressPkg.setPriority(PackagePriority.EXPRESS);
        expressPkg.setOrderTime(System.currentTimeMillis());
        expressPkg.setDeadline(System.currentTimeMillis() + 3600000);

        Package standardPkg = new Package();
        standardPkg.setId(2);
        standardPkg.setLocation("Chennai");
        standardPkg.setPriority(PackagePriority.STANDARD);
        standardPkg.setOrderTime(System.currentTimeMillis());
        standardPkg.setDeadline(System.currentTimeMillis() + 7200000);

        dsp.setPackages(standardPkg);
        dsp.setPackages(expressPkg);

        Rider rider = new Rider();
        rider.setId(1);
        rider.setLocation("Chennai");
        rider.setReliabilityRating(4.5);
        rider.setStatus(RiderStatus.AVAILABLE);
        dsp.addRiderDetails(rider);

        PriorityQueue<Package> result = dsp.assignPackageToRider();

        assertEquals(1, result.size());
        assertEquals(PackagePriority.STANDARD, result.peek().getPriority());
    }

    @Test
    @Order(9)
    void testNoRidersAssignToPackageAndInvalidLocation() {
        Package pkg = new Package();
        pkg.setId(1);
        pkg.setLocation("Chennai");
        pkg.setPriority(PackagePriority.EXPRESS);
        pkg.setOrderTime(System.currentTimeMillis());
        pkg.setDeadline(System.currentTimeMillis() + 3600000);
        dsp.setPackages(pkg);

        PriorityQueue<Package> result = dsp.assignPackageToRider();
        assertFalse(result.isEmpty());

        Package invalidPkg = new Package();
        invalidPkg.setId(2);
        invalidPkg.setLocation("");
        invalidPkg.setPriority(PackagePriority.STANDARD);
        invalidPkg.setOrderTime(System.currentTimeMillis());
        invalidPkg.setDeadline(System.currentTimeMillis() + 3600000);
        dsp.setPackages(invalidPkg);

        result = dsp.assignPackageToRider();
        assertTrue(result.contains(invalidPkg));
    }
}
