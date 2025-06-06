package core.basesyntax.strategy.operation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import core.basesyntax.db.Storage;
import core.basesyntax.model.FruitOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OperationHandlerTest {

    private BalanceOperationHandler balanceHandler;
    private SupplyOperationHandler supplyHandler;
    private ReturnOperationHandler returnHandler;
    private PurchaseOperationHandler purchaseHandler;

    @BeforeEach
    public void setUp() {
        Storage.SHOP_STORE.clear();
        balanceHandler = new BalanceOperationHandler();
        supplyHandler = new SupplyOperationHandler();
        returnHandler = new ReturnOperationHandler();
        purchaseHandler = new PurchaseOperationHandler();
    }

    @Test
    public void balanceHandler_shouldSetQuantity() {
        FruitOperation fruitOperation = new FruitOperation(FruitOperation.Operation.BALANCE,
                "apple", 0);
        int quantity = balanceHandler.getQuantityFromStore(fruitOperation, 20);
        assertEquals(20, quantity);
        assertEquals(20, fruitOperation.getQuantity());
    }

    @Test
    public void supplyHandler_shouldAddQuantity() {
        FruitOperation fruitOperation = new FruitOperation(FruitOperation.Operation.SUPPLY,
                "banana", 10);
        int quantity = supplyHandler.getQuantityFromStore(fruitOperation, 15);
        assertEquals(25, quantity);
        assertEquals(25, fruitOperation.getQuantity());
    }

    @Test
    public void returnHandler_shouldAddQuantity() {
        FruitOperation fruitOperation = new FruitOperation(FruitOperation.Operation.RETURN,
                "orange", 5);
        int quantity = returnHandler.getQuantityFromStore(fruitOperation, 7);
        assertEquals(12, quantity);
        assertEquals(12, fruitOperation.getQuantity());
    }

    @Test
    public void purchaseHandler_shouldSubtractQuantity() {
        FruitOperation fruitOperation = new FruitOperation(FruitOperation.Operation.PURCHASE,
                "pear", 30);
        int quantity = purchaseHandler.getQuantityFromStore(fruitOperation, 10);
        assertEquals(20, quantity);
        assertEquals(20, fruitOperation.getQuantity());
    }

    @Test
    public void purchaseHandler_shouldThrow_whenRequestedMoreThanAvailable() {
        FruitOperation fruitOperation = new FruitOperation(FruitOperation.Operation.PURCHASE,
                "pear", 5);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                purchaseHandler.getQuantityFromStore(fruitOperation, 10));
        assertTrue(exception.getMessage().contains("Cannot purchase more than"));
    }
}
