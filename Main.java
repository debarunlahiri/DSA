import java.util.*;

/**
 * DSA playground: each algorithm is paired with a real-world scenario.
 *
 *  1. Longest Consecutive Sequence -> longest daily login streak
 *  2. A* Search                    -> warehouse robot path planning
 *  3. Dijkstra                     -> cheapest route on a road network (maps)
 *  4. Two Sum (HashMap)            -> matching two items to a gift-card balance
 *  5. Sliding Window               -> longest session without a repeated request id
 *  6. Binary Search                -> git bisect: first bad build
 *  7. Merge Intervals              -> merging overlapping calendar meetings
 *  8. Topological Sort             -> build / package dependency order
 *  9. Union-Find                   -> social network friend groups
 * 10. LRU Cache                    -> browser / CDN cache eviction
 * 11. Top-K Frequent (heap)        -> trending hashtags
 * 12. 0/1 Knapsack (DP)            -> best items to buy within a budget
 * 13. Trie                         -> search-bar autocomplete
 * 14. Min Heap Merge K Lists       -> merging sorted log files from servers
 */
public class Main {

    public static void main(String[] args) {
        section("1. Longest Consecutive Sequence (login streak)");
        int[] loginDays = {100, 4, 200, 1, 3, 2}; // day numbers the user logged in
        System.out.println("Longest streak (days): " + longestConsecutive(loginDays));

        section("2. A* (warehouse robot)");
        int[][] grid = {
                {0, 0, 0, 0},
                {1, 1, 0, 1},
                {0, 0, 0, 0},
                {0, 1, 1, 0}
        }; // 1 = shelf (blocked)
        System.out.println("Robot path (row,col): " + aStar(grid, new int[]{0, 0}, new int[]{3, 3}));

        section("3. Dijkstra (road network)");
        Map<String, Map<String, Integer>> roads = new HashMap<>();
        addRoad(roads, "Home", "Mall", 4);
        addRoad(roads, "Home", "Office", 2);
        addRoad(roads, "Office", "Mall", 1);
        addRoad(roads, "Mall", "Airport", 5);
        addRoad(roads, "Office", "Airport", 8);
        System.out.println("Shortest Home->Airport (minutes): " + dijkstra(roads, "Home").get("Airport"));

        section("4. Two Sum (gift card)");
        int[] prices = {15, 40, 25, 10, 60};
        System.out.println("Pick two items totalling 65 -> indices " + Arrays.toString(twoSum(prices, 65)));

        section("5. Sliding Window (unique request ids)");
        System.out.println("Longest run without repeat in \"abcabcbb\": " + longestUniqueWindow("abcabcbb"));

        section("6. Binary Search (git bisect)");
        boolean[] buildIsBad = {false, false, false, false, true, true, true, true};
        System.out.println("First bad build index: " + firstBadBuild(buildIsBad));

        section("7. Merge Intervals (calendar)");
        int[][] meetings = {{9, 11}, {10, 12}, {14, 15}, {15, 17}, {20, 21}};
        System.out.println("Busy blocks: " + Arrays.deepToString(mergeIntervals(meetings)));

        section("8. Topological Sort (build order)");
        Map<String, List<String>> deps = new LinkedHashMap<>(); // module -> modules it depends on
        deps.put("app", List.of("ui", "network"));
        deps.put("ui", List.of("core"));
        deps.put("network", List.of("core"));
        deps.put("core", List.of());
        System.out.println("Build order: " + buildOrder(deps));

        section("9. Union-Find (friend groups)");
        UnionFind uf = new UnionFind(6);
        uf.union(0, 1);
        uf.union(1, 2);
        uf.union(3, 4);
        System.out.println("Groups among 6 users: " + uf.components());
        System.out.println("User 0 and 2 connected? " + uf.connected(0, 2));
        System.out.println("User 2 and 3 connected? " + uf.connected(2, 3));

        section("10. LRU Cache (browser cache)");
        LRUCache<String, String> cache = new LRUCache<>(2);
        cache.put("a.com", "pageA");
        cache.put("b.com", "pageB");
        cache.get("a.com");              // a.com becomes most recent
        cache.put("c.com", "pageC");     // evicts b.com
        System.out.println("Cache keys (oldest->newest): " + cache.keys());
        System.out.println("b.com cached? " + (cache.get("b.com") != null));

        section("11. Top-K Frequent (trending hashtags)");
        String[] tags = {"#java", "#dsa", "#java", "#ai", "#dsa", "#java", "#ai", "#news"};
        System.out.println("Top 2 trending: " + topKFrequent(tags, 2));

        section("12. 0/1 Knapsack (budget shopping)");
        int[] cost = {20, 30, 50};
        int[] value = {60, 90, 120}; // happiness score
        System.out.println("Max happiness with budget 80: " + knapsack(cost, value, 80));

        section("13. Trie (autocomplete)");
        Trie trie = new Trie();
        for (String w : new String[]{"apple", "app", "application", "apt", "banana"}) trie.insert(w);
        System.out.println("Suggestions for \"app\": " + trie.withPrefix("app"));

        section("14. Merge K Sorted Lists (server logs)");
        List<List<Integer>> logs = List.of(
                List.of(1, 4, 9),     // server A timestamps
                List.of(2, 3, 10),    // server B
                List.of(5, 6, 7));    // server C
        System.out.println("Unified timeline: " + mergeKSorted(logs));
    }

    private static void section(String title) {
        System.out.println("\n=== " + title + " ===");
    }

    // ---------------------------------------------------------------- 1
    /** O(n): only start counting from numbers that begin a run. */
    public static int longestConsecutive(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        Set<Integer> set = new HashSet<>();
        for (int n : nums) set.add(n);

        int best = 0;
        for (int n : set) {
            if (!set.contains(n - 1)) {
                int cur = n, len = 1;
                while (set.contains(cur + 1)) { cur++; len++; }
                best = Math.max(best, len);
            }
        }
        return best;
    }

    // ---------------------------------------------------------------- 2
    static class Node {
        final int x, y, g, f;
        Node parent;

        Node(int x, int y, int g, int h) {
            this.x = x; this.y = y; this.g = g; this.f = g + h;
        }
    }

    /** Returns the path as "row,col" strings, or an empty list if unreachable. */
    public static List<String> aStar(int[][] grid, int[] start, int[] goal) {
        PriorityQueue<Node> open = new PriorityQueue<>(Comparator.comparingInt((Node n) -> n.f));
        Map<String, Integer> bestG = new HashMap<>();
        open.add(new Node(start[0], start[1], 0, manhattan(start[0], start[1], goal)));
        bestG.put(start[0] + "," + start[1], 0);

        int[][] dirs = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        while (!open.isEmpty()) {
            Node cur = open.poll();
            if (cur.x == goal[0] && cur.y == goal[1]) return reconstructPath(cur);
            if (cur.g > bestG.getOrDefault(cur.x + "," + cur.y, Integer.MAX_VALUE)) continue; // stale entry

            for (int[] d : dirs) {
                int nx = cur.x + d[0], ny = cur.y + d[1];
                if (!isFree(grid, nx, ny)) continue;
                int g = cur.g + 1;
                String key = nx + "," + ny;
                if (g < bestG.getOrDefault(key, Integer.MAX_VALUE)) {
                    bestG.put(key, g);
                    Node next = new Node(nx, ny, g, manhattan(nx, ny, goal));
                    next.parent = cur;
                    open.add(next);
                }
            }
        }
        return Collections.emptyList();
    }

    private static int manhattan(int x, int y, int[] goal) {
        return Math.abs(x - goal[0]) + Math.abs(y - goal[1]);
    }

    private static boolean isFree(int[][] grid, int x, int y) {
        return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length && grid[x][y] == 0;
    }

    private static List<String> reconstructPath(Node node) {
        List<String> path = new ArrayList<>();
        for (; node != null; node = node.parent) path.add(node.x + "," + node.y);
        Collections.reverse(path);
        return path;
    }

    // ---------------------------------------------------------------- 3
    static void addRoad(Map<String, Map<String, Integer>> g, String a, String b, int w) {
        g.computeIfAbsent(a, k -> new HashMap<>()).put(b, w);
        g.computeIfAbsent(b, k -> new HashMap<>()).put(a, w);
    }

    /** Shortest distance from source to every reachable node. O((V+E) log V). */
    public static Map<String, Integer> dijkstra(Map<String, Map<String, Integer>> graph, String source) {
        Map<String, Integer> dist = new HashMap<>();
        PriorityQueue<Map.Entry<String, Integer>> pq =
                new PriorityQueue<>(Map.Entry.comparingByValue());
        pq.add(Map.entry(source, 0));

        while (!pq.isEmpty()) {
            var top = pq.poll();
            String u = top.getKey();
            if (dist.containsKey(u)) continue;
            dist.put(u, top.getValue());
            for (var edge : graph.getOrDefault(u, Map.of()).entrySet()) {
                if (!dist.containsKey(edge.getKey())) {
                    pq.add(Map.entry(edge.getKey(), top.getValue() + edge.getValue()));
                }
            }
        }
        return dist;
    }

    // ---------------------------------------------------------------- 4
    /** O(n): remember what we've seen, look for the complement. */
    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> seen = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            Integer j = seen.get(target - nums[i]);
            if (j != null) return new int[]{j, i};
            seen.put(nums[i], i);
        }
        return new int[0];
    }

    // ---------------------------------------------------------------- 5
    /** Longest substring without repeating characters. O(n). */
    public static int longestUniqueWindow(String s) {
        Map<Character, Integer> last = new HashMap<>();
        int best = 0, left = 0;
        for (int right = 0; right < s.length(); right++) {
            Integer prev = last.put(s.charAt(right), right);
            if (prev != null && prev >= left) left = prev + 1;
            best = Math.max(best, right - left + 1);
        }
        return best;
    }

    // ---------------------------------------------------------------- 6
    /** Builds are good...good,bad...bad. Find the first bad one in O(log n). */
    public static int firstBadBuild(boolean[] bad) {
        int lo = 0, hi = bad.length - 1, ans = -1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (bad[mid]) { ans = mid; hi = mid - 1; }
            else lo = mid + 1;
        }
        return ans;
    }

    // ---------------------------------------------------------------- 7
    public static int[][] mergeIntervals(int[][] intervals) {
        if (intervals.length == 0) return new int[0][];
        int[][] sorted = intervals.clone();
        Arrays.sort(sorted, Comparator.comparingInt(a -> a[0]));

        List<int[]> merged = new ArrayList<>();
        int[] cur = sorted[0].clone();
        for (int i = 1; i < sorted.length; i++) {
            if (sorted[i][0] <= cur[1]) cur[1] = Math.max(cur[1], sorted[i][1]);
            else { merged.add(cur); cur = sorted[i].clone(); }
        }
        merged.add(cur);
        return merged.toArray(new int[0][]);
    }

    // ---------------------------------------------------------------- 8
    /** Kahn's algorithm. Throws if there is a circular dependency. */
    public static List<String> buildOrder(Map<String, List<String>> deps) {
        Map<String, Integer> indegree = new LinkedHashMap<>();
        Map<String, List<String>> dependents = new HashMap<>();
        for (var e : deps.entrySet()) {
            indegree.putIfAbsent(e.getKey(), 0);
            for (String d : e.getValue()) {
                indegree.putIfAbsent(d, 0);
                indegree.merge(e.getKey(), 1, Integer::sum);
                dependents.computeIfAbsent(d, k -> new ArrayList<>()).add(e.getKey());
            }
        }
        Deque<String> ready = new ArrayDeque<>();
        indegree.forEach((k, v) -> { if (v == 0) ready.add(k); });

        List<String> order = new ArrayList<>();
        while (!ready.isEmpty()) {
            String m = ready.poll();
            order.add(m);
            for (String next : dependents.getOrDefault(m, List.of())) {
                if (indegree.merge(next, -1, Integer::sum) == 0) ready.add(next);
            }
        }
        if (order.size() != indegree.size()) throw new IllegalStateException("Circular dependency!");
        return order;
    }

    // ---------------------------------------------------------------- 9
    static class UnionFind {
        private final int[] parent, rank;
        private int components;

        UnionFind(int n) {
            parent = new int[n];
            rank = new int[n];
            components = n;
            for (int i = 0; i < n; i++) parent[i] = i;
        }

        int find(int x) {
            while (parent[x] != x) { parent[x] = parent[parent[x]]; x = parent[x]; }
            return x;
        }

        void union(int a, int b) {
            int ra = find(a), rb = find(b);
            if (ra == rb) return;
            if (rank[ra] < rank[rb]) { int t = ra; ra = rb; rb = t; }
            parent[rb] = ra;
            if (rank[ra] == rank[rb]) rank[ra]++;
            components--;
        }

        boolean connected(int a, int b) { return find(a) == find(b); }
        int components() { return components; }
    }

    // ---------------------------------------------------------------- 10
    static class LRUCache<K, V> {
        private final int capacity;
        private final LinkedHashMap<K, V> map;

        LRUCache(int capacity) {
            this.capacity = capacity;
            this.map = new LinkedHashMap<>(16, 0.75f, true) { // access-order
                @Override protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                    return size() > LRUCache.this.capacity;
                }
            };
        }

        V get(K key) { return map.get(key); }
        void put(K key, V value) { map.put(key, value); }
        List<K> keys() { return new ArrayList<>(map.keySet()); }
    }

    // ---------------------------------------------------------------- 11
    /** Count with a map, keep the K largest with a min-heap. O(n log k). */
    public static List<String> topKFrequent(String[] items, int k) {
        Map<String, Integer> counts = new HashMap<>();
        for (String s : items) counts.merge(s, 1, Integer::sum);

        PriorityQueue<Map.Entry<String, Integer>> heap =
                new PriorityQueue<>(Map.Entry.comparingByValue());
        for (var e : counts.entrySet()) {
            heap.add(e);
            if (heap.size() > k) heap.poll();
        }
        LinkedList<String> result = new LinkedList<>();
        while (!heap.isEmpty()) result.addFirst(heap.poll().getKey());
        return result;
    }

    // ---------------------------------------------------------------- 12
    /** Classic 0/1 knapsack using a 1D DP array. O(n * budget). */
    public static int knapsack(int[] cost, int[] value, int budget) {
        int[] dp = new int[budget + 1];
        for (int i = 0; i < cost.length; i++) {
            for (int b = budget; b >= cost[i]; b--) {
                dp[b] = Math.max(dp[b], dp[b - cost[i]] + value[i]);
            }
        }
        return dp[budget];
    }

    // ---------------------------------------------------------------- 13
    static class Trie {
        private static class TrieNode {
            final Map<Character, TrieNode> next = new TreeMap<>();
            boolean word;
        }

        private final TrieNode root = new TrieNode();

        void insert(String w) {
            TrieNode n = root;
            for (char c : w.toCharArray()) n = n.next.computeIfAbsent(c, k -> new TrieNode());
            n.word = true;
        }

        List<String> withPrefix(String prefix) {
            TrieNode n = root;
            for (char c : prefix.toCharArray()) {
                n = n.next.get(c);
                if (n == null) return List.of();
            }
            List<String> out = new ArrayList<>();
            collect(n, new StringBuilder(prefix), out);
            return out;
        }

        private void collect(TrieNode n, StringBuilder sb, List<String> out) {
            if (n.word) out.add(sb.toString());
            for (var e : n.next.entrySet()) {
                sb.append(e.getKey());
                collect(e.getValue(), sb, out);
                sb.deleteCharAt(sb.length() - 1);
            }
        }
    }

    // ---------------------------------------------------------------- 14
    /** Min-heap holds the current head of each list. O(N log k). */
    public static List<Integer> mergeKSorted(List<List<Integer>> lists) {
        // heap entry: {value, listIndex, indexInList}
        PriorityQueue<int[]> heap = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        for (int i = 0; i < lists.size(); i++) {
            if (!lists.get(i).isEmpty()) heap.add(new int[]{lists.get(i).get(0), i, 0});
        }
        List<Integer> out = new ArrayList<>();
        while (!heap.isEmpty()) {
            int[] top = heap.poll();
            out.add(top[0]);
            int nextIdx = top[2] + 1;
            if (nextIdx < lists.get(top[1]).size()) {
                heap.add(new int[]{lists.get(top[1]).get(nextIdx), top[1], nextIdx});
            }
        }
        return out;
    }
}
