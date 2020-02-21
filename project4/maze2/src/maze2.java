import java.util.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class maze2 {

    private static final int right = 0;
    private static final int down = 1;
    private static final int left = 2;
    private static final int up = 3;
    private static Random randomGenerator;  // for random numbers

    public static int Size;

    public static class Point {  // a Point is a position in the maze

        public int x, y;

        // Constructor
        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void copy(Point p) {
            this.x = p.x;
            this.y = p.y;
        }
    }

    public static class Edge {
        // an Edge links two neighboring Points:
        // For the grid graph, an edge can be represented by a point and a direction.
        Point point;
        int direction;    // one of right, down, left, up
        boolean used;     // for maze creation
        boolean deleted;  // for maze creation

        // Constructor
        public Edge(Point p, int d) {
            this.point = p;
            this.direction = d;
            this.used = false;
            this.deleted = false;
        }
    }
    /*
    * not crucial to program, found online at https://www.journaldev.com/32661/shuffle-array-java
    *
    */
        public static Integer[] swap () {
            int j = 0;
            Integer[] intArray = new Integer[N];
            for(int i = 0; i<N; i++){
                intArray[i] = j;
                j++;
                if(j == Size - 1)j = 0;
            }

            List<Integer> intList = Arrays.asList(intArray);

            Collections.shuffle(intList);

            intList.toArray(intArray);

            System.out.println(Arrays.toString(intArray));
            return intArray;
        }


    // A board is an SizexSize array whose values are Points
    public static Point[][] board;

    // A graph is simply a set of edges: graph[i][d] is the edge
    // where i is the index for a Point and d is the direction
    public static Edge[][] graph;
    public static int N;   // number of points in the graph
    public static void worker(){
        Integer[] x = new Integer[N]; Integer[] y = new Integer[N]; int z = 0;
        x = swap();
        y = swap();
        for(int i = 0; i < x.length; ++i){
            z = randomGenerator.nextInt(4);
            deleter((x[i]*Size+y[i]),z);
            System.out.print(i+"= pt " + z + "=dir ");
        }
        System.out.println();
    }
    public static void deleter(int pt, int dir){
        int x;
        if(dir == down){
            if((pt-pt%Size)/Size == Size-1){return;}
            graph[pt+Size][down].deleted = true;
            if(pt<(N-(2*Size))) graph[pt+(2*Size)][up].deleted = true;
            return;
        }
        else if(dir == up){
            if((pt-pt%Size)/Size == 0){return;}
            graph[pt-Size][up].deleted = true;
            if(pt>(2*Size)) graph[pt-(2*Size)][down].deleted = true;
            return;
        }
        else if(dir == left){
            if(pt%Size == 0){return;}
            graph[pt][left].deleted = true;
            if(pt>1) graph[pt-1][right].deleted = true;
            return;
        }
        else{
            if(pt%Size == Size-1){return;}
            graph[pt][right].deleted = true;
            if(pt<Size-1) graph[pt+1][left].deleted = true;
            return;
        }
    }
    public static void home(){
        int x = 0; int b = 0;
        while(x < N-1){
        if((x-x%Size)/Size != Size-1 && graph[x+Size][1].deleted){ graph[x+Size][1].used = true; x+=Size;}
        else if(graph[x][0].deleted){graph[x][0].used = true; x++;}
        else if((x-x%Size)/Size != Size-1){
            deleter(x,1);
        }
        else {
            deleter(x, 0);
        }
        }

    }
    public static void displayInitBoard() {
        int d = 0;
        for (int i = 0; i < Size; ++i) {
            System.out.print("    -");
            for (int j = 0; j < Size; ++j) {
                if (i != (Size - 1)) {
                    d = i + 1;
                } else d = i;
                if(i == 0){System.out.print("----");}
                else if (graph[i * Size + j][1].used && i<Size-1){
                    System.out.print(" X ");
                }
                else if (graph[i * Size + j][1].deleted || graph[(d) * Size + j][3].deleted) {
                    System.out.print("    ");
                } else {
                    System.out.print("----");
                }
            }

            System.out.println();
            if (i == 0) System.out.print("Start");
            else System.out.print("    |");
            for (int j = 0; j < Size; ++j) {
                if(i != Size - 1){d = j+1;}
                else d = j;
                if (i == Size - 1 && j == Size - 1)
                    System.out.print("    End");
                else if(j == Size - 1){System.out.print("   |");}
                else if (graph[i * Size + j][0].used)  System.out.print("  X ");
                else if (graph[i * Size + j][0].deleted || graph[(i)*Size + d][2].deleted)
                    System.out.print("    ");
                else System.out.print("   |");
            }
            System.out.println();
        }
        System.out.print("    -");
        for (int j = 0; j < Size; ++j) {
            System.out.print("----");
        }
        System.out.println();
    }

    public static void main(String[] args) {

        // Read in the Size of a maze
        Scanner scan = new Scanner(System.in);
        try {
            System.out.println("What's the size of your maze? ");
            Size = scan.nextInt();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        scan.close();


        // Create one dummy edge for all boundary edges.
        Edge dummy = new Edge(new Point(0, 0), 0);
        dummy.used = false;

        // Create board and graph.
        board = new Point[Size][Size];
        N = Size*Size;  // number of points
        graph = new Edge[N][4];

        for (int i = 0; i < Size; ++i)
            for (int j = 0; j < Size; ++j) {
                Point p = new Point(i, j);
                int pindex = i*Size+j;   // Point(i, j)'s index is i*Size + j

                board[i][j] = p;

                graph[pindex][right] = (j < Size-1)? new Edge(p, right): dummy;
                graph[pindex][down] = (i < Size-1)? new Edge(p, down) : dummy;
                graph[pindex][left] = (j > 0)? graph[pindex-1][right] : dummy;
                graph[pindex][up] = (i > 0)? graph[pindex-Size][down] : dummy;

            }
        randomGenerator = new Random();
        worker();
        home();
        displayInitBoard();

        // Hint: To randomly pick an edge in the maze, you may
        // randomly pick a point first, then randomly pick
        // a direction to get the edge associated with the point.
    }
}

