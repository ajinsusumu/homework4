public class Board {
    private int N;
    private int[][] tiles;
    private int blankrow() {
        int p;
        for (p = 0; p < N*N; p++) {
            if (tiles[p/N][p%N] == 0) break;
        }
        return p/N;
    }
    private int inversions() {
        int invs = 0;
        for (int p = 0; p < N*N-1; p++) {
            int j1 = p % N; int i1 = p / N;
            if (tiles[i1][j1] > 0) {
                for (int q = p+1; q < N*N; q++) {
                    int j2 = q % N; int i2 = q / N;
                    if (tiles[i2][j2] > 0
                        && tiles[i1][j1] > tiles[i2][j2]) invs++;
                }
            }
        }
        return invs;
    }
    public Board(int[][] blocks) {
        tiles = blocks;
        N = tiles.length;
    }
    public int size() { return N; }
    public int hamming() {
        int ham = 0;
        for (int p = 0; p < N*N-1; p++) {
            int j = p % N; int i = p / N;
            if (tiles[i][j] != p+1) ham++;
        }
        return ham;
    }
    public int manhattan() {
        int man = 0;
        for (int p = 0; p < N*N; p++) {
            int j = p % N; int i = p / N;
            int q = tiles[i][j] - 1;
            if (q > 0) {
                int jj = q % N; int ii = q / N;
                man += Math.abs(jj - j) + Math.abs(ii - i);
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
            if (this.tiles[p/N][p%N] != that.tiles[p/N][p%N]) return false;
        return true;
    }
    private Board nbr(int x, int y, int a, int b) {
        int[][] nt = new int[N][N];
        for (int p = 0; p < N*N; p++) nt[p/N][p%N] = tiles[p/N][p%N];
        int orig = nt[x][y];
        nt[x][y] = 0;
        nt[a][b] = orig;
        return new Board(nt);
    }
    public Iterable<Board> neighbors() {
        Queue<Board> q = new Queue<Board>();
        int p;
        for (p = 0; p < N*N; p++) if (tiles[p/N][p%N] == 0) break;
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
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }
    public static void main(String[] args) {}
}
