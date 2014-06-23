public class Board {
    private int N;
    private byte[] tiles;
    private int blankrow() {
        int p;
        for (p = 0; p < N*N; p++) {
            if (tiles[p] == 0) break;
        }
        return p/N;
    }
    private int inversions() {
        int invs = 0;
        for (int p = 0; p < N*N-1; p++) {
            if (tiles[p] > 0) {
                for (int q = p+1; q < N*N; q++) {
                    if (tiles[q] > 0
                        && tiles[p] > tiles[q]) invs++;
                }
            }
        }
        return invs;
    }
    private Board(byte[] blks, int n) {
        N = n;
        tiles = blks;
    }
    public Board(int[][] blocks) {
        N = blocks.length;
        tiles = new byte[N*N];
        for (int p = 0; p< N*N; p++) {
            tiles[p] = (byte)blocks[p/N][p%N];
        }

    }
    public int size() { return N; }
    public int hamming() {
        int ham = 0;
        for (int p = 0; p < N*N-1; p++) {
            if (tiles[p] != p+1) ham++;
        }
        return ham;
    }
    public int manhattan() {
        int man = 0;
        for (int p = 0; p < N*N; p++) {
            int q = tiles[p] - 1;
            if (q >= 0) {
                man += Math.abs(p/N - q/N) + Math.abs(p%N - q%N);
            }
        }
        return man;
    }
    public boolean isGoal() {
        return hamming() == 0;
    }
    public boolean isSolvable() {
        int invsmod2 = inversions() % 2;
        if ((N%2 != 0) && invsmod2 != 0) return false;
        if ((N%2 == 0) && (invsmod2 + blankrow())%2 == 0) return false;
        return true;
    }
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (this.N != that.N) return false;
        for (int p = 0; p < N*N; p++)
            if (this.tiles[p] != that.tiles[p]) return false;
        return true;
    }
    private Board nbr(int x, int y, int a, int b) {
        byte[] nt = new byte[N*N];
        for (int p = 0; p < N*N; p++) nt[p] = tiles[p];
        byte orig = nt[x*N+y];
        nt[x*N+y] = 0;
        nt[a*N+b] = orig;
        return new Board(nt, N);
    }
    public Iterable<Board> neighbors() {
        Queue<Board> q = new Queue<Board>();
        int p;
        for (p = 0; p < N*N; p++) if (tiles[p] == 0) break;
        int i = p/N; int j = p%N;
        if (i-1 >= 0) q.enqueue(nbr(i-1,j,  i,j));
        if (j+1 <  N) q.enqueue(nbr(i  ,j+1,i,j));
        if (i+1 <  N) q.enqueue(nbr(i+1,j,  i,j));
        if (j-1 >= 0) q.enqueue(nbr(i  ,j-1,i,j));
        return q;
    }
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", tiles[i*N+j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
    public static void main(String[] args) {}
}
