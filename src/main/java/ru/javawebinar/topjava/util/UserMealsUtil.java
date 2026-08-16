package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));


//        System.out.println(getAllDescriptions(meals));
//        System.out.println(countMealsWithCaloriesAbove(meals,500 ));
//        System.out.println(findMostCaloricMeal(meals));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with excess. Implement by cycles

        HashMap<LocalDate, Integer> caloriesByDate = new HashMap<>();
        List<UserMealWithExcess> meals1 = new ArrayList<>();

        for (UserMeal meal : meals) {
            LocalDate date = meal.getDateTime().toLocalDate();
            caloriesByDate.put(date, caloriesByDate.getOrDefault(date, 0) + meal.getCalories());
        }
        for (UserMeal meal : meals)
            if (TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                boolean excess = false;
                LocalDate date = meal.getDateTime().toLocalDate();
                if (caloriesByDate.get(date) > caloriesPerDay) {
                    excess = true;
                }
                meals1.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess));
            }

        return meals1;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO Implement by streams

        Map<LocalDate, Integer> caloriesByDate = meals.stream()
                .collect(Collectors.groupingBy(meal -> meal.getDateTime().toLocalDate(),
                        Collectors.summingInt(meal -> meal.getCalories())));

        return meals.stream()
                .filter(meal -> TimeUtil.isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> {
                    boolean excess = caloriesByDate.get(meal.getDateTime().toLocalDate()) > caloriesPerDay;
                    return new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excess);
                })
                .collect(Collectors.toList());
    }


//    public static List<String> getAllDescriptions(List<UserMeal> meals) {
//        return meals.stream()
//                .map(UserMeal::getDescription)
//                .collect(Collectors.toList());
//    }
//
//    public static long countMealsWithCaloriesAbove(List<UserMeal> meals, int caloriesThreshold){
//        return meals.stream()
//                .filter(meal -> meal.getCalories() > caloriesThreshold).count();
//    }
//    public static Optional<UserMeal> findMostCaloricMeal(List<UserMeal> meals){
//        return meals.stream()
//                .max(Comparator.comparingInt(UserMeal::getCalories));
//    }
}
