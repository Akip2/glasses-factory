import fr.smartglasses.frontend.controller.SerialController;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

public class SerialControllerTest {

    private final SerialController serialController = new SerialController();

    @Test
    void isValid_retourneUnCompletableFuture() {
        CompletableFuture<Boolean> future = serialController.isValid("SN-PRO-001");
        assertNotNull(future);
    }

    @Test
    void isValid_lanceUneRuntimeExceptionSiServeurIndisponible() {
        CompletableFuture<Boolean> future = serialController.isValid("SN-PRO-001");
        ExecutionException exception = assertThrows(ExecutionException.class, future::get);
        assertInstanceOf(RuntimeException.class, exception.getCause());
        assertTrue(exception.getCause().getMessage().contains("Erreur lors de la vérification"));
    }
}