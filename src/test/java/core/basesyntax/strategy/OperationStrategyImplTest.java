package core.basesyntax.strategy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import core.basesyntax.model.FruitOperation;
import core.basesyntax.strategy.operation.BalanceOperationHandler;
import core.basesyntax.strategy.operation.OperationHandler;
import core.basesyntax.strategy.operation.PurchaseOperationHandler;
import core.basesyntax.strategy.operation.ReturnOperationHandler;
import core.basesyntax.strategy.operation.SupplyOperationHandler;
import java.util.EnumMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OperationStrategyImplTest {
    private OperationStrategy operationStrategy;

    @BeforeEach
    void setUp() {
        Map<FruitOperation.Operation, OperationHandler> handlerMap
                = new EnumMap<>(FruitOperation.Operation.class);
        handlerMap.put(FruitOperation.Operation.BALANCE, new BalanceOperationHandler());
        handlerMap.put(FruitOperation.Operation.SUPPLY, new SupplyOperationHandler());
        handlerMap.put(FruitOperation.Operation.RETURN, new ReturnOperationHandler());
        handlerMap.put(FruitOperation.Operation.PURCHASE, new PurchaseOperationHandler());

        operationStrategy = new OperationStrategyImpl(handlerMap);
    }

    @Test
    void get_supplyOperation_returnsSupplyHandler() {
        OperationHandler handler = operationStrategy.get(FruitOperation.Operation.SUPPLY);
        assertTrue(handler instanceof SupplyOperationHandler);
    }

    @Test
    void get_purchaseOperation_returnsPurchaseHandler() {
        OperationHandler handler = operationStrategy.get(FruitOperation.Operation.PURCHASE);
        assertTrue(handler instanceof PurchaseOperationHandler);
    }

    @Test
    void get_returnOperation_returnsReturnHandler() {
        OperationHandler handler = operationStrategy.get(FruitOperation.Operation.RETURN);
        assertTrue(handler instanceof ReturnOperationHandler);
    }

    @Test
    void get_unknownOperation_returnsNull() {
        OperationStrategy emptyStrategy
                = new OperationStrategyImpl(new EnumMap<>(FruitOperation.Operation.class));
        OperationHandler handler = emptyStrategy.get(FruitOperation.Operation.SUPPLY);
        assertNull(handler, "Handler should be null for unknown operation");
    }

    @Test
    void use_supplyHandler_correctlyAddsQuantity() {
        FruitOperation op = new FruitOperation(FruitOperation.Operation.SUPPLY,
                "apple", 10);
        int result = operationStrategy.get(FruitOperation.Operation.SUPPLY)
                .getQuantityFromStore(op, 5);
        assertEquals(15, result);
        assertEquals(15, op.getQuantity());
    }

    @Test
    void use_purchaseHandler_decreasesQuantity() {
        FruitOperation op = new FruitOperation(FruitOperation.Operation.PURCHASE,
                "banana", 20);
        int result = operationStrategy.get(FruitOperation.Operation.PURCHASE)
                .getQuantityFromStore(op, 5);
        assertEquals(15, result);
        assertEquals(15, op.getQuantity());
    }
}
