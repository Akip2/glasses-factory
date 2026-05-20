
import fr.smartglasses.frontend.model.GlassesModel;
import fr.smartglasses.frontend.model.Order;
import fr.smartglasses.frontend.model.SerialPair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {

    private Order order;
    private GlassesModel modelPro;
    private GlassesModel modelLite;

    @BeforeEach
    void setUp() {
        order = new Order("CMD-TEST01");
        modelPro  = new GlassesModel("PRO",  "SmartGlasses Pro",  "499 €", "/img/pro.png",  "Bestseller", "Modèle professionnel");
        modelLite = new GlassesModel("LITE", "SmartGlasses Lite", "299 €", "/img/lite.png", "",            "Modèle entrée de gamme");
    }


    @Test
    void getId_retourneLIdFourniALaCreation() {
        assertEquals("CMD-TEST01", order.getId());
    }

    @Test
    void addGlasses_ajouteLeProduitDansLaCommande() {
        order.addGlasses(modelPro, 2);
        assertTrue(order.getOrder().containsKey(modelPro));
        assertEquals(2, order.getOrder().get(modelPro));
    }

    @Test
    void addGlasses_remplaceLaQuantiteSiModelDejaPresent() {
        order.addGlasses(modelPro, 2);
        order.addGlasses(modelPro, 5);
        // la quantité doit être remplacée, pas cumulée
        assertEquals(5, order.getOrder().get(modelPro));
        assertEquals(1, order.getOrder().size());
    }

    @Test
    void addGlasses_peutAjouterPlusieursModelsDifferents() {
        order.addGlasses(modelPro, 1);
        order.addGlasses(modelLite, 3);
        assertEquals(2, order.getOrder().size());
        assertEquals(1, order.getOrder().get(modelPro));
        assertEquals(3, order.getOrder().get(modelLite));
    }

    @Test
    void addGlasses_avecQuantiteDeUn_estBienEnregistre() {
        order.addGlasses(modelPro, 1);
        assertEquals(1, order.getOrder().get(modelPro));
    }

    @Test
    void addGlasses_avecQuantiteMax_estBienEnregistre() {
        order.addGlasses(modelPro, 9);
        assertEquals(9, order.getOrder().get(modelPro));
    }

    @Test
    void getOrder_estVideeParDefaut() {
        assertTrue(order.getOrder().isEmpty());
    }

    @Test
    void getSerialNumbers_estVideParDefaut() {
        assertTrue(order.getSerialNumbers().isEmpty());
    }

    @Test
    void setSerialNumbers_stockeLesNumerosDeSerie() {
        List<SerialPair> serials = List.of(
                new SerialPair("PRO", "SN-PRO-001"),
                new SerialPair("PRO", "SN-PRO-002")
        );
        order.setSerialNumbers(serials);
        assertEquals(2, order.getSerialNumbers().size());
        assertEquals("SN-PRO-001", order.getSerialNumbers().get(0).numeroSerie());
        assertEquals("SN-PRO-002", order.getSerialNumbers().get(1).numeroSerie());
    }

    @Test
    void setSerialNumbers_avecNull_retourneListeVideSansException() {
        order.setSerialNumbers(null);
        assertNotNull(order.getSerialNumbers());
        assertTrue(order.getSerialNumbers().isEmpty());
    }

    @Test
    void setSerialNumbers_remplaceLesAnciensNumeros() {
        order.setSerialNumbers(List.of(new SerialPair("PRO", "SN-PRO-001")));
        order.setSerialNumbers(List.of(new SerialPair("LITE", "SN-LITE-099")));
        assertEquals(1, order.getSerialNumbers().size());
        assertEquals("SN-LITE-099", order.getSerialNumbers().getFirst().numeroSerie());
    }

    @Test
    void toString_contientLIdDeLaCommande() {
        assertTrue(order.toString().contains("CMD-TEST01"));
    }

    @Test
    void toString_contientLeCodeDuModelApresAjout() {
        order.addGlasses(modelPro, 1);
        assertTrue(order.toString().contains("PRO"));
    }

    @Test
    void toString_contientLaQuantite() {
        order.addGlasses(modelPro, 3);
        assertTrue(order.toString().contains("3"));
    }

    @Test
    void toString_contientLesNumerosDeSerieSiPresents() {
        order.addGlasses(modelPro, 1);
        order.setSerialNumbers(List.of(new SerialPair("PRO", "SN-PRO-001")));
        assertTrue(order.toString().contains("SN-PRO-001"));
    }

    @Test
    void toString_neMentionnePasLesSerialsSiListeVide() {
        order.addGlasses(modelPro, 1);
        assertFalse(order.toString().contains("Numéros de série"));
    }
}