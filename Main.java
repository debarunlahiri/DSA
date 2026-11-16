import java.util.*;
import java.util.PriorityQueue;
import java.util.Set;

public class Node {
    int x, y;
    int g; // Cost from start to current node
    int h; // Heuristic cost to goal
    int f; // Total cost (g + h)
    Node parent;

    public Node(int x, int y, int g, int h) {
        this.x = x;
        this.y = y;
        this.g = g;
        this.h = h;
        this.f = g + h;
        this.parent = null;
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, World!");

        int[] nums = {100, 4, 200, 1, 3, 2};
        int longestStreak = longestConsecutive(nums);
        System.out.println("Longest consecutive sequence length: " + longestStreak);

        int[][] grid = {
                {0, 0, 0, 0},
                {1, 1, 0, 1},
                {0, 0, 0, 0},
                {0, 1, 1, 0}
        };
        int[] start = {0, 0};
        int[] goal = {3, 3};
        List<Integer> path = aStar(grid, start, goal);
        System.out.println("Path from start to goal: " + path);

        
    }


    public static int longestConsecutive(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        Set<Integer> numSet = new HashSet<>();
        for (int num : nums) {
            numSet.add(num);
        }

        int longestStreak = 0;

        for (int num : numSet) {
            if (!numSet.contains(num - 1)) { // Check if it's the start of a sequence
                int currentNum = num;
                int currentStreak = 1;

                while (numSet.contains(currentNum + 1)) {
                    currentNum++;
                    currentStreak++;
                }

                longestStreak = Math.max(longestStreak, currentStreak);
            }
        }

        return longestStreak;
    }

    public static List<Integer> aStar(int[][] grid, int[] start, int[] goal) {
        // A* algorithm implementation
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(n -> n.f));
        Set<String> closedSet = new HashSet<>();

        Node startNode = new Node(start[0], start[1], 0, heuristic(start, goal));
        openSet.add(startNode);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();

            if (current.x == goal[0] && current.y == goal[1]) {
                return reconstructPath(current);
            }

            closedSet.add(current.x + "," + current.y);

            for (int[] dir : new int[][] { { 0, 1 }, { 1, 0 }, { 0, -1 }, { -1, 0 } }) {
                int neighborX = current.x + dir[0];
                int neighborY = current.y + dir[1];

                if (isValid(grid, neighborX, neighborY) && !closedSet.contains(neighborX + "," + neighborY)) {
                    int gScore = current.g + 1;
                    int hScore = heuristic(new int[] { neighborX, neighborY }, goal);
                    Node neighborNode = new Node(neighborX, neighborY, gScore, hScore);
                    neighborNode.parent = current;

                    openSet.add(neighborNode);
                }
            }
        }

        return Collections.emptyList(); // No path found
    }
    
    public static int heuristic(int[] a, int[] b) {
        return Math.abs(a[0] - b[0]) + Math.abs(a[1] - b[1]); // Manhattan distance
    }

    public static boolean isValid(int[][] grid, int x, int y) {
        return x >= 0 && x < grid.length && y >= 0 && y < grid[0].length && grid[x][y] == 0;
    }

    public static List<Integer> reconstructPath(Node node) {
        List<Integer> path = new ArrayList<>();
        while (node != null) {
            path.add(node.x * 10 + node.y); // Encode coordinates as a single integer
            node = node.parent;
        }
        Collections.reverse(path);
        return path;
    }


}
