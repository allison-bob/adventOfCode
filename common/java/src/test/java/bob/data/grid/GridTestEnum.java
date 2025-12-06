package bob.data.grid;

public enum GridTestEnum {
    A, B, C, D, E, F, G, H, I,
    J, K, L, M, N, O, P, Q, R,
    S, T, U, V, W, X, Y, Z, AA,
    BORDER;
    
    public static GridTestEnum valueOf(char c) {
        return GridTestEnum.valueOf(String.valueOf(c));
    }
}
