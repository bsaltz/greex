# Greex README

Greex is a Java library that can be used to generate matches for regular expressions. It supports both random generation as well as complete generation. This can be used for creating test data, generating random tokens, or creating unit test cases.

## How to Use Greex

To use Greex, add it as a dependency with your favorite build tool, or download the JAR and the brics automata JAR and add it to your classpath. Greex is available on [Maven Central](http://mvnrepository.com/artifact/com.navigamez/greex/).

Once it's added to your classpath, you can use the `GreexGenerator` class to start generating matches for your regular expressions. For example:

```java
GreexGenerator generator = new GreexGenerator("(white|black)|((light|dark) )?(red|green|blue|gray)");
String match = generator.generateRandom();
System.out.println(match); // e.g. "dark red"
```

`generateRandom` does exactly what it says: it generates random matches based on the input regex for the generator. Similarly, you can also generate all of the matches for a given regular expression. For example:

```java
GreexGenerator generator = new GreexGenerator("(white|black)|((light|dark) )?(red|green|blue|gray)");
List<String> matches = generator.generateAll();
System.out.println(matches.size()); // "14"
```

To prevent heap and stack memory issues, you can also limit the number of characters in your generated matches. For example:

```java
GreexGenerator generator = new GreexGenerator("a*([bd])+c?");
Set<String> results = generator.generateAll(10);
System.out.println(results.size()); // "6098"
```

This generates all possible matches with 10 or fewer characters.

## Problems Greex Solves

Primarily, Greex is most useful for unit testing, but it can also be used for token, ID, or password generation.

### Unit Testing

Often times when working with regular expressions, it can be hard to get coverage of different matching values to ensure that downstream code behaves properly with many different kinds of matches. Greex can be used to generate a set of random matches (or all matches, when applicable) of a regular expression that can then be used to do unit testing on code.

Since it supports taking existing instances of `java.util.Random` or custom seed values, it can also be set up to consistently produce the same values when generating random matches so that unit tests can be stable.

### Value Generation

When generating values for a system, e.g. IDs or passwords, you can supply a regex that can be used to generate a random value that matches your requirements for the value's format. For example, to generate a securely random, 12-character, alphanumeric password:

```java
SecureRandom rand = ... ;
GreexGenerator generator = new GreexGenerator("[a-zA-Z0-9_]{12}");
String password = generator.generateRandom(rand);
```

The regex can be further tweaked to meet security requirements, readability requirements, etc.
