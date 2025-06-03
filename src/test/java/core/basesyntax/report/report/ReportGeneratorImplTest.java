package core.basesyntax.report.report;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import core.basesyntax.db.Storage;
import core.basesyntax.model.FruitOperation;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReportGeneratorImplTest {
    private ReportGeneratorImpl reportGenerator;

    @BeforeEach
    void setUp() {
        reportGenerator = new ReportGeneratorImpl();
        Storage.SHOP_STORE.clear();
    }

    @AfterEach
    void tearDown() {
        Storage.SHOP_STORE.clear();
    }

    @Test
    void getReport_withMultipleFruits_generatesCorrectReport() {
        Storage.SHOP_STORE.put("apple", new FruitOperation(FruitOperation.Operation.BALANCE,
                "apple", 10));
        Storage.SHOP_STORE.put("banana", new FruitOperation(FruitOperation.Operation.BALANCE,
                "banana", 20));

        String actualReport = reportGenerator.getReport();
        String[] lines = actualReport.split(System.lineSeparator());

        assertEquals("fruit,quantity", lines[0]);
        List<String> expectedLines = List.of("apple,10", "banana,20");
        List<String> actualLines = Arrays.asList(lines).subList(1, lines.length);

        assertTrue(actualLines.containsAll(expectedLines)
                && expectedLines.containsAll(actualLines));
    }

    @Test
    void getReport_withEmptyStore_returnsHeaderOnly() {
        String expectedReport = "fruit,quantity";
        String actualReport = reportGenerator.getReport();
        assertEquals(expectedReport, actualReport);
    }
}
