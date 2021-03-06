public class Solver {
    public class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private SearchNode parent;
        private byte moves;
        private byte priority;
        public SearchNode(Board b, SearchNode p, byte m, byte pr) {
            board = b;
            parent = p;
            moves = m;
            priority = pr;
        }
        public int compareTo(SearchNode a) {
            return this.priority - a.priority;
        }
    }
    private SearchNode gsn;
    private boolean newNode(Board b, SearchNode p, MinPQ<SearchNode> pq) {
        for (SearchNode n = p.parent;
                n != null;) {
            if (n.board.equals(b)) return false;
            return true;
        }
        return true;
    }
    private byte dist(Board b) {
//        return b.hamming();
        return (byte)b.manhattan();
    }
    private void newLevel(SearchNode p, MinPQ<SearchNode> pq) {
        for (Board b: p.board.neighbors()) {
            if (newNode(b,p,pq)) pq.insert(new SearchNode(b, p, (byte)(p.moves+1), (byte)(dist(b)+p.moves+1)));
        }
    }
    public Solver(Board initial) {
        if (!initial.isSolvable())
            throw new IllegalArgumentException("not solvable");
        MinPQ<SearchNode> pq = new MinPQ<SearchNode>();
        pq.insert(new SearchNode(initial, null, (byte)0, dist(initial)));
        SearchNode n = pq.delMin();
        while (!n.board.isGoal()) {
            newLevel(n, pq);
            n = pq.delMin();
        }
        gsn = n;
    }
    public int moves() {
        return gsn.moves;
    }
    public Iterable<Board> solution() {
        Stack<Board> s = new Stack<Board>();
        SearchNode n = gsn;
        while (n != null) {
            s.push(n.board);
            n = n.parent;
        }
        return s;
    }
    public static void main(String[] args) {

        // create initial board from file
//        In in = new In(args[0]);
        In in = new In("data/puzzle4x4-78.txt");
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // check if puzzle is solvable; if so, solve it and output solution
        if (initial.isSolvable()) {
            Solver solver = new Solver(initial);
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }

        // if not, report unsolvable
        else {
            StdOut.println("Unsolvable puzzle");
        }
    }
}
