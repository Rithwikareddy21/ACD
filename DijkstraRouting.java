import java.util.*;
public class DijkstraRouting {
static final int INF = Integer.MAX_VALUE;
public static int[] dijkstra(int[][] adjMatrix, int start) { int n = adjMatrix.length;
int[] distances = new int[n]; boolean[] visited = new boolean[n]; Arrays.fill(distances, INF);
distances[start] = 0;
PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0])); pq.offer(new int[]{0, start});
while (!pq.isEmpty()) { int[] current = pq.poll();
int currentDistance = current[0]; int u = current[1];
if (visited[u]) continue; visited[u] = true;
for (int v = 0; v < n; v++) {
int weight = adjMatrix[u][v];
if (weight != -1 && !visited[v]) {
int distance = currentDistance + weight; if (distance < distances[v]) {
distances[v] = distance;
pq.offer(new int[]{distance, v});
}
}
}
}
return distances;
}
public static void main(String[] args) {
Scanner sc = new Scanner(System.in); System.out.print("Enter number of nodes: "); int n = sc.nextInt();
int[][] adjMatrix = new int[n][n];
System.out.println("Enter adjacency matrix row by row (-1 for no edge, 0 for self- distance):");
for (int i = 0; i < n; i++) {
System.out.print("Row " + (i + 1) + ": "); for (int j = 0; j < n; j++) {
adjMatrix[i][j] = sc.nextInt();
}
 
}
System.out.print("Enter starting node index (0 to n-1): "); int startNode = sc.nextInt();
int[] shortestDistances = dijkstra(adjMatrix, startNode);
System.out.println("\nShortest distances from node " + startNode + ":"); for (int i = 0; i < n; i++) {
if (shortestDistances[i] == INF)
System.out.println("Node " + i + ": Unreachable"); else
System.out.println("Node " + i + ": " + shortestDistances[i]);
}
sc.close();
}
}
