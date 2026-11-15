import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * INTERVIEW PREP - Modern Java (9 to 21) for 7 years experience.
 *
 * Needs JDK 21 to compile and run:
 *     javac WitchJava21Prep.java
 *     java WitchJava21Prep
 *
 * Interviewers at WITCH companies usually ask:
 *   "What is new after Java 8?"  and  "Have you used records / sealed / virtual threads?"
 * Each topic below: what it is, a small example, and why it is useful.
 *
 * QUICK TIMELINE (LTS = Long Term Support, the versions companies actually use)
 *   Java 8  (LTS) : lambda, Stream API, Optional, default methods, java.time
 *   Java 9        : List.of / Set.of / Map.of, takeWhile / dropWhile, modules
 *   Java 10       : var (local variable type inference)
 *   Java 11 (LTS) : String isBlank / strip / repeat / lines, Predicate.not, HttpClient
 *   Java 14       : switch expressions (arrow syntax, yield)
 *   Java 15       : text blocks  """ ... """
 *   Java 16       : records, instanceof pattern, Stream.toList(), mapMulti
 *   Java 17 (LTS) : sealed classes and interfaces
 *   Java 21 (LTS) : virtual threads, pattern matching for switch, record patterns,
 *                   sequenced collections (getFirst / getLast / reversed)
 */
public class WitchJava21Prep {

    // =====================================================================
    // 1. RECORDS  (Java 16)
    // A record is a short way to write an immutable data class.
    // The compiler writes: constructor, getters (name(), not getName()),
    // equals, hashCode and toString for you.
    // Interviewer may ask: "Record vs Lombok @Value?"
    //   -> Record is built into the language, no library needed. Records are
    //      final, fields are final, and they cannot extend another class
    //      (they can implement interfaces).
    // =====================================================================

    record Employee(int id, String name, String department, double salary) {

        // "Compact constructor": runs before the fields are set. Good for validation.
        Employee {
            if (salary < 0) {
                throw new IllegalArgumentException("Salary cannot be negative");
            }
        }

        // Records can have normal methods too.
        String initials() {
            return name.substring(0, 1).toUpperCase();
        }
    }

    static void recordsDemo() {
        Employee amit = new Employee(1, "Amit", "IT", 90000);
        Employee same = new Employee(1, "Amit", "IT", 90000);

        print("1 record toString", amit);                    // Employee[id=1, name=Amit, ...]
        print("1 getter", amit.name());                      // no "get" prefix
        print("1 equals (same values)", amit.equals(same));  // true - compares all fields
        print("1 initials()", amit.initials());
    }

    // =====================================================================
    // 2. var  (Java 10)
    // The compiler works out the type from the right side. Only for LOCAL variables.
    // Use it when the type is obvious. Do not use it when it hides the type.
    // =====================================================================

    static void varDemo() {
        var names = new ArrayList<String>();      // compiler knows: ArrayList<String>
        names.add("Amit");
        names.add("Neha");
        for (var name : names) {                  // works in for loops too
            print("2 var loop", name);
        }
        // var cannot be used for fields, method parameters or return types.
    }

    // =====================================================================
    // 3. TEXT BLOCKS  (Java 15)
    // Write multi-line strings without \n and \" escapes.
    // =====================================================================

    static void textBlockDemo() {
        String json = """
                {
                  "name": "Amit",
                  "department": "IT"
                }
                """;
        print("3 text block", json.trim());
    }

    // =====================================================================
    // 4. SWITCH EXPRESSIONS  (Java 14)
    // Old switch: needs break, easy to forget, cannot return a value.
    // New switch: arrow syntax, no fall-through, returns a value.
    // =====================================================================

    enum Day { MON, TUE, WED, THU, FRI, SAT, SUN }

    static String dayType(Day day) {
        return switch (day) {
            case SAT, SUN -> "Weekend";
            case MON, TUE, WED, THU, FRI -> "Weekday";
            // No default needed for an enum when every value is covered.
        };
    }

    static int daysInMonth(int month) {
        return switch (month) {
            case 2 -> 28;
            case 4, 6, 9, 11 -> 30;
            default -> {
                if (month < 1 || month > 12) {
                    throw new IllegalArgumentException("Bad month: " + month);
                }
                yield 31;       // "yield" returns a value from a block
            }
        };
    }

    // =====================================================================
    // 5. PATTERN MATCHING FOR instanceof  (Java 16)
    // Old: if (obj instanceof String) { String s = (String) obj; ... }
    // New: the cast is done for you.
    // =====================================================================

    static String instanceOfDemo(Object obj) {
        if (obj instanceof String text && !text.isEmpty()) {
            return "String with length " + text.length();
        }
        return "not a non-empty String";
    }

    // =====================================================================
    // 6. SEALED TYPES + PATTERN MATCHING FOR switch + RECORD PATTERNS
    //    (sealed: Java 17, pattern switch and record patterns: Java 21)
    //
    // sealed   : you decide EXACTLY which classes may implement the interface.
    // Benefit  : the compiler knows every possible type, so a switch over a
    //            sealed type needs no default and the compiler warns you if
    //            a new type is added and a switch is not updated.
    // =====================================================================

    sealed interface Shape permits Circle, Rectangle, Triangle { }

    record Circle(double radius) implements Shape { }

    record Rectangle(double width, double height) implements Shape { }

    record Triangle(double base, double height) implements Shape { }

    static double area(Shape shape) {
        return switch (shape) {
            case Circle c -> Math.PI * c.radius() * c.radius();
            case Rectangle r -> r.width() * r.height();
            case Triangle t -> 0.5 * t.base() * t.height();
            // no default - the compiler checks that all Shape types are covered
        };
    }

    // Record pattern: take a record apart directly inside the pattern.
    static double areaWithRecordPattern(Shape shape) {
        return switch (shape) {
            case Circle(double radius) -> Math.PI * radius * radius;
            case Rectangle(double width, double height) -> width * height;
            case Triangle(double base, double height) -> 0.5 * base * height;
        };
    }

    // Switch on ANY object: type patterns, "when" guards, and null handling.
    static String describe(Object obj) {
        return switch (obj) {
            case null -> "null value";                                  // null is allowed now
            case Integer i when i > 100 -> "big integer " + i;          // "when" = extra condition
            case Integer i -> "integer " + i;
            case String s when s.isBlank() -> "blank string";
            case String s -> "string of length " + s.length();
            case Circle(double radius) -> "circle with radius " + radius;
            case int[] array -> "int array of size " + array.length;
            default -> "something else";
        };
    }

    // Nested record patterns
    record Point(int x, int y) { }

    record Line(Point start, Point end) { }

    static String lineInfo(Object obj) {
        if (obj instanceof Line(Point(var x1, var y1), Point(var x2, var y2))) {
            return "line from (" + x1 + "," + y1 + ") to (" + x2 + "," + y2 + ")";
        }
        return "not a line";
    }

    static void patternDemo() {
        print("6 area(circle r=2)", area(new Circle(2)));
        print("6 area(rectangle 3x4)", area(new Rectangle(3, 4)));
        print("6 area record pattern", areaWithRecordPattern(new Triangle(4, 5)));
        print("6 describe(null)", describe(null));
        print("6 describe(500)", describe(500));
        print("6 describe(7)", describe(7));
        print("6 describe(\"  \")", describe("  "));
        print("6 describe(\"hello\")", describe("hello"));
        print("6 describe(new int[3])", describe(new int[3]));
        print("6 lineInfo", lineInfo(new Line(new Point(0, 0), new Point(3, 4))));
    }

    // =====================================================================
    // 7. SEQUENCED COLLECTIONS  (Java 21)
    // Before: getting the last element of a List was list.get(list.size() - 1),
    //         and a LinkedHashSet had no way to get its first/last element.
    // Now   : List, Deque, LinkedHashSet, LinkedHashMap, SortedSet ... all have
    //         getFirst(), getLast(), addFirst(), addLast(), removeFirst(),
    //         removeLast() and reversed().
    // =====================================================================

    static void sequencedDemo() {
        List<Integer> list = new ArrayList<>(List.of(10, 20, 30));
        print("7 getFirst / getLast", list.getFirst() + " / " + list.getLast());   // 10 / 30
        list.addFirst(5);
        list.removeLast();
        print("7 after addFirst(5), removeLast()", list);                           // [5, 10, 20]
        print("7 reversed view", list.reversed());                                  // [20, 10, 5]

        LinkedHashSet<String> set = new LinkedHashSet<>(List.of("a", "b", "c"));
        print("7 set first / last", set.getFirst() + " / " + set.getLast());

        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        map.put("one", 1);
        map.put("two", 2);
        map.putFirst("zero", 0);                                                    // new in 21
        print("7 map firstEntry / lastEntry", map.firstEntry() + " / " + map.lastEntry());
    }

    // =====================================================================
    // 8. VIRTUAL THREADS  (Java 21)
    // A normal (platform) thread = one OS thread. Heavy: about 1 MB of stack,
    // and you can only have a few thousand.
    // A virtual thread is managed by the JVM. Very light: you can create
    // MILLIONS. When it blocks (DB call, HTTP call, sleep), the JVM parks it and
    // lets the OS thread run something else.
    //
    // Use for: many tasks that WAIT (I/O, REST calls, database).
    // Do not use for: heavy CPU work (it will not be faster).
    // Do not pool virtual threads - create one per task.
    // Interviewer may ask: "Virtual threads vs reactive (WebFlux)?"
    //   -> Virtual threads keep the simple blocking code style and still scale.
    // =====================================================================

    static void virtualThreadDemo() throws InterruptedException, ExecutionException {
        // (a) start a single virtual thread
        Thread thread = Thread.ofVirtual().name("my-virtual-thread").start(
                () -> print("8 running in", Thread.currentThread().getName()));
        thread.join();      // wait for it to finish

        // (b) one virtual thread per task using an executor (try-with-resources closes it)
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<Integer>> futures = new ArrayList<>();
            for (int i = 1; i <= 5; i++) {
                int number = i;                                     // must be effectively final
                futures.add(executor.submit(() -> number * number));
            }
            List<Integer> squares = new ArrayList<>();
            for (Future<Integer> future : futures) {
                squares.add(future.get());                          // wait for each result
            }
            print("8 squares from virtual threads", squares);
        }
    }

    // =====================================================================
    // 9. IMMUTABLE COLLECTIONS  (Java 9+)
    // List.of, Set.of, Map.of create collections that can NOT be changed.
    // Adding or removing throws UnsupportedOperationException.
    // They also do not allow null values.
    // =====================================================================

    static void immutableDemo() {
        List<String> fixedList = List.of("a", "b", "c");
        Map<String, Integer> fixedMap = Map.of("one", 1, "two", 2);
        print("9 List.of", fixedList);
        print("9 Map.of get(two)", fixedMap.get("two"));

        try {
            fixedList.add("d");
        } catch (UnsupportedOperationException e) {
            print("9 add to List.of", "UnsupportedOperationException (as expected)");
        }

        // Make an independent mutable copy when you need to change it.
        List<String> copy = new ArrayList<>(fixedList);
        copy.add("d");
        print("9 mutable copy", copy);
    }

    // =====================================================================
    // 10. STREAM API - NEW METHODS AFTER JAVA 8
    // =====================================================================

    static void streamDemos() {
        List<Employee> employees = List.of(
                new Employee(1, "Amit", "IT", 90000),
                new Employee(2, "Neha", "IT", 120000),
                new Employee(3, "Ravi", "HR", 50000),
                new Employee(4, "Sara", "HR", 65000),
                new Employee(5, "John", "Finance", 80000));

        List<Integer> numbers = List.of(1, 2, 3, 10, 4, 5);

        // (a) Stream.toList()  (Java 16)  - shorter than collect(Collectors.toList()).
        //     The result is unmodifiable.
        List<String> richNames = employees.stream()
                .filter(e -> e.salary() > 75000)
                .map(Employee::name)
                .toList();
        print("10a toList()", richNames);

        // (b) takeWhile / dropWhile  (Java 9)
        //     takeWhile: take items until the condition is FIRST false, then stop.
        //     dropWhile: skip items until the condition is FIRST false, keep the rest.
        //     Different from filter, which checks EVERY item.
        print("10b takeWhile(x < 5)", numbers.stream().takeWhile(x -> x < 5).toList());   // [1, 2, 3]
        print("10b dropWhile(x < 5)", numbers.stream().dropWhile(x -> x < 5).toList());   // [10, 4, 5]

        // (c) Stream.iterate with a stop condition (Java 9): like a for loop
        print("10c iterate", Stream.iterate(1, x -> x <= 100, x -> x * 2).toList());      // 1,2,4,...,64

        // (d) Collectors.teeing (Java 12): run TWO collectors and combine the results
        double average = numbers.stream().collect(Collectors.teeing(
                Collectors.summingInt(Integer::intValue),
                Collectors.counting(),
                (sum, count) -> (double) sum / count));
        print("10d teeing average", average);

        // (e) Collectors.filtering / flatMapping (Java 9): filter INSIDE each group
        //     Plain filter() before groupingBy would remove empty groups completely.
        //     filtering keeps the group (with an empty list).
        Map<String, List<String>> wellPaidByDept = employees.stream().collect(
                Collectors.groupingBy(Employee::department,
                        Collectors.filtering((Employee e) -> e.salary() > 75000,
                                Collectors.mapping(Employee::name, Collectors.toList()))));
        print("10e filtering per dept", wellPaidByDept);

        // (f) mapMulti (Java 16): turn one item into zero, one or many items
        print("10f mapMulti", Stream.of(1, 2, 3)
                .<Integer>mapMulti((number, sink) -> {
                    sink.accept(number);
                    sink.accept(number * 10);
                })
                .toList());                                                                // [1, 10, 2, 20, 3, 30]

        // (g) Predicate.not (Java 11): readable negation with method references
        List<String> words = List.of("java", " ", "", "stream");
        print("10g non blank", words.stream().filter(Predicate.not(String::isBlank)).toList());

        // (h) Stream.ofNullable (Java 9): empty stream if the value is null
        String maybeNull = null;
        print("10h ofNullable count", Stream.ofNullable(maybeNull).count());               // 0

        // (i) Highest and lowest paid in one pass using teeing
        String summary = employees.stream().collect(Collectors.teeing(
                Collectors.minBy(Comparator.comparingDouble(Employee::salary)),
                Collectors.maxBy(Comparator.comparingDouble(Employee::salary)),
                (min, max) -> min.get().name() + " lowest, " + max.get().name() + " highest"));
        print("10i min and max", summary);

        // (j) Group + count + sort the result by count (typical 7 YOE question)
        Map<String, Long> countByDept = employees.stream()
                .collect(Collectors.groupingBy(Employee::department, Collectors.counting()));
        List<String> sortedDepts = countByDept.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .toList();
        print("10j depts by head count", sortedDepts);
    }

    // =====================================================================
    // 11. OPTIONAL - NEW METHODS
    // =====================================================================

    static void optionalDemo() {
        Optional<String> present = Optional.of("Java");
        Optional<String> empty = Optional.empty();

        present.ifPresentOrElse(                       // Java 9
                value -> print("11 present", value),
                () -> print("11 present", "nothing"));
        empty.ifPresentOrElse(
                value -> print("11 empty", value),
                () -> print("11 empty", "nothing"));

        print("11 isEmpty()", empty.isEmpty());                          // Java 11
        print("11 or(...)", empty.or(() -> Optional.of("fallback")).get());   // Java 9
        print("11 orElseThrow()", present.orElseThrow());                // Java 10: get() with a clearer name

        // Rule: do not use Optional for fields or method parameters.
        // It is meant for RETURN values that may be missing.
        // Never call get() without checking.
    }

    // =====================================================================
    // 12. STRING AND UTILITY METHODS (Java 11 - 21)
    // =====================================================================

    static void stringDemo() {
        print("12 isBlank", "   ".isBlank());                                    // true
        print("12 strip", "[" + "  hi  ".strip() + "]");                          // [hi]   (Unicode aware)
        print("12 repeat", "ab".repeat(3));                                      // ababab
        print("12 lines", "a\nb\nc".lines().toList());                           // [a, b, c]
        print("12 formatted", "Hello %s, you are %d".formatted("Amit", 32));
        print("12 requireNonNullElse", Objects.requireNonNullElse(null, "default"));
        print("12 Math.clamp (21)", Math.clamp(150, 0, 100));                    // 100
        print("12 chars sum", "abc".chars().sum());                              // 294
    }

    // =====================================================================
    // 13. DSA THE MODERN WAY  - same classic questions, shorter Java 21 code
    // =====================================================================

    // Second highest number
    static Optional<Integer> secondHighest(List<Integer> numbers) {
        return numbers.stream()
                .distinct()
                .sorted(Comparator.reverseOrder())
                .skip(1)
                .findFirst();
    }

    // Frequency of each character, keeping first-seen order
    static Map<Character, Long> charFrequency(String text) {
        return text.chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(c -> c, LinkedHashMap::new, Collectors.counting()));
    }

    // First non-repeating character
    static Optional<Character> firstNonRepeating(String text) {
        return charFrequency(text).entrySet().stream()
                .filter(entry -> entry.getValue() == 1)
                .map(Map.Entry::getKey)
                .findFirst();
    }

    // Group anagrams using computeIfAbsent
    static List<List<String>> groupAnagrams(List<String> words) {
        Map<String, List<String>> groups = new LinkedHashMap<>();
        for (String word : words) {
            char[] letters = word.toCharArray();
            Arrays.sort(letters);
            groups.computeIfAbsent(new String(letters), key -> new ArrayList<>()).add(word);
        }
        return List.copyOf(groups.values());
    }

    // Prime check with IntStream
    static boolean isPrime(int n) {
        return n > 1 && IntStream.rangeClosed(2, (int) Math.sqrt(n)).noneMatch(i -> n % i == 0);
    }

    // Palindrome check with StringBuilder.reverse
    static boolean isPalindrome(String text) {
        String clean = text.toLowerCase().replaceAll("[^a-z0-9]", "");
        return clean.equals(new StringBuilder(clean).reverse().toString());
    }

    // Two Sum using a map (same idea, modern style)
    static int[] twoSum(int[] numbers, int target) {
        Map<Integer, Integer> seen = new java.util.HashMap<>();
        for (int i = 0; i < numbers.length; i++) {
            Integer partnerIndex = seen.get(target - numbers[i]);
            if (partnerIndex != null) {
                return new int[]{partnerIndex, i};
            }
            seen.put(numbers[i], i);
        }
        return new int[0];
    }

    static void dsaDemo() {
        print("13 secondHighest", secondHighest(List.of(5, 3, 8, 8, 1)).orElse(-1));    // 5
        print("13 charFrequency", charFrequency("hello"));                              // {h=1, e=1, l=2, o=1}
        print("13 firstNonRepeating", firstNonRepeating("swiss").orElse('-'));          // w
        print("13 groupAnagrams", groupAnagrams(List.of("eat", "tea", "tan", "ate", "nat", "bat")));
        print("13 isPrime(29)", isPrime(29));                                           // true
        print("13 isPalindrome", isPalindrome("A man, a plan, a canal: Panama"));        // true
        print("13 twoSum", Arrays.toString(twoSum(new int[]{2, 7, 11, 15}, 9)));         // [0, 1]
    }

    // =====================================================================
    // 14. THEORY QUESTIONS WITH SHORT ANSWERS
    // =====================================================================
    //
    // Q: What is new in Java 17 and Java 21 that you have used?
    // A: 17: records, sealed classes, text blocks, switch expressions.
    //    21: virtual threads, pattern matching for switch, record patterns,
    //    sequenced collections.
    //
    // Q: Why are records good for DTOs?
    // A: Less code, immutable by default, equals/hashCode/toString for free.
    //    Note: JPA entities cannot be records (they need a no-arg constructor
    //    and mutable fields). Use records for DTOs and API request/response.
    //
    // Q: What problem do sealed classes solve?
    // A: They limit who can extend a type. The compiler then knows all subtypes,
    //    so switch needs no default and missing cases become compile errors.
    //
    // Q: Platform thread vs virtual thread?
    // A: Platform = OS thread, heavy, limited number. Virtual = JVM managed, very
    //    light, millions possible, best for blocking I/O work.
    //
    // Q: Does a virtual thread make CPU-heavy code faster?
    // A: No. It helps when threads WAIT. CPU work is limited by CPU cores.
    //
    // Q: What is the difference between List.of() and Arrays.asList()?
    // A: List.of: fully immutable, no nulls. Arrays.asList: fixed size but you can
    //    change elements (set), and it is backed by the original array.
    //
    // Q: Stream.toList() vs collect(Collectors.toList())?
    // A: toList() returns an unmodifiable list. collect(toList()) returns an
    //    ArrayList you can modify (but this is not guaranteed by the spec).
    //
    // Q: What does "var" do, and when should you avoid it?
    // A: Compiler guesses the type from the right side. Avoid it when the right
    //    side does not make the type clear, like var x = getData();
    //
    // Q: What is the difference between a switch statement and a switch expression?
    // A: Expression returns a value, uses arrows, has no fall-through, and must
    //    cover every case. Statement is the old style with break.
    //
    // Q: What are LTS versions?
    // A: Long Term Support releases with years of updates: 8, 11, 17, 21.
    //    Most companies run an LTS version in production.
    //
    // Q: Which garbage collectors do you know?
    // A: G1 (default since 9), ZGC and Shenandoah (very low pause times),
    //    Parallel GC (throughput), Serial GC (small apps). Generational ZGC
    //    arrived in Java 21.

    // =====================================================================
    // RUNNER
    // =====================================================================

    private static void print(String label, Object value) {
        System.out.println(label + " => " + value);
    }

    public static void main(String[] args) throws Exception {
        recordsDemo();
        varDemo();
        textBlockDemo();
        print("4 dayType(SAT)", dayType(Day.SAT));            // Weekend
        print("4 daysInMonth(4)", daysInMonth(4));             // 30
        print("5 instanceOf", instanceOfDemo("hello"));        // String with length 5
        patternDemo();
        sequencedDemo();
        virtualThreadDemo();
        immutableDemo();
        streamDemos();
        optionalDemo();
        stringDemo();
        dsaDemo();
    }
}
