import fr.smartglasses.frontend.controller.OrderController;
import fr.smartglasses.frontend.model.GlassesModel;
import fr.smartglasses.frontend.model.Order;
import fr.smartglasses.frontend.model.SerialPair;
import fr.smartglasses.frontend.service.ClientMqtt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderControllerTest {

    private OrderController orderController;
    private GlassesModel modelPro;
    private GlassesModel modelLite;

    @BeforeEach
    void setUp() {
        orderController = new OrderController();
        modelPro  = new GlassesModel("PRO",  "SmartGlasses Pro",  "499 €", "/img/pro.png",  "Bestseller", "Modèle professionnel");
        modelLite = new GlassesModel("LITE", "SmartGlasses Lite", "299 €", "/img/lite.png", "",            "Modèle entrée de gamme");
    }


    @Test
    void getCurrentOrder_estNullAuDepart() {
        assertNull(orderController.getCurrentOrder());
    }

    @Test
    void getInfosCommande_retourneMessageSiAucuneCommande() {
        assertEquals("Aucune commande en cours", orderController.getInfosCommande());
    }

    @Test
    void infosCommandeProperty_estVideeAuDepart() {
        assertTrue(orderController.infosCommandeProperty().get().isBlank());
    }

    @Test
    void createOrder_retourneUneCommandeAvecUnId() {
        Order order = orderController.createOrder(modelPro, 1);
        assertNotNull(order.getId());
        assertTrue(order.getId().startsWith("CMD-"));
    }

    @Test
    void createOrder_chaqueAppelGenereUnIdUnique() {
        Order order1 = orderController.createOrder(modelPro, 1);
        Order order2 = orderController.createOrder(modelPro, 1);
        assertNotEquals(order1.getId(), order2.getId());
    }

    @Test
    void createOrder_contientLeModelEtLaQuantite() {
        Order order = orderController.createOrder(modelPro, 3);
        assertTrue(order.getOrder().containsKey(modelPro));
        assertEquals(3, order.getOrder().get(modelPro));
    }

    @Test
    void addGlasses_creeUneCommandeSiAucuneEnCours() {
        orderController.addGlasses(modelPro, 2);
        assertNotNull(orderController.getCurrentOrder());
    }

    @Test
    void addGlasses_ajouteLeProduitDansLaCommandeExistante() {
        orderController.addGlasses(modelPro, 1);
        orderController.addGlasses(modelLite, 2);
        assertEquals(2, orderController.getCurrentOrder().getOrder().size());
    }

    @Test
    void addGlasses_neCreesPasDeNouvelleCommandeSiUneExisteDeja() {
        orderController.addGlasses(modelPro, 1);
        String idAvant = orderController.getCurrentOrder().getId();
        orderController.addGlasses(modelLite, 2);
        assertEquals(idAvant, orderController.getCurrentOrder().getId());
    }

    @Test
    void addGlasses_metAJourLaProperty() {
        orderController.addGlasses(modelPro, 2);
        String value = orderController.infosCommandeProperty().get();
        assertFalse(value.isBlank());
        assertTrue(value.contains("CMD-"));
    }

    @Test
    void resetOrder_metLaCommandeANull() {
        orderController.addGlasses(modelPro, 1);
        orderController.resetOrder();
        assertNull(orderController.getCurrentOrder());
    }

    @Test
    void resetOrder_metAJourLaProperty() {
        orderController.addGlasses(modelPro, 1);
        orderController.resetOrder();
        String value = orderController.infosCommandeProperty().get();
        assertTrue(value.isBlank() || value.equals("Aucune commande en cours"));
    }

    @Test
    void resetOrder_puisAddGlasses_creeUneNouvelleCommande() {
        orderController.addGlasses(modelPro, 1);
        String idAvant = orderController.getCurrentOrder().getId();
        orderController.resetOrder();
        orderController.addGlasses(modelLite, 3);
        assertNotEquals(idAvant, orderController.getCurrentOrder().getId());
    }

    @Test
    void getInfosCommande_contientLIdApresCreation() {
        orderController.addGlasses(modelPro, 1);
        assertTrue(orderController.getInfosCommande().contains("CMD-"));
    }

    @Test
    void refreshInfos_metAJourLaPropertyAvecLeContenuDeLaCommande() {
        orderController.addGlasses(modelPro, 2);
        orderController.refreshInfos();
        assertTrue(orderController.infosCommandeProperty().get().contains("PRO"));
    }

    @Test
    void getCatalogue_retourneUneListeNonVide() {
        assertFalse(orderController.getCatalogue().isEmpty());
    }

    @Test
    void getCatalogue_contientDesGlassesModel() {
        orderController.getCatalogue().forEach(m -> assertNotNull(m.code()));
    }

    @Test
    void startFabrication_stockeLesNumerosDeSerieDansLaCommande() throws Exception {
        orderController.addGlasses(modelPro, 2);

        try (MockedConstruction<ClientMqtt> mocked = mockConstruction(ClientMqtt.class,
                (mock, context) -> when(mock.passerCommande(anyString(), anyString()))
                        .thenReturn("PRO:SN-PRO-001;PRO:SN-PRO-002;"))) {

            orderController.startFabrication();

            List<SerialPair> serials = orderController.getCurrentOrder().getSerialNumbers();
            assertEquals(2, serials.size());
            assertEquals("SN-PRO-001", serials.get(0).numeroSerie());
            assertEquals("SN-PRO-002", serials.get(1).numeroSerie());
        }
    }

    @Test
    void startFabrication_appelleDisconnectApresLaCommande() throws Exception {
        orderController.addGlasses(modelPro, 1);

        try (MockedConstruction<ClientMqtt> mocked = mockConstruction(ClientMqtt.class,
                (mock, context) -> when(mock.passerCommande(anyString(), anyString()))
                        .thenReturn("PRO:SN-PRO-001;"))) {

            orderController.startFabrication();

            ClientMqtt mockClient = mocked.constructed().get(0);
            verify(mockClient, times(1)).disconnect();
        }
    }

    @Test
    void startFabrication_metAJourLaPropertyApresReception() throws Exception {
        orderController.addGlasses(modelPro, 1);

        try (MockedConstruction<ClientMqtt> mocked = mockConstruction(ClientMqtt.class,
                (mock, context) -> when(mock.passerCommande(anyString(), anyString()))
                        .thenReturn("PRO:SN-PRO-001;"))) {

            orderController.startFabrication();

            assertTrue(orderController.infosCommandeProperty().get().contains("SN-PRO-001"));
        }
    }

    @Test
    void startFabrication_construitLePayloadCorrectement() throws Exception {
        orderController.addGlasses(modelPro, 3);

        try (MockedConstruction<ClientMqtt> mocked = mockConstruction(ClientMqtt.class,
                (mock, context) -> when(mock.passerCommande(anyString(), anyString()))
                        .thenReturn("PRO:SN-PRO-001;PRO:SN-PRO-002;PRO:SN-PRO-003;"))) {

            orderController.startFabrication();

            // On vérifie que passerCommande a été appelé avec un payload contenant "PRO:3"
            ClientMqtt mockClient = mocked.constructed().get(0);
            verify(mockClient).passerCommande(anyString(), contains("PRO:3"));
        }
    }

    @Test
    void startFabrication_lanceRuntimeException_siServeurLeveUneException() throws Exception {
        orderController.addGlasses(modelPro, 1);

        try (MockedConstruction<ClientMqtt> mocked = mockConstruction(ClientMqtt.class,
                (mock, context) -> when(mock.passerCommande(anyString(), anyString()))
                        .thenThrow(new Exception("Timeout : pas de réponse du serveur")))) {

            RuntimeException ex = assertThrows(RuntimeException.class,
                    () -> orderController.startFabrication());

            assertTrue(ex.getMessage().contains("Timeout"));
        }
    }

    @Test
    void startFabrication_ignoreLesSerialsAvecFormatInvalide() throws Exception {
        orderController.addGlasses(modelPro, 1);

        // Le payload contient une entrée malformée "INVALIDE" sans ":"
        try (MockedConstruction<ClientMqtt> mocked = mockConstruction(ClientMqtt.class,
                (mock, context) -> when(mock.passerCommande(anyString(), anyString()))
                        .thenReturn("PRO:SN-PRO-001;INVALIDE;PRO:SN-PRO-002;"))) {

            orderController.startFabrication();

            // Seules les deux entrées valides doivent être enregistrées
            assertEquals(2, orderController.getCurrentOrder().getSerialNumbers().size());
        }
    }
}