package com.navigamez.greex;

import org.junit.Test;

import java.util.*;
import java.util.concurrent.TimeUnit;
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
    public void generateRandom_6() {
        String regex = "a*([bd])+c?";
        Pattern pattern = Pattern.compile(regex);
        GreexGenerator generator = new GreexGenerator(regex);
        int count = 50;
        List<String> s1s = generator.generateRandom(1989465435487498L, count);
        List<String> s2s = generator.generateRandom(1989465435487498L, count);
        assertEquals(s1s, s2s);
        assertEquals(count, s1s.size());
        assertEquals(count, new HashSet<String>(s1s).size());
        for (String s1 : s1s) {
            assertTrue(pattern.matcher(s1).matches());
        }
    }

    @Test
    public void generateRandom_7() {
        String regex = "a*([bd])+c?";
        Pattern pattern = Pattern.compile(regex);
        GreexGenerator generator = new GreexGenerator(regex);
        int count = 50;
        List<String> s1s = generator.generateRandom(1989465435487498L, count, false);
        List<String> s2s = generator.generateRandom(1989465435487498L, count, false);
        assertEquals(s1s, s2s);
        assertEquals(count, s1s.size());
        Set<String> uniqueS1s = new HashSet<String>(s1s);
        assertTrue(count > uniqueS1s.size());
        for (String s1 : uniqueS1s) {
            assertTrue(pattern.matcher(s1).matches());
        }
    }

    @Test
    public void generateRandom_8() {
        // Uses regex for which it is impossible to generate 50 matches
        String regex = "a";
        Pattern pattern = Pattern.compile(regex);
        GreexGenerator generator = new GreexGenerator(regex);
        int count = 50;
        long start1 = System.currentTimeMillis();
        List<String> s1s = generator.generateRandom(1989465435487498L, count, true, 2, TimeUnit.SECONDS);
        long end1 = System.currentTimeMillis();
        long start2 = System.currentTimeMillis();
        generator.generateRandom(1989465435487498L, count, true, 2, TimeUnit.SECONDS);
        long end2 = System.currentTimeMillis();
        Set<String> uniqueS1s = new HashSet<String>(s1s);
        assertTrue(count > uniqueS1s.size());
        for (String s1 : uniqueS1s) {
            assertTrue(pattern.matcher(s1).matches());
        }
        assertTrue(end1 - start1 >= 2000);
        assertTrue(end2 - start2 >= 2000);
    }

    @Test
    public void generateRandom_9() {
        String regex = "a*([bd])+c?";
        Pattern pattern = Pattern.compile(regex);
        GreexGenerator generator = new GreexGenerator(regex);
        int count = 50;
        // Verify behavior is the same
        List<String> s1s = generator.generateRandom(1989465435487498L, count, false, 1, null);
        List<String> s2s = generator.generateRandom(1989465435487498L, count, false, -1, TimeUnit.MILLISECONDS);
        assertEquals(s1s, s2s);
        assertEquals(count, s1s.size());
        Set<String> uniqueS1s = new HashSet<String>(s1s);
        assertTrue(count > uniqueS1s.size());
        for (String s1 : uniqueS1s) {
            assertTrue(pattern.matcher(s1).matches());
        }
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

    @Test
    public void generateAllLimited_1() {
        String regex = "(white|black)|((light|dark) )?(red|green|blue|gray)";
        int uniqueStrings = 14;
        int maxCount = 5;
        Pattern pattern = Pattern.compile(regex);
        GreexGenerator generator = new GreexGenerator(regex);
        Set<String> results = new TreeSet<String>();
        results.addAll(generator.generateAllLimited(maxCount));
        System.out.println("Unique strings: " + results.size());
        System.out.printf("Coverage: %%%.2f\n", results.size() * 100.0 / uniqueStrings);
        for (String s : results) {
            System.out.println(s);
            assertTrue(pattern.matcher(s).matches());
        }
        assertEquals(maxCount, results.size());
    }

    @Test
    public void generateAllLimited_2() {
        String regex = "a*([bd])+c?";
        int uniqueStrings = 6098;
        int maxCount = 1000;
        Pattern pattern = Pattern.compile(regex);
        GreexGenerator generator = new GreexGenerator(regex);
        Set<String> results = generator.generateAllLimited(maxCount, 10);
        System.out.println("Unique strings: " + results.size());
        System.out.printf("Coverage: %%%.2f\n", results.size() * 100.0 / uniqueStrings);
        assertEquals(maxCount, results.size());
        for (String s : results) {
            assertTrue(pattern.matcher(s).matches());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void construct() {
        new GreexGenerator(null);
    }
}