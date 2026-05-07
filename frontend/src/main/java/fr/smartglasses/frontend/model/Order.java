package fr.smartglasses.frontend.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Order {

    private final String id;
    private final GlassesModel model;
    private final List<SerialNumber> serialNumbers = new ArrayList<>();
    private OrderStatus status = OrderStatus.CREATED;

    public Order(String id, GlassesModel model) {
        this.id = id;
        this.model = model;
    }

    public String getId() {
        return id;
    }

    public GlassesModel getModel() {
        return model;
    }

    public List<SerialNumber> getSerialNumbers() {
        return Collections.unmodifiableList(serialNumbers);
    }

    public void addSerialNumber(SerialNumber serialNumber) {
        serialNumbers.add(serialNumber);
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void startFabrication() {
        status = OrderStatus.IN_PROGRESS;
    }

    public void complete() {
        status = OrderStatus.COMPLETED;
    }

    public boolean isCompleted() {
        return status == OrderStatus.COMPLETED;
    }
}
