package fr.smartglasses.frontend.model;

import java.util.List;

/**
 * Modèle de lunettes du catalogue
 *
 * @param code identifiant du modèle
 * @param name nom du modèle
 * @param description courte description affichée dans le catalogue
 * @param price prix
 * @param imagePath chemin vers l'image
 * @param badge badge affiché sur la carte
 */
public record GlassesModel(
        String code,
        String name,
        String description,
        String price,
        String imagePath,
        String badge
) {

    // Liste de tous les modèles disponibles à la commande
    public static final List<GlassesModel> CATALOGUE = List.of(
            new GlassesModel(
                    "BANANA",
                    "Bananaaaa",
                    "Design iconique des annees 50, parfait pour un look vintage et decontracte.",
                    "89.99 EUR",
                    "/images/banana.png",
                    "Nouveau"
            ),
            new GlassesModel(
                    "CHATGPT",
                    "BlaBlaBla",
                    "Lunettes style aviateur avec verres polarises et monture en metal dore.",
                    "74.99 EUR",
                    "/images/chatgpt.png",
                    ""
            ),
            new GlassesModel(
                    "LE_CHAT",
                    "Miaousse",
                    "Lunettes de vue sophistiquees avec monture fine et design contemporain.",
                    "129.99 EUR",
                    "/images/le_chat.png",
                    "-10%"
            ),
            new GlassesModel(
                    "CLAUDE",
                    "Claude",
                    "Monture ultra-legere et resistante, ideale pour les activites sportives.",
                    "99.99 EUR",
                    "/images/claude.png",
                    "Bestseller"
            )
    );
}
