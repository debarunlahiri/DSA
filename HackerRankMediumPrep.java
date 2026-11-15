import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * ONLINE CODING ASSESSMENT - MEDIUM LEVEL (HackerRank / LeetCode style).
 *
 * These are the "second question" problems in TCS Digital / Prime, Infosys
 * Specialist Programmer, Cognizant GenC Next, Wipro Turbo, HCL, and in
 * experienced-hire rounds. They are harder than HackerRankStylePrep.java.
 *
 * SAME RULES AS BEFORE
 *   - Read from standard input, print to standard output, no extra text.
 *   - main() asks for a problem number first, so you can practise any problem:
 *         javac HackerRankMediumPrep.java
 *         java HackerRankMediumPrep
 *         (type the problem number, then the sample input)
 *     In the real test, copy only the body of one method into main() and remove
 *     the problem-number line.
 *
 * THE 10 PATTERNS (recognise the pattern and the problem becomes easy)
 *   M01-M07   Sliding window / two pointers / prefix sums
 *   M08-M11   HashMap, sorting, heap (PriorityQueue)
 *   M12-M13   Grid BFS, binary search variant
 *   M14-M22   Dynamic programming (LIS, coins, LCS, edit distance, knapsack ...)
 *   M23-M25   Backtracking (generate all combinations)
 *   M26-M27   Trees and queue design
 *   M28-M30   Graphs (Dijkstra, union-find, topological sort)
 *   M31-M35   Stack / monotonic stack / deque
 *   M36-M37   Design problems (LRU, frequency queries)
 *   M38-M45   HackerRank classics and interval / greedy problems
 *
 * HOW TO PICK AN APPROACH (say this out loud in the interview)
 *   "Brute force is O(n^2). The constraint says n up to 10^5, so I need O(n) or
 *    O(n log n). I will use a HashMap / two pointers / sorting / heap."
 */
public class HackerRankMediumPrep {

    // ---------- small helpers ----------

    static int[] readInts(Scanner sc, int n) {
        int[] numbers = new int[n];
        for (int i = 0; i < n; i++) {
            numbers[i] = sc.nextInt();
        }
        return numbers;
    }

    static String join(int[] numbers) {
        return Arrays.stream(numbers).mapToObj(String::valueOf).collect(Collectors.joining(" "));
    }

    static String join(List<?> list) {
        return list.stream().map(String::valueOf).collect(Collectors.joining(" "));
    }

    // =====================================================================
    // SLIDING WINDOW / TWO POINTERS / PREFIX SUM
    // =====================================================================

    // ---------------------------------------------------------------------
    // M01. Longest Substring Without Repeating Characters
    // Input : one string                   Sample: abcabcbb
    // Output: length of the longest part with all different letters   -> 3
    // Idea  : Sliding window. Move the right end. If the letter is already in the
    //         window, move the left end forward until it is gone.
    // Time  : O(n)
    // ---------------------------------------------------------------------
    static void m01(Scanner sc) {
        String text = sc.next();
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
        System.out.println(longest);
    }

    // ---------------------------------------------------------------------
    // M02. Subarray Sum Equals K
    // Input : n, the array, then k         Sample: 3 / 1 1 1 / 2
    // Output: number of continuous parts whose sum is k      -> 2
    // Idea  : Prefix sum. If (prefix now - k) was seen before, then the part
    //         between that point and now sums to k. Count how often it was seen.
    //         Works with negative numbers too (sliding window does not).
    // Time  : O(n)   Space : O(n)
    // ---------------------------------------------------------------------
    static void m02(Scanner sc) {
        int n = sc.nextInt();
        int[] numbers = readInts(sc, n);
        long k = sc.nextLong();
        Map<Long, Integer> seen = new HashMap<>();
        seen.put(0L, 1);                         // empty prefix
        long prefix = 0;
        long count = 0;
        for (int number : numbers) {
            prefix += number;
            count += seen.getOrDefault(prefix - k, 0);
            seen.put(prefix, seen.getOrDefault(prefix, 0) + 1);
        }
        System.out.println(count);
    }

    // ---------------------------------------------------------------------
    // M03. Longest Palindromic Substring
    // Input : one string                   Sample: babad
    // Output: the longest palindrome (first one found)       -> bab
    // Idea  : Every palindrome has a centre. Try each position as a centre
    //         (odd length) and each gap between letters (even length) and expand
    //         outward while both sides match.
    // Time  : O(n^2)
    // ---------------------------------------------------------------------
    static void m03(Scanner sc) {
        String text = sc.next();
        int bestStart = 0;
        int bestLength = 1;
        for (int centre = 0; centre < text.length(); centre++) {
            for (int type = 0; type < 2; type++) {          // 0 = odd length, 1 = even length
                int left = centre;
                int right = centre + type;
                while (left >= 0 && right < text.length() && text.charAt(left) == text.charAt(right)) {
                    left--;
                    right++;
                }
                int length = right - left - 1;              // loop went one step too far
                if (length > bestLength) {
                    bestLength = length;
                    bestStart = left + 1;
                }
            }
        }
        System.out.println(text.substring(bestStart, bestStart + bestLength));
    }

    // ---------------------------------------------------------------------
    // M04. Merge Intervals
    // Input : n, then n lines "start end"  Sample: 4 / 1 3 / 2 6 / 8 10 / 15 18
    // Output: merged intervals, one per line     -> 1 6 / 8 10 / 15 18
    // Idea  : Sort by start. If the next interval starts before the current one
    //         ends, they overlap - stretch the end. Otherwise start a new one.
    // Time  : O(n log n)
    // ---------------------------------------------------------------------
    static void m04(Scanner sc) {
        int n = sc.nextInt();
        int[][] intervals = new int[n][2];
        for (int i = 0; i < n; i++) {
            intervals[i][0] = sc.nextInt();
            intervals[i][1] = sc.nextInt();
        }
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
        int start = intervals[0][0];
        int end = intervals[0][1];
        for (int i = 1; i < n; i++) {
            if (intervals[i][0] <= end) {
                end = Math.max(end, intervals[i][1]);       // overlap - extend
            } else {
                System.out.println(start + " " + end);
                start = intervals[i][0];
                end = intervals[i][1];
            }
        }
        System.out.println(start + " " + end);
    }

    // ---------------------------------------------------------------------
    // M05. 3Sum
    // Input : n, then the array            Sample: 6 / -1 0 1 2 -1 -4
    // Output: every UNIQUE triplet with sum 0, one per line, sorted
    //         -> -1 -1 2 / -1 0 1          (print NONE if there is none)
    // Idea  : Sort. Fix the first number, then use two pointers (left, right)
    //         on the rest. Skip equal numbers so triplets are not repeated.
    // Time  : O(n^2)
    // ---------------------------------------------------------------------
    static void m05(Scanner sc) {
        int n = sc.nextInt();
        int[] numbers = readInts(sc, n);
        Arrays.sort(numbers);
        boolean found = false;
        for (int i = 0; i < n - 2; i++) {
            if (i > 0 && numbers[i] == numbers[i - 1]) {
                continue;                                    // same first number again
            }
            int left = i + 1;
            int right = n - 1;
            while (left < right) {
                int sum = numbers[i] + numbers[left] + numbers[right];
                if (sum == 0) {
                    System.out.println(numbers[i] + " " + numbers[left] + " " + numbers[right]);
                    found = true;
                    left++;
                    right--;
                    while (left < right && numbers[left] == numbers[left - 1]) {
                        left++;
                    }
                    while (left < right && numbers[right] == numbers[right + 1]) {
                        right--;
                    }
                } else if (sum < 0) {
                    left++;                                  // need a bigger sum
                } else {
                    right--;                                 // need a smaller sum
                }
            }
        }
        if (!found) {
            System.out.println("NONE");
        }
    }

    // ---------------------------------------------------------------------
    // M06. Container With Most Water
    // Input : n, then n heights            Sample: 9 / 1 8 6 2 5 4 8 3 7
    // Output: biggest area between two lines (width x shorter height)   -> 49
    // Idea  : Start with the widest container. The shorter line limits the
    //         height, so move the SHORTER side inward - moving the taller one
    //         can never help.
    // Time  : O(n)
    // ---------------------------------------------------------------------
    static void m06(Scanner sc) {
        int n = sc.nextInt();
        int[] heights = readInts(sc, n);
        int left = 0;
        int right = n - 1;
        long best = 0;
        while (left < right) {
            long area = (long) (right - left) * Math.min(heights[left], heights[right]);
            best = Math.max(best, area);
            if (heights[left] < heights[right]) {
                left++;
            } else {
                right--;
            }
        }
        System.out.println(best);
    }

    // ---------------------------------------------------------------------
    // M07. Product of Array Except Self (without division)
    // Input : n, then the array            Sample: 4 / 1 2 3 4
    // Output: for each index, product of all OTHER numbers    -> 24 12 8 6
    // Idea  : answer[i] = (product of everything left of i) x (everything right of i).
    //         Do one pass left-to-right and one pass right-to-left.
    // Time  : O(n)
    // ---------------------------------------------------------------------
    static void m07(Scanner sc) {
        int n = sc.nextInt();
        int[] numbers = readInts(sc, n);
        long[] answer = new long[n];
        long leftProduct = 1;
        for (int i = 0; i < n; i++) {
            answer[i] = leftProduct;
            leftProduct *= numbers[i];
        }
        long rightProduct = 1;
        for (int i = n - 1; i >= 0; i--) {
            answer[i] *= rightProduct;
            rightProduct *= numbers[i];
        }
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < n; i++) {
            line.append(answer[i]).append(i < n - 1 ? " " : "");
        }
        System.out.println(line);
    }

    // =====================================================================
    // HASHMAP / SORTING / HEAP
    // =====================================================================

    // ---------------------------------------------------------------------
    // M08. Group Anagrams
    // Input : n, then n words              Sample: 6 / eat tea tan ate nat bat
    // Output: each group on a line. Words inside a group sorted, groups sorted
    //         by their first word.         -> ate eat tea / bat / nat tan
    // Idea  : Sorted letters are the same for anagrams ("eat" -> "aet"). Use that
    //         as the map key.
    // ---------------------------------------------------------------------
    static void m08(Scanner sc) {
        int n = sc.nextInt();
        Map<String, List<String>> groups = new HashMap<>();
        for (int i = 0; i < n; i++) {
            String word = sc.next();
            char[] letters = word.toCharArray();
            Arrays.sort(letters);
            groups.computeIfAbsent(new String(letters), key -> new ArrayList<>()).add(word);
        }
        List<List<String>> all = new ArrayList<>(groups.values());
        for (List<String> group : all) {
            Collections.sort(group);
        }
        all.sort((a, b) -> a.get(0).compareTo(b.get(0)));
        for (List<String> group : all) {
            System.out.println(String.join(" ", group));
        }
    }

    // ---------------------------------------------------------------------
    // M09. Top K Frequent Elements
    // Input : n, the array, then k         Sample: 6 / 1 1 1 2 2 3 / 2
    // Output: the k most frequent numbers (tie: smaller number first)   -> 1 2
    // Idea  : Count with a map, sort the entries by (count desc, number asc).
    // Time  : O(n log n)   (a heap gives O(n log k))
    // ---------------------------------------------------------------------
    static void m09(Scanner sc) {
        int n = sc.nextInt();
        int[] numbers = readInts(sc, n);
        int k = sc.nextInt();
        Map<Integer, Integer> count = new HashMap<>();
        for (int number : numbers) {
            count.put(number, count.getOrDefault(number, 0) + 1);
        }
        List<Map.Entry<Integer, Integer>> entries = new ArrayList<>(count.entrySet());
        entries.sort((a, b) -> {
            if (!a.getValue().equals(b.getValue())) {
                return b.getValue() - a.getValue();         // higher count first
            }
            return a.getKey() - b.getKey();                 // then smaller number
        });
        List<Integer> top = new ArrayList<>();
        for (int i = 0; i < k && i < entries.size(); i++) {
            top.add(entries.get(i).getKey());
        }
        System.out.println(join(top));
    }

    // ---------------------------------------------------------------------
    // M10. Rotate Matrix 90 Degrees Clockwise
    // Input : n, then an n x n matrix      Sample: 3 / 1 2 3 / 4 5 6 / 7 8 9
    // Output: rotated matrix               -> 7 4 1 / 8 5 2 / 9 6 3
    // Idea  : The cell at (row, col) moves to (col, n - 1 - row).
    // ---------------------------------------------------------------------
    static void m10(Scanner sc) {
        int n = sc.nextInt();
        int[][] matrix = new int[n][n];
        for (int r = 0; r < n; r++) {
            matrix[r] = readInts(sc, n);
        }
        int[][] rotated = new int[n][n];
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                rotated[c][n - 1 - r] = matrix[r][c];
            }
        }
        for (int[] row : rotated) {
            System.out.println(join(row));
        }
    }

    // ---------------------------------------------------------------------
    // M11. Kth Largest Element
    // Input : n, the array, then k         Sample: 6 / 3 2 1 5 6 4 / 2
    // Output: the kth largest number       -> 5
    // Idea  : Keep a MIN-heap of size k. The smallest of the k biggest numbers
    //         sits on top - that is the answer.
    // Time  : O(n log k)
    // ---------------------------------------------------------------------
    static void m11(Scanner sc) {
        int n = sc.nextInt();
        int[] numbers = readInts(sc, n);
        int k = sc.nextInt();
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        for (int number : numbers) {
            minHeap.add(number);
            if (minHeap.size() > k) {
                minHeap.poll();                 // remove the smallest
            }
        }
        System.out.println(minHeap.peek());
    }

    // =====================================================================
    // GRID BFS / BINARY SEARCH VARIANT
    // =====================================================================

    // ---------------------------------------------------------------------
    // M12. Number of Islands
    // Input : rows cols, then the grid of 0 (water) and 1 (land)
    //         Sample: 4 5 / 1 1 0 0 0 / 1 1 0 0 0 / 0 0 1 0 0 / 0 0 0 1 1
    // Output: number of islands (groups of connected land, up/down/left/right)  -> 3
    // Idea  : Find a land cell, count one island, then flood-fill (BFS) all
    //         connected land and mark it as water so it is not counted again.
    // Time  : O(rows * cols)
    // ---------------------------------------------------------------------
    static void m12(Scanner sc) {
        int rows = sc.nextInt();
        int cols = sc.nextInt();
        int[][] grid = new int[rows][cols];
        for (int r = 0; r < rows; r++) {
            grid[r] = readInts(sc, cols);
        }
        int[][] moves = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        int islands = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (grid[row][col] == 1) {
                    islands++;
                    grid[row][col] = 0;
                    Deque<int[]> queue = new ArrayDeque<>();
                    queue.add(new int[]{row, col});
                    while (!queue.isEmpty()) {
                        int[] cell = queue.poll();
                        for (int[] move : moves) {
                            int nextRow = cell[0] + move[0];
                            int nextCol = cell[1] + move[1];
                            if (nextRow >= 0 && nextRow < rows && nextCol >= 0 && nextCol < cols
                                    && grid[nextRow][nextCol] == 1) {
                                grid[nextRow][nextCol] = 0;
                                queue.add(new int[]{nextRow, nextCol});
                            }
                        }
                    }
                }
            }
        }
        System.out.println(islands);
    }

    // ---------------------------------------------------------------------
    // M13. Search in a Rotated Sorted Array
    // Input : n, the array (distinct), then the target   Sample: 7 / 4 5 6 7 0 1 2 / 0
    // Output: index of the target, or -1                 -> 4
    // Idea  : Binary search. In any split, ONE half is always properly sorted.
    //         Check if the target lies inside the sorted half; if yes go there,
    //         otherwise go to the other half.
    // Time  : O(log n)
    // ---------------------------------------------------------------------
    static void m13(Scanner sc) {
        int n = sc.nextInt();
        int[] numbers = readInts(sc, n);
        int target = sc.nextInt();
        int low = 0;
        int high = n - 1;
        int answer = -1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (numbers[mid] == target) {
                answer = mid;
                break;
            }
            if (numbers[low] <= numbers[mid]) {                       // left half is sorted
                if (target >= numbers[low] && target < numbers[mid]) {
                    high = mid - 1;
                } else {
                    low = mid + 1;
                }
            } else {                                                  // right half is sorted
                if (target > numbers[mid] && target <= numbers[high]) {
                    low = mid + 1;
                } else {
                    high = mid - 1;
                }
            }
        }
        System.out.println(answer);
    }

    // =====================================================================
    // DYNAMIC PROGRAMMING
    // How to start every DP problem: (1) what does dp[i] mean?  (2) how does
    // dp[i] come from smaller answers?  (3) what are the starting values?
    // =====================================================================

    // ---------------------------------------------------------------------
    // M14. Longest Increasing Subsequence
    // Input : n, then the array            Sample: 8 / 10 9 2 5 3 7 101 18
    // Output: length of the longest strictly increasing subsequence   -> 4  (2 3 7 18)
    // Idea  : tails[len] = smallest possible last number of an increasing
    //         subsequence of length len+1. For each number, binary search where it
    //         fits and overwrite - it keeps the tails as small as possible.
    // Time  : O(n log n)   (the simple dp[i] version is O(n^2))
    // ---------------------------------------------------------------------
    static void m14(Scanner sc) {
        int n = sc.nextInt();
        int[] numbers = readInts(sc, n);
        int[] tails = new int[n];
        int size = 0;
        for (int number : numbers) {
            int low = 0;
            int high = size;
            while (low < high) {                      // find first tail >= number
                int mid = (low + high) / 2;
                if (tails[mid] < number) {
                    low = mid + 1;
                } else {
                    high = mid;
                }
            }
            tails[low] = number;
            if (low == size) {
                size++;                               // the sequence got longer
            }
        }
        System.out.println(size);
    }

    // ---------------------------------------------------------------------
    // M15. Coin Change - Number of Ways
    // Input : n coins, the coins, then the amount     Sample: 3 / 1 2 5 / 5
    // Output: number of different combinations that make the amount   -> 4
    // Idea  : ways[a] = number of ways to make amount a. For each coin (outer
    //         loop) add ways[a - coin]. Coins in the OUTER loop means 1+2 and 2+1
    //         are counted once (combinations, not permutations).
    // ---------------------------------------------------------------------
    static void m15(Scanner sc) {
        int n = sc.nextInt();
        int[] coins = readInts(sc, n);
        int amount = sc.nextInt();
        long[] ways = new long[amount + 1];
        ways[0] = 1;
        for (int coin : coins) {
            for (int a = coin; a <= amount; a++) {
                ways[a] += ways[a - coin];
            }
        }
        System.out.println(ways[amount]);
    }

    // ---------------------------------------------------------------------
    // M16. Longest Common Subsequence
    // Input : two strings                  Sample: abcde ace
    // Output: length of the longest common subsequence   -> 3  ("ace")
    // Idea  : dp[i][j] = answer for the first i letters of A and first j of B.
    //         Letters match -> 1 + dp[i-1][j-1].  Else max(dp[i-1][j], dp[i][j-1]).
    // ---------------------------------------------------------------------
    static void m16(Scanner sc) {
        String a = sc.next();
        String b = sc.next();
        int[][] dp = new int[a.length() + 1][b.length() + 1];
        for (int i = 1; i <= a.length(); i++) {
            for (int j = 1; j <= b.length(); j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i - 1][j], dp[i][j - 1]);
                }
            }
        }
        System.out.println(dp[a.length()][b.length()]);
    }

    // ---------------------------------------------------------------------
    // M17. Edit Distance
    // Input : two strings                  Sample: horse ros
    // Output: minimum insert / delete / replace operations to turn A into B   -> 3
    // Idea  : dp[i][j] = cost for first i letters of A -> first j letters of B.
    //         Same letter: copy dp[i-1][j-1]. Different: 1 + min(replace, delete, insert).
    // ---------------------------------------------------------------------
    static void m17(Scanner sc) {
        String a = sc.next();
        String b = sc.next();
        int[][] dp = new int[a.length() + 1][b.length() + 1];
        for (int i = 0; i <= a.length(); i++) {
            dp[i][0] = i;                              // delete all i letters
        }
        for (int j = 0; j <= b.length(); j++) {
            dp[0][j] = j;                              // insert all j letters
        }
        for (int i = 1; i <= a.length(); i++) {
            for (int j = 1; j <= b.length(); j++) {
                if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1]));
                }
            }
        }
        System.out.println(dp[a.length()][b.length()]);
    }

    // ---------------------------------------------------------------------
    // M18. House Robber
    // Input : n, then the money in each house     Sample: 4 / 1 2 3 1
    // Output: most money without robbing two neighbouring houses   -> 4  (1 + 3)
    // Idea  : For each house choose: skip it (keep best so far) or rob it
    //         (money + best from two houses back).
    // ---------------------------------------------------------------------
    static void m18(Scanner sc) {
        int n = sc.nextInt();
        int[] money = readInts(sc, n);
        long twoBack = 0;      // best up to house i-2
        long oneBack = 0;      // best up to house i-1
        for (int house : money) {
            long best = Math.max(oneBack, twoBack + house);
            twoBack = oneBack;
            oneBack = best;
        }
        System.out.println(oneBack);
    }

    // ---------------------------------------------------------------------
    // M19. 0/1 Knapsack
    // Input : n and W, then n weights, then n values      Sample: 3 50 / 10 20 30 / 60 100 120
    // Output: maximum value that fits in capacity W      -> 220
    // Idea  : best[w] = max value with capacity w. For each item go through w from
    //         HIGH to LOW so the same item is not used twice.
    // Time  : O(n * W)
    // ---------------------------------------------------------------------
    static void m19(Scanner sc) {
        int n = sc.nextInt();
        int capacity = sc.nextInt();
        int[] weights = readInts(sc, n);
        int[] values = readInts(sc, n);
        int[] best = new int[capacity + 1];
        for (int item = 0; item < n; item++) {
            for (int w = capacity; w >= weights[item]; w--) {
                best[w] = Math.max(best[w], best[w - weights[item]] + values[item]);
            }
        }
        System.out.println(best[capacity]);
    }

    // ---------------------------------------------------------------------
    // M20. Minimum Path Sum in a Grid
    // Input : rows cols, then the grid     Sample: 3 3 / 1 3 1 / 1 5 1 / 4 2 1
    // Output: smallest sum from top-left to bottom-right moving only right or down   -> 7
    // Idea  : cost[r][c] = grid[r][c] + min(cost from above, cost from left).
    // ---------------------------------------------------------------------
    static void m20(Scanner sc) {
        int rows = sc.nextInt();
        int cols = sc.nextInt();
        int[][] grid = new int[rows][cols];
        for (int r = 0; r < rows; r++) {
            grid[r] = readInts(sc, cols);
        }
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (r == 0 && c == 0) {
                    continue;
                }
                int fromAbove = r > 0 ? grid[r - 1][c] : Integer.MAX_VALUE;
                int fromLeft = c > 0 ? grid[r][c - 1] : Integer.MAX_VALUE;
                grid[r][c] += Math.min(fromAbove, fromLeft);
            }
        }
        System.out.println(grid[rows - 1][cols - 1]);
    }

    // ---------------------------------------------------------------------
    // M21. Word Break
    // Input : the string, n, then n dictionary words      Sample: leetcode / 2 / leet code
    // Output: YES if the string can be split into dictionary words, else NO   -> YES
    // Idea  : canBreak[i] = the first i letters can be split. canBreak[i] is true
    //         if some earlier point j is true and s[j..i] is a dictionary word.
    // ---------------------------------------------------------------------
    static void m21(Scanner sc) {
        String text = sc.next();
        int n = sc.nextInt();
        Set<String> dictionary = new HashSet<>();
        for (int i = 0; i < n; i++) {
            dictionary.add(sc.next());
        }
        boolean[] canBreak = new boolean[text.length() + 1];
        canBreak[0] = true;
        for (int i = 1; i <= text.length(); i++) {
            for (int j = 0; j < i; j++) {
                if (canBreak[j] && dictionary.contains(text.substring(j, i))) {
                    canBreak[i] = true;
                    break;
                }
            }
        }
        System.out.println(canBreak[text.length()] ? "YES" : "NO");
    }

    // ---------------------------------------------------------------------
    // M22. Decode Ways  (A=1, B=2 ... Z=26)
    // Input : a string of digits           Sample: 226
    // Output: number of ways to decode it  -> 3  (2 2 6, 22 6, 2 26)
    // Idea  : Like climbing stairs. ways[i] = (last 1 digit is valid ? ways[i-1])
    //                                       + (last 2 digits are 10..26 ? ways[i-2]).
    // ---------------------------------------------------------------------
    static void m22(Scanner sc) {
        String digits = sc.next();
        int n = digits.length();
        long[] ways = new long[n + 1];
        ways[0] = 1;
        ways[1] = digits.charAt(0) != '0' ? 1 : 0;
        for (int i = 2; i <= n; i++) {
            if (digits.charAt(i - 1) != '0') {
                ways[i] += ways[i - 1];
            }
            int twoDigits = Integer.parseInt(digits.substring(i - 2, i));
            if (twoDigits >= 10 && twoDigits <= 26) {
                ways[i] += ways[i - 2];
            }
        }
        System.out.println(ways[n]);
    }

    // =====================================================================
    // BACKTRACKING  (try a choice, go deeper, UNDO the choice, try the next)
    // =====================================================================

    // ---------------------------------------------------------------------
    // M23. Generate Parentheses
    // Input : n                            Sample: 3
    // Output: all valid strings with n pairs, in dictionary order
    //         -> ((())) / (()()) / (())() / ()(()) / ()()()
    // Idea  : Add '(' if we still have some left. Add ')' only if it would not
    //         close more than we opened.
    // ---------------------------------------------------------------------
    static void m23(Scanner sc) {
        int n = sc.nextInt();
        generateParentheses(0, 0, n, new StringBuilder());
    }

    static void generateParentheses(int open, int close, int n, StringBuilder current) {
        if (current.length() == 2 * n) {
            System.out.println(current);
            return;
        }
        if (open < n) {
            current.append('(');
            generateParentheses(open + 1, close, n, current);
            current.deleteCharAt(current.length() - 1);        // undo
        }
        if (close < open) {
            current.append(')');
            generateParentheses(open, close + 1, n, current);
            current.deleteCharAt(current.length() - 1);        // undo
        }
    }

    // ---------------------------------------------------------------------
    // M24. All Subsets (Power Set)
    // Input : n, then n distinct numbers   Sample: 3 / 1 2 3
    // Output: all subsets, each as [a, b], in this order
    //         -> [] / [1] / [1, 2] / [1, 2, 3] / [1, 3] / [2] / [2, 3] / [3]
    // Idea  : At each step add the current subset to the answer, then try adding
    //         each remaining number one by one (and remove it again after).
    // ---------------------------------------------------------------------
    static void m24(Scanner sc) {
        int n = sc.nextInt();
        int[] numbers = readInts(sc, n);
        Arrays.sort(numbers);
        List<List<Integer>> result = new ArrayList<>();
        collectSubsets(0, numbers, new ArrayList<>(), result);
        for (List<Integer> subset : result) {
            System.out.println(subset);
        }
    }

    static void collectSubsets(int start, int[] numbers, List<Integer> current, List<List<Integer>> result) {
        result.add(new ArrayList<>(current));                  // copy - current keeps changing
        for (int i = start; i < numbers.length; i++) {
            current.add(numbers[i]);
            collectSubsets(i + 1, numbers, current, result);
            current.remove(current.size() - 1);                // undo
        }
    }

    // ---------------------------------------------------------------------
    // M25. Distinct Permutations of a String
    // Input : a string (letters may repeat)   Sample: aab
    // Output: every different arrangement, in dictionary order   -> aab / aba / baa
    // Idea  : Sort the letters. Pick an unused letter for each position. Skip a
    //         letter if it equals the previous letter and that one is not used
    //         (this avoids the same arrangement twice).
    // ---------------------------------------------------------------------
    static void m25(Scanner sc) {
        char[] letters = sc.next().toCharArray();
        Arrays.sort(letters);
        permute(letters, new boolean[letters.length], new StringBuilder());
    }

    static void permute(char[] letters, boolean[] used, StringBuilder current) {
        if (current.length() == letters.length) {
            System.out.println(current);
            return;
        }
        for (int i = 0; i < letters.length; i++) {
            if (used[i]) {
                continue;
            }
            if (i > 0 && letters[i] == letters[i - 1] && !used[i - 1]) {
                continue;                                      // duplicate arrangement
            }
            used[i] = true;
            current.append(letters[i]);
            permute(letters, used, current);
            current.deleteCharAt(current.length() - 1);        // undo
            used[i] = false;
        }
    }

    // =====================================================================
    // TREES AND QUEUE DESIGN
    // =====================================================================

    static class TreeNode {
        int value;
        TreeNode left;
        TreeNode right;

        TreeNode(int value) {
            this.value = value;
        }
    }

    // ---------------------------------------------------------------------
    // M26. Binary Search Tree: height, inorder, level order
    // Input : n, then n numbers inserted one by one into a BST (duplicates ignored)
    //         Sample: 6 / 4 2 6 1 3 5
    // Output: line 1: height (number of EDGES on the longest root-to-leaf path)  -> 2
    //         line 2: inorder traversal (left, root, right)  -> 1 2 3 4 5 6
    //         line 3: level order (row by row)               -> 4 2 6 1 3 5
    // Idea  : Smaller goes left, bigger goes right. Inorder of a BST is sorted.
    //         Level order uses a queue (BFS).
    // ---------------------------------------------------------------------
    static void m26(Scanner sc) {
        int n = sc.nextInt();
        TreeNode root = null;
        for (int i = 0; i < n; i++) {
            root = insertIntoBst(root, sc.nextInt());
        }
        System.out.println(treeHeight(root));

        List<Integer> inorder = new ArrayList<>();
        collectInorder(root, inorder);
        System.out.println(join(inorder));

        List<Integer> levelOrder = new ArrayList<>();
        Deque<TreeNode> queue = new ArrayDeque<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            levelOrder.add(node.value);
            if (node.left != null) {
                queue.add(node.left);
            }
            if (node.right != null) {
                queue.add(node.right);
            }
        }
        System.out.println(join(levelOrder));
    }

    static TreeNode insertIntoBst(TreeNode root, int value) {
        if (root == null) {
            return new TreeNode(value);
        }
        if (value < root.value) {
            root.left = insertIntoBst(root.left, value);
        } else if (value > root.value) {
            root.right = insertIntoBst(root.right, value);
        }
        return root;
    }

    static int treeHeight(TreeNode node) {
        if (node == null) {
            return -1;                                          // empty tree has height -1 (edges)
        }
        return 1 + Math.max(treeHeight(node.left), treeHeight(node.right));
    }

    static void collectInorder(TreeNode node, List<Integer> out) {
        if (node == null) {
            return;
        }
        collectInorder(node.left, out);
        out.add(node.value);
        collectInorder(node.right, out);
    }

    // ---------------------------------------------------------------------
    // M27. Queue Using Two Stacks (HackerRank classic)
    // Input : q, then q queries: "1 x" enqueue x, "2" dequeue, "3" print the front
    //         Sample: 10 / 1 42 / 2 / 1 14 / 3 / 1 28 / 3 / 1 60 / 1 78 / 2 / 2
    // Output: for every "3" query print the front element     -> 14 / 14
    // Idea  : "in" stack receives new items. When we need the front and "out" is
    //         empty, pour everything from "in" into "out" (this reverses the
    //         order, so the oldest item ends on top). Each item moves only once,
    //         so every operation is O(1) on average.
    // ---------------------------------------------------------------------
    static void m27(Scanner sc) {
        int q = sc.nextInt();
        Deque<Integer> in = new ArrayDeque<>();
        Deque<Integer> out = new ArrayDeque<>();
        for (int i = 0; i < q; i++) {
            int type = sc.nextInt();
            if (type == 1) {
                in.push(sc.nextInt());
                continue;
            }
            if (out.isEmpty()) {
                while (!in.isEmpty()) {
                    out.push(in.pop());
                }
            }
            if (type == 2) {
                out.pop();
            } else {
                System.out.println(out.peek());
            }
        }
    }

    // =====================================================================
    // GRAPHS
    // =====================================================================

    // ---------------------------------------------------------------------
    // M28. Shortest Path - Dijkstra (weighted, no negative weights)
    // Input : n m, then m lines "u v w" (undirected edge), then the source node
    //         Sample: 4 4 / 1 2 1 / 2 3 2 / 1 3 5 / 3 4 1 / 1
    // Output: shortest distance from the source to nodes 1..n (-1 if unreachable)
    //         -> 0 1 3 4
    // Idea  : Always take the nearest unfinished node from a min-heap, then try to
    //         improve the distance of its neighbours.
    // Time  : O((n + m) log n)
    // ---------------------------------------------------------------------
    static void m28(Scanner sc) {
        int n = sc.nextInt();
        int m = sc.nextInt();
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int i = 0; i < m; i++) {
            int u = sc.nextInt(), v = sc.nextInt(), w = sc.nextInt();
            graph.get(u).add(new int[]{v, w});
            graph.get(v).add(new int[]{u, w});
        }
        int source = sc.nextInt();

        long[] distance = new long[n + 1];
        Arrays.fill(distance, Long.MAX_VALUE);
        distance[source] = 0;
        PriorityQueue<long[]> heap = new PriorityQueue<>((a, b) -> Long.compare(a[0], b[0]));
        heap.add(new long[]{0, source});
        while (!heap.isEmpty()) {
            long[] top = heap.poll();
            long dist = top[0];
            int node = (int) top[1];
            if (dist > distance[node]) {
                continue;                                    // old, worse entry - ignore
            }
            for (int[] edge : graph.get(node)) {
                long newDist = dist + edge[1];
                if (newDist < distance[edge[0]]) {
                    distance[edge[0]] = newDist;
                    heap.add(new long[]{newDist, edge[0]});
                }
            }
        }
        StringBuilder line = new StringBuilder();
        for (int i = 1; i <= n; i++) {
            line.append(distance[i] == Long.MAX_VALUE ? -1 : distance[i]);
            line.append(i < n ? " " : "");
        }
        System.out.println(line);
    }

    // ---------------------------------------------------------------------
    // M29. Connected Components (Union-Find / DSU)
    // Input : n m, then m lines "a b" (friends)     Sample: 6 3 / 1 2 / 2 3 / 4 5
    // Output: number of groups and size of the biggest group    -> 3 3
    // Idea  : Each person starts alone. For every pair, UNION their groups.
    //         find() returns the group leader. Path compression keeps it fast.
    // ---------------------------------------------------------------------
    static void m29(Scanner sc) {
        int n = sc.nextInt();
        int m = sc.nextInt();
        int[] parent = new int[n + 1];
        int[] size = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
        for (int i = 0; i < m; i++) {
            int a = findLeader(parent, sc.nextInt());
            int b = findLeader(parent, sc.nextInt());
            if (a != b) {
                parent[b] = a;                 // join group b into group a
                size[a] += size[b];
            }
        }
        int groups = 0;
        int biggest = 0;
        for (int i = 1; i <= n; i++) {
            if (findLeader(parent, i) == i) {  // i is a leader
                groups++;
                biggest = Math.max(biggest, size[i]);
            }
        }
        System.out.println(groups + " " + biggest);
    }

    static int findLeader(int[] parent, int x) {
        while (parent[x] != x) {
            parent[x] = parent[parent[x]];     // path compression
            x = parent[x];
        }
        return x;
    }

    // ---------------------------------------------------------------------
    // M30. Course Schedule - Topological Sort
    // Input : n courses, m rules, then m lines "a b" meaning take b BEFORE a
    //         Sample: 4 3 / 2 1 / 3 1 / 4 2
    // Output: a valid order (smallest course number first when there is a choice),
    //         or IMPOSSIBLE if there is a cycle    -> 1 2 3 4
    // Idea  : Kahn's algorithm. Count how many prerequisites each course has. Take
    //         courses with 0 prerequisites, "finish" them and reduce the count of
    //         the courses that depend on them. If not all courses get taken, there
    //         is a cycle.
    // ---------------------------------------------------------------------
    static void m30(Scanner sc) {
        int n = sc.nextInt();
        int m = sc.nextInt();
        List<List<Integer>> next = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            next.add(new ArrayList<>());
        }
        int[] prerequisites = new int[n + 1];
        for (int i = 0; i < m; i++) {
            int a = sc.nextInt();
            int b = sc.nextInt();
            next.get(b).add(a);                  // b -> a
            prerequisites[a]++;
        }
        PriorityQueue<Integer> ready = new PriorityQueue<>();
        for (int course = 1; course <= n; course++) {
            if (prerequisites[course] == 0) {
                ready.add(course);
            }
        }
        List<Integer> order = new ArrayList<>();
        while (!ready.isEmpty()) {
            int course = ready.poll();
            order.add(course);
            for (int dependent : next.get(course)) {
                prerequisites[dependent]--;
                if (prerequisites[dependent] == 0) {
                    ready.add(dependent);
                }
            }
        }
        System.out.println(order.size() == n ? join(order) : "IMPOSSIBLE");
    }

    // =====================================================================
    // STACK / MONOTONIC STACK / DEQUE
    // Monotonic stack = a stack that always stays sorted. Use it for
    // "next greater / next smaller element" type problems. O(n).
    // =====================================================================

    // ---------------------------------------------------------------------
    // M31. Sliding Window Maximum
    // Input : n and k, then the array      Sample: 8 3 / 1 3 -1 -3 5 3 6 7
    // Output: the maximum of every window of size k     -> 3 3 5 5 6 7
    // Idea  : Keep a deque of INDEXES whose values are in decreasing order. The
    //         front is always the maximum of the current window.
    // Time  : O(n)
    // ---------------------------------------------------------------------
    static void m31(Scanner sc) {
        int n = sc.nextInt();
        int k = sc.nextInt();
        int[] numbers = readInts(sc, n);
        Deque<Integer> indexes = new ArrayDeque<>();
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (!indexes.isEmpty() && indexes.peekFirst() <= i - k) {
                indexes.pollFirst();                         // front left the window
            }
            while (!indexes.isEmpty() && numbers[indexes.peekLast()] <= numbers[i]) {
                indexes.pollLast();                          // smaller numbers can never be max again
            }
            indexes.addLast(i);
            if (i >= k - 1) {
                result.add(numbers[indexes.peekFirst()]);
            }
        }
        System.out.println(join(result));
    }

    // ---------------------------------------------------------------------
    // M32. Longest Substring With At Most K Distinct Characters
    // Input : the string, then k           Sample: eceba 2
    // Output: length of the longest part using at most k different letters   -> 3  ("ece")
    // Idea  : Sliding window with a count map. If the map has more than k letters,
    //         shrink from the left until it is k again.
    // ---------------------------------------------------------------------
    static void m32(Scanner sc) {
        String text = sc.next();
        int k = sc.nextInt();
        Map<Character, Integer> count = new HashMap<>();
        int left = 0;
        int longest = 0;
        for (int right = 0; right < text.length(); right++) {
            char added = text.charAt(right);
            count.put(added, count.getOrDefault(added, 0) + 1);
            while (count.size() > k) {
                char removed = text.charAt(left);
                count.put(removed, count.get(removed) - 1);
                if (count.get(removed) == 0) {
                    count.remove(removed);
                }
                left++;
            }
            longest = Math.max(longest, right - left + 1);
        }
        System.out.println(longest);
    }

    // ---------------------------------------------------------------------
    // M33. Next Greater Element
    // Input : n, then the array            Sample: 4 / 4 5 2 25
    // Output: for each number, the first bigger number to its right (or -1)
    //         -> 5 25 25 -1
    // Idea  : Walk left to right with a stack of indexes still waiting for a
    //         bigger number. A new number answers every smaller number on the stack.
    // ---------------------------------------------------------------------
    static void m33(Scanner sc) {
        int n = sc.nextInt();
        int[] numbers = readInts(sc, n);
        int[] answer = new int[n];
        Arrays.fill(answer, -1);
        Deque<Integer> waiting = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            while (!waiting.isEmpty() && numbers[waiting.peek()] < numbers[i]) {
                answer[waiting.pop()] = numbers[i];
            }
            waiting.push(i);
        }
        System.out.println(join(answer));
    }

    // ---------------------------------------------------------------------
    // M34. Daily Temperatures
    // Input : n, then the temperatures     Sample: 8 / 73 74 75 71 69 72 76 73
    // Output: for each day, how many days until a warmer day (0 if never)
    //         -> 1 1 4 2 1 1 0 0
    // Idea  : Same monotonic-stack idea as M33, but store the distance in days.
    // ---------------------------------------------------------------------
    static void m34(Scanner sc) {
        int n = sc.nextInt();
        int[] temperatures = readInts(sc, n);
        int[] answer = new int[n];
        Deque<Integer> waiting = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            while (!waiting.isEmpty() && temperatures[waiting.peek()] < temperatures[i]) {
                int earlierDay = waiting.pop();
                answer[earlierDay] = i - earlierDay;
            }
            waiting.push(i);
        }
        System.out.println(join(answer));
    }

    // ---------------------------------------------------------------------
    // M35. Trapping Rain Water
    // Input : n, then the bar heights      Sample: 12 / 0 1 0 2 1 0 1 3 2 1 2 1
    // Output: units of water trapped       -> 6
    // Idea  : Water above a bar = min(tallest bar on left, tallest on right) - bar.
    //         Two pointers: always move the side with the smaller tallest-so-far,
    //         because that side decides the water level.
    // Time  : O(n)   Space : O(1)
    // ---------------------------------------------------------------------
    static void m35(Scanner sc) {
        int n = sc.nextInt();
        int[] heights = readInts(sc, n);
        int left = 0;
        int right = n - 1;
        int leftMax = 0;
        int rightMax = 0;
        long water = 0;
        while (left < right) {
            if (heights[left] < heights[right]) {
                leftMax = Math.max(leftMax, heights[left]);
                water += leftMax - heights[left];
                left++;
            } else {
                rightMax = Math.max(rightMax, heights[right]);
                water += rightMax - heights[right];
                right--;
            }
        }
        System.out.println(water);
    }

    // =====================================================================
    // DESIGN PROBLEMS
    // =====================================================================

    static class LruCache extends LinkedHashMap<Integer, Integer> {
        private final int capacity;

        LruCache(int capacity) {
            super(capacity, 0.75f, true);          // true = keep items in order of last use
            this.capacity = capacity;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
            return size() > capacity;
        }
    }

    // ---------------------------------------------------------------------
    // M36. LRU Cache
    // Input : capacity and q, then q commands "PUT key value" or "GET key"
    //         Sample: 2 7 / PUT 1 1 / PUT 2 2 / GET 1 / PUT 3 3 / GET 2 / PUT 4 4 / GET 1
    // Output: for each GET print the value, or -1 if it is not in the cache
    //         -> 1 / -1 / -1
    // Idea  : See LruCache above. When it is full, the least recently used key is
    //         removed. GET also counts as "use".
    // ---------------------------------------------------------------------
    static void m36(Scanner sc) {
        int capacity = sc.nextInt();
        int q = sc.nextInt();
        LruCache cache = new LruCache(capacity);
        for (int i = 0; i < q; i++) {
            String command = sc.next();
            if (command.equals("PUT")) {
                cache.put(sc.nextInt(), sc.nextInt());
            } else {
                System.out.println(cache.getOrDefault(sc.nextInt(), -1));
            }
        }
    }

    // ---------------------------------------------------------------------
    // M37. Frequency Queries (HackerRank medium classic)
    // Input : q, then q queries:  "1 x" add x,  "2 x" remove ONE x (if present),
    //         "3 f" is there any number that appears exactly f times?
    //         Sample: 8 / 1 5 / 1 6 / 3 2 / 1 10 / 1 10 / 1 6 / 2 5 / 3 2
    // Output: for every type-3 query print 1 (yes) or 0 (no)    -> 0 / 1
    // Idea  : Keep TWO maps: count[x] = how many times x appears, and
    //         howManyHave[f] = how many numbers appear exactly f times.
    //         Update both on every add/remove. A type-3 answer is then O(1).
    // ---------------------------------------------------------------------
    static void m37(Scanner sc) {
        int q = sc.nextInt();
        Map<Integer, Integer> count = new HashMap<>();
        Map<Integer, Integer> howManyHave = new HashMap<>();
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < q; i++) {
            int type = sc.nextInt();
            int value = sc.nextInt();
            if (type == 1) {
                int old = count.getOrDefault(value, 0);
                changeFrequency(howManyHave, old, -1);
                count.put(value, old + 1);
                changeFrequency(howManyHave, old + 1, +1);
            } else if (type == 2) {
                int old = count.getOrDefault(value, 0);
                if (old > 0) {
                    changeFrequency(howManyHave, old, -1);
                    count.put(value, old - 1);
                    changeFrequency(howManyHave, old - 1, +1);
                }
            } else {
                output.append(howManyHave.getOrDefault(value, 0) > 0 ? 1 : 0).append("\n");
            }
        }
        System.out.print(output);
    }

    static void changeFrequency(Map<Integer, Integer> howManyHave, int frequency, int change) {
        if (frequency > 0) {                       // frequency 0 means "not present" - not tracked
            howManyHave.put(frequency, howManyHave.getOrDefault(frequency, 0) + change);
        }
    }

    // =====================================================================
    // HACKERRANK CLASSICS, INTERVALS AND GREEDY
    // =====================================================================

    // ---------------------------------------------------------------------
    // M38. Largest Rectangle in a Histogram (HackerRank classic)
    // Input : n, then the bar heights      Sample: 6 / 2 1 5 6 2 3
    // Output: area of the biggest rectangle   -> 10  (bars 5 and 6, height 5, width 2)
    // Idea  : Monotonic stack of increasing heights. When a shorter bar arrives,
    //         every taller bar on the stack ends here - compute its rectangle:
    //         height x (distance between this bar and the new top of the stack).
    // Time  : O(n)
    // ---------------------------------------------------------------------
    static void m38(Scanner sc) {
        int n = sc.nextInt();
        int[] heights = readInts(sc, n);
        Deque<Integer> stack = new ArrayDeque<>();
        long best = 0;
        for (int i = 0; i <= n; i++) {
            int current = (i == n) ? 0 : heights[i];       // extra 0 at the end empties the stack
            while (!stack.isEmpty() && heights[stack.peek()] >= current) {
                int height = heights[stack.pop()];
                int leftBoundary = stack.isEmpty() ? -1 : stack.peek();
                long area = (long) height * (i - leftBoundary - 1);
                best = Math.max(best, area);
            }
            stack.push(i);
        }
        System.out.println(best);
    }

    // ---------------------------------------------------------------------
    // M39. Minimum Swaps 2 (HackerRank classic)
    // Input : n, then a permutation of 1..n   Sample: 4 / 4 3 1 2
    // Output: minimum swaps (any two positions) to sort it    -> 3
    // Idea  : Number x belongs at index x - 1. While the number at position i is
    //         not i + 1, swap it into its correct place. Each swap fixes one number.
    // ---------------------------------------------------------------------
    static void m39(Scanner sc) {
        int n = sc.nextInt();
        int[] numbers = readInts(sc, n);
        int swaps = 0;
        for (int i = 0; i < n; i++) {
            while (numbers[i] != i + 1) {
                int correctIndex = numbers[i] - 1;
                int temp = numbers[i];
                numbers[i] = numbers[correctIndex];
                numbers[correctIndex] = temp;
                swaps++;
            }
        }
        System.out.println(swaps);
    }

    // ---------------------------------------------------------------------
    // M40. Array Manipulation (HackerRank classic - needs a smart idea)
    // Input : n and m, then m lines "a b k": add k to every element from a to b
    //         (1-indexed). The array starts as all zeros.
    //         Sample: 5 3 / 1 2 100 / 2 5 100 / 3 4 100
    // Output: the biggest value in the array after all operations    -> 200
    // Idea  : Doing every addition one by one is O(n * m) = too slow.
    //         Difference array: add k at index a, subtract k at index b+1.
    //         The running (prefix) sum of that array gives the real values.
    // Time  : O(n + m).  Use long - values get huge.
    // ---------------------------------------------------------------------
    static void m40(Scanner sc) {
        int n = sc.nextInt();
        int m = sc.nextInt();
        long[] difference = new long[n + 2];
        for (int i = 0; i < m; i++) {
            int a = sc.nextInt();
            int b = sc.nextInt();
            long k = sc.nextLong();
            difference[a] += k;
            difference[b + 1] -= k;
        }
        long running = 0;
        long best = Long.MIN_VALUE;
        for (int i = 1; i <= n; i++) {
            running += difference[i];
            best = Math.max(best, running);
        }
        System.out.println(best);
    }

    // ---------------------------------------------------------------------
    // M41. Sherlock and Anagrams (HackerRank classic)
    // Input : a string                     Sample: abba
    // Output: number of pairs of substrings that are anagrams of each other   -> 4
    // Idea  : For each substring length, sort every substring's letters and count
    //         how many substrings share the same sorted form. A group of c equal
    //         ones makes c * (c - 1) / 2 pairs.
    // ---------------------------------------------------------------------
    static void m41(Scanner sc) {
        String text = sc.next();
        long pairs = 0;
        for (int length = 1; length < text.length(); length++) {
            Map<String, Integer> count = new HashMap<>();
            for (int start = 0; start + length <= text.length(); start++) {
                char[] letters = text.substring(start, start + length).toCharArray();
                Arrays.sort(letters);
                String key = new String(letters);
                count.put(key, count.getOrDefault(key, 0) + 1);
            }
            for (int groupSize : count.values()) {
                pairs += (long) groupSize * (groupSize - 1) / 2;
            }
        }
        System.out.println(pairs);
    }

    // ---------------------------------------------------------------------
    // M42. Maximum Product Subarray
    // Input : n, then the array            Sample: 4 / 2 3 -2 4
    // Output: biggest product of a continuous part      -> 6
    // Idea  : A negative number can turn the smallest product into the biggest,
    //         so track BOTH the max and the min product ending at each position.
    // ---------------------------------------------------------------------
    static void m42(Scanner sc) {
        int n = sc.nextInt();
        int[] numbers = readInts(sc, n);
        long maxEnding = numbers[0];
        long minEnding = numbers[0];
        long best = numbers[0];
        for (int i = 1; i < n; i++) {
            long x = numbers[i];
            if (x < 0) {
                long temp = maxEnding;          // multiplying by a negative flips max and min
                maxEnding = minEnding;
                minEnding = temp;
            }
            maxEnding = Math.max(x, maxEnding * x);
            minEnding = Math.min(x, minEnding * x);
            best = Math.max(best, maxEnding);
        }
        System.out.println(best);
    }

    // ---------------------------------------------------------------------
    // M43. Jump Game II
    // Input : n, then the array: from index i you can jump 1..array[i] steps
    //         Sample: 5 / 2 3 1 1 4
    // Output: minimum jumps to reach the last index     -> 2
    // Idea  : Greedy. Track how far we can reach with the current number of jumps
    //         (currentEnd). When we pass it, we need one more jump, and the new
    //         reach is the farthest point seen so far.
    // ---------------------------------------------------------------------
    static void m43(Scanner sc) {
        int n = sc.nextInt();
        int[] steps = readInts(sc, n);
        int jumps = 0;
        int currentEnd = 0;
        int farthest = 0;
        for (int i = 0; i < n - 1; i++) {
            farthest = Math.max(farthest, i + steps[i]);
            if (i == currentEnd) {
                jumps++;
                currentEnd = farthest;
            }
        }
        System.out.println(jumps);
    }

    // ---------------------------------------------------------------------
    // M44. Meeting Rooms II
    // Input : n, then n lines "start end"  Sample: 3 / 0 30 / 5 10 / 15 20
    // Output: minimum number of rooms so no meetings overlap     -> 2
    // Idea  : Sort all start times and all end times separately. Walk through the
    //         starts: if a meeting starts before the earliest one ends, we need a
    //         new room; otherwise a room was freed.
    // ---------------------------------------------------------------------
    static void m44(Scanner sc) {
        int n = sc.nextInt();
        int[] starts = new int[n];
        int[] ends = new int[n];
        for (int i = 0; i < n; i++) {
            starts[i] = sc.nextInt();
            ends[i] = sc.nextInt();
        }
        Arrays.sort(starts);
        Arrays.sort(ends);
        int rooms = 0;
        int maxRooms = 0;
        int endIndex = 0;
        for (int start : starts) {
            if (start < ends[endIndex]) {
                rooms++;                 // no room is free yet
            } else {
                endIndex++;              // a meeting ended - reuse its room
            }
            maxRooms = Math.max(maxRooms, rooms);
        }
        System.out.println(maxRooms);
    }

    // ---------------------------------------------------------------------
    // M45. Longest Consecutive Sequence
    // Input : n, then the array            Sample: 6 / 100 4 200 1 3 2
    // Output: length of the longest run of consecutive numbers (1,2,3,4)   -> 4
    // Idea  : Put everything in a Set. Only start counting from a number that has
    //         NO smaller neighbour (x - 1 is missing). That makes it O(n), not
    //         O(n log n) like sorting.
    // ---------------------------------------------------------------------
    static void m45(Scanner sc) {
        int n = sc.nextInt();
        Set<Integer> numbers = new HashSet<>();
        for (int i = 0; i < n; i++) {
            numbers.add(sc.nextInt());
        }
        int longest = 0;
        for (int number : numbers) {
            if (!numbers.contains(number - 1)) {         // start of a run
                int length = 1;
                while (numbers.contains(number + length)) {
                    length++;
                }
                longest = Math.max(longest, length);
            }
        }
        System.out.println(longest);
    }

    // =====================================================================
    // PRACTICE RUNNER
    // =====================================================================

    public static void main(String[] args) {
        List<Consumer<Scanner>> problems = Arrays.asList(
                HackerRankMediumPrep::m01, HackerRankMediumPrep::m02, HackerRankMediumPrep::m03,
                HackerRankMediumPrep::m04, HackerRankMediumPrep::m05, HackerRankMediumPrep::m06,
                HackerRankMediumPrep::m07, HackerRankMediumPrep::m08, HackerRankMediumPrep::m09,
                HackerRankMediumPrep::m10, HackerRankMediumPrep::m11, HackerRankMediumPrep::m12,
                HackerRankMediumPrep::m13, HackerRankMediumPrep::m14, HackerRankMediumPrep::m15,
                HackerRankMediumPrep::m16, HackerRankMediumPrep::m17, HackerRankMediumPrep::m18,
                HackerRankMediumPrep::m19, HackerRankMediumPrep::m20, HackerRankMediumPrep::m21,
                HackerRankMediumPrep::m22, HackerRankMediumPrep::m23, HackerRankMediumPrep::m24,
                HackerRankMediumPrep::m25, HackerRankMediumPrep::m26, HackerRankMediumPrep::m27,
                HackerRankMediumPrep::m28, HackerRankMediumPrep::m29, HackerRankMediumPrep::m30,
                HackerRankMediumPrep::m31, HackerRankMediumPrep::m32, HackerRankMediumPrep::m33,
                HackerRankMediumPrep::m34, HackerRankMediumPrep::m35, HackerRankMediumPrep::m36,
                HackerRankMediumPrep::m37, HackerRankMediumPrep::m38, HackerRankMediumPrep::m39,
                HackerRankMediumPrep::m40, HackerRankMediumPrep::m41, HackerRankMediumPrep::m42,
                HackerRankMediumPrep::m43, HackerRankMediumPrep::m44, HackerRankMediumPrep::m45);

        Scanner sc = new Scanner(System.in);
        int problem = sc.nextInt();
        sc.nextLine();                         // throw away the rest of the first line
        if (problem < 1 || problem > problems.size()) {
            System.out.println("Unknown problem number. Use 1 to " + problems.size() + ".");
            return;
        }
        problems.get(problem - 1).accept(sc);
    }
}
