package com.example.springsoap;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;


import io.foodmenu.gt.webservice.*;


import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class MealRepository {
    private static final Map<String, Meal> meals = new HashMap<String, Meal>();

    @PostConstruct
    public void initData() {

        Meal a = new Meal();
        a.setName("Steak");
        a.setDescription("Steak with fries");
        a.setMealtype(Mealtype.MEAT);
        a.setKcal(1100);
        a.setPrice(24.5f);


        meals.put(a.getName(), a);

        Meal b = new Meal();
        b.setName("Portobello");
        b.setDescription("Portobello Mushroom Burger");
        b.setMealtype(Mealtype.VEGAN);
        b.setKcal(637);
        b.setPrice(16.0f);


        meals.put(b.getName(), b);

        Meal c = new Meal();
        c.setName("Fish and Chips");
        c.setDescription("Fried fish with chips");
        c.setMealtype(Mealtype.FISH);
        c.setKcal(950);
        c.setPrice(19.5f);

        meals.put(c.getName(), c);
    }

    public Meal findMeal(String name) {
        Assert.notNull(name, "The meal's code must not be null");
        return meals.get(name);
    }

    public Meal findBiggestMeal() {

        if (meals == null) return null;
        if (meals.size() == 0) return null;

        var values = meals.values();
        return values.stream().max(Comparator.comparing(Meal::getKcal)).orElseThrow(NoSuchElementException::new);

    }

    public Meal findCheapestMeal() {
        if (meals == null) return null;
        if (meals.size() == 0) return null;

        var values = meals.values();
        return values.stream().min(Comparator.comparing(Meal::getPrice)).orElseThrow(NoSuchElementException::new);
    }

    public OrderConfirmation addOrder(Order order) {
        Assert.notNull(order, "Order must not be null");

        OrderConfirmation confirmation = new OrderConfirmation();

        if (order.getAddress() == null || order.getAddress().isBlank()) {
            confirmation.setSuccess(false);
            confirmation.setMessage("Address must not be empty");
            confirmation.setTotalPrice(0.0f);
            confirmation.setMealCount(0);
            confirmation.setAddress("");
            return confirmation;
        }

        float total = 0.0f;
        int count = 0;

        for (String mealName : order.getMealName()) {
            Meal meal = meals.get(mealName);

            if (meal == null) {
                confirmation.setSuccess(false);
                confirmation.setMessage("Meal not found: " + mealName);
                confirmation.setTotalPrice(0.0f);
                confirmation.setMealCount(0);
                confirmation.setAddress(order.getAddress());
                return confirmation;
            }

            total += meal.getPrice();
            count++;
        }

        confirmation.setSuccess(true);
        confirmation.setMessage("Order placed successfully");
        confirmation.setTotalPrice(total);
        confirmation.setMealCount(count);
        confirmation.setAddress(order.getAddress());

        return confirmation;
    }


}