package be.kuleuven.foodrestservice.domain;

import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class MealsRepository {
    // map: id -> meal
    private static final Map<String, Meal> meals = new HashMap<>();

    @PostConstruct
    public void initData() {

        Meal a = new Meal();
        a.setId("5268203c-de76-4921-a3e3-439db69c462a");
        a.setName("Steak");
        a.setDescription("Steak with fries");
        a.setMealType(MealType.MEAT);
        a.setKcal(1100);
        a.setPrice((10.00));

        meals.put(a.getId(), a);

        Meal b = new Meal();
        b.setId("4237681a-441f-47fc-a747-8e0169bacea1");
        b.setName("Portobello");
        b.setDescription("Portobello Mushroom Burger");
        b.setMealType(MealType.VEGAN);
        b.setKcal(637);
        b.setPrice((7.00));

        meals.put(b.getId(), b);

        Meal c = new Meal();
        c.setId("cfd1601f-29a0-485d-8d21-7607ec0340c8");
        c.setName("Fish and Chips");
        c.setDescription("Fried fish with chips");
        c.setMealType(MealType.FISH);
        c.setKcal(950);
        c.setPrice(5.00);

        meals.put(c.getId(), c);
    }

    public Optional<Meal> findMeal(String id) {
        Assert.notNull(id, "The meal id must not be null");
        Meal meal = meals.get(id);
        return Optional.ofNullable(meal);
    }

    public Collection<Meal> getAllMeal() {
        return meals.values();
    }

    public Optional<Meal> findCheapestMeal() {
        if (meals.isEmpty()) {
            return Optional.empty();
        }

        return meals.values().stream()
                .min(Comparator.comparing(Meal::getPrice));
    }

    public Optional<Meal> findLargestMeal() {
        return meals.values().stream()
                .max(Comparator.comparing(Meal::getKcal));
    }

    public Meal addMeal(Meal meal) {
        Assert.notNull(meal, "Meal can't be null");

        meals.put(meal.getId(), meal);
        return meal;
    }

    public Optional<Meal> updateMeal(String id, Meal updatedMeal) {
        Assert.notNull(id, "Meal id must not be null");
        Assert.notNull(updatedMeal, "Meal must not be null");

        if (!meals.containsKey(id)) {
            return Optional.empty();
        }

        updatedMeal.setId(id);
        meals.put(id, updatedMeal);
        return Optional.of(updatedMeal);
    }

    public boolean deleteMeal(String id) {
        Assert.notNull(id, "Meal id must not be null");
        return meals.remove(id) != null;
    }

    public OrderConfirmation addOrder(Order order) {
        Assert.notNull(order, "Order must not be null");
        Assert.notNull(order.getAddress(), "Address must not be null");
        Assert.notNull(order.getMealIds(), "Meal ids must not be null");

        List<Meal> orderedMeals = order.getMealIds().stream()
                .map(id -> Optional.ofNullable(meals.get(id))
                        .orElseThrow(() -> new NoSuchElementException("Meal not found: " + id)))
                .collect(Collectors.toList());

        double total = orderedMeals.stream()
                .mapToDouble(Meal::getPrice)
                .sum();

        OrderConfirmation confirmation = new OrderConfirmation();
        confirmation.setMessage("Order accepted");
        confirmation.setAddress(order.getAddress());
        confirmation.setOrderedMealIds(new ArrayList<>(order.getMealIds()));
        confirmation.setTotalPrice(total);

        return confirmation;
    }
}
