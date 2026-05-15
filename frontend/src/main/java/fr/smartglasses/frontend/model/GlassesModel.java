package fr.smartglasses.frontend.model;

public record GlassesModel(
        String code,
        String name,
        String description,
        String price,
        String imagePath,
        String badge
) {
}
