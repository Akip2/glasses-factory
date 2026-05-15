import bernard_flou.Fabricateur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MessageParserTest {

    private MessageParser parser;

    @BeforeEach
    void setUp() {
        parser = new MessageParser();
    }

    @Test
    void parseTypeValide() {
        assertDoesNotThrow(() -> parser.parseType("CLAUDE"));
    }

    @Test
    void parseTypeInconnuLeveException() {
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> parser.parseType("INCONNU"));
        assertEquals(ErreurMessage.TYPE_INCONNU.getMessage(), ex.getMessage());
    }

    @Test
    void qteLegalZeroAutorise() {
        assertTrue(parser.qteLegal(0));
    }

    @Test
    void qteLegalNeufAutorise() {
        assertTrue(parser.qteLegal(9));
    }

    @Test
    void qteLegalDixRefuse() {
        assertFalse(parser.qteLegal(10));
    }

    @Test
    void qteLegalNegatifRefuse() {
        assertFalse(parser.qteLegal(-1));
    }

    @Test
    void parsePayloadValide() {
        Map<Fabricateur.TypeLunette, Integer> result = parser.parsePayload("CLAUDE:3;BANANA:2");
        assertEquals(3, result.get(Fabricateur.TypeLunette.CLAUDE));
        assertEquals(2, result.get(Fabricateur.TypeLunette.BANANA));
    }

    @Test
    void parsePayloadFormatInvalideLeveException() {
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> parser.parsePayload("CHATGPT:1:1"));
        assertEquals(ErreurMessage.FORMAT_INVALIDE.getMessage(), ex.getMessage());
    }

    @Test
    void parsePayloadTypeInconnuLeveException() {
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> parser.parsePayload("INCONNU:3"));
        assertEquals(ErreurMessage.TYPE_INCONNU.getMessage(), ex.getMessage());
    }

    @Test
    void parsePayloadQuantiteDixLeveException() {
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> parser.parsePayload("CLAUDE:10"));
        assertEquals(ErreurMessage.QUANTITE_INVALIDE.getMessage(), ex.getMessage());
    }

    @Test
    void parsePayloadQuantiteNegativeLeveException() {
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> parser.parsePayload("CLAUDE:-1"));
        assertEquals(ErreurMessage.QUANTITE_INVALIDE.getMessage(), ex.getMessage());
    }

    @Test
    void parsePayloadTotalZeroLeveException() {
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> parser.parsePayload("CHATGPT:0"));
        assertEquals(ErreurMessage.QUANTITE_TOTALE_INVALIDE.getMessage(), ex.getMessage());
    }

    @Test
    void serializeLunettesValide() {
        Fabricateur.Lunette lunette = new Fabricateur.Lunette(Fabricateur.TypeLunette.BANANA, "1234");

        String result = parser.serializeLunettes(List.of(lunette));
        assertEquals("BANANA:1234;", result);
    }

    @Test
    void serializeLunettesVide() {
        String result = parser.serializeLunettes(List.of());
        assertEquals("", result);
    }
}