package core.basesyntax.report.report;

import static org.junit.jupiter.api.Assertions.assertEquals;

import core.basesyntax.model.FruitOperation;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReportGeneratorImplTest {
    private ReportGeneratorImpl reportGenerator;

    @BeforeEach
    void setUp() {
        reportGenerator = new ReportGeneratorImpl();
    }

    @Test
    void getReport_withMultipleFruits_generatesCorrectReport() {
        List<FruitOperation> store = Arrays.asList(
                new FruitOperation(FruitOperation.Operation.BALANCE, "apple", 10),
                new FruitOperation(FruitOperation.Operation.BALANCE, "banana", 20)
        );

        String expectedReport = "fruit,quantity" + System.lineSeparator()
                + "apple,10" + System.lineSeparator()
                + "banana,20";

        String actualReport = reportGenerator.getReport();
        assertEquals(expectedReport, actualReport);
    }

    @Test
    void getReport_withEmptyList_returnsHeaderOnly() {
        List<FruitOperation> store = Collections.emptyList();
        String expectedReport = "fruit,quantity";
        String actualReport = reportGenerator.getReport();
        assertEquals(expectedReport, actualReport);
    }
}
