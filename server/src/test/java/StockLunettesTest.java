import bernard_flou.Fabricateur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StockLunettesTest {

    private StockLunettes stockLunettes;

    @BeforeEach
    void setUp() {
        stockLunettes = new StockLunettes();
    }

    @Test
    void chercherSerialConnu() {
        Fabricateur.Lunette lunette = new Fabricateur.Lunette(Fabricateur.TypeLunette.BANANA, "1234");
        stockLunettes.ajouter(lunette);

        assertEquals(Fabricateur.TypeLunette.BANANA.toString(), stockLunettes.chercher("1234"));
    }

    @Test
    void chercherSerialInconnu() {
        assertEquals("invalid", stockLunettes.chercher("INCONNU"));
    }

    @Test
    void ajouterPlusieursLunettes() {
        Fabricateur.Lunette l1 = new Fabricateur.Lunette(Fabricateur.TypeLunette.BANANA, "AAA");
        Fabricateur.Lunette l2 = new Fabricateur.Lunette(Fabricateur.TypeLunette.CHATGPT, "BBB");
        stockLunettes.ajouter(l1);
        stockLunettes.ajouter(l2);

        assertEquals(Fabricateur.TypeLunette.BANANA.toString(), stockLunettes.chercher("AAA"));
        assertEquals(Fabricateur.TypeLunette.CHATGPT.toString(), stockLunettes.chercher("BBB"));
    }
}