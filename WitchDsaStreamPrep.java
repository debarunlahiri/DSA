import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IntSummaryStatistics;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * INTERVIEW PREP - WITCH companies (Wipro, Infosys, TCS, Cognizant, HCL), 7 years experience.
 *
 * This file is written to be EASY TO READ and EASY TO EXPLAIN out loud.
 *   - Simple loops, simple names, no clever tricks.
 *   - Every question has: the problem, the idea in plain English, an example,
 *     and "Interviewer may ask" follow-ups with short answers.
 *
 * PART 1 : DSA questions (arrays, strings, math)
 * PART 2 : Java 8 Stream API questions
 *
 * Works on Java 8 or newer.
 * For Java 9 to Java 21 features (records, sealed, pattern switch, virtual threads,
 * sequenced collections, Stream.toList ...) see WitchJava21Prep.java (needs JDK 21).
 */
public class WitchDsaStreamPrep {

    // =====================================================================
    // PART 1 : DSA
    // =====================================================================

    // ---------------------------------------------------------------------
    // Q1. Reverse a String
    // Example : "hello" -> "olleh"
    // Idea    : Walk the string from the LAST character to the FIRST and
    //           add each character to a new string.
    // Time    : O(n)   Space : O(n)
    // Interviewer may ask:
    //   Why StringBuilder and not "result = result + c"?
    //   -> Strings are immutable. Each + creates a new String object (slow).
    //      StringBuilder changes the same object (fast).
    // ---------------------------------------------------------------------
    static String reverseString(String text) {
        if (text == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        for (int i = text.length() - 1; i >= 0; i--) {
            result.append(text.charAt(i));
        }
        return result.toString();
    }

    // ---------------------------------------------------------------------
    // Q2. Check Palindrome
    // Example : "madam" -> true, "hello" -> false
    // Idea    : Compare first char with last, second with second-last, and so on.
    //           If any pair is different, it is NOT a palindrome.
    // Time    : O(n)   Space : O(1)
    // Interviewer may ask: "What about 'Madam' or 'A man a plan'?"
    //   -> Convert to lower case and remove spaces first (done below).
    // ---------------------------------------------------------------------
    static boolean isPalindrome(String text) {
        if (text == null) {
            return false;
        }
        String clean = text.toLowerCase().replace(" ", "");
        int left = 0;
        int right = clean.length() - 1;
        while (left < right) {
            if (clean.charAt(left) != clean.charAt(right)) {
                return false;      // found a mismatch
            }
            left++;
            right--;
        }
        return true;
    }

    // ---------------------------------------------------------------------
    // Q3. Check Prime Number
    // Example : 17 -> true, 15 -> false
    // Idea    : A prime is only divisible by 1 and itself.
    //           We try dividing by 2, 3, 4 ... up to the square root of n.
    //           If nothing divides it, it is prime.
    // Why square root? If n = a * b, one of a or b is always <= sqrt(n).
    // Time    : O(sqrt n)   Space : O(1)
    // ---------------------------------------------------------------------
    static boolean isPrime(int n) {
        if (n <= 1) {
            return false;          // 0 and 1 are not prime
        }
        for (int i = 2; i * i <= n; i++) {
            if (n % i == 0) {
                return false;      // divisible, so not prime
            }
        }
        return true;
    }

    // ---------------------------------------------------------------------
    // Q4. Fibonacci Series
    // Example : 5 terms -> 0 1 1 2 3
    // Idea    : Every number = sum of the previous two. Keep two variables.
    // Time    : O(n)   Space : O(1)
    // Interviewer may ask: "Why is recursion slow here?"
    //   -> fib(5) calls fib(4) and fib(3); fib(4) calls fib(3) again...
    //      The same work is repeated again and again (O(2^n)).
    // ---------------------------------------------------------------------
    static List<Long> fibonacci(int count) {
        List<Long> series = new ArrayList<>();
        long previous = 0;
        long current = 1;
        for (int i = 0; i < count; i++) {
            series.add(previous);
            long next = previous + current;
            previous = current;
            current = next;
        }
        return series;
    }

    // ---------------------------------------------------------------------
    // Q5. Factorial
    // Example : 5! = 5 x 4 x 3 x 2 x 1 = 120
    // Idea    : Multiply all numbers from 1 to n.
    // Time    : O(n)   Space : O(1)
    // Interviewer may ask: "What happens for a big n like 25?"
    //   -> long overflows after 20!. Use BigInteger for bigger values.
    // ---------------------------------------------------------------------
    static long factorial(int n) {
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result = result * i;
        }
        return result;
    }

    // ---------------------------------------------------------------------
    // Q6. Second Largest Number in an Array
    // Example : {10, 5, 8, 20, 15} -> 15
    // Idea    : Keep two boxes: "largest" and "secondLargest". Go through the
    //           array once and update the boxes when you find a bigger number.
    // Time    : O(n)   Space : O(1)
    // Interviewer may ask: "Can you do it by sorting?"
    //   -> Yes, but sorting is O(n log n). One loop is faster.
    // ---------------------------------------------------------------------
    static int secondLargest(int[] numbers) {
        int largest = Integer.MIN_VALUE;
        int second = Integer.MIN_VALUE;
        for (int number : numbers) {
            if (number > largest) {
                second = largest;      // old largest becomes second
                largest = number;
            } else if (number > second && number != largest) {
                second = number;
            }
        }
        return second;
    }

    // ---------------------------------------------------------------------
    // Q7. Remove Duplicates from an Array
    // Example : {1, 2, 2, 3, 4, 4, 5} -> {1, 2, 3, 4, 5}
    // Idea    : A Set never keeps duplicates. LinkedHashSet also keeps the
    //           original order.
    // Time    : O(n)   Space : O(n)
    // Interviewer may ask: "HashSet vs LinkedHashSet vs TreeSet?"
    //   -> HashSet: no order. LinkedHashSet: insertion order. TreeSet: sorted.
    // ---------------------------------------------------------------------
    static int[] removeDuplicates(int[] numbers) {
        Set<Integer> unique = new java.util.LinkedHashSet<>();
        for (int number : numbers) {
            unique.add(number);
        }
        int[] result = new int[unique.size()];
        int index = 0;
        for (int number : unique) {
            result[index] = number;
            index++;
        }
        return result;
    }

    // ---------------------------------------------------------------------
    // Q8. Two Sum   (asked very often)
    // Example : {2, 7, 11, 15}, target 9 -> indexes [0, 1] because 2 + 7 = 9
    // Idea    : For each number, the partner we need is (target - number).
    //           Keep a map of numbers we have already seen.
    //           If the partner is in the map, we are done.
    // Time    : O(n)   Space : O(n)
    // Interviewer may ask: "What is the brute force?"
    //   -> Two nested loops checking every pair: O(n^2). The map makes it O(n).
    // ---------------------------------------------------------------------
    static int[] twoSum(int[] numbers, int target) {
        Map<Integer, Integer> seen = new HashMap<>();   // number -> its index
        for (int i = 0; i < numbers.length; i++) {
            int partner = target - numbers[i];
            if (seen.containsKey(partner)) {
                return new int[]{seen.get(partner), i};
            }
            seen.put(numbers[i], i);
        }
        return new int[]{-1, -1};   // not found
    }

    // ---------------------------------------------------------------------
    // Q9. Check Anagram
    // Example : "listen" and "silent" -> true
    // Idea    : Two words are anagrams if they use the same letters the same
    //           number of times. Sort both and compare.
    // Time    : O(n log n)   Space : O(n)
    // Interviewer may ask: "Can you do better than sorting?"
    //   -> Yes. Count letters in an int[26] array: O(n).
    // ---------------------------------------------------------------------
    static boolean isAnagram(String first, String second) {
        if (first.length() != second.length()) {
            return false;
        }
        char[] a = first.toLowerCase().toCharArray();
        char[] b = second.toLowerCase().toCharArray();
        Arrays.sort(a);
        Arrays.sort(b);
        return Arrays.equals(a, b);
    }

    // ---------------------------------------------------------------------
    // Q10. Count Frequency of Each Character
    // Example : "hello" -> {h=1, e=1, l=2, o=1}
    // Idea    : Use a map. If the letter is new put 1, else add 1 to its count.
    // Time    : O(n)   Space : O(k)  (k = number of different letters)
    // Interviewer may ask: "HashMap vs LinkedHashMap vs TreeMap?"
    //   -> HashMap: no order. LinkedHashMap: insertion order. TreeMap: sorted by key.
    // ---------------------------------------------------------------------
    static Map<Character, Integer> charFrequency(String text) {
        Map<Character, Integer> count = new LinkedHashMap<>();
        for (char letter : text.toCharArray()) {
            if (count.containsKey(letter)) {
                count.put(letter, count.get(letter) + 1);
            } else {
                count.put(letter, 1);
            }
        }
        return count;
    }

    // ---------------------------------------------------------------------
    // Q11. Sort an array of 0s, 1s and 2s
    // Example : {2, 0, 1, 2, 0, 1} -> {0, 0, 1, 1, 2, 2}
    // Idea    : Easy way - count how many 0s, 1s, 2s there are, then write
    //           them back in order.
    // Time    : O(n)   Space : O(1)
    // Interviewer may ask: "Can you do it in ONE pass?"
    //   -> Yes, the Dutch National Flag algorithm using three pointers
    //      (low, mid, high). Mention it, explain only if asked.
    // ---------------------------------------------------------------------
    static void sortZeroOneTwo(int[] numbers) {
        int zeros = 0, ones = 0, twos = 0;
        for (int number : numbers) {
            if (number == 0) {
                zeros++;
            } else if (number == 1) {
                ones++;
            } else {
                twos++;
            }
        }
        int index = 0;
        for (int i = 0; i < zeros; i++) {
            numbers[index++] = 0;
        }
        for (int i = 0; i < ones; i++) {
            numbers[index++] = 1;
        }
        for (int i = 0; i < twos; i++) {
            numbers[index++] = 2;
        }
    }

    // ---------------------------------------------------------------------
    // Q12. Find the Missing Number (1 to n)
    // Example : {1, 2, 4, 5}, n = 5 -> 3
    // Idea    : Sum of 1..n is n * (n + 1) / 2. Subtract the sum of the array.
    //           What is left is the missing number.
    // Time    : O(n)   Space : O(1)
    // ---------------------------------------------------------------------
    static int missingNumber(int[] numbers, int n) {
        int expectedSum = n * (n + 1) / 2;
        int actualSum = 0;
        for (int number : numbers) {
            actualSum += number;
        }
        return expectedSum - actualSum;
    }

    // ---------------------------------------------------------------------
    // Q13. Maximum Subarray Sum (Kadane's Algorithm)
    // Example : {-2, 1, -3, 4, -1, 2, 1, -5, 4} -> 6  (subarray 4, -1, 2, 1)
    // Idea    : Walk through the array keeping a running sum.
    //           If the running sum becomes negative, throw it away and start
    //           again from the next number (a negative sum can only hurt).
    //           Remember the best sum seen.
    // Time    : O(n)   Space : O(1)
    // ---------------------------------------------------------------------
    static int maxSubarraySum(int[] numbers) {
        int best = numbers[0];
        int running = 0;
        for (int number : numbers) {
            running = running + number;
            if (running > best) {
                best = running;
            }
            if (running < 0) {
                running = 0;        // start fresh
            }
        }
        return best;
    }

    // ---------------------------------------------------------------------
    // Q14. Valid Parentheses
    // Example : "()[]{}" -> true, "(]" -> false
    // Idea    : Use a stack. Push every opening bracket.
    //           For a closing bracket, the top of the stack must be its pair.
    //           At the end the stack must be empty.
    // Time    : O(n)   Space : O(n)
    // Interviewer may ask: "Why a stack?"
    //   -> The LAST opened bracket must be the FIRST one closed (LIFO).
    // ---------------------------------------------------------------------
    static boolean isValidParentheses(String text) {
        Stack<Character> stack = new Stack<>();
        for (char c : text.toCharArray()) {
            if (c == '(' || c == '{' || c == '[') {
                stack.push(c);
            } else {
                if (stack.isEmpty()) {
                    return false;
                }
                char open = stack.pop();
                boolean pairMatches = (c == ')' && open == '(')
                        || (c == '}' && open == '{')
                        || (c == ']' && open == '[');
                if (!pairMatches) {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }

    // ---------------------------------------------------------------------
    // Q15. Merge Two Sorted Arrays
    // Example : {1, 3, 5} + {2, 4, 6} -> {1, 2, 3, 4, 5, 6}
    // Idea    : Put one finger on the start of each array. Take the smaller
    //           value, move that finger. When one array ends, copy the rest.
    // Time    : O(n + m)   Space : O(n + m)
    // ---------------------------------------------------------------------
    static int[] mergeSortedArrays(int[] first, int[] second) {
        int[] result = new int[first.length + second.length];
        int i = 0, j = 0, k = 0;
        while (i < first.length && j < second.length) {
            if (first[i] <= second[j]) {
                result[k++] = first[i++];
            } else {
                result[k++] = second[j++];
            }
        }
        while (i < first.length) {
            result[k++] = first[i++];
        }
        while (j < second.length) {
            result[k++] = second[j++];
        }
        return result;
    }

    // ---------------------------------------------------------------------
    // Q16. First Non-Repeating Character
    // Example : "swiss" -> 'w'
    // Idea    : Count every letter, then find the first letter with count 1.
    // ---------------------------------------------------------------------
    static char firstNonRepeating(String text) {
        Map<Character, Integer> count = charFrequency(text);
        for (char letter : text.toCharArray()) {
            if (count.get(letter) == 1) {
                return letter;
            }
        }
        return '-';   // none found
    }

    // ---------------------------------------------------------------------
    // Q17. GCD and LCM
    // Example : 12 and 18 -> GCD 6, LCM 36
    // Idea    : Euclid's rule: gcd(a, b) = gcd(b, a % b). Stop when b is 0.
    //           LCM = (a * b) / GCD.
    // ---------------------------------------------------------------------
    static int gcd(int a, int b) {
        while (b != 0) {
            int remainder = a % b;
            a = b;
            b = remainder;
        }
        return a;
    }

    static int lcm(int a, int b) {
        return (a * b) / gcd(a, b);
    }

    // ---------------------------------------------------------------------
    // Q18. Armstrong Number
    // Example : 153 -> 1^3 + 5^3 + 3^3 = 153 -> true
    // Idea    : Take each digit, raise it to the power of the number of digits,
    //           add them all. If the total equals the number, it is Armstrong.
    // ---------------------------------------------------------------------
    static boolean isArmstrong(int number) {
        int digitCount = String.valueOf(number).length();
        int total = 0;
        int temp = number;
        while (temp > 0) {
            int digit = temp % 10;                       // last digit
            total += (int) Math.pow(digit, digitCount);
            temp = temp / 10;                            // remove last digit
        }
        return total == number;
    }

    // ---------------------------------------------------------------------
    // Q19. Move All Zeros to the End
    // Example : {0, 1, 0, 3, 12} -> {1, 3, 12, 0, 0}
    // Idea    : Copy the non-zero numbers to the front, then fill zeros.
    // ---------------------------------------------------------------------
    static void moveZerosToEnd(int[] numbers) {
        int position = 0;
        for (int number : numbers) {
            if (number != 0) {
                numbers[position] = number;
                position++;
            }
        }
        while (position < numbers.length) {
            numbers[position] = 0;
            position++;
        }
    }

    // ---------------------------------------------------------------------
    // Q20. Reverse Words in a Sentence
    // Example : "I love Java" -> "Java love I"
    // Idea    : Split into words, then read the words from the back.
    // ---------------------------------------------------------------------
    static String reverseWords(String sentence) {
        String[] words = sentence.trim().split("\\s+");
        StringBuilder result = new StringBuilder();
        for (int i = words.length - 1; i >= 0; i--) {
            result.append(words[i]);
            if (i > 0) {
                result.append(" ");
            }
        }
        return result.toString();
    }

    // ---------------------------------------------------------------------
    // Q21. Majority Element (appears more than n/2 times)
    // Example : {2, 2, 1, 1, 2, 2, 3} -> 2
    // Idea    : Count each number with a map. Return the one whose count is
    //           more than half of the array size.
    // ---------------------------------------------------------------------
    static int majorityElement(int[] numbers) {
        Map<Integer, Integer> count = new HashMap<>();
        for (int number : numbers) {
            count.put(number, count.getOrDefault(number, 0) + 1);
        }
        for (Map.Entry<Integer, Integer> entry : count.entrySet()) {
            if (entry.getValue() > numbers.length / 2) {
                return entry.getKey();
            }
        }
        return -1;
    }

    // ---------------------------------------------------------------------
    // Q22. Climbing Stairs (1 or 2 steps at a time) - basic DP
    // Example : 5 stairs -> 8 ways
    // Idea    : Ways to reach stair n = ways to reach (n-1) + ways to reach (n-2).
    //           It is the Fibonacci pattern again.
    // ---------------------------------------------------------------------
    static int climbStairs(int n) {
        if (n <= 2) {
            return n;
        }
        int waysTwoBack = 1;   // ways for stair 1
        int waysOneBack = 2;   // ways for stair 2
        for (int stair = 3; stair <= n; stair++) {
            int ways = waysOneBack + waysTwoBack;
            waysTwoBack = waysOneBack;
            waysOneBack = ways;
        }
        return waysOneBack;
    }

    // ---------------------------------------------------------------------
    // Q23. Longest Substring Without Repeating Characters (sliding window)
    // Example : "abcabcbb" -> 3 ("abc")
    // Idea    : Keep a "window" of unique letters using a Set.
    //           Move the right end forward. If the new letter is already in the
    //           window, shrink from the left until it is gone.
    // Time    : O(n)   Space : O(k)
    // ---------------------------------------------------------------------
    static int longestUniqueSubstring(String text) {
        Set<Character> window = new HashSet<>();
        int left = 0;
        int longest = 0;
        for (int right = 0; right < text.length(); right++) {
            while (window.contains(text.charAt(right))) {
                window.remove(text.charAt(left));
                left++;
            }
            window.add(text.charAt(right));
            longest = Math.max(longest, right - left + 1);
        }
        return longest;
    }

    // ---------------------------------------------------------------------
    // Q24. Binary Search
    // Example : {1, 3, 5, 7, 9}, target 7 -> index 3
    // Idea    : The array MUST be sorted. Look at the middle element.
    //           Target is bigger -> search the right half. Smaller -> left half.
    //           Each step throws away half the array.
    // Time    : O(log n)   Space : O(1)
    // Interviewer may ask: "Why low + (high - low) / 2 and not (low + high) / 2?"
    //   -> low + high can overflow int for very large arrays.
    // ---------------------------------------------------------------------
    static int binarySearch(int[] sorted, int target) {
        int low = 0;
        int high = sorted.length - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (sorted[mid] == target) {
                return mid;
            } else if (sorted[mid] < target) {
                low = mid + 1;       // go right
            } else {
                high = mid - 1;      // go left
            }
        }
        return -1;                   // not found
    }

    // ---------------------------------------------------------------------
    // Q25. Reverse a Number, Sum of Digits, Palindrome Number
    // Example : 1234 -> reverse 4321, digit sum 10.  121 is a palindrome number.
    // Idea    : number % 10 gives the LAST digit. number / 10 removes it.
    // ---------------------------------------------------------------------
    static int reverseNumber(int number) {
        int reversed = 0;
        while (number > 0) {
            int lastDigit = number % 10;
            reversed = reversed * 10 + lastDigit;
            number = number / 10;
        }
        return reversed;
    }

    static int sumOfDigits(int number) {
        int sum = 0;
        while (number > 0) {
            sum += number % 10;
            number = number / 10;
        }
        return sum;
    }

    static boolean isPalindromeNumber(int number) {
        return number == reverseNumber(number);
    }

    // ---------------------------------------------------------------------
    // Q26. Count Vowels and Consonants
    // Example : "Hello World" -> 3 vowels, 7 consonants
    // Idea    : Look at every letter. If it is in "aeiou" it is a vowel.
    // ---------------------------------------------------------------------
    static int[] countVowelsAndConsonants(String text) {
        int vowels = 0;
        int consonants = 0;
        for (char c : text.toLowerCase().toCharArray()) {
            if (Character.isLetter(c)) {
                if ("aeiou".indexOf(c) >= 0) {
                    vowels++;
                } else {
                    consonants++;
                }
            }
        }
        return new int[]{vowels, consonants};
    }

    // ---------------------------------------------------------------------
    // Q27. Rotate Array to the Right by k
    // Example : {1,2,3,4,5,6,7}, k = 3 -> {5,6,7,1,2,3,4}
    // Idea    : The element at index i moves to index (i + k) % length.
    //           % also handles k bigger than the array length.
    // Time    : O(n)   Space : O(n)
    // ---------------------------------------------------------------------
    static int[] rotateRight(int[] numbers, int k) {
        int n = numbers.length;
        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            result[(i + k) % n] = numbers[i];
        }
        return result;
    }

    // ---------------------------------------------------------------------
    // Q28. Leaders in an Array (bigger than everything on its right)
    // Example : {16, 17, 4, 3, 5, 2} -> [17, 5, 2]
    // Idea    : Walk from the RIGHT. Remember the biggest number seen so far.
    //           A number bigger than that is a leader.
    // ---------------------------------------------------------------------
    static List<Integer> leaders(int[] numbers) {
        List<Integer> result = new ArrayList<>();
        int biggestOnRight = Integer.MIN_VALUE;
        for (int i = numbers.length - 1; i >= 0; i--) {
            if (numbers[i] > biggestOnRight) {
                result.add(numbers[i]);
                biggestOnRight = numbers[i];
            }
        }
        Collections.reverse(result);     // we collected from the right, so flip back
        return result;
    }

    // ---------------------------------------------------------------------
    // Q29. Reverse a Linked List (asked in almost every Java interview)
    // Example : 1 -> 2 -> 3 -> null   becomes   3 -> 2 -> 1 -> null
    // Idea    : Walk the list. For each node, point its "next" backwards to the
    //           previous node. Keep three variables: previous, current, nextNode.
    // Time    : O(n)   Space : O(1)
    // ---------------------------------------------------------------------
    static class Node {
        int value;
        Node next;

        Node(int value) {
            this.value = value;
        }
    }

    static Node reverseLinkedList(Node head) {
        Node previous = null;
        Node current = head;
        while (current != null) {
            Node nextNode = current.next;   // 1. remember the rest of the list
            current.next = previous;        // 2. turn the arrow backwards
            previous = current;             // 3. move previous forward
            current = nextNode;             // 4. move current forward
        }
        return previous;                    // previous is the new head
    }

    static String listToString(Node head) {
        StringBuilder text = new StringBuilder();
        for (Node n = head; n != null; n = n.next) {
            text.append(n.value).append(n.next != null ? " -> " : "");
        }
        return text.toString();
    }

    // ---------------------------------------------------------------------
    // Q30. Bubble Sort
    // Example : {5, 2, 9, 1} -> {1, 2, 5, 9}
    // Idea    : Repeatedly compare neighbours and swap if they are in the wrong
    //           order. The biggest value "bubbles" to the end each round.
    //           If a full round has no swap, the array is already sorted - stop.
    // Time    : O(n^2) worst, O(n) best   Space : O(1)
    // Interviewer may ask: "Which sort does Arrays.sort use?"
    //   -> Dual-pivot Quicksort for primitives, TimSort (merge-based) for objects.
    // ---------------------------------------------------------------------
    static void bubbleSort(int[] numbers) {
        for (int round = 0; round < numbers.length - 1; round++) {
            boolean swapped = false;
            for (int i = 0; i < numbers.length - 1 - round; i++) {
                if (numbers[i] > numbers[i + 1]) {
                    int temp = numbers[i];
                    numbers[i] = numbers[i + 1];
                    numbers[i + 1] = temp;
                    swapped = true;
                }
            }
            if (!swapped) {
                break;
            }
        }
    }

    // ---------------------------------------------------------------------
    // Q31. Common Elements of Two Arrays
    // Example : {1,2,3,4} and {3,4,5,6} -> [3, 4]
    // Idea    : Put the first array in a Set. Check each element of the second.
    // Time    : O(n + m)   Space : O(n)
    // ---------------------------------------------------------------------
    static List<Integer> commonElements(int[] first, int[] second) {
        Set<Integer> firstSet = new HashSet<>();
        for (int number : first) {
            firstSet.add(number);
        }
        Set<Integer> common = new LinkedHashSet<>();
        for (int number : second) {
            if (firstSet.contains(number)) {
                common.add(number);
            }
        }
        return new ArrayList<>(common);
    }

    // ---------------------------------------------------------------------
    // Q32. Find Duplicates in an Array
    // Example : {4, 3, 2, 4, 3, 5} -> [4, 3]
    // Idea    : Remember what you have seen. If you see it again, it is a duplicate.
    //           Set.add() returns false when the value is already present.
    // ---------------------------------------------------------------------
    static List<Integer> findDuplicates(int[] numbers) {
        Set<Integer> seen = new HashSet<>();
        Set<Integer> duplicates = new LinkedHashSet<>();
        for (int number : numbers) {
            if (!seen.add(number)) {
                duplicates.add(number);
            }
        }
        return new ArrayList<>(duplicates);
    }

    // ---------------------------------------------------------------------
    // Q33. Compress a String
    // Example : "aaabbc" -> "a3b2c1"
    // Idea    : Count how many times the same letter repeats in a row.
    //           When the letter changes, write the letter and its count.
    // ---------------------------------------------------------------------
    static String compressString(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        StringBuilder result = new StringBuilder();
        int count = 1;
        for (int i = 1; i <= text.length(); i++) {
            if (i < text.length() && text.charAt(i) == text.charAt(i - 1)) {
                count++;
            } else {
                result.append(text.charAt(i - 1)).append(count);
                count = 1;
            }
        }
        return result.toString();
    }

    // ---------------------------------------------------------------------
    // Q34. Longest Common Prefix
    // Example : {"flower", "flow", "flight"} -> "fl"
    // Idea    : Take the first word as the prefix. While another word does not
    //           start with it, cut the last letter off the prefix.
    // ---------------------------------------------------------------------
    static String longestCommonPrefix(String[] words) {
        if (words == null || words.length == 0) {
            return "";
        }
        String prefix = words[0];
        for (String word : words) {
            while (!word.startsWith(prefix)) {
                prefix = prefix.substring(0, prefix.length() - 1);
            }
        }
        return prefix;
    }

    // ---------------------------------------------------------------------
    // Q35. LRU Cache (7 years experience favourite - "design a cache")
    // LRU = Least Recently Used. When the cache is full, remove the item that
    // was used the longest time ago.
    // Idea    : LinkedHashMap can keep items in ACCESS order (third constructor
    //           argument = true). removeEldestEntry is called after every put;
    //           returning true removes the oldest item.
    // Time    : O(1) for get and put
    // Interviewer may ask: "How would you build it without LinkedHashMap?"
    //   -> HashMap + doubly linked list. The map gives O(1) lookup, the list
    //      gives O(1) move-to-front and remove-last.
    // ---------------------------------------------------------------------
    static class LruCache<K, V> extends LinkedHashMap<K, V> {
        private final int capacity;

        LruCache(int capacity) {
            super(capacity, 0.75f, true);   // true = order by last access
            this.capacity = capacity;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > capacity;       // too many items -> drop the oldest
        }
    }

    // ---------------------------------------------------------------------
    // Q36. Group Anagrams
    // Example : {"eat","tea","tan","ate","nat","bat"} -> [[eat,tea,ate],[tan,nat],[bat]]
    // Idea    : Words that are anagrams become the SAME word when their letters
    //           are sorted ("eat" -> "aet"). Use the sorted word as a map key.
    // Time    : O(n * k log k)  (k = word length)
    // ---------------------------------------------------------------------
    static List<List<String>> groupAnagrams(String[] words) {
        Map<String, List<String>> groups = new LinkedHashMap<>();
        for (String word : words) {
            char[] letters = word.toCharArray();
            Arrays.sort(letters);
            String key = new String(letters);
            if (!groups.containsKey(key)) {
                groups.put(key, new ArrayList<>());
            }
            groups.get(key).add(word);
        }
        return new ArrayList<>(groups.values());
    }

    // ---------------------------------------------------------------------
    // Q37. Print a Pyramid Pattern (TCS / Wipro love pattern questions)
    //        *
    //       ***
    //      *****
    // Idea    : For row i: (rows - i) spaces, then (2 * i - 1) stars.
    // ---------------------------------------------------------------------
    static void printPyramid(int rows) {
        for (int i = 1; i <= rows; i++) {
            StringBuilder line = new StringBuilder();
            for (int space = 0; space < rows - i; space++) {
                line.append(" ");
            }
            for (int star = 0; star < 2 * i - 1; star++) {
                line.append("*");
            }
            System.out.println(line);
        }
    }

    // ---------------------------------------------------------------------
    // Q38. Swap Two Numbers Without a Third Variable
    // Example : a = 5, b = 9 -> a = 9, b = 5
    // Idea    : a = a + b; b = a - b; a = a - b.
    // ---------------------------------------------------------------------
    static int[] swapWithoutTemp(int a, int b) {
        a = a + b;
        b = a - b;
        a = a - b;
        return new int[]{a, b};
    }

    // ---------------------------------------------------------------------
    // Q39. Check if Two Strings Are Rotations of Each Other
    // Example : "abcd" and "cdab" -> true
    // Idea    : If you write the first string twice ("abcdabcd"), every rotation
    //           of it is hiding inside.
    // ---------------------------------------------------------------------
    static boolean isRotation(String first, String second) {
        return first.length() == second.length() && (first + first).contains(second);
    }

    // ---------------------------------------------------------------------
    // Q40. Transpose a Matrix
    // Example : {{1,2,3},{4,5,6}} -> {{1,4},{2,5},{3,6}}
    // Idea    : Row and column swap places: result[col][row] = matrix[row][col].
    // ---------------------------------------------------------------------
    static int[][] transpose(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        int[][] result = new int[cols][rows];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                result[c][r] = matrix[r][c];
            }
        }
        return result;
    }

    // =====================================================================
    // PART 2 : STREAM API (Java 8)
    // =====================================================================
    //
    // WHAT IS A STREAM? (say this in the interview)
    //   A stream is a pipeline to process data from a collection:
    //       source  ->  intermediate steps  ->  terminal step
    //       list        filter, map, sorted     collect, count, forEach
    //   - Intermediate steps are LAZY: nothing runs until the terminal step.
    //   - A stream can be used only ONCE.
    //   - A stream does NOT change the original list.
    //
    // Most used methods:
    //   filter(x -> condition)   keep only matching items
    //   map(x -> something)      change each item into something else
    //   sorted()                 sort
    //   distinct()               remove duplicates
    //   limit(n) / skip(n)       take first n / skip first n
    //   collect(...)             put the result into a List / Set / Map
    //   count(), sum(), max()    give a single result
    //
    // Employee class used in the examples below.
    // ---------------------------------------------------------------------

    static class Employee {
        private final String name;
        private final String department;
        private final double salary;
        private final int age;

        Employee(String name, String department, double salary, int age) {
            this.name = name;
            this.department = department;
            this.salary = salary;
            this.age = age;
        }

        String getName() { return name; }
        String getDepartment() { return department; }
        double getSalary() { return salary; }
        int getAge() { return age; }

        @Override
        public String toString() {
            return name + "(" + department + ", " + salary + ")";
        }
    }

    static void streamExamples() {
        List<Employee> employees = Arrays.asList(
                new Employee("Amit", "IT", 90000, 32),
                new Employee("Neha", "IT", 120000, 38),
                new Employee("Ravi", "HR", 50000, 29),
                new Employee("Sara", "HR", 65000, 35),
                new Employee("John", "Finance", 80000, 41),
                new Employee("Priya", "Finance", 80000, 27));

        List<Integer> numbers = Arrays.asList(5, 3, 8, 3, 9, 1, 5, 7, 2, 8);

        // S1. Names of employees with salary above 75000
        //     filter = keep matching, map = take only the name.
        List<String> richNames = employees.stream()
                .filter(e -> e.getSalary() > 75000)
                .map(e -> e.getName())
                .collect(Collectors.toList());
        print("S1  salary > 75000", richNames);

        // S2. Square of every number, then sum
        //     Interviewer: "What is the difference between map and forEach?"
        //     map returns a new stream; forEach is a final step that returns nothing.
        int sumOfSquares = numbers.stream()
                .map(n -> n * n)
                .mapToInt(n -> n)
                .sum();
        print("S2  sum of squares", sumOfSquares);

        // S3. Only even numbers, sorted, no duplicates
        List<Integer> evens = numbers.stream()
                .filter(n -> n % 2 == 0)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        print("S3  even, distinct, sorted", evens);

        // S4. Maximum, minimum, count, average
        print("S4  max", numbers.stream().max(Integer::compare).get());
        print("S4  min", numbers.stream().min(Integer::compare).get());
        print("S4  count", numbers.stream().count());
        print("S4  average", numbers.stream().mapToInt(n -> n).average().getAsDouble());

        // S5. Group employees by department
        //     groupingBy gives a Map<department, list of employees>.
        Map<String, List<Employee>> byDepartment = employees.stream()
                .collect(Collectors.groupingBy(e -> e.getDepartment()));
        print("S5  group by department", byDepartment);

        // S6. Count of employees in each department
        Map<String, Long> countByDepartment = employees.stream()
                .collect(Collectors.groupingBy(e -> e.getDepartment(), Collectors.counting()));
        print("S6  count per department", countByDepartment);

        // S7. Average salary of each department
        Map<String, Double> avgSalary = employees.stream()
                .collect(Collectors.groupingBy(e -> e.getDepartment(),
                        Collectors.averagingDouble(e -> e.getSalary())));
        print("S7  average salary", avgSalary);

        // S8. Highest salary in the company (very common)
        Employee highest = employees.stream()
                .max(Comparator.comparingDouble(e -> e.getSalary()))
                .get();
        print("S8  highest paid", highest);

        // S9. SECOND highest salary (very common)
        //     Steps: take salaries -> remove duplicates -> sort high to low
        //            -> skip the first -> take the next one.
        double secondHighest = employees.stream()
                .map(e -> e.getSalary())
                .distinct()
                .sorted(Comparator.reverseOrder())
                .skip(1)
                .findFirst()
                .get();
        print("S9  second highest salary", secondHighest);

        // S10. Sort employees by salary, high to low
        List<Employee> sortedBySalary = employees.stream()
                .sorted(Comparator.comparingDouble((Employee e) -> e.getSalary()).reversed())
                .collect(Collectors.toList());
        print("S10 sorted by salary desc", sortedBySalary);

        // S11. Split into two groups: age above 30 and not above 30
        //      partitioningBy always gives exactly two groups: true and false.
        Map<Boolean, List<Employee>> partition = employees.stream()
                .collect(Collectors.partitioningBy(e -> e.getAge() > 30));
        print("S11 age > 30", partition.get(true));
        print("S11 age <= 30", partition.get(false));

        // S12. Join names into one string
        String allNames = employees.stream()
                .map(e -> e.getName())
                .collect(Collectors.joining(", "));
        print("S12 joined names", allNames);

        // S13. Find duplicate numbers in a list
        //      Count each number; keep those that appear more than once.
        Map<Integer, Long> numberCount = numbers.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        List<Integer> duplicates = numberCount.entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .map(entry -> entry.getKey())
                .collect(Collectors.toList());
        print("S13 duplicates", duplicates);

        // S14. Character frequency of a string
        //      chars() gives int codes; mapToObj changes them back to Character.
        Map<Character, Long> letterCount = "programming".chars()
                .mapToObj(c -> (char) c)
                .collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()));
        print("S14 letter count", letterCount);

        // S15. First non-repeating character using streams
        Character firstUnique = letterCount.entrySet().stream()
                .filter(entry -> entry.getValue() == 1)
                .map(entry -> entry.getKey())
                .findFirst()
                .get();
        print("S15 first non-repeating", firstUnique);

        // S16. Flatten a list of lists into one list
        //      map would give a stream of lists. flatMap opens each list.
        List<List<Integer>> listOfLists = Arrays.asList(
                Arrays.asList(1, 2), Arrays.asList(3, 4), Arrays.asList(5));
        List<Integer> flat = listOfLists.stream()
                .flatMap(list -> list.stream())
                .collect(Collectors.toList());
        print("S16 flatMap", flat);

        // S17. Check conditions on all items
        print("S17 anyMatch salary > 100000", employees.stream().anyMatch(e -> e.getSalary() > 100000));
        print("S17 allMatch salary > 40000", employees.stream().allMatch(e -> e.getSalary() > 40000));
        print("S17 noneMatch age < 20", employees.stream().noneMatch(e -> e.getAge() < 20));

        // S18. Convert list to map (name -> salary)
        //      If two keys are the same, toMap throws IllegalStateException
        //      unless you give a "merge" rule as a third argument.
        Map<String, Double> nameToSalary = employees.stream()
                .collect(Collectors.toMap(e -> e.getName(), e -> e.getSalary()));
        print("S18 name -> salary", nameToSalary);

        // S19. Top 3 highest paid employees
        List<Employee> top3 = employees.stream()
                .sorted(Comparator.comparingDouble((Employee e) -> e.getSalary()).reversed())
                .limit(3)
                .collect(Collectors.toList());
        print("S19 top 3", top3);

        // S20. Palindrome, factorial, prime using streams
        String word = "madam";
        boolean isPal = word.equals(new StringBuilder(word).reverse().toString());
        print("S20 palindrome(madam)", isPal);

        int factorial5 = IntStream.rangeClosed(1, 5).reduce(1, (a, b) -> a * b);
        print("S20 factorial(5)", factorial5);

        int candidate = 29;
        boolean prime = candidate > 1
                && IntStream.rangeClosed(2, (int) Math.sqrt(candidate)).noneMatch(i -> candidate % i == 0);
        print("S20 isPrime(29)", prime);

        // S21. Word count in a sentence
        String sentence = "java is fun and java is powerful";
        Map<String, Long> wordCount = Arrays.stream(sentence.split(" "))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        print("S21 word count", wordCount);

        // S22. Highest paid employee in EACH department
        //      maxBy gives an Optional because a group could be empty in theory.
        Map<String, Optional<Employee>> topPerDepartment = employees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment,
                        Collectors.maxBy(Comparator.comparingDouble(Employee::getSalary))));
        topPerDepartment.forEach((dept, emp) -> print("S22 top in " + dept, emp.get().getName()));

        // S23. Nth highest salary (here N = 3). Same idea as S9, but skip(N - 1).
        int rank = 3;
        double nthSalary = employees.stream()
                .map(e -> e.getSalary())
                .distinct()
                .sorted(Comparator.reverseOrder())
                .skip(rank - 1)
                .findFirst()
                .get();
        print("S23 3rd highest salary", nthSalary);

        // S24. Total salary of each department
        Map<String, Double> totalPerDepartment = employees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment,
                        Collectors.summingDouble(Employee::getSalary)));
        print("S24 total salary per dept", totalPerDepartment);

        // S25. Sort by department (A-Z), and inside a department by salary (high to low)
        List<Employee> twoLevelSort = employees.stream()
                .sorted(Comparator.comparing(Employee::getDepartment)
                        .thenComparing(Employee::getSalary, Comparator.reverseOrder()))
                .collect(Collectors.toList());
        print("S25 dept asc, salary desc", twoLevelSort);

        // S26. Min, max, sum, average, count in ONE call
        IntSummaryStatistics stats = numbers.stream().mapToInt(n -> n).summaryStatistics();
        print("S26 statistics", stats);

        // S27. Names of employees in each department
        //      mapping = "after grouping, change each employee into just the name".
        Map<String, List<String>> namesPerDepartment = employees.stream()
                .collect(Collectors.groupingBy(Employee::getDepartment,
                        Collectors.mapping(Employee::getName, Collectors.toList())));
        print("S27 names per dept", namesPerDepartment);

        // S28. String list practice
        List<String> fruits = Arrays.asList("banana", "apple", "cherry", "apple", "date");
        print("S28 unique, short first", fruits.stream()
                .distinct()
                .sorted(Comparator.comparingInt(String::length).thenComparing(Comparator.naturalOrder()))
                .collect(Collectors.toList()));
        print("S28 upper case", fruits.stream().map(String::toUpperCase).collect(Collectors.toList()));
        print("S28 longest fruit", fruits.stream().max(Comparator.comparingInt(String::length)).get());

        // S29. Numbers that start with digit 1 (convert to String first)
        List<Integer> mixed = Arrays.asList(10, 15, 8, 49, 25, 98, 32);
        print("S29 start with 1", mixed.stream()
                .map(String::valueOf)
                .filter(s -> s.startsWith("1"))
                .collect(Collectors.toList()));

        // S30. Reverse every word in a sentence (keep word order)
        print("S30 reverse each word", Arrays.stream(sentence.split(" "))
                .map(w -> new StringBuilder(w).reverse().toString())
                .collect(Collectors.joining(" ")));

        // S31. Generate a sequence: 1, 2, 4, 8 ... (iterate needs limit, or it runs forever)
        print("S31 powers of 2", Stream.iterate(1, x -> x * 2).limit(8).collect(Collectors.toList()));

        // S32. Total of all salaries, and a parallel sum
        //      mapToDouble avoids boxing, so it is faster than map + reduce.
        print("S32 total salary", employees.stream().mapToDouble(Employee::getSalary).sum());
        print("S32 parallel sum 1..1000", IntStream.rangeClosed(1, 1000).parallel().sum());

        // S33. peek() is only for debugging - it shows the pipeline is LAZY and runs
        //      one element at a time through all steps.
        List<Integer> lazyDemo = Arrays.asList(1, 2, 3, 4);
        lazyDemo.stream()
                .peek(x -> System.out.println("S33 before filter: " + x))
                .filter(x -> x % 2 == 0)
                .peek(x -> System.out.println("S33 after filter : " + x))
                .collect(Collectors.toList());
    }

    // =====================================================================
    // PART 3 : Theory questions interviewers ask along with the code
    // =====================================================================
    //
    // Q: Collection vs Stream?
    // A: Collection STORES data. Stream PROCESSES data and stores nothing.
    //
    // Q: map vs flatMap?
    // A: map changes one item into one item. flatMap changes one item into many
    //    items and joins them into a single stream.
    //
    // Q: Intermediate vs terminal operations?
    // A: Intermediate (filter, map, sorted) return another stream and are lazy.
    //    Terminal (collect, count, forEach) give the result and start the work.
    //
    // Q: groupingBy vs partitioningBy?
    // A: groupingBy can make many groups. partitioningBy makes exactly two
    //    (true and false).
    //
    // Q: Can you reuse a stream?
    // A: No. After a terminal operation it is closed. Reuse throws
    //    IllegalStateException. Create a new stream from the list.
    //
    // Q: What is parallelStream()?
    // A: Splits the work across CPU cores. Good for big, independent
    //    calculations. Not good for small lists or code that changes shared data.
    //
    // Q: findFirst vs findAny?
    // A: Same result in a normal stream. findAny can be faster in a parallel
    //    stream because it does not care about order.
    //
    // Q: What is Optional?
    // A: A box that may or may not hold a value. It avoids NullPointerException.
    //    Use orElse(), ifPresent(), map() instead of calling get() blindly.

    // =====================================================================
    // RUNNER - prints the answer for every question
    // =====================================================================

    private static void print(String label, Object value) {
        System.out.println(label + " => " + value);
    }

    public static void main(String[] args) {
        System.out.println("========== PART 1 : DSA ==========");
        print("Q1  reverse(hello)", reverseString("hello"));                       // olleh
        print("Q2  palindrome(racecar)", isPalindrome("racecar"));                 // true
        print("Q3  isPrime(17)", isPrime(17));                                     // true
        print("Q4  fibonacci(10)", fibonacci(10));                                 // 0 1 1 2 3 5 8 13 21 34
        print("Q5  factorial(5)", factorial(5));                                   // 120
        print("Q6  secondLargest", secondLargest(new int[]{10, 5, 8, 20, 15}));    // 15
        print("Q7  removeDuplicates", Arrays.toString(removeDuplicates(new int[]{1, 2, 2, 3, 4, 4, 5})));
        print("Q8  twoSum", Arrays.toString(twoSum(new int[]{2, 7, 11, 15}, 9)));  // [0, 1]
        print("Q9  anagram(listen, silent)", isAnagram("listen", "silent"));       // true
        print("Q10 charFrequency(hello)", charFrequency("hello"));                 // {h=1, e=1, l=2, o=1}

        int[] colors = {2, 0, 1, 2, 0, 1};
        sortZeroOneTwo(colors);
        print("Q11 sort 0/1/2", Arrays.toString(colors));                          // [0, 0, 1, 1, 2, 2]

        print("Q12 missingNumber", missingNumber(new int[]{1, 2, 4, 5}, 5));       // 3
        print("Q13 maxSubarraySum", maxSubarraySum(new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4})); // 6
        print("Q14 valid ()[]{}", isValidParentheses("()[]{}"));                   // true
        print("Q14 valid (]", isValidParentheses("(]"));                           // false
        print("Q15 merge sorted", Arrays.toString(mergeSortedArrays(new int[]{1, 3, 5}, new int[]{2, 4, 6})));
        print("Q16 firstNonRepeating(swiss)", firstNonRepeating("swiss"));          // w
        print("Q17 gcd / lcm (12, 18)", gcd(12, 18) + " / " + lcm(12, 18));        // 6 / 36
        print("Q18 armstrong(153)", isArmstrong(153));                             // true

        int[] withZeros = {0, 1, 0, 3, 12};
        moveZerosToEnd(withZeros);
        print("Q19 moveZeros", Arrays.toString(withZeros));                        // [1, 3, 12, 0, 0]

        print("Q20 reverseWords", reverseWords("I love Java"));                    // Java love I
        print("Q21 majority", majorityElement(new int[]{2, 2, 1, 1, 2, 2, 3}));     // 2
        print("Q22 climbStairs(5)", climbStairs(5));                               // 8
        print("Q23 longestUniqueSubstring(abcabcbb)", longestUniqueSubstring("abcabcbb")); // 3
        print("Q24 binarySearch(7)", binarySearch(new int[]{1, 3, 5, 7, 9}, 7));    // 3
        print("Q25 reverse/sum/palindrome(121)",
                reverseNumber(1234) + " / " + sumOfDigits(1234) + " / " + isPalindromeNumber(121)); // 4321 / 10 / true
        print("Q26 vowels, consonants", Arrays.toString(countVowelsAndConsonants("Hello World"))); // [3, 7]
        print("Q27 rotateRight(3)", Arrays.toString(rotateRight(new int[]{1, 2, 3, 4, 5, 6, 7}, 3))); // [5,6,7,1,2,3,4]
        print("Q28 leaders", leaders(new int[]{16, 17, 4, 3, 5, 2}));                // [17, 5, 2]

        Node head = new Node(1);
        head.next = new Node(2);
        head.next.next = new Node(3);
        print("Q29 linked list before", listToString(head));                         // 1 -> 2 -> 3
        print("Q29 linked list after ", listToString(reverseLinkedList(head)));      // 3 -> 2 -> 1

        int[] toSort = {5, 2, 9, 1};
        bubbleSort(toSort);
        print("Q30 bubbleSort", Arrays.toString(toSort));                            // [1, 2, 5, 9]
        print("Q31 common", commonElements(new int[]{1, 2, 3, 4}, new int[]{3, 4, 5, 6})); // [3, 4]
        print("Q32 duplicates", findDuplicates(new int[]{4, 3, 2, 4, 3, 5}));         // [4, 3]
        print("Q33 compress(aaabbc)", compressString("aaabbc"));                      // a3b2c1
        print("Q34 commonPrefix", longestCommonPrefix(new String[]{"flower", "flow", "flight"})); // fl

        LruCache<Integer, String> cache = new LruCache<>(2);
        cache.put(1, "A");
        cache.put(2, "B");
        cache.get(1);                  // 1 is now the most recently used
        cache.put(3, "C");             // cache is full, so 2 (least recently used) is removed
        print("Q35 LRU cache", cache);                                               // {1=A, 3=C}

        print("Q36 groupAnagrams", groupAnagrams(new String[]{"eat", "tea", "tan", "ate", "nat", "bat"}));
        System.out.println("Q37 pyramid:");
        printPyramid(3);
        print("Q38 swap(5, 9)", Arrays.toString(swapWithoutTemp(5, 9)));              // [9, 5]
        print("Q39 isRotation(abcd, cdab)", isRotation("abcd", "cdab"));              // true
        print("Q40 transpose", Arrays.deepToString(transpose(new int[][]{{1, 2, 3}, {4, 5, 6}}))); // [[1, 4], [2, 5], [3, 6]]

        System.out.println();
        System.out.println("========== PART 2 : STREAM API ==========");
        streamExamples();
    }
}
