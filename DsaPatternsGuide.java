import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

/**
 * DSA PATTERNS GUIDE - how to solve ANY coding question, from easy to hard.
 *
 * The secret: there are only about 20 patterns. A new question is almost always
 * an old pattern in a new costume. Learn to RECOGNISE the pattern, then the
 * code is just the template.
 *
 * Each pattern below has:
 *   WHEN TO USE : the words in the question that give it away
 *   TEMPLATE    : the idea in plain English
 *   EASY / MEDIUM / HARD examples, solved with the same template
 *
 * Study order (easy -> hard):
 *   1 HashMap  2 Two pointers  3 Sliding window  4 Prefix sum  5 Binary search
 *   6 Linked list  7 Stack  8 Heap  9 Intervals  10 Backtracking  11 Trees
 *   12 Graph BFS/DFS  13 Union-Find  14 Shortest path / Topological sort
 *   15 DP (1D)  16 DP (strings and grids)  17 Greedy  18 Bits  19 Trie  20 Math
 *
 * Java 8 compatible. Run (optional):  javac DsaPatternsGuide.java && java DsaPatternsGuide
 */
public class DsaPatternsGuide {

    // =====================================================================
    // STEP 0 : HOW TO ATTACK ANY QUESTION  (do this in order, out loud)
    // =====================================================================
    //
    //  1. Repeat the problem in your own words. Ask about edge cases:
    //     empty input? negative numbers? duplicates? sorted? how big is n?
    //  2. Write a tiny example and solve it by hand.
    //  3. Say the BRUTE FORCE first (usually two loops, O(n^2)). It always scores.
    //  4. Look at the constraint table below to see what speed you need.
    //  5. Find the pattern from the keyword table below, use its template.
    //  6. Code it. Use clear names.
    //  7. Test with your example + edge cases. Then say time and space complexity.
    //
    // CONSTRAINT -> WHAT SPEED YOU NEED
    //   n <= 10            O(n!)         backtracking, try all orders
    //   n <= 20            O(2^n)        backtracking, bitmask
    //   n <= 500           O(n^3)        triple loops, interval DP
    //   n <= 5,000         O(n^2)        two loops, 2D DP
    //   n <= 100,000       O(n log n)    sorting, heap, binary search
    //   n <= 1,000,000     O(n)          one pass, hash map, two pointers
    //   n up to 10^9       O(log n)      binary search, math
    //
    // KEYWORD -> PATTERN
    //   "pair / two numbers that sum"             HashMap (1) or Two pointers (2)
    //   "sorted array", "palindrome", "reverse"   Two pointers (2)
    //   "longest/shortest SUBARRAY or SUBSTRING"  Sliding window (3)
    //   "sum of a range", "subarray sum equals"   Prefix sum (4)
    //   "sorted" + "find", or "minimum X such that it works"   Binary search (5)
    //   "linked list", "cycle", "middle"          Fast and slow pointers (6)
    //   "brackets", "next greater", "histogram"   Stack (7)
    //   "top K", "kth largest", "median", "merge K"   Heap (8)
    //   "intervals", "meetings", "overlap"        Sort + sweep (9)
    //   "all combinations / permutations / subsets"   Backtracking (10)
    //   "tree", "depth", "path", "BST"            Tree DFS / BFS (11)
    //   "grid of islands", "shortest steps", "connected"   Graph BFS / DFS (12)
    //   "groups", "are A and B connected", "cycle in undirected"   Union-Find (13)
    //   "prerequisites", "order of tasks"         Topological sort (14)
    //   "cheapest / shortest weighted path"       Dijkstra / Bellman-Ford (14)
    //   "number of ways", "minimum cost", "maximum profit" + choices   DP (15, 16)
    //   "at each step take the best local choice" Greedy (17)
    //   "single number", "power of 2", "XOR"      Bit manipulation (18)
    //   "prefix", "dictionary of words", "autocomplete"   Trie (19)
    //   "prime", "gcd", "modulo 1e9+7", "nCr"     Math (20)

    // =====================================================================
    // PATTERN 1 : HASHMAP / HASHSET   ("have I seen this before?")
    // WHEN   : find a pair, count something, detect duplicates, group things.
    // TEMPLATE: walk once. Before storing the current item, ask the map about the
    //          thing you NEED. Trade memory for speed: O(n^2) -> O(n).
    // =====================================================================

    // EASY : Two Sum - indexes of two numbers that add up to target.
    static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> seen = new HashMap<>();          // number -> index
        for (int i = 0; i < nums.length; i++) {
            Integer partnerIndex = seen.get(target - nums[i]);
            if (partnerIndex != null) {
                return new int[]{partnerIndex, i};
            }
            seen.put(nums[i], i);
        }
        return new int[]{-1, -1};
    }

    // MEDIUM : Longest Consecutive Sequence in O(n).
    // Only start counting from a number whose predecessor is missing.
    static int longestConsecutive(int[] nums) {
        Set<Integer> all = new HashSet<>();
        for (int number : nums) {
            all.add(number);
        }
        int longest = 0;
        for (int number : all) {
            if (!all.contains(number - 1)) {              // start of a run
                int length = 1;
                while (all.contains(number + length)) {
                    length++;
                }
                longest = Math.max(longest, length);
            }
        }
        return longest;
    }

    // HARD : First Missing Positive in O(n) time and O(1) extra space.
    // Trick: the ARRAY ITSELF is the hash. Number x belongs at index x - 1.
    // Put every number in its place, then the first wrong place shows the answer.
    static int firstMissingPositive(int[] nums) {
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            while (nums[i] >= 1 && nums[i] <= n && nums[nums[i] - 1] != nums[i]) {
                int target = nums[i] - 1;
                int temp = nums[target];
                nums[target] = nums[i];
                nums[i] = temp;
            }
        }
        for (int i = 0; i < n; i++) {
            if (nums[i] != i + 1) {
                return i + 1;
            }
        }
        return n + 1;
    }

    // =====================================================================
    // PATTERN 2 : TWO POINTERS   (one pointer at each end, move inward)
    // WHEN   : sorted array, palindrome, pairs/triplets with a target, reverse.
    // TEMPLATE: left = 0, right = n - 1. Look at both. Decide which pointer moves
    //          and WHY (that "why" is the whole problem).
    // =====================================================================

    // EASY : Valid Palindrome (ignore case and symbols).
    static boolean isPalindrome(String text) {
        int left = 0;
        int right = text.length() - 1;
        while (left < right) {
            while (left < right && !Character.isLetterOrDigit(text.charAt(left))) {
                left++;
            }
            while (left < right && !Character.isLetterOrDigit(text.charAt(right))) {
                right--;
            }
            if (Character.toLowerCase(text.charAt(left)) != Character.toLowerCase(text.charAt(right))) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }

    // MEDIUM : 3Sum - all unique triplets that add up to 0.
    // Sort, fix one number, then two-pointer the rest (sum too small -> left++,
    // too big -> right--). Skip equal numbers so triplets are not repeated.
    static List<List<Integer>> threeSum(int[] nums) {
        int[] sorted = nums.clone();
        Arrays.sort(sorted);
        List<List<Integer>> result = new ArrayList<>();
        for (int i = 0; i < sorted.length - 2; i++) {
            if (i > 0 && sorted[i] == sorted[i - 1]) {
                continue;
            }
            int left = i + 1;
            int right = sorted.length - 1;
            while (left < right) {
                int sum = sorted[i] + sorted[left] + sorted[right];
                if (sum == 0) {
                    result.add(Arrays.asList(sorted[i], sorted[left], sorted[right]));
                    left++;
                    right--;
                    while (left < right && sorted[left] == sorted[left - 1]) {
                        left++;
                    }
                    while (left < right && sorted[right] == sorted[right + 1]) {
                        right--;
                    }
                } else if (sum < 0) {
                    left++;
                } else {
                    right--;
                }
            }
        }
        return result;
    }

    // HARD : Trapping Rain Water.
    // Water over a bar = min(tallest on left, tallest on right) - bar height.
    // Move the pointer on the SHORTER side; that side decides the water level.
    static int trapRainWater(int[] heights) {
        int left = 0;
        int right = heights.length - 1;
        int leftMax = 0;
        int rightMax = 0;
        int water = 0;
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
        return water;
    }

    // =====================================================================
    // PATTERN 3 : SLIDING WINDOW   (a window [left..right] that grows and shrinks)
    // WHEN   : longest / shortest / best SUBARRAY or SUBSTRING with a condition.
    // TEMPLATE: for right in 0..n-1: add item at right to the window.
    //          while the window is invalid (or can shrink): remove item at left, left++.
    //          update the answer.   Every item enters once and leaves once: O(n).
    // =====================================================================

    // EASY : Maximum sum of any subarray of size k (fixed window).
    static int maxSumOfSizeK(int[] nums, int k) {
        int windowSum = 0;
        for (int i = 0; i < k; i++) {
            windowSum += nums[i];
        }
        int best = windowSum;
        for (int i = k; i < nums.length; i++) {
            windowSum += nums[i] - nums[i - k];       // one in, one out
            best = Math.max(best, windowSum);
        }
        return best;
    }

    // MEDIUM : Longest substring without repeating characters (flexible window).
    static int longestUniqueSubstring(String text) {
        Set<Character> window = new HashSet<>();
        int left = 0;
        int best = 0;
        for (int right = 0; right < text.length(); right++) {
            while (window.contains(text.charAt(right))) {      // invalid -> shrink
                window.remove(text.charAt(left));
                left++;
            }
            window.add(text.charAt(right));
            best = Math.max(best, right - left + 1);
        }
        return best;
    }

    // HARD : Minimum Window Substring - smallest part of s that contains every
    // letter of t (with counts).
    // "missing" = how many needed letters the window still lacks. When it hits 0
    // the window is valid, so shrink from the left as far as possible.
    static String minWindow(String s, String t) {
        int[] need = new int[128];
        for (char c : t.toCharArray()) {
            need[c]++;
        }
        int missing = t.length();
        int left = 0;
        int bestStart = 0;
        int bestLength = Integer.MAX_VALUE;
        for (int right = 0; right < s.length(); right++) {
            char added = s.charAt(right);
            if (need[added] > 0) {
                missing--;
            }
            need[added]--;                                      // may go negative = extra letters
            while (missing == 0) {
                if (right - left + 1 < bestLength) {
                    bestLength = right - left + 1;
                    bestStart = left;
                }
                char removed = s.charAt(left);
                need[removed]++;
                if (need[removed] > 0) {
                    missing++;                                  // we just removed a needed letter
                }
                left++;
            }
        }
        return bestLength == Integer.MAX_VALUE ? "" : s.substring(bestStart, bestStart + bestLength);
    }

    // =====================================================================
    // PATTERN 4 : PREFIX SUM   (precompute running totals)
    // WHEN   : many range-sum questions, "subarray with sum K", "equal halves".
    // TEMPLATE: prefix[i] = sum of the first i numbers.
    //          sum(l..r) = prefix[r + 1] - prefix[l].
    //          "Subarray sum = K" -> look for (current prefix - K) in a map.
    // =====================================================================

    // EASY : Range sum query - O(1) per question after O(n) setup.
    static class RangeSum {
        private final long[] prefix;

        RangeSum(int[] nums) {
            prefix = new long[nums.length + 1];
            for (int i = 0; i < nums.length; i++) {
                prefix[i + 1] = prefix[i] + nums[i];
            }
        }

        long sum(int left, int right) {
            return prefix[right + 1] - prefix[left];
        }
    }

    // MEDIUM : Subarray Sum Equals K (works with negative numbers).
    static int subarraySumEqualsK(int[] nums, int k) {
        Map<Integer, Integer> seen = new HashMap<>();
        seen.put(0, 1);                                    // empty prefix
        int prefix = 0;
        int count = 0;
        for (int number : nums) {
            prefix += number;
            count += seen.getOrDefault(prefix - k, 0);
            seen.put(prefix, seen.getOrDefault(prefix, 0) + 1);
        }
        return count;
    }

    // HARD : 2D prefix sum - sum of ANY rectangle in O(1).
    // p[r][c] = sum of everything above-left of (r, c).
    // Rectangle = big - top strip - left strip + corner (added back once).
    static class Matrix2D {
        private final long[][] p;

        Matrix2D(int[][] matrix) {
            int rows = matrix.length;
            int cols = matrix[0].length;
            p = new long[rows + 1][cols + 1];
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    p[r + 1][c + 1] = matrix[r][c] + p[r][c + 1] + p[r + 1][c] - p[r][c];
                }
            }
        }

        long region(int r1, int c1, int r2, int c2) {
            return p[r2 + 1][c2 + 1] - p[r1][c2 + 1] - p[r2 + 1][c1] + p[r1][c1];
        }
    }

    // =====================================================================
    // PATTERN 5 : BINARY SEARCH   (throw away half every step)
    // WHEN   : sorted data, OR "find the minimum X such that something works"
    //          (the answer is a number, and if X works then X+1 also works).
    // TEMPLATE: lo = smallest possible answer, hi = biggest possible answer.
    //          while lo < hi: mid; if mid works hi = mid else lo = mid + 1.
    //          At the end lo is the answer.   O(log range * cost of one check).
    // =====================================================================

    // EASY : Lower bound - first index whose value is >= target (array is sorted).
    static int lowerBound(int[] sorted, int target) {
        int low = 0;
        int high = sorted.length;
        while (low < high) {
            int mid = low + (high - low) / 2;
            if (sorted[mid] < target) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }
        return low;
    }

    // MEDIUM : Koko eating bananas - smallest speed so all piles finish in h hours.
    // Bigger speed always works if a smaller one works -> binary search the SPEED.
    static int minEatingSpeed(int[] piles, int hours) {
        int low = 1;
        int high = 0;
        for (int pile : piles) {
            high = Math.max(high, pile);
        }
        while (low < high) {
            int speed = low + (high - low) / 2;
            long neededHours = 0;
            for (int pile : piles) {
                neededHours += (pile + speed - 1) / speed;      // ceiling division
            }
            if (neededHours <= hours) {
                high = speed;
            } else {
                low = speed + 1;
            }
        }
        return low;
    }

    // HARD : Split Array Largest Sum - cut into m parts so the LARGEST part sum
    // is as small as possible. Binary search the answer (the largest sum allowed);
    // check with a greedy count of how many parts are needed.
    static int splitArray(int[] nums, int m) {
        int low = 0;
        int high = 0;
        for (int number : nums) {
            low = Math.max(low, number);          // a part must hold at least the biggest number
            high += number;                       // one part holds everything
        }
        while (low < high) {
            int limit = low + (high - low) / 2;
            if (partsNeeded(nums, limit) <= m) {
                high = limit;
            } else {
                low = limit + 1;
            }
        }
        return low;
    }

    static int partsNeeded(int[] nums, int limit) {
        int parts = 1;
        int current = 0;
        for (int number : nums) {
            if (current + number > limit) {
                parts++;
                current = 0;
            }
            current += number;
        }
        return parts;
    }

    // =====================================================================
    // PATTERN 6 : LINKED LIST + FAST/SLOW POINTERS
    // WHEN   : linked list, "middle", "cycle", "k-th from end", "reverse".
    // TEMPLATE: slow moves 1 step, fast moves 2. When fast reaches the end, slow
    //          is in the middle. If there is a cycle they meet.
    //          Reversing: keep previous / current / next. Draw the arrows!
    // =====================================================================

    static class ListNode {
        int val;
        ListNode next;

        ListNode(int val) {
            this.val = val;
        }
    }

    static ListNode listFrom(int... values) {
        ListNode dummy = new ListNode(0);
        ListNode tail = dummy;
        for (int value : values) {
            tail.next = new ListNode(value);
            tail = tail.next;
        }
        return dummy.next;
    }

    static String listToString(ListNode head) {
        StringBuilder text = new StringBuilder();
        for (ListNode node = head; node != null; node = node.next) {
            text.append(node.val).append(node.next != null ? "->" : "");
        }
        return text.toString();
    }

    // EASY : Middle of the list.
    static ListNode middleNode(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }

    // MEDIUM : Where does the cycle start? (Floyd's algorithm)
    // Phase 1: slow and fast meet inside the cycle.
    // Phase 2: a pointer from the head and one from the meeting point, both
    //          moving 1 step at a time, meet exactly at the cycle start.
    static ListNode cycleStart(ListNode head) {
        ListNode slow = head;
        ListNode fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                ListNode pointer = head;
                while (pointer != slow) {
                    pointer = pointer.next;
                    slow = slow.next;
                }
                return pointer;
            }
        }
        return null;
    }

    // HARD : Reverse nodes in groups of k. If the last group has fewer than k
    // nodes, leave it as it is.
    static ListNode reverseKGroup(ListNode head, int k) {
        ListNode check = head;
        int count = 0;
        while (check != null && count < k) {                  // is there a full group?
            check = check.next;
            count++;
        }
        if (count < k) {
            return head;
        }
        ListNode previous = null;
        ListNode current = head;
        for (int i = 0; i < k; i++) {                         // reverse exactly k nodes
            ListNode nextNode = current.next;
            current.next = previous;
            previous = current;
            current = nextNode;
        }
        head.next = reverseKGroup(current, k);                // old head is now the tail of this group
        return previous;
    }

    // =====================================================================
    // PATTERN 7 : STACK / MONOTONIC STACK
    // WHEN   : brackets, "undo", "next greater / smaller element", "histogram".
    // TEMPLATE: Normal stack: last in, first out.
    //          Monotonic stack: keep the stack sorted. When a new item breaks
    //          the order, pop items and ANSWER them. Every item pushed/popped once.
    // =====================================================================

    // EASY : Valid brackets.
    static boolean isValidBrackets(String text) {
        Deque<Character> stack = new ArrayDeque<>();
        for (char c : text.toCharArray()) {
            if (c == '(' || c == '{' || c == '[') {
                stack.push(c);
            } else {
                if (stack.isEmpty()) {
                    return false;
                }
                char open = stack.pop();
                if ((c == ')' && open != '(') || (c == '}' && open != '{') || (c == ']' && open != '[')) {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }

    // MEDIUM : Daily Temperatures - days to wait for a warmer day.
    static int[] dailyTemperatures(int[] temps) {
        int[] answer = new int[temps.length];
        Deque<Integer> waiting = new ArrayDeque<>();           // indexes still waiting for a warmer day
        for (int i = 0; i < temps.length; i++) {
            while (!waiting.isEmpty() && temps[waiting.peek()] < temps[i]) {
                int earlier = waiting.pop();
                answer[earlier] = i - earlier;
            }
            waiting.push(i);
        }
        return answer;
    }

    // HARD : Largest Rectangle in Histogram.
    // Keep bars in increasing height. A shorter bar ends every taller bar on the
    // stack; compute their rectangles at that moment.
    static int largestRectangle(int[] heights) {
        Deque<Integer> stack = new ArrayDeque<>();
        int best = 0;
        for (int i = 0; i <= heights.length; i++) {
            int current = (i == heights.length) ? 0 : heights[i];      // 0 at the end pops everything
            while (!stack.isEmpty() && heights[stack.peek()] >= current) {
                int height = heights[stack.pop()];
                int leftEdge = stack.isEmpty() ? -1 : stack.peek();
                best = Math.max(best, height * (i - leftEdge - 1));
            }
            stack.push(i);
        }
        return best;
    }

    // =====================================================================
    // PATTERN 8 : HEAP / PRIORITY QUEUE
    // WHEN   : "top K", "kth largest/smallest", "median", "merge K sorted", "closest".
    // TEMPLATE: For K LARGEST keep a MIN-heap of size K (throw out the smallest).
    //          For K SMALLEST keep a MAX-heap of size K.   O(n log k).
    // =====================================================================

    // EASY : Kth largest element.
    static int kthLargest(int[] nums, int k) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        for (int number : nums) {
            minHeap.add(number);
            if (minHeap.size() > k) {
                minHeap.poll();
            }
        }
        return minHeap.peek();
    }

    // MEDIUM : Top K frequent numbers (most frequent first).
    static List<Integer> topKFrequent(int[] nums, int k) {
        Map<Integer, Integer> count = new HashMap<>();
        for (int number : nums) {
            count.put(number, count.getOrDefault(number, 0) + 1);
        }
        PriorityQueue<Integer> heap = new PriorityQueue<>((a, b) -> count.get(a) - count.get(b));
        for (int number : count.keySet()) {
            heap.add(number);
            if (heap.size() > k) {
                heap.poll();                                   // drop the least frequent
            }
        }
        List<Integer> result = new ArrayList<>(heap);
        result.sort((a, b) -> count.get(b) - count.get(a));
        return result;
    }

    // HARD : Median from a data stream - TWO heaps.
    // lower = max-heap with the smaller half, upper = min-heap with the bigger half.
    // The median is always at the top of one heap (or between the two tops).
    static class MedianFinder {
        private final PriorityQueue<Integer> lower = new PriorityQueue<>(Collections.reverseOrder());
        private final PriorityQueue<Integer> upper = new PriorityQueue<>();

        void add(int number) {
            lower.add(number);
            upper.add(lower.poll());                           // keeps every lower <= every upper
            if (upper.size() > lower.size()) {
                lower.add(upper.poll());                       // keep sizes balanced
            }
        }

        double median() {
            if (lower.size() > upper.size()) {
                return lower.peek();
            }
            return (lower.peek() + upper.peek()) / 2.0;
        }
    }

    // =====================================================================
    // PATTERN 9 : INTERVALS   (sort by start, then sweep)
    // WHEN   : meetings, ranges, "overlap", "merge", "minimum rooms".
    // TEMPLATE: SORT by start time. Compare each interval with the previous one.
    // =====================================================================

    // EASY : Can one person attend all meetings?
    static boolean canAttendAll(int[][] meetings) {
        int[][] sorted = meetings.clone();
        Arrays.sort(sorted, (a, b) -> Integer.compare(a[0], b[0]));
        for (int i = 1; i < sorted.length; i++) {
            if (sorted[i][0] < sorted[i - 1][1]) {
                return false;                                  // starts before the previous one ends
            }
        }
        return true;
    }

    // MEDIUM : Merge overlapping intervals.
    static int[][] mergeIntervals(int[][] intervals) {
        int[][] sorted = intervals.clone();
        Arrays.sort(sorted, (a, b) -> Integer.compare(a[0], b[0]));
        List<int[]> merged = new ArrayList<>();
        for (int[] interval : sorted) {
            if (merged.isEmpty() || merged.get(merged.size() - 1)[1] < interval[0]) {
                merged.add(new int[]{interval[0], interval[1]});
            } else {
                int[] last = merged.get(merged.size() - 1);
                last[1] = Math.max(last[1], interval[1]);
            }
        }
        return merged.toArray(new int[0][]);
    }

    // HARD-ish : Minimum meeting rooms. A min-heap holds the END time of every
    // room in use. If the earliest-ending room is free before the new meeting
    // starts, reuse it; otherwise open a new room.
    static int minMeetingRooms(int[][] meetings) {
        int[][] sorted = meetings.clone();
        Arrays.sort(sorted, (a, b) -> Integer.compare(a[0], b[0]));
        PriorityQueue<Integer> endTimes = new PriorityQueue<>();
        for (int[] meeting : sorted) {
            if (!endTimes.isEmpty() && endTimes.peek() <= meeting[0]) {
                endTimes.poll();                               // reuse that room
            }
            endTimes.add(meeting[1]);
        }
        return endTimes.size();
    }

    // =====================================================================
    // PATTERN 10 : BACKTRACKING   (choose -> explore -> UN-choose)
    // WHEN   : "all subsets / permutations / combinations", "place N queens", sudoku.
    // TEMPLATE:
    //     void go(state):
    //         if state is complete: save it; return
    //         for each choice:
    //             if choice is allowed:
    //                 apply choice
    //                 go(new state)
    //                 undo choice          <- the most forgotten line
    // =====================================================================

    // EASY : All subsets.
    static List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        buildSubsets(0, nums, new ArrayList<>(), result);
        return result;
    }

    static void buildSubsets(int start, int[] nums, List<Integer> current, List<List<Integer>> result) {
        result.add(new ArrayList<>(current));                  // every path is a valid subset
        for (int i = start; i < nums.length; i++) {
            current.add(nums[i]);
            buildSubsets(i + 1, nums, current, result);
            current.remove(current.size() - 1);                // undo
        }
    }

    // MEDIUM : Combination Sum - numbers can be reused. All combinations adding to target.
    static List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        buildCombinations(candidates, 0, target, new ArrayList<>(), result);
        return result;
    }

    static void buildCombinations(int[] candidates, int start, int remaining,
                                  List<Integer> current, List<List<Integer>> result) {
        if (remaining == 0) {
            result.add(new ArrayList<>(current));
            return;
        }
        for (int i = start; i < candidates.length; i++) {
            if (candidates[i] <= remaining) {
                current.add(candidates[i]);
                buildCombinations(candidates, i, remaining - candidates[i], current, result);   // i, not i+1: reuse allowed
                current.remove(current.size() - 1);
            }
        }
    }

    // HARD : N-Queens - count the ways to place n queens so none attack each other.
    // One queen per row. Remember which columns and diagonals are taken, so each
    // check is O(1).
    static int totalNQueens(int n) {
        return placeQueen(0, n, new boolean[n], new boolean[2 * n], new boolean[2 * n]);
    }

    static int placeQueen(int row, int n, boolean[] columns, boolean[] diagonal1, boolean[] diagonal2) {
        if (row == n) {
            return 1;
        }
        int ways = 0;
        for (int col = 0; col < n; col++) {
            int d1 = row + col;                  // same value along "/" diagonals
            int d2 = row - col + n;              // same value along "\" diagonals
            if (columns[col] || diagonal1[d1] || diagonal2[d2]) {
                continue;
            }
            columns[col] = diagonal1[d1] = diagonal2[d2] = true;       // place
            ways += placeQueen(row + 1, n, columns, diagonal1, diagonal2);
            columns[col] = diagonal1[d1] = diagonal2[d2] = false;      // undo
        }
        return ways;
    }

    // =====================================================================
    // PATTERN 11 : TREES  (DFS = recursion, BFS = queue)
    // WHEN   : anything with a tree: depth, path, BST, level order.
    // TEMPLATE (DFS): solve(node) = combine( solve(node.left), solve(node.right) ).
    //          First line: if node is null return the "empty" answer.
    //          BFS: queue + process one LEVEL at a time using the queue size.
    // =====================================================================

    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }
    }

    // Build a tree from level-order values (null = no node), like LeetCode.
    static TreeNode buildTree(Integer... values) {
        if (values.length == 0 || values[0] == null) {
            return null;
        }
        TreeNode root = new TreeNode(values[0]);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        int i = 1;
        while (!queue.isEmpty() && i < values.length) {
            TreeNode node = queue.poll();
            if (i < values.length && values[i] != null) {
                node.left = new TreeNode(values[i]);
                queue.add(node.left);
            }
            i++;
            if (i < values.length && values[i] != null) {
                node.right = new TreeNode(values[i]);
                queue.add(node.right);
            }
            i++;
        }
        return root;
    }

    // EASY : Maximum depth.
    static int maxDepth(TreeNode node) {
        if (node == null) {
            return 0;
        }
        return 1 + Math.max(maxDepth(node.left), maxDepth(node.right));
    }

    // MEDIUM : Level order traversal (BFS) and BST validation (pass allowed range down).
    static List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> levels = new ArrayList<>();
        if (root == null) {
            return levels;
        }
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            int levelSize = queue.size();                      // nodes in THIS level
            List<Integer> level = new ArrayList<>();
            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                level.add(node.val);
                if (node.left != null) {
                    queue.add(node.left);
                }
                if (node.right != null) {
                    queue.add(node.right);
                }
            }
            levels.add(level);
        }
        return levels;
    }

    static boolean isValidBst(TreeNode node, long min, long max) {
        if (node == null) {
            return true;
        }
        if (node.val <= min || node.val >= max) {
            return false;
        }
        return isValidBst(node.left, min, node.val) && isValidBst(node.right, node.val, max);
    }

    // HARD : Binary Tree Maximum Path Sum (a path can start and end anywhere).
    // For each node: the best path that BENDS at this node = node + best left
    // branch + best right branch. But only ONE branch can continue upward.
    static int maxPathSum(TreeNode root) {
        int[] best = {Integer.MIN_VALUE};
        branchGain(root, best);
        return best[0];
    }

    static int branchGain(TreeNode node, int[] best) {
        if (node == null) {
            return 0;
        }
        int left = Math.max(0, branchGain(node.left, best));   // ignore a negative branch
        int right = Math.max(0, branchGain(node.right, best));
        best[0] = Math.max(best[0], node.val + left + right);  // path bending here
        return node.val + Math.max(left, right);               // path going up
    }

    // =====================================================================
    // PATTERN 12 : GRAPH BFS / DFS
    // WHEN   : grid problems, islands, "connected", "shortest number of steps"
    //          (unweighted).
    // TEMPLATE: BFS = queue, gives the SHORTEST path in steps.
    //          DFS = recursion, explores everything reachable.
    //          ALWAYS keep a visited set so you never loop forever.
    // =====================================================================

    // EASY : Is there a path between two nodes? (BFS)
    static boolean validPath(int n, int[][] edges, int source, int destination) {
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            graph.get(edge[0]).add(edge[1]);
            graph.get(edge[1]).add(edge[0]);
        }
        boolean[] visited = new boolean[n];
        Queue<Integer> queue = new LinkedList<>();
        queue.add(source);
        visited[source] = true;
        while (!queue.isEmpty()) {
            int node = queue.poll();
            if (node == destination) {
                return true;
            }
            for (int neighbour : graph.get(node)) {
                if (!visited[neighbour]) {
                    visited[neighbour] = true;
                    queue.add(neighbour);
                }
            }
        }
        return false;
    }

    // MEDIUM : Number of Islands (DFS "sinks" each island so it is not counted twice).
    static int numIslands(int[][] grid) {
        int[][] copy = new int[grid.length][];
        for (int i = 0; i < grid.length; i++) {
            copy[i] = grid[i].clone();
        }
        int islands = 0;
        for (int r = 0; r < copy.length; r++) {
            for (int c = 0; c < copy[0].length; c++) {
                if (copy[r][c] == 1) {
                    islands++;
                    sinkIsland(copy, r, c);
                }
            }
        }
        return islands;
    }

    static void sinkIsland(int[][] grid, int r, int c) {
        if (r < 0 || c < 0 || r >= grid.length || c >= grid[0].length || grid[r][c] != 1) {
            return;
        }
        grid[r][c] = 0;                                        // mark visited
        sinkIsland(grid, r + 1, c);
        sinkIsland(grid, r - 1, c);
        sinkIsland(grid, r, c + 1);
        sinkIsland(grid, r, c - 1);
    }

    // HARD : Word Ladder - fewest words in a chain from begin to end, changing ONE
    // letter at a time, every word must be in the list.
    // Trick: each word is a node; neighbours = words one letter away. BFS gives
    // the shortest chain.
    static int ladderLength(String begin, String end, List<String> wordList) {
        Set<String> dictionary = new HashSet<>(wordList);
        if (!dictionary.contains(end)) {
            return 0;
        }
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        queue.add(begin);
        visited.add(begin);
        int steps = 1;
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            for (int i = 0; i < levelSize; i++) {
                String word = queue.poll();
                if (word.equals(end)) {
                    return steps;
                }
                char[] letters = word.toCharArray();
                for (int position = 0; position < letters.length; position++) {
                    char original = letters[position];
                    for (char c = 'a'; c <= 'z'; c++) {
                        letters[position] = c;
                        String next = new String(letters);
                        if (dictionary.contains(next) && visited.add(next)) {
                            queue.add(next);
                        }
                    }
                    letters[position] = original;
                }
            }
            steps++;
        }
        return 0;
    }

    // =====================================================================
    // PATTERN 13 : UNION-FIND (Disjoint Set)
    // WHEN   : "are these two connected?", "number of groups", "does adding this
    //          edge make a cycle?", minimum spanning tree.
    // TEMPLATE: each item starts in its own group. union(a, b) merges groups.
    //          find(x) gives the group leader. If find(a) == find(b) already,
    //          a and b are connected (an edge between them would form a cycle).
    // =====================================================================

    static class DisjointSet {
        private final int[] parent;
        private final int[] rank;
        int components;

        DisjointSet(int n) {
            parent = new int[n];
            rank = new int[n];
            components = n;
            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
        }

        int find(int x) {
            while (parent[x] != x) {
                parent[x] = parent[parent[x]];                 // path compression
                x = parent[x];
            }
            return x;
        }

        boolean union(int a, int b) {
            int rootA = find(a);
            int rootB = find(b);
            if (rootA == rootB) {
                return false;                                  // already together
            }
            if (rank[rootA] < rank[rootB]) {
                int temp = rootA;
                rootA = rootB;
                rootB = temp;
            }
            parent[rootB] = rootA;
            if (rank[rootA] == rank[rootB]) {
                rank[rootA]++;
            }
            components--;
            return true;
        }
    }

    // EASY-MEDIUM : Number of provinces (groups of connected cities).
    static int countProvinces(int[][] connected) {
        int n = connected.length;
        DisjointSet set = new DisjointSet(n);
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (connected[i][j] == 1) {
                    set.union(i, j);
                }
            }
        }
        return set.components;
    }

    // MEDIUM : Redundant connection - the edge that creates a cycle (nodes are 1..n).
    static int[] redundantConnection(int[][] edges) {
        DisjointSet set = new DisjointSet(edges.length + 1);
        for (int[] edge : edges) {
            if (!set.union(edge[0], edge[1])) {
                return edge;
            }
        }
        return new int[0];
    }

    // HARD-ish : Kruskal's Minimum Spanning Tree - cheapest way to connect all nodes.
    // Sort edges by weight; take an edge only if it joins two different groups.
    // edges are {u, v, weight}; nodes are 0..n-1. Returns -1 if it cannot connect everyone.
    static int minimumSpanningTreeCost(int n, int[][] edges) {
        int[][] sorted = edges.clone();
        Arrays.sort(sorted, (a, b) -> Integer.compare(a[2], b[2]));
        DisjointSet set = new DisjointSet(n);
        int total = 0;
        int used = 0;
        for (int[] edge : sorted) {
            if (set.union(edge[0], edge[1])) {
                total += edge[2];
                used++;
            }
        }
        return used == n - 1 ? total : -1;
    }

    // =====================================================================
    // PATTERN 14 : TOPOLOGICAL SORT AND SHORTEST PATH
    // WHEN   : "prerequisites / order of tasks"  -> topological sort
    //          "cheapest / fastest route with weights" -> Dijkstra
    //          "at most K stops" -> Bellman-Ford style (repeat K+1 rounds)
    // =====================================================================

    // MEDIUM : Course Schedule - can all courses be finished? (cycle detection, Kahn)
    // prerequisites[i] = {course, mustTakeFirst}
    static boolean canFinish(int courses, int[][] prerequisites) {
        List<List<Integer>> next = new ArrayList<>();
        for (int i = 0; i < courses; i++) {
            next.add(new ArrayList<>());
        }
        int[] waitingFor = new int[courses];
        for (int[] pair : prerequisites) {
            next.get(pair[1]).add(pair[0]);
            waitingFor[pair[0]]++;
        }
        Queue<Integer> ready = new LinkedList<>();
        for (int i = 0; i < courses; i++) {
            if (waitingFor[i] == 0) {
                ready.add(i);
            }
        }
        int finished = 0;
        while (!ready.isEmpty()) {
            int course = ready.poll();
            finished++;
            for (int dependent : next.get(course)) {
                if (--waitingFor[dependent] == 0) {
                    ready.add(dependent);
                }
            }
        }
        return finished == courses;                            // stuck courses = a cycle
    }

    // MEDIUM : Network Delay Time - Dijkstra from node k. times[i] = {from, to, time}.
    // Returns the time for the signal to reach ALL nodes (nodes are 1..n), or -1.
    static int networkDelayTime(int[][] times, int n, int k) {
        List<List<int[]>> graph = new ArrayList<>();
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        for (int[] t : times) {
            graph.get(t[0]).add(new int[]{t[1], t[2]});
        }
        int[] distance = new int[n + 1];
        Arrays.fill(distance, Integer.MAX_VALUE);
        distance[k] = 0;
        PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> Integer.compare(a[1], b[1]));
        heap.add(new int[]{k, 0});
        while (!heap.isEmpty()) {
            int[] top = heap.poll();
            if (top[1] > distance[top[0]]) {
                continue;                                      // outdated entry
            }
            for (int[] edge : graph.get(top[0])) {
                int newDistance = top[1] + edge[1];
                if (newDistance < distance[edge[0]]) {
                    distance[edge[0]] = newDistance;
                    heap.add(new int[]{edge[0], newDistance});
                }
            }
        }
        int slowest = 0;
        for (int i = 1; i <= n; i++) {
            if (distance[i] == Integer.MAX_VALUE) {
                return -1;
            }
            slowest = Math.max(slowest, distance[i]);
        }
        return slowest;
    }

    // HARD : Cheapest Flights Within K Stops. flights[i] = {from, to, price}.
    // Dijkstra does not respect "at most K stops", so use rounds: round i = best
    // prices using at most i flights. Use a COPY each round so one round adds
    // exactly one flight.
    static int cheapestFlight(int n, int[][] flights, int source, int destination, int maxStops) {
        int infinity = Integer.MAX_VALUE / 2;
        int[] cost = new int[n];
        Arrays.fill(cost, infinity);
        cost[source] = 0;
        for (int round = 0; round <= maxStops; round++) {
            int[] next = cost.clone();
            for (int[] flight : flights) {
                if (cost[flight[0]] < infinity && cost[flight[0]] + flight[2] < next[flight[1]]) {
                    next[flight[1]] = cost[flight[0]] + flight[2];
                }
            }
            cost = next;
        }
        return cost[destination] >= infinity ? -1 : cost[destination];
    }

    // =====================================================================
    // PATTERN 15 : DYNAMIC PROGRAMMING (1D)
    // WHEN   : "number of ways", "minimum cost", "maximum profit", "can we reach"
    //          AND the problem is made of smaller copies of itself.
    // THE 4 QUESTIONS (answer them and the code writes itself):
    //   1. What does dp[i] MEAN?              (write it in a comment!)
    //   2. How does dp[i] come from smaller dp values?   (the formula)
    //   3. What are the starting values?      (base cases)
    //   4. In what order do I fill the table? (small to big)
    // =====================================================================

    // EASY : Climbing stairs. dp[i] = ways to reach stair i = dp[i-1] + dp[i-2].
    static long climbStairs(int n) {
        if (n <= 2) {
            return n;
        }
        long twoBack = 1;
        long oneBack = 2;
        for (int i = 3; i <= n; i++) {
            long current = oneBack + twoBack;
            twoBack = oneBack;
            oneBack = current;
        }
        return oneBack;
    }

    // MEDIUM : Coin change - fewest coins for an amount.
    // dp[a] = fewest coins to make amount a = 1 + min(dp[a - coin]).
    static int coinChange(int[] coins, int amount) {
        int impossible = amount + 1;
        int[] dp = new int[amount + 1];
        Arrays.fill(dp, impossible);
        dp[0] = 0;
        for (int a = 1; a <= amount; a++) {
            for (int coin : coins) {
                if (coin <= a) {
                    dp[a] = Math.min(dp[a], dp[a - coin] + 1);
                }
            }
        }
        return dp[amount] == impossible ? -1 : dp[amount];
    }

    // MEDIUM : Partition Equal Subset Sum (0/1 knapsack).
    // dp[s] = can some subset make sum s? Go from HIGH to LOW so an item is used once.
    static boolean canPartition(int[] nums) {
        int total = 0;
        for (int number : nums) {
            total += number;
        }
        if (total % 2 != 0) {
            return false;
        }
        int target = total / 2;
        boolean[] dp = new boolean[target + 1];
        dp[0] = true;
        for (int number : nums) {
            for (int s = target; s >= number; s--) {
                dp[s] = dp[s] || dp[s - number];
            }
        }
        return dp[target];
    }

    // HARD : Burst Balloons (interval DP).
    // Think BACKWARDS: dp[l][r] = best coins from bursting everything strictly
    // between l and r. Pick the LAST balloon k to burst - its neighbours are then
    // known to be l and r.
    static int maxCoins(int[] balloons) {
        int n = balloons.length;
        int[] nums = new int[n + 2];
        nums[0] = 1;
        nums[n + 1] = 1;
        for (int i = 0; i < n; i++) {
            nums[i + 1] = balloons[i];
        }
        int[][] dp = new int[n + 2][n + 2];
        for (int length = 2; length <= n + 1; length++) {
            for (int left = 0; left + length <= n + 1; left++) {
                int right = left + length;
                for (int last = left + 1; last < right; last++) {
                    int coins = dp[left][last] + dp[last][right] + nums[left] * nums[last] * nums[right];
                    dp[left][right] = Math.max(dp[left][right], coins);
                }
            }
        }
        return dp[0][n + 1];
    }

    // =====================================================================
    // PATTERN 16 : DYNAMIC PROGRAMMING (strings and grids - 2D table)
    // WHEN   : two strings (compare, transform), or a grid with moves.
    // TEMPLATE: dp[i][j] = answer for the first i items of A and first j items of B.
    //          Look at the last item of each: same -> use diagonal; different ->
    //          best of the neighbours (up, left).
    // =====================================================================

    // EASY : Unique paths in a grid (only right or down).
    static int uniquePaths(int rows, int cols) {
        int[] ways = new int[cols];
        Arrays.fill(ways, 1);
        for (int r = 1; r < rows; r++) {
            for (int c = 1; c < cols; c++) {
                ways[c] += ways[c - 1];                        // from above + from left
            }
        }
        return ways[cols - 1];
    }

    // MEDIUM : Longest Common Subsequence.
    static int longestCommonSubsequence(String a, String b) {
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
        return dp[a.length()][b.length()];
    }

    // MEDIUM-HARD : Edit Distance (insert / delete / replace).
    static int editDistance(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];
        for (int i = 0; i <= a.length(); i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= b.length(); j++) {
            dp[0][j] = j;
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
        return dp[a.length()][b.length()];
    }

    // HARD : Regular Expression Matching with '.' (any one letter) and '*'
    // (zero or more of the letter before it).
    // dp[i][j] = do the first i letters of s match the first j letters of p?
    //   p[j-1] is '*'  -> use it ZERO times: dp[i][j-2]
    //                     or ONE MORE time (if the letter before '*' matches s[i-1]): dp[i-1][j]
    //   otherwise      -> s[i-1] must match p[j-1] and dp[i-1][j-1] must be true
    static boolean isMatch(String s, String p) {
        int m = s.length();
        int n = p.length();
        boolean[][] dp = new boolean[m + 1][n + 1];
        dp[0][0] = true;
        for (int j = 2; j <= n; j++) {
            if (p.charAt(j - 1) == '*') {
                dp[0][j] = dp[0][j - 2];                       // "a*b*" can match empty text
            }
        }
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                char pattern = p.charAt(j - 1);
                if (pattern == '*') {
                    boolean zeroTimes = j >= 2 && dp[i][j - 2];
                    boolean oneMore = j >= 2 && charMatches(s.charAt(i - 1), p.charAt(j - 2)) && dp[i - 1][j];
                    dp[i][j] = zeroTimes || oneMore;
                } else {
                    dp[i][j] = charMatches(s.charAt(i - 1), pattern) && dp[i - 1][j - 1];
                }
            }
        }
        return dp[m][n];
    }

    static boolean charMatches(char text, char pattern) {
        return pattern == '.' || pattern == text;
    }

    // =====================================================================
    // PATTERN 17 : GREEDY   (take the best choice NOW and never look back)
    // WHEN   : "maximum / minimum" and a local best choice is always safe.
    //          Often paired with SORTING first.
    // CAREFUL: greedy is not always right. Try to find a counter-example before
    //          trusting it (e.g. coin change with coins 1, 3, 4 is NOT greedy).
    // =====================================================================

    // EASY : Best time to buy and sell once.
    static int maxProfit(int[] prices) {
        int lowestPrice = Integer.MAX_VALUE;
        int best = 0;
        for (int price : prices) {
            lowestPrice = Math.min(lowestPrice, price);
            best = Math.max(best, price - lowestPrice);
        }
        return best;
    }

    // MEDIUM : Jump Game II - minimum jumps to reach the end.
    static int minJumps(int[] steps) {
        int jumps = 0;
        int currentEnd = 0;
        int farthest = 0;
        for (int i = 0; i < steps.length - 1; i++) {
            farthest = Math.max(farthest, i + steps[i]);
            if (i == currentEnd) {                             // must jump now
                jumps++;
                currentEnd = farthest;
            }
        }
        return jumps;
    }

    // HARD : Candy - each child gets >= 1 candy; a child with a higher rating than
    // a neighbour gets more than that neighbour. Fewest candies in total?
    // Two passes: fix the left-neighbour rule, then the right-neighbour rule.
    static int candy(int[] ratings) {
        int n = ratings.length;
        int[] candies = new int[n];
        Arrays.fill(candies, 1);
        for (int i = 1; i < n; i++) {
            if (ratings[i] > ratings[i - 1]) {
                candies[i] = candies[i - 1] + 1;
            }
        }
        for (int i = n - 2; i >= 0; i--) {
            if (ratings[i] > ratings[i + 1]) {
                candies[i] = Math.max(candies[i], candies[i + 1] + 1);
            }
        }
        int total = 0;
        for (int c : candies) {
            total += c;
        }
        return total;
    }

    // =====================================================================
    // PATTERN 18 : BIT MANIPULATION
    // CHEAT SHEET:
    //   x & 1          is x odd?              x & (x - 1)   removes the lowest 1 bit
    //   x >> 1         divide by 2            x << 1        multiply by 2
    //   a ^ a = 0      a ^ 0 = a              x & (x - 1) == 0  -> x is a power of 2
    //   subsets of n items: loop mask from 0 to (1 << n) - 1; bit i set = item i chosen
    // =====================================================================

    // EASY : Single number - every number appears twice except one (XOR cancels pairs).
    static int singleNumber(int[] nums) {
        int result = 0;
        for (int number : nums) {
            result ^= number;
        }
        return result;
    }

    // MEDIUM : Count set bits for every number 0..n.
    // bits(i) = bits(i / 2) + (last bit of i).
    static int[] countBits(int n) {
        int[] bits = new int[n + 1];
        for (int i = 1; i <= n; i++) {
            bits[i] = bits[i >> 1] + (i & 1);
        }
        return bits;
    }

    // HARD-ish : Maximum XOR of two numbers in the array.
    // Build the answer bit by bit from the top. For each bit try to set it: it is
    // possible if two PREFIXES (the numbers cut to the bits so far) XOR to that goal.
    static int findMaximumXor(int[] nums) {
        int max = 0;
        int mask = 0;
        for (int bit = 30; bit >= 0; bit--) {
            mask |= (1 << bit);
            Set<Integer> prefixes = new HashSet<>();
            for (int number : nums) {
                prefixes.add(number & mask);
            }
            int candidate = max | (1 << bit);                  // can we make this bit a 1?
            for (int prefix : prefixes) {
                if (prefixes.contains(candidate ^ prefix)) {   // a ^ b = candidate  <=>  b = candidate ^ a
                    max = candidate;
                    break;
                }
            }
        }
        return max;
    }

    // =====================================================================
    // PATTERN 19 : TRIE (prefix tree)
    // WHEN   : many words, "starts with", autocomplete, spell check, word search.
    // TEMPLATE: each node has up to 26 children, one per letter, and a flag that
    //          says "a word ends here". Insert/search cost = word length.
    // HARD VERSION (idea only): Word Search II = build a Trie of all words, then
    //          backtrack on the grid following the Trie, so you search ALL words
    //          at the same time instead of one by one.
    // =====================================================================

    static class Trie {
        private static class Node {
            Node[] children = new Node[26];
            boolean isWord;
        }

        private final Node root = new Node();

        void insert(String word) {
            Node node = root;
            for (char c : word.toCharArray()) {
                if (node.children[c - 'a'] == null) {
                    node.children[c - 'a'] = new Node();
                }
                node = node.children[c - 'a'];
            }
            node.isWord = true;
        }

        boolean search(String word) {
            Node node = find(word);
            return node != null && node.isWord;
        }

        boolean startsWith(String prefix) {
            return find(prefix) != null;
        }

        // MEDIUM-HARD : autocomplete - every stored word that starts with the prefix.
        List<String> wordsWithPrefix(String prefix) {
            List<String> result = new ArrayList<>();
            Node start = find(prefix);
            if (start != null) {
                collect(start, new StringBuilder(prefix), result);
            }
            return result;
        }

        private Node find(String text) {
            Node node = root;
            for (char c : text.toCharArray()) {
                node = node.children[c - 'a'];
                if (node == null) {
                    return null;
                }
            }
            return node;
        }

        private void collect(Node node, StringBuilder current, List<String> result) {
            if (node.isWord) {
                result.add(current.toString());
            }
            for (int i = 0; i < 26; i++) {
                if (node.children[i] != null) {
                    current.append((char) ('a' + i));
                    collect(node.children[i], current, result);
                    current.deleteCharAt(current.length() - 1);
                }
            }
        }
    }

    // =====================================================================
    // PATTERN 20 : MATH AND NUMBER THEORY
    // =====================================================================

    // EASY : GCD (Euclid).
    static int gcd(int a, int b) {
        return b == 0 ? a : gcd(b, a % b);
    }

    // MEDIUM : Count primes below n - Sieve of Eratosthenes, O(n log log n).
    // Cross out the multiples of every prime, starting from prime * prime.
    static int countPrimes(int n) {
        if (n < 3) {
            return 0;
        }
        boolean[] composite = new boolean[n];
        int count = 0;
        for (int i = 2; i < n; i++) {
            if (!composite[i]) {
                count++;
                for (long multiple = (long) i * i; multiple < n; multiple += i) {
                    composite[(int) multiple] = true;
                }
            }
        }
        return count;
    }

    // MEDIUM : Fast power (a^b mod m) in O(log b). Halve the exponent each step.
    // Used whenever the question says "answer modulo 1,000,000,007".
    static long modPow(long base, long exponent, long mod) {
        long result = 1;
        base %= mod;
        while (exponent > 0) {
            if ((exponent & 1) == 1) {
                result = result * base % mod;
            }
            base = base * base % mod;
            exponent >>= 1;
        }
        return result;
    }

    // HARD-ish : nCr modulo a prime. Division does not work with modulo, so
    // multiply by the INVERSE: inverse(x) = x^(p-2) mod p  (Fermat's little theorem).
    static final long MOD = 1_000_000_007L;

    static long nCr(int n, int r) {
        if (r < 0 || r > n) {
            return 0;
        }
        long[] factorial = new long[n + 1];
        factorial[0] = 1;
        for (int i = 1; i <= n; i++) {
            factorial[i] = factorial[i - 1] * i % MOD;
        }
        long denominator = factorial[r] * factorial[n - r] % MOD;
        return factorial[n] * modPow(denominator, MOD - 2, MOD) % MOD;
    }

    // =====================================================================
    // COMMON MISTAKES (check this list before you submit)
    // =====================================================================
    //  - Off-by-one in loops and binary search (< vs <=, mid + 1 vs mid).
    //  - Forgetting to UNDO the choice in backtracking.
    //  - int overflow: use long for sums, products, and (low + high) in big arrays.
    //  - Modifying a list while looping over it.
    //  - Forgetting the visited set in graph problems (infinite loop).
    //  - Not handling empty input, one element, all equal, negatives.
    //  - Using greedy without checking a counter-example.
    //  - Comparing Integer objects with == (use .equals or unbox).
    //
    // PRACTICE ROADMAP (about 2 problems a day)
    //  Week 1: patterns 1-3      Week 2: patterns 4-7      Week 3: patterns 8-11
    //  Week 4: patterns 12-14    Week 5: patterns 15-17    Week 6: patterns 18-20
    //  After each pattern: solve 3 NEW problems of that pattern WITHOUT looking.

    // =====================================================================
    // DEMO RUNNER (expected answer in the comment on each line)
    // =====================================================================

    private static void print(String label, Object value) {
        System.out.println(label + " => " + value);
    }

    public static void main(String[] args) {
        System.out.println("--- 1 HashMap ---");
        print("twoSum", Arrays.toString(twoSum(new int[]{2, 7, 11, 15}, 9)));                 // [0, 1]
        print("longestConsecutive", longestConsecutive(new int[]{100, 4, 200, 1, 3, 2}));     // 4
        print("firstMissingPositive", firstMissingPositive(new int[]{3, 4, -1, 1}));          // 2

        System.out.println("--- 2 Two pointers ---");
        print("isPalindrome", isPalindrome("A man, a plan, a canal: Panama"));                // true
        print("threeSum", threeSum(new int[]{-1, 0, 1, 2, -1, -4}));                          // [[-1, -1, 2], [-1, 0, 1]]
        print("trapRainWater", trapRainWater(new int[]{0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1})); // 6

        System.out.println("--- 3 Sliding window ---");
        print("maxSumOfSizeK", maxSumOfSizeK(new int[]{2, 1, 5, 1, 3, 2}, 3));                // 9
        print("longestUniqueSubstring", longestUniqueSubstring("abcabcbb"));                  // 3
        print("minWindow", minWindow("ADOBECODEBANC", "ABC"));                                // BANC

        System.out.println("--- 4 Prefix sum ---");
        print("rangeSum(1..3)", new RangeSum(new int[]{1, 2, 3, 4, 5}).sum(1, 3));            // 9
        print("subarraySumEqualsK", subarraySumEqualsK(new int[]{1, 1, 1}, 2));                // 2
        print("region sum", new Matrix2D(new int[][]{{1, 2, 3}, {4, 5, 6}, {7, 8, 9}}).region(1, 1, 2, 2)); // 28

        System.out.println("--- 5 Binary search ---");
        print("lowerBound", lowerBound(new int[]{1, 3, 5, 7}, 4));                            // 2
        print("minEatingSpeed", minEatingSpeed(new int[]{3, 6, 7, 11}, 8));                   // 4
        print("splitArray", splitArray(new int[]{7, 2, 5, 10, 8}, 2));                        // 18

        System.out.println("--- 6 Linked list ---");
        print("middleNode", middleNode(listFrom(1, 2, 3, 4, 5)).val);                         // 3
        print("reverseKGroup", listToString(reverseKGroup(listFrom(1, 2, 3, 4, 5), 2)));      // 2->1->4->3->5
        ListNode cyclic = listFrom(1, 2, 3, 4, 5);
        ListNode tail = cyclic;
        while (tail.next != null) {
            tail = tail.next;
        }
        tail.next = cyclic.next.next;                                                        // 5 points back to 3
        print("cycleStart", cycleStart(cyclic).val);                                          // 3

        System.out.println("--- 7 Stack ---");
        print("isValidBrackets", isValidBrackets("{[()]}"));                                  // true
        print("dailyTemperatures", Arrays.toString(dailyTemperatures(new int[]{73, 74, 75, 71, 69, 72, 76, 73}))); // [1,1,4,2,1,1,0,0]
        print("largestRectangle", largestRectangle(new int[]{2, 1, 5, 6, 2, 3}));             // 10

        System.out.println("--- 8 Heap ---");
        print("kthLargest", kthLargest(new int[]{3, 2, 1, 5, 6, 4}, 2));                      // 5
        print("topKFrequent", topKFrequent(new int[]{1, 1, 1, 2, 2, 3}, 2));                  // [1, 2]
        MedianFinder finder = new MedianFinder();
        finder.add(1);
        finder.add(2);
        finder.add(3);
        print("median", finder.median());                                                     // 2.0

        System.out.println("--- 9 Intervals ---");
        print("canAttendAll", canAttendAll(new int[][]{{0, 30}, {5, 10}, {15, 20}}));         // false
        print("mergeIntervals", Arrays.deepToString(mergeIntervals(new int[][]{{1, 3}, {2, 6}, {8, 10}}))); // [[1, 6], [8, 10]]
        print("minMeetingRooms", minMeetingRooms(new int[][]{{0, 30}, {5, 10}, {15, 20}}));   // 2

        System.out.println("--- 10 Backtracking ---");
        print("subsets", subsets(new int[]{1, 2, 3}));                                        // [[], [1], [1, 2], [1, 2, 3], [1, 3], [2], [2, 3], [3]]
        print("combinationSum", combinationSum(new int[]{2, 3, 6, 7}, 7));                    // [[2, 2, 3], [7]]
        print("totalNQueens(6)", totalNQueens(6));                                            // 4

        System.out.println("--- 11 Trees ---");
        TreeNode tree = buildTree(3, 9, 20, null, null, 15, 7);
        print("maxDepth", maxDepth(tree));                                                    // 3
        print("levelOrder", levelOrder(tree));                                               // [[3], [9, 20], [15, 7]]
        print("isValidBst", isValidBst(buildTree(2, 1, 3), Long.MIN_VALUE, Long.MAX_VALUE));  // true
        print("maxPathSum", maxPathSum(buildTree(-10, 9, 20, null, null, 15, 7)));            // 42

        System.out.println("--- 12 Graph BFS/DFS ---");
        print("validPath", validPath(3, new int[][]{{0, 1}, {1, 2}}, 0, 2));                  // true
        print("numIslands", numIslands(new int[][]{{1, 1, 0}, {0, 0, 1}, {0, 0, 1}}));        // 2
        print("ladderLength", ladderLength("hit", "cog", Arrays.asList("hot", "dot", "dog", "lot", "log", "cog"))); // 5

        System.out.println("--- 13 Union-Find ---");
        print("countProvinces", countProvinces(new int[][]{{1, 1, 0}, {1, 1, 0}, {0, 0, 1}})); // 2
        print("redundantConnection", Arrays.toString(redundantConnection(new int[][]{{1, 2}, {1, 3}, {2, 3}}))); // [2, 3]
        print("MST cost", minimumSpanningTreeCost(4, new int[][]{{0, 1, 1}, {1, 2, 2}, {0, 2, 5}, {2, 3, 1}})); // 4

        System.out.println("--- 14 Topological / shortest path ---");
        print("canFinish", canFinish(2, new int[][]{{1, 0}}));                                // true
        print("canFinish cycle", canFinish(2, new int[][]{{1, 0}, {0, 1}}));                  // false
        print("networkDelayTime", networkDelayTime(new int[][]{{2, 1, 1}, {2, 3, 1}, {3, 4, 1}}, 4, 2)); // 2
        print("cheapestFlight", cheapestFlight(4, new int[][]{{0, 1, 100}, {1, 2, 100}, {2, 0, 100},
                {1, 3, 600}, {2, 3, 200}}, 0, 3, 1));                                         // 700

        System.out.println("--- 15 DP 1D ---");
        print("climbStairs(5)", climbStairs(5));                                              // 8
        print("coinChange", coinChange(new int[]{1, 2, 5}, 11));                              // 3
        print("canPartition", canPartition(new int[]{1, 5, 11, 5}));                          // true
        print("maxCoins", maxCoins(new int[]{3, 1, 5, 8}));                                   // 167

        System.out.println("--- 16 DP strings/grids ---");
        print("uniquePaths", uniquePaths(3, 7));                                              // 28
        print("LCS", longestCommonSubsequence("abcde", "ace"));                               // 3
        print("editDistance", editDistance("horse", "ros"));                                  // 3
        print("isMatch(aab, c*a*b)", isMatch("aab", "c*a*b"));                                // true

        System.out.println("--- 17 Greedy ---");
        print("maxProfit", maxProfit(new int[]{7, 1, 5, 3, 6, 4}));                           // 5
        print("minJumps", minJumps(new int[]{2, 3, 1, 1, 4}));                                // 2
        print("candy", candy(new int[]{1, 0, 2}));                                            // 5

        System.out.println("--- 18 Bits ---");
        print("singleNumber", singleNumber(new int[]{4, 1, 2, 1, 2}));                        // 4
        print("countBits(5)", Arrays.toString(countBits(5)));                                 // [0, 1, 1, 2, 1, 2]
        print("findMaximumXor", findMaximumXor(new int[]{3, 10, 5, 25, 2, 8}));               // 28

        System.out.println("--- 19 Trie ---");
        Trie trie = new Trie();
        for (String word : new String[]{"apple", "app", "apt", "bat"}) {
            trie.insert(word);
        }
        print("search(app)", trie.search("app"));                                             // true
        print("startsWith(ap)", trie.startsWith("ap"));                                       // true
        print("wordsWithPrefix(ap)", trie.wordsWithPrefix("ap"));                             // [app, apple, apt]

        System.out.println("--- 20 Math ---");
        print("gcd(12, 18)", gcd(12, 18));                                                    // 6
        print("countPrimes(10)", countPrimes(10));                                            // 4
        print("modPow(2, 10, 1000)", modPow(2, 10, 1000));                                    // 24
        print("nCr(5, 2)", nCr(5, 2));                                                        // 10
    }
}
