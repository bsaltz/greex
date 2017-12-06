package com.navigamez.greex;

import org.junit.Test;

import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GreexGeneratorTest {

    @Test
    public void generateRandom_1() {
        String regex = "(white|black)|((light|dark) )?(red|green|blue|gray)";
        int uniqueStrings = 14;
        Pattern pattern = Pattern.compile(regex);
        GreexGenerator generator = new GreexGenerator(regex);
        Random random = new Random(879870238957089L);
        Set<String> results = new TreeSet<String>();
        int generations = 100;
        for (int i = 0; i < generations; i++) {
            results.add(generator.generateRandom(random));
        }
        System.out.println("Unique strings: " + results.size());
        System.out.printf("Coverage: %%%.2f\n", results.size() * 100.0 / uniqueStrings);
        for (String s : results) {
            System.out.println(s);
            assertTrue(pattern.matcher(s).matches());
        }
    }

    @Test
    public void generateRandom_2() {
        String regex = "a*b+c?";
        Pattern pattern = Pattern.compile(regex);
        GreexGenerator generator = new GreexGenerator(regex);
        Random random = new Random(879870238957089L);
        Set<String> results = new TreeSet<String>();
        int generations = 200;
        for (int i = 0; i < generations; i++) {
            results.add(generator.generateRandom(random));
        }
        System.out.println("Unique strings: " + results.size());
        System.out.printf("Uniqueness: %%%.2f\n", results.size() * 100.0 / generations);
        for (String s : results) {
            System.out.println(s);
            assertTrue(pattern.matcher(s).matches());
        }
    }

    @Test
    public void generateRandom_3() {
        String regex = "a*([bd])+c?";
        Pattern pattern = Pattern.compile(regex);
        GreexGenerator generator = new GreexGenerator(regex);
        Random random = new Random(879870238957089L);
        Set<String> results = new TreeSet<String>();
        int generations = 200;
        for (int i = 0; i < generations; i++) {
            results.add(generator.generateRandom(random));
        }
        System.out.println("Unique strings: " + results.size());
        System.out.printf("Uniqueness: %%%.2f\n", results.size() * 100.0 / generations);
        for (String s : results) {
            System.out.println(s);
            assertTrue(pattern.matcher(s).matches());
        }
    }

    @Test
    public void generateRandom_4() {
        String regex = "a*([bd])+c?";
        Pattern pattern = Pattern.compile(regex);
        GreexGenerator generator = new GreexGenerator(regex);
        String s = generator.generateRandom();
        System.out.println(s);
        assertTrue(pattern.matcher(s).matches());
    }

    @Test
    public void generateRandom_5() {
        String regex = "a*([bd])+c?";
        Pattern pattern = Pattern.compile(regex);
        GreexGenerator generator = new GreexGenerator(regex);
        long seed = 1989465435487498L;
        String s1 = generator.generateRandom(seed);
        String s2 = generator.generateRandom(seed);
        System.out.println(s1);
        System.out.println(s2);
        assertTrue(pattern.matcher(s1).matches());
        assertTrue(pattern.matcher(s2).matches());
        assertEquals("abc", s1);
        assertEquals("abc", s2);
    }

    @Test
    public void generateAll_1() {
        String regex = "(white|black)|((light|dark) )?(red|green|blue|gray)";
        int uniqueStrings = 14;
        Pattern pattern = Pattern.compile(regex);
        GreexGenerator generator = new GreexGenerator(regex);
        Set<String> results = new TreeSet<String>();
        results.addAll(generator.generateAll());
        System.out.println("Unique strings: " + results.size());
        System.out.printf("Coverage: %%%.2f\n", results.size() * 100.0 / uniqueStrings);
        for (String s : results) {
            System.out.println(s);
            assertTrue(pattern.matcher(s).matches());
        }
        assertEquals(uniqueStrings, results.size());
    }

    @Test
    public void generateAll_2() {
        String regex = "a*([bd])+c?";
        int uniqueStrings = 6098;
        Pattern pattern = Pattern.compile(regex);
        GreexGenerator generator = new GreexGenerator(regex);
        Set<String> results = generator.generateAll(10);
        System.out.println("Unique strings: " + results.size());
        System.out.printf("Coverage: %%%.2f\n", results.size() * 100.0 / uniqueStrings);
        assertEquals(uniqueStrings, results.size());
        for (String s : results) {
            assertTrue(pattern.matcher(s).matches());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void construct() {
        new GreexGenerator(null);
    }
}