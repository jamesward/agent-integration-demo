package com.jamesward.internal;

import org.springframework.ai.tool.annotation.Tool;

import java.util.List;

public class ManyTools {

    @Tool(name = "add", description = "Adds two numbers")
    public Integer add(Integer a, Integer b) {
        return a + b;
    }

    @Tool(name = "subtract", description = "Subtracts two numbers")
    public Integer subtract(Integer a, Integer b) {
        return a - b;
    }

    @Tool(name = "multiply", description = "Multiplies two numbers")
    public Integer multiply(Integer a, Integer b) {
        return a * b;
    }

    @Tool(name = "divide", description = "Divides two numbers")
    public Integer divide(Integer a, Integer b) {
        return a / b;
    }

    @Tool(name = "currentdate", description = "Returns the current date")
    public String currentdate() {
        return java.time.LocalDate.now().toString();
    }

    @Tool(name = "currenttime", description = "Returns the current time")
    public String currenttime() {
        return java.time.LocalTime.now().toString();
    }

    @Tool(name = "currentdatetime", description = "Returns the current date and time")
    public String currentdatetime() {
        return java.time.LocalDateTime.now().toString();
    }

    @Tool(name = "uppercase", description = "Converts a string to uppercase")
    public String uppercase(String input) {
        return input.toUpperCase();
    }

    @Tool(name = "lowercase", description = "Converts a string to lowercase")
    public String lowercase(String input) {
        return input.toLowerCase();
    }

    @Tool(name = "reverse", description = "Reverses a string")
    public String reverse(String input) {
        return new StringBuilder(input).reverse().toString();
    }

    @Tool(name = "repeat", description = "Repeats a string a specified number of times")
    public String repeat(String input, Integer times) {
        return String.join("", java.util.Collections.nCopies(times, input));
    }

    @Tool(name = "trim", description = "Trims whitespace from a string")
    public String trim(String input) {
        return input.trim();
    }

    @Tool(name = "capitalize", description = "Capitalizes the first letter of a string")
    public String capitalize(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    @Tool(name = "sum", description = "Sums a list of numbers")
    public Integer sum(List<Integer> numbers) {
        return numbers.stream().reduce(0, Integer::sum);
    }

    @Tool(name = "average", description = "Calculates the average of a list of numbers")
    public Double average(List<Integer> numbers) {
        return numbers.stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }

    @Tool(name = "random", description = "Generates a random number between a minimum and maximum value")
    public Integer random(Integer min, Integer max) {
        return (int) (Math.random() * (max - min + 1)) + min;
    }

    @Tool(name = "randomstring", description = "Generates a random string of a specified length")
    public String randomstring(Integer length) {
        return java.util.UUID.randomUUID().toString().substring(0, length);
    }

    @Tool(name = "randomboolean", description = "Generates a random boolean value")
    public Boolean randomboolean() {
        return Math.random() < 0.5;
    }

    @Tool(name = "shuffle", description = "Shuffles a list of items")
    public <T> List<T> shuffle(List<T> items) {
        java.util.Collections.shuffle(items);
        return items;
    }

    @Tool(name = "sort", description = "Sorts a list of items")
    public <T extends Comparable<T>> List<T> sort(List<T> items) {
        java.util.Collections.sort(items);
        return items;
    }

    @Tool(name = "reverselist", description = "Reverses a list of items")
    public <T> List<T> reverse(List<T> items) {
        java.util.Collections.reverse(items);
        return items;
    }

}
