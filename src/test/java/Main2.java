import java.util.Scanner;

public class Main2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            int N = scanner.nextInt();
            scanner.nextLine();
            int[] height = new int[N];
            for (int i = 0; i < N; i++) {
                height[i] = scanner.nextInt();
            }
            int ans = solve(N, height);
            System.out.println(ans);
        }
    }

    private static int solve(int n, int[] height) {
        int[] left = new int[n];
        int[] right = new int[n];
        left[0] = 1;
        right[n - 1] = 1;
        for (int i = 0; i < n; i++) {
            left[i] = 1;
            for (int j = 0; j < i; j++) {
                if (height[j] < height[i]) {
                    left[i] = Math.max(left[j] + 1, left[i]);
                }
            }
        }
        for (int i = n - 1; i >= 0; i--) {
            right[i] = 1;
            for (int j = n - 1; j > i; j--) {
                if (height[j] < height[i]) {
                    right[i] = Math.max(right[j] + 1, right[i]);
                }
            }
        }
        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            res[i] = left[i] + right[i] - 1;
        }
        int maxSurvive = 0;
        for (int i = 0; i < n; i++) {
            maxSurvive = res[i] > maxSurvive ? res[i] : maxSurvive;
        }
        return n-maxSurvive;
    }
}
