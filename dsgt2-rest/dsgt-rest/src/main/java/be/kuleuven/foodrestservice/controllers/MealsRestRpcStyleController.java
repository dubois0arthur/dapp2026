package be.kuleuven.foodrestservice.controllers;

import be.kuleuven.foodrestservice.domain.Meal;
import be.kuleuven.foodrestservice.domain.MealsRepository;
import be.kuleuven.foodrestservice.domain.Order;
import be.kuleuven.foodrestservice.domain.OrderConfirmation;
import be.kuleuven.foodrestservice.exceptions.MealNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;

@RestController
public class MealsRestRpcStyleController {

    private final MealsRepository mealsRepository;

    @Autowired
    MealsRestRpcStyleController(MealsRepository mealsRepository) {
        this.mealsRepository = mealsRepository;
    }

    @GetMapping("/restrpc/meals/{id}")
    Meal getMealById(@PathVariable String id) {
        Optional<Meal> meal = mealsRepository.findMeal(id);

        return meal.orElseThrow(() -> new MealNotFoundException(id));
    }

    @GetMapping("/restrpc/meals")
    Collection<Meal> getMeals() {
        return mealsRepository.getAllMeal();
    }

    @GetMapping("/restrpc/meals/cheapest")
    Meal getCheapestMeal() {
        return mealsRepository.findCheapestMeal().orElseThrow(() -> new RuntimeException("No meals found"));
    }

    @GetMapping("/restrpc/meals/largest")
    public Meal getLargestMeal() {
        return mealsRepository.findLargestMeal()
                .orElseThrow(() -> new RuntimeException("No meals found"));
    }

    @PostMapping("/restrpc/meals")
    public ResponseEntity<Meal> addMeal(@RequestBody Meal meal) {
        Meal created = mealsRepository.addMeal(meal);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/restrpc/meals/{id}")
    public ResponseEntity<Meal> updateMeal(@PathVariable String id, @RequestBody Meal meal) {
        return mealsRepository.updateMeal(id, meal)
                .map(updated -> new ResponseEntity<>(updated, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/restrpc/meals/{id}")
    public ResponseEntity<Void> deleteMeal(@PathVariable String id) {
        boolean deleted = mealsRepository.deleteMeal(id);
        return deleted
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/restrpc/orders")
    public ResponseEntity<OrderConfirmation> addOrder(@RequestBody Order order) {
        OrderConfirmation confirmation = mealsRepository.addOrder(order);
        return new ResponseEntity<>(confirmation, HttpStatus.CREATED);
    }
}
