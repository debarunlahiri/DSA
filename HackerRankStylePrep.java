import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * ONLINE CODING ASSESSMENT PRACTICE - HackerRank / TCS NQT / Wipro NTH / Infosys /
 * Cognizant GenC / HCL style problems.
 *
 * HOW THESE TESTS WORK (read this first)
 *   1. You get a problem + "Input Format" + "Output Format" + sample test cases.
 *   2. Your program must READ from standard input (keyboard) and PRINT to standard
 *      output. There is no function to fill in on many platforms - you write main().
 *   3. Your code runs against many hidden test cases. Output must match EXACTLY
 *      (spaces, new lines, decimal places).
 *   4. Partial marks: you get marks for each test case that passes.
 *
 * HOW TO USE THIS FILE
 *   Each problem below is a method pNN(...) that reads its own input. main() asks
 *   for a problem number first, so you can practise any problem:
 *       javac HackerRankStylePrep.java
 *       java HackerRankStylePrep
 *       (type the problem number, then the sample input)
 *   In the REAL test, copy just the body of one method into main(). Do NOT keep the
 *   problem-number line - the real input does not have it.
 *
 * CHECKLIST BEFORE YOU SUBMIT (most lost marks come from these)
 *   [ ] Do NOT print anything extra ("Enter number:" prompts break the output).
 *   [ ] Sums and products: use long when numbers can be large (int max is ~2.1 billion).
 *   [ ] Read the constraints: if n can be 10^5 or more, avoid O(n^2) loops.
 *   [ ] Scanner is slow for very large input. Use the FastReader at the bottom.
 *   [ ] Mixing nextInt() and nextLine(): nextInt() leaves the new line behind. Call
 *       sc.nextLine() once to throw it away before reading a full line.
 *   [ ] Test the edge cases: n = 1, all same numbers, negative numbers, empty-like input.
 *   [ ] Decimals: System.out.printf("%.2f%n", value) prints 2 decimal places.
 *
 * The solutions are kept simple on purpose, so you can write them fast and
 * explain them easily. The idea for each problem is written in the comment above it.
 */
public class HackerRankStylePrep {

    // =====================================================================
    // SECTION 1 : ARRAYS AND MATH (most common, easy)
    // =====================================================================

    // ---------------------------------------------------------------------
    // P01. Simple Array Sum
    // Input : n, then n integers          Sample: 6 / 1 2 3 4 10 11
    // Output: sum of the array            Sample: 31
    // Idea  : Add all numbers. Use long in case the sum is big.
    // ---------------------------------------------------------------------
    static void p01SimpleArraySum(Scanner sc) {
        int n = sc.nextInt();
        long sum = 0;
        for (int i = 0; i < n; i++) {
            sum += sc.nextInt();
        }
        System.out.println(sum);
    }

    // ---------------------------------------------------------------------
    // P02. Diagonal Difference
    // Input : n, then an n x n matrix
    //         3 / 11 2 4 / 4 5 6 / 10 8 -12
    // Output: absolute difference of the two diagonal sums    Sample: 15
    // Idea  : Main diagonal = cells [i][i]. Other diagonal = cells [i][n-1-i].
    // ---------------------------------------------------------------------
    static void p02DiagonalDifference(Scanner sc) {
        int n = sc.nextInt();
        int mainDiagonal = 0;
        int otherDiagonal = 0;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                int value = sc.nextInt();
                if (row == col) {
                    mainDiagonal += value;
                }
                if (row + col == n - 1) {
                    otherDiagonal += value;
                }
            }
        }
        System.out.println(Math.abs(mainDiagonal - otherDiagonal));
    }

    // ---------------------------------------------------------------------
    // P03. Plus Minus
    // Input : n, then n integers          Sample: 6 / -4 3 -9 0 4 1
    // Output: ratio of positive, negative, zero numbers - each on its own line,
    //         6 decimal places            Sample: 0.500000 / 0.333333 / 0.166667
    // Idea  : Count each kind, divide by n. Cast to double before dividing,
    //         otherwise integer division gives 0.
    // ---------------------------------------------------------------------
    static void p03PlusMinus(Scanner sc) {
        int n = sc.nextInt();
        int positive = 0, negative = 0, zero = 0;
        for (int i = 0; i < n; i++) {
            int value = sc.nextInt();
            if (value > 0) {
                positive++;
            } else if (value < 0) {
                negative++;
            } else {
                zero++;
            }
        }
        System.out.printf("%.6f%n", (double) positive / n);
        System.out.printf("%.6f%n", (double) negative / n);
        System.out.printf("%.6f%n", (double) zero / n);
    }

    // ---------------------------------------------------------------------
    // P04. Mini-Max Sum
    // Input : 5 integers                  Sample: 1 2 3 4 5
    // Output: smallest and largest sum of exactly 4 of the 5 numbers
    //                                     Sample: 10 14
    // Idea  : total - biggest = smallest sum. total - smallest = biggest sum.
    // ---------------------------------------------------------------------
    static void p04MiniMaxSum(Scanner sc) {
        long total = 0;
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        for (int i = 0; i < 5; i++) {
            long value = sc.nextLong();
            total += value;
            min = Math.min(min, value);
            max = Math.max(max, value);
        }
        System.out.println((total - max) + " " + (total - min));
    }

    // ---------------------------------------------------------------------
    // P05. Birthday Cake Candles
    // Input : n, then n heights           Sample: 4 / 3 2 1 3
    // Output: how many candles have the maximum height     Sample: 2
    // ---------------------------------------------------------------------
    static void p05BirthdayCakeCandles(Scanner sc) {
        int n = sc.nextInt();
        int tallest = Integer.MIN_VALUE;
        int count = 0;
        for (int i = 0; i < n; i++) {
            int height = sc.nextInt();
            if (height > tallest) {
                tallest = height;      // new record, restart the count
                count = 1;
            } else if (height == tallest) {
                count++;
            }
        }
        System.out.println(count);
    }

    // ---------------------------------------------------------------------
    // P06. Compare the Triplets
    // Input : two lines of 3 integers (Alice, Bob)     Sample: 5 6 7 / 3 6 10
    // Output: Alice's points and Bob's points          Sample: 1 1
    // Idea  : For each position the bigger number gets 1 point. Equal = no point.
    // ---------------------------------------------------------------------
    static void p06CompareTriplets(Scanner sc) {
        int[] alice = new int[3];
        int[] bob = new int[3];
        for (int i = 0; i < 3; i++) {
            alice[i] = sc.nextInt();
        }
        for (int i = 0; i < 3; i++) {
            bob[i] = sc.nextInt();
        }
        int alicePoints = 0, bobPoints = 0;
        for (int i = 0; i < 3; i++) {
            if (alice[i] > bob[i]) {
                alicePoints++;
            } else if (bob[i] > alice[i]) {
                bobPoints++;
            }
        }
        System.out.println(alicePoints + " " + bobPoints);
    }

    // ---------------------------------------------------------------------
    // P07. Lonely Integer
    // Input : n, then n integers. Every number appears twice except one.
    //                                     Sample: 5 / 1 1 2 3 3
    // Output: the number that appears once     Sample: 2
    // Idea  : XOR trick. a ^ a = 0 and a ^ 0 = a. All pairs cancel out.
    // Time  : O(n)   Space : O(1)
    // ---------------------------------------------------------------------
    static void p07LonelyInteger(Scanner sc) {
        int n = sc.nextInt();
        int result = 0;
        for (int i = 0; i < n; i++) {
            result ^= sc.nextInt();
        }
        System.out.println(result);
    }

    // ---------------------------------------------------------------------
    // P08. Left Rotation
    // Input : n and d on the first line, array on the second
    //                                     Sample: 5 4 / 1 2 3 4 5
    // Output: array rotated LEFT by d      Sample: 5 1 2 3 4
    // Idea  : Element at index i moves to (i - d + n) % n.
    // ---------------------------------------------------------------------
    static void p08LeftRotation(Scanner sc) {
        int n = sc.nextInt();
        int d = sc.nextInt() % n;                // d can be bigger than n
        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            result[(i - d + n) % n] = sc.nextInt();
        }
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < n; i++) {
            line.append(result[i]);
            if (i < n - 1) {
                line.append(" ");
            }
        }
        System.out.println(line);
    }

    // ---------------------------------------------------------------------
    // P09. Grading Students (rounding rules)
    // Rule  : If grade < 38 -> no change. Otherwise, if the next multiple of 5 is
    //         less than 3 above the grade, round up to it.
    // Input : n, then n grades            Sample: 4 / 73 67 38 33
    // Output: final grades, one per line  Sample: 75 67 40 33
    // ---------------------------------------------------------------------
    static void p09GradingStudents(Scanner sc) {
        int n = sc.nextInt();
        for (int i = 0; i < n; i++) {
            int grade = sc.nextInt();
            if (grade >= 38) {
                int nextMultipleOfFive = ((grade / 5) + 1) * 5;
                if (nextMultipleOfFive - grade < 3) {
                    grade = nextMultipleOfFive;
                }
            }
            System.out.println(grade);
        }
    }

    // ---------------------------------------------------------------------
    // P10. Migratory Birds
    // Input : n, then n bird type ids     Sample: 6 / 1 4 4 4 5 3
    // Output: the most common type id. If tied, print the SMALLEST id.   Sample: 4
    // ---------------------------------------------------------------------
    static void p10MigratoryBirds(Scanner sc) {
        int n = sc.nextInt();
        Map<Integer, Integer> count = new TreeMap<>();      // TreeMap = sorted by id
        for (int i = 0; i < n; i++) {
            int type = sc.nextInt();
            count.put(type, count.getOrDefault(type, 0) + 1);
        }
        int bestType = 0;
        int bestCount = 0;
        for (Map.Entry<Integer, Integer> entry : count.entrySet()) {
            if (entry.getValue() > bestCount) {      // ">" keeps the first (smallest) id on a tie
                bestCount = entry.getValue();
                bestType = entry.getKey();
            }
        }
        System.out.println(bestType);
    }

    // ---------------------------------------------------------------------
    // P11. Sock Merchant
    // Input : n, then n sock colours      Sample: 9 / 10 20 20 10 10 30 50 10 20
    // Output: number of matching pairs    Sample: 3
    // Idea  : Count each colour. Pairs = count / 2 for every colour.
    // ---------------------------------------------------------------------
    static void p11SockMerchant(Scanner sc) {
        int n = sc.nextInt();
        Map<Integer, Integer> count = new HashMap<>();
        for (int i = 0; i < n; i++) {
            int colour = sc.nextInt();
            count.put(colour, count.getOrDefault(colour, 0) + 1);
        }
        int pairs = 0;
        for (int value : count.values()) {
            pairs += value / 2;
        }
        System.out.println(pairs);
    }

    // ---------------------------------------------------------------------
    // P12. Number Line Jumps (Kangaroo)
    // Two kangaroos start at x1 and x2 and jump v1 and v2 metres per jump.
    // Input : x1 v1 x2 v2                 Sample: 0 3 4 2
    // Output: YES if they land on the same spot at the same time, else NO. -> YES
    // Idea  : After t jumps: x1 + v1*t == x2 + v2*t  ->  t = (x2 - x1) / (v1 - v2).
    //         t must be a whole number >= 0.
    // ---------------------------------------------------------------------
    static void p12Kangaroo(Scanner sc) {
        int x1 = sc.nextInt(), v1 = sc.nextInt(), x2 = sc.nextInt(), v2 = sc.nextInt();
        if (v1 == v2) {
            System.out.println(x1 == x2 ? "YES" : "NO");
        } else if ((x2 - x1) % (v1 - v2) == 0 && (x2 - x1) / (v1 - v2) >= 0) {
            System.out.println("YES");
        } else {
            System.out.println("NO");
        }
    }

    // ---------------------------------------------------------------------
    // P13. FizzBuzz
    // Input : n                           Sample: 15
    // Output: for 1..n: "FizzBuzz" if divisible by 3 and 5, "Fizz" if by 3,
    //         "Buzz" if by 5, otherwise the number.
    // Careful: check the "both" case FIRST.
    // ---------------------------------------------------------------------
    static void p13FizzBuzz(Scanner sc) {
        int n = sc.nextInt();
        for (int i = 1; i <= n; i++) {
            if (i % 15 == 0) {
                System.out.println("FizzBuzz");
            } else if (i % 3 == 0) {
                System.out.println("Fizz");
            } else if (i % 5 == 0) {
                System.out.println("Buzz");
            } else {
                System.out.println(i);
            }
        }
    }

    // ---------------------------------------------------------------------
    // P14. Prime Numbers in a Range
    // Input : a b                         Sample: 10 30
    // Output: count on the first line, then the primes   -> 6 / 11 13 17 19 23 29
    // ---------------------------------------------------------------------
    static void p14PrimesInRange(Scanner sc) {
        int a = sc.nextInt();
        int b = sc.nextInt();
        List<Integer> primes = new ArrayList<>();
        for (int number = a; number <= b; number++) {
            if (isPrime(number)) {
                primes.add(number);
            }
        }
        System.out.println(primes.size());
        System.out.println(primes.stream().map(String::valueOf).collect(Collectors.joining(" ")));
    }

    static boolean isPrime(int n) {
        if (n < 2) {
            return false;
        }
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    // ---------------------------------------------------------------------
    // P15. Armstrong Numbers in a Range
    // Input : a b                         Sample: 100 500
    // Output: Armstrong numbers in the range, space separated   -> 153 370 371 407
    // ---------------------------------------------------------------------
    static void p15ArmstrongInRange(Scanner sc) {
        int a = sc.nextInt();
        int b = sc.nextInt();
        List<Integer> result = new ArrayList<>();
        for (int number = a; number <= b; number++) {
            int digits = String.valueOf(number).length();
            int total = 0;
            int temp = number;
            while (temp > 0) {
                total += (int) Math.pow(temp % 10, digits);
                temp /= 10;
            }
            if (total == number) {
                result.add(number);
            }
        }
        System.out.println(result.stream().map(String::valueOf).collect(Collectors.joining(" ")));
    }

    // ---------------------------------------------------------------------
    // P16. Second Largest Number
    // Input : n, then n integers          Sample: 5 / 10 20 20 5 15
    // Output: second largest DISTINCT number, or "No second largest". -> 15
    // ---------------------------------------------------------------------
    static void p16SecondLargest(Scanner sc) {
        int n = sc.nextInt();
        long first = Long.MIN_VALUE;
        long second = Long.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            long value = sc.nextLong();
            if (value > first) {
                second = first;
                first = value;
            } else if (value > second && value != first) {
                second = value;
            }
        }
        if (second == Long.MIN_VALUE) {
            System.out.println("No second largest");
        } else {
            System.out.println(second);
        }
    }

    // ---------------------------------------------------------------------
    // P17. Maximum Subarray Sum (Kadane)
    // Input : n, then n integers          Sample: 9 / -2 1 -3 4 -1 2 1 -5 4
    // Output: largest sum of a continuous part of the array   -> 6
    // ---------------------------------------------------------------------
    static void p17MaxSubarray(Scanner sc) {
        int n = sc.nextInt();
        long best = Long.MIN_VALUE;
        long running = 0;
        for (int i = 0; i < n; i++) {
            running += sc.nextInt();
            best = Math.max(best, running);
            if (running < 0) {
                running = 0;      // a negative running sum only hurts - start fresh
            }
        }
        System.out.println(best);
    }

    // ---------------------------------------------------------------------
    // P18. Two Sum
    // Input : n, then n integers, then the target     Sample: 4 / 2 7 11 15 / 9
    // Output: the two indexes (0-based), or -1 if no pair exists   -> 0 1
    // ---------------------------------------------------------------------
    static void p18TwoSum(Scanner sc) {
        int n = sc.nextInt();
        int[] numbers = new int[n];
        for (int i = 0; i < n; i++) {
            numbers[i] = sc.nextInt();
        }
        int target = sc.nextInt();
        Map<Integer, Integer> seen = new HashMap<>();
        for (int i = 0; i < n; i++) {
            Integer partnerIndex = seen.get(target - numbers[i]);
            if (partnerIndex != null) {
                System.out.println(partnerIndex + " " + i);
                return;
            }
            seen.put(numbers[i], i);
        }
        System.out.println(-1);
    }

    // ---------------------------------------------------------------------
    // P19. Count Set Bits (number of 1s in binary)
    // Input : n                           Sample: 13   (binary 1101)
    // Output: 3
    // Idea  : n & 1 checks the last bit. n >> 1 removes it.
    //         (Built-in: Integer.bitCount(n) - mention it, but write the loop.)
    // ---------------------------------------------------------------------
    static void p19CountSetBits(Scanner sc) {
        int n = sc.nextInt();
        int count = 0;
        while (n > 0) {
            count += n & 1;
            n = n >> 1;
        }
        System.out.println(count);
    }

    // ---------------------------------------------------------------------
    // P20. Decimal to Binary and Binary to Decimal
    // Input : decimal number, then a binary string      Sample: 10 / 1010
    // Output: binary of the number, decimal of the string   -> 1010 / 10
    // Idea  : Divide by 2 and collect remainders (read them backwards).
    // ---------------------------------------------------------------------
    static void p20BinaryConversion(Scanner sc) {
        int n = sc.nextInt();
        String binary = sc.next();

        StringBuilder bits = new StringBuilder();
        if (n == 0) {
            bits.append("0");
        }
        while (n > 0) {
            bits.insert(0, n % 2);        // put each remainder at the FRONT
            n /= 2;
        }
        System.out.println(bits);
        System.out.println(Integer.parseInt(binary, 2));
    }

    // =====================================================================
    // SECTION 2 : STRINGS
    // =====================================================================

    // ---------------------------------------------------------------------
    // P21. Time Conversion (12-hour to 24-hour)
    // Input : hh:mm:ssAM or hh:mm:ssPM    Sample: 07:05:45PM
    // Output: 24-hour time                Sample: 19:05:45
    // Rules : 12:xx AM -> 00:xx.  12:xx PM stays 12.  Other PM hours add 12.
    // ---------------------------------------------------------------------
    static void p21TimeConversion(Scanner sc) {
        String time = sc.next();
        int hour = Integer.parseInt(time.substring(0, 2));
        boolean isPm = time.endsWith("PM");
        if (hour == 12) {
            hour = 0;               // treat 12 as 0, then the PM rule adds 12 back
        }
        if (isPm) {
            hour += 12;
        }
        System.out.println(String.format("%02d", hour) + time.substring(2, 8));
    }

    // ---------------------------------------------------------------------
    // P22. Staircase
    // Input : n                           Sample: 4
    // Output: a right-aligned staircase made of '#'
    //            #
    //           ##
    //          ###
    //         ####
    // ---------------------------------------------------------------------
    static void p22Staircase(Scanner sc) {
        int n = sc.nextInt();
        for (int row = 1; row <= n; row++) {
            StringBuilder line = new StringBuilder();
            for (int i = 0; i < n - row; i++) {
                line.append(" ");
            }
            for (int i = 0; i < row; i++) {
                line.append("#");
            }
            System.out.println(line);
        }
    }

    // ---------------------------------------------------------------------
    // P23. Balanced Brackets (many test cases)
    // Input : t, then t strings made of ( ) { } [ ]    Sample: 3 / {[()]} / {[(])} / {{[[(())]]}}
    // Output: YES or NO for each string                -> YES / NO / YES
    // ---------------------------------------------------------------------
    static void p23BalancedBrackets(Scanner sc) {
        int t = sc.nextInt();
        for (int test = 0; test < t; test++) {
            String text = sc.next();
            Deque<Character> stack = new ArrayDeque<>();
            boolean balanced = true;
            for (char c : text.toCharArray()) {
                if (c == '(' || c == '{' || c == '[') {
                    stack.push(c);
                } else if (stack.isEmpty()) {
                    balanced = false;
                    break;
                } else {
                    char open = stack.pop();
                    if ((c == ')' && open != '(') || (c == '}' && open != '{') || (c == ']' && open != '[')) {
                        balanced = false;
                        break;
                    }
                }
            }
            System.out.println(balanced && stack.isEmpty() ? "YES" : "NO");
        }
    }

    // ---------------------------------------------------------------------
    // P24. Making Anagrams (minimum deletions)
    // Input : two lowercase strings       Sample: cde / abc
    // Output: minimum characters to delete (from both) to make them anagrams   -> 4
    // Idea  : Count letters of each string. Add up the difference for every letter.
    // ---------------------------------------------------------------------
    static void p24MakingAnagrams(Scanner sc) {
        String first = sc.next();
        String second = sc.next();
        int[] count = new int[26];
        for (char c : first.toCharArray()) {
            count[c - 'a']++;
        }
        for (char c : second.toCharArray()) {
            count[c - 'a']--;
        }
        int deletions = 0;
        for (int difference : count) {
            deletions += Math.abs(difference);
        }
        System.out.println(deletions);
    }

    // ---------------------------------------------------------------------
    // P25. Pangram (uses every letter a-z at least once)
    // Input : one line of text            Sample: We promptly judged antique ivory buckles for the next prize
    // Output: "pangram" or "not pangram"  -> pangram
    // ---------------------------------------------------------------------
    static void p25Pangram(Scanner sc) {
        String line = sc.nextLine().toLowerCase();
        Set<Character> letters = new LinkedHashSet<>();
        for (char c : line.toCharArray()) {
            if (c >= 'a' && c <= 'z') {
                letters.add(c);
            }
        }
        System.out.println(letters.size() == 26 ? "pangram" : "not pangram");
    }

    // ---------------------------------------------------------------------
    // P26. Caesar Cipher
    // Input : n, the string, then k (shift)     Sample: 11 / middle-Outz / 2
    // Output: each letter moved k places forward, wrapping z -> a. Case is kept;
    //         other characters (like '-') stay the same.   -> okffng-Qwvb
    // ---------------------------------------------------------------------
    static void p26CaesarCipher(Scanner sc) {
        int n = sc.nextInt();
        String text = sc.next();
        int k = sc.nextInt() % 26;
        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            if (Character.isUpperCase(c)) {
                result.append((char) ('A' + (c - 'A' + k) % 26));
            } else if (Character.isLowerCase(c)) {
                result.append((char) ('a' + (c - 'a' + k) % 26));
            } else {
                result.append(c);
            }
        }
        System.out.println(result);
    }

    // ---------------------------------------------------------------------
    // P27. CamelCase - count the words
    // Input : one camelCase string         Sample: saveChangesInTheEditor
    // Output: number of words              -> 5
    // Idea  : 1 word + 1 for every capital letter.
    // ---------------------------------------------------------------------
    static void p27CamelCase(Scanner sc) {
        String text = sc.next();
        int words = 1;
        for (char c : text.toCharArray()) {
            if (Character.isUpperCase(c)) {
                words++;
            }
        }
        System.out.println(words);
    }

    // ---------------------------------------------------------------------
    // P28. Counting Valleys
    // A hiker takes steps U (up) or D (down) starting at sea level.
    // A valley = a trip below sea level that ends when coming back UP to sea level.
    // Input : n, then the path            Sample: 8 / UDDDUDUU
    // Output: number of valleys           -> 1
    // ---------------------------------------------------------------------
    static void p28CountingValleys(Scanner sc) {
        int n = sc.nextInt();
        String path = sc.next();
        int level = 0;
        int valleys = 0;
        for (char step : path.toCharArray()) {
            if (step == 'U') {
                level++;
                if (level == 0) {          // just climbed back up to sea level
                    valleys++;
                }
            } else {
                level--;
            }
        }
        System.out.println(valleys);
    }

    // ---------------------------------------------------------------------
    // P29. Repeated String
    // The string s is repeated forever. Count the letter 'a' in the first n letters.
    // Input : s, then n (can be up to 10^12 -> use long!)    Sample: aba / 10
    // Output: count of 'a'                -> 7
    // Idea  : Do NOT build the huge string. Count 'a' in one copy, multiply by the
    //         number of full copies, then add the 'a's in the leftover part.
    // ---------------------------------------------------------------------
    static void p29RepeatedString(Scanner sc) {
        String s = sc.next();
        long n = sc.nextLong();
        long aInOneCopy = 0;
        for (char c : s.toCharArray()) {
            if (c == 'a') {
                aInOneCopy++;
            }
        }
        long fullCopies = n / s.length();
        int leftover = (int) (n % s.length());
        long aInLeftover = 0;
        for (int i = 0; i < leftover; i++) {
            if (s.charAt(i) == 'a') {
                aInLeftover++;
            }
        }
        System.out.println(fullCopies * aInOneCopy + aInLeftover);
    }

    // ---------------------------------------------------------------------
    // P30. Character Frequency (sorted alphabetically)
    // Input : one word                    Sample: banana
    // Output: each letter and its count, in alphabetical order   -> a 3 / b 1 / n 2
    // ---------------------------------------------------------------------
    static void p30CharFrequency(Scanner sc) {
        String text = sc.next();
        Map<Character, Integer> count = new TreeMap<>();
        for (char c : text.toCharArray()) {
            count.put(c, count.getOrDefault(c, 0) + 1);
        }
        for (Map.Entry<Character, Integer> entry : count.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
    }

    // ---------------------------------------------------------------------
    // P31. Remove Duplicate Characters (keep first occurrence)
    // Input : one word                    Sample: programming
    // Output: word without repeated letters   -> progamin
    // ---------------------------------------------------------------------
    static void p31RemoveDuplicateChars(Scanner sc) {
        String text = sc.next();
        Set<Character> seen = new LinkedHashSet<>();
        for (char c : text.toCharArray()) {
            seen.add(c);
        }
        StringBuilder result = new StringBuilder();
        for (char c : seen) {
            result.append(c);
        }
        System.out.println(result);
    }

    // =====================================================================
    // SECTION 3 : MATRIX, PATTERNS, SIMPLE DP, DATA STRUCTURES
    // =====================================================================

    // ---------------------------------------------------------------------
    // P32. Spiral Matrix (TCS favourite)
    // Input : r c, then an r x c matrix   Sample: 3 3 / 1 2 3 / 4 5 6 / 7 8 9
    // Output: elements in spiral order    -> 1 2 3 6 9 8 7 4 5
    // Idea  : Four borders (top, bottom, left, right). Walk the top row, right
    //         column, bottom row, left column; then shrink the borders.
    // ---------------------------------------------------------------------
    static void p32SpiralMatrix(Scanner sc) {
        int rows = sc.nextInt();
        int cols = sc.nextInt();
        int[][] matrix = new int[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                matrix[r][c] = sc.nextInt();
            }
        }
        List<Integer> result = new ArrayList<>();
        int top = 0, bottom = rows - 1, left = 0, right = cols - 1;
        while (top <= bottom && left <= right) {
            for (int c = left; c <= right; c++) {
                result.add(matrix[top][c]);
            }
            top++;
            for (int r = top; r <= bottom; r++) {
                result.add(matrix[r][right]);
            }
            right--;
            if (top <= bottom) {
                for (int c = right; c >= left; c--) {
                    result.add(matrix[bottom][c]);
                }
                bottom--;
            }
            if (left <= right) {
                for (int r = bottom; r >= top; r--) {
                    result.add(matrix[r][left]);
                }
                left++;
            }
        }
        System.out.println(result.stream().map(String::valueOf).collect(Collectors.joining(" ")));
    }

    // ---------------------------------------------------------------------
    // P33. Floyd's Triangle
    // Input : n                           Sample: 4
    // Output: 1 / 2 3 / 4 5 6 / 7 8 9 10  (each row has one more number)
    // ---------------------------------------------------------------------
    static void p33FloydsTriangle(Scanner sc) {
        int n = sc.nextInt();
        int number = 1;
        for (int row = 1; row <= n; row++) {
            StringBuilder line = new StringBuilder();
            for (int i = 0; i < row; i++) {
                line.append(number++);
                if (i < row - 1) {
                    line.append(" ");
                }
            }
            System.out.println(line);
        }
    }

    // ---------------------------------------------------------------------
    // P34. Jumping on the Clouds
    // Clouds are 0 (safe) or 1 (thunderhead - must avoid). You can jump 1 or 2
    // clouds ahead. Reach the last cloud with the fewest jumps.
    // Input : n, then the clouds          Sample: 7 / 0 0 1 0 0 1 0
    // Output: minimum jumps               -> 4
    // Idea  : Greedy - always jump 2 when the cloud two ahead is safe, else jump 1.
    // ---------------------------------------------------------------------
    static void p34JumpingOnClouds(Scanner sc) {
        int n = sc.nextInt();
        int[] clouds = new int[n];
        for (int i = 0; i < n; i++) {
            clouds[i] = sc.nextInt();
        }
        int position = 0;
        int jumps = 0;
        while (position < n - 1) {
            if (position + 2 < n && clouds[position + 2] == 0) {
                position += 2;
            } else {
                position += 1;
            }
            jumps++;
        }
        System.out.println(jumps);
    }

    // ---------------------------------------------------------------------
    // P35. Minimum Coins (basic dynamic programming)
    // Input : n coins, the coin values, then the amount     Sample: 3 / 1 2 5 / 11
    // Output: fewest coins that make the amount, or -1      -> 3  (5 + 5 + 1)
    // Idea  : best[a] = fewest coins to make amount a.
    //         best[a] = 1 + min(best[a - coin]) over all coins that fit.
    // Time  : O(amount * n)
    // ---------------------------------------------------------------------
    static void p35MinimumCoins(Scanner sc) {
        int n = sc.nextInt();
        int[] coins = new int[n];
        for (int i = 0; i < n; i++) {
            coins[i] = sc.nextInt();
        }
        int amount = sc.nextInt();

        int notPossible = amount + 1;                 // a value that is bigger than any real answer
        int[] best = new int[amount + 1];
        Arrays.fill(best, notPossible);
        best[0] = 0;                                  // 0 coins make amount 0
        for (int a = 1; a <= amount; a++) {
            for (int coin : coins) {
                if (coin <= a) {
                    best[a] = Math.min(best[a], best[a - coin] + 1);
                }
            }
        }
        System.out.println(best[amount] == notPossible ? -1 : best[amount]);
    }

    // ---------------------------------------------------------------------
    // P36. Stack Operations
    // Input : q, then q commands: "push x", "pop", "peek"
    //         Sample: 5 / push 10 / push 20 / peek / pop / pop
    // Output: for peek and pop print the value, or EMPTY if the stack is empty
    //         -> 20 / 20 / 10
    // ---------------------------------------------------------------------
    static void p36StackOperations(Scanner sc) {
        int q = sc.nextInt();
        Deque<Integer> stack = new ArrayDeque<>();
        for (int i = 0; i < q; i++) {
            String command = sc.next();
            if (command.equals("push")) {
                stack.push(sc.nextInt());
            } else if (command.equals("pop")) {
                System.out.println(stack.isEmpty() ? "EMPTY" : String.valueOf(stack.pop()));
            } else if (command.equals("peek")) {
                System.out.println(stack.isEmpty() ? "EMPTY" : String.valueOf(stack.peek()));
            }
        }
    }

    // =====================================================================
    // SECTION 4 : "REAL PROJECT" STYLE (common for experienced candidates)
    // These use classes, sorting, collections and streams.
    // =====================================================================

    static class Student {
        String name;
        int marks;

        Student(String name, int marks) {
            this.name = name;
            this.marks = marks;
        }
    }

    // ---------------------------------------------------------------------
    // P37. Sort Students
    // Input : n, then n lines "name marks"     Sample: 4 / Ravi 80 / Amit 90 / Neha 80 / Sara 70
    // Output: sorted by marks (high to low); same marks -> by name (A to Z)
    //         -> Amit 90 / Neha 80 / Ravi 80 / Sara 70
    // ---------------------------------------------------------------------
    static void p37SortStudents(Scanner sc) {
        int n = sc.nextInt();
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            students.add(new Student(sc.next(), sc.nextInt()));
        }
        students.sort((a, b) -> {
            if (a.marks != b.marks) {
                return b.marks - a.marks;          // bigger marks first
            }
            return a.name.compareTo(b.name);       // then A to Z
        });
        for (Student s : students) {
            System.out.println(s.name + " " + s.marks);
        }
    }

    static class Employee {
        String name;
        String department;
        double salary;

        Employee(String name, String department, double salary) {
            this.name = name;
            this.department = department;
            this.salary = salary;
        }
    }

    // ---------------------------------------------------------------------
    // P38. Employees per Department (Stream API)
    // Input : n, then n lines "name department salary"
    //         Sample: 4 / Amit IT 90000 / Neha IT 120000 / Ravi HR 50000 / Sara HR 65000
    // Output: for each department (A to Z): name of highest paid + average salary
    //         -> HR highest=Sara avg=57500.00 / IT highest=Neha avg=105000.00
    // ---------------------------------------------------------------------
    static void p38EmployeesPerDepartment(Scanner sc) {
        int n = sc.nextInt();
        List<Employee> employees = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            employees.add(new Employee(sc.next(), sc.next(), sc.nextDouble()));
        }

        // TreeMap::new makes the departments come out sorted A to Z.
        Map<String, List<Employee>> byDepartment = employees.stream()
                .collect(Collectors.groupingBy((Employee e) -> e.department, TreeMap::new, Collectors.toList()));

        for (Map.Entry<String, List<Employee>> entry : byDepartment.entrySet()) {
            List<Employee> group = entry.getValue();
            Employee top = group.stream()
                    .max(Comparator.comparingDouble((Employee e) -> e.salary))
                    .get();
            double average = group.stream().mapToDouble((Employee e) -> e.salary).average().orElse(0);
            System.out.printf("%s highest=%s avg=%.2f%n", entry.getKey(), top.name, average);
        }
    }

    // ---------------------------------------------------------------------
    // P39. Days Between Two Dates
    // Input : two dates as dd-MM-yyyy      Sample: 01-01-2024 15-03-2024
    // Output: number of days between them  -> 74
    // Idea  : java.time is allowed in assessments - do not calculate by hand.
    // ---------------------------------------------------------------------
    static void p39DaysBetweenDates(Scanner sc) {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate first = LocalDate.parse(sc.next(), format);
        LocalDate second = LocalDate.parse(sc.next(), format);
        System.out.println(Math.abs(ChronoUnit.DAYS.between(first, second)));
    }

    // ---------------------------------------------------------------------
    // P40. Word Count and Longest Word
    // Input : one line of text            Sample: I love java programming
    // Output: number of words, then the longest word (first one if tied)
    //         -> 4 / programming
    // ---------------------------------------------------------------------
    static void p40WordCountAndLongest(Scanner sc) {
        String line = sc.nextLine().trim();
        String[] words = line.split("\\s+");
        String longest = "";
        for (String word : words) {
            if (word.length() > longest.length()) {
                longest = word;
            }
        }
        System.out.println(words.length);
        System.out.println(longest);
    }

    // =====================================================================
    // FAST INPUT (use when n is 10^5 or more - Scanner can time out)
    // Usage:  FastReader in = new FastReader();
    //         int n = in.nextInt();
    //         String word = in.next();
    // =====================================================================

    static class FastReader {
        private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        private StringTokenizer tokens;

        String next() {
            try {
                while (tokens == null || !tokens.hasMoreTokens()) {
                    String line = reader.readLine();
                    if (line == null) {
                        return null;
                    }
                    tokens = new StringTokenizer(line);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return tokens.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }

        long nextLong() {
            return Long.parseLong(next());
        }
    }

    // Fast OUTPUT: printing inside a loop with System.out.println is slow for
    // big output. Collect into a StringBuilder and print ONCE at the end.

    // =====================================================================
    // PRACTICE RUNNER - type the problem number first, then the sample input
    // =====================================================================

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int problem = sc.nextInt();
        sc.nextLine();                 // throw away the rest of the first line

        switch (problem) {
            case 1: p01SimpleArraySum(sc); break;
            case 2: p02DiagonalDifference(sc); break;
            case 3: p03PlusMinus(sc); break;
            case 4: p04MiniMaxSum(sc); break;
            case 5: p05BirthdayCakeCandles(sc); break;
            case 6: p06CompareTriplets(sc); break;
            case 7: p07LonelyInteger(sc); break;
            case 8: p08LeftRotation(sc); break;
            case 9: p09GradingStudents(sc); break;
            case 10: p10MigratoryBirds(sc); break;
            case 11: p11SockMerchant(sc); break;
            case 12: p12Kangaroo(sc); break;
            case 13: p13FizzBuzz(sc); break;
            case 14: p14PrimesInRange(sc); break;
            case 15: p15ArmstrongInRange(sc); break;
            case 16: p16SecondLargest(sc); break;
            case 17: p17MaxSubarray(sc); break;
            case 18: p18TwoSum(sc); break;
            case 19: p19CountSetBits(sc); break;
            case 20: p20BinaryConversion(sc); break;
            case 21: p21TimeConversion(sc); break;
            case 22: p22Staircase(sc); break;
            case 23: p23BalancedBrackets(sc); break;
            case 24: p24MakingAnagrams(sc); break;
            case 25: p25Pangram(sc); break;
            case 26: p26CaesarCipher(sc); break;
            case 27: p27CamelCase(sc); break;
            case 28: p28CountingValleys(sc); break;
            case 29: p29RepeatedString(sc); break;
            case 30: p30CharFrequency(sc); break;
            case 31: p31RemoveDuplicateChars(sc); break;
            case 32: p32SpiralMatrix(sc); break;
            case 33: p33FloydsTriangle(sc); break;
            case 34: p34JumpingOnClouds(sc); break;
            case 35: p35MinimumCoins(sc); break;
            case 36: p36StackOperations(sc); break;
            case 37: p37SortStudents(sc); break;
            case 38: p38EmployeesPerDepartment(sc); break;
            case 39: p39DaysBetweenDates(sc); break;
            case 40: p40WordCountAndLongest(sc); break;
            default: System.out.println("Unknown problem number. Use 1 to 40.");
        }
    }
}
