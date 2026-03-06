package be.kuleuven.foodrestservice.domain;

import java.util.List;

public class OrderConfirmation {
    private String message;
    private String address;
    private List<String> orderedMealIds;
    private double totalPrice;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getOrderedMealIds() {
        return orderedMealIds;
    }

    public void setOrderedMealIds(List<String> orderedMealIds) {
        this.orderedMealIds = orderedMealIds;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}