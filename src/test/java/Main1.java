import java.util.*;
public class Main1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            int money = sc.nextInt();
            int m = sc.nextInt();
            sc.nextLine();
            money /= 10;
            int[][] prices = new int[m+1][3];
            int[][] weights = new int[m+1][3];
            for (int i = 1; i <= m; i++) {
                int a = sc.nextInt();
                int b = sc.nextInt();
                int c = sc.nextInt();
                a /= 10;//price
                b = b * a;//weight
                if (c == 0) {
                    // 主件
                    prices[i][0] = a;
                    weights[i][0] = b;
                } else if (prices[c][1] == 0) {
                    // 附件1
                    prices[c][1] = a;
                    weights[c][1] = b;
                } else {
                    // 附件2
                    prices[c][2] = a;
                    weights[c][2] = b;
                }
                sc.nextLine();
            }

            int[][] dp = new int[m+1][money+1];
            for (int i = 1; i <= m; i++) {
                for(int j = 1; j <= money; j++) {
                    int a = prices[i][0];
                    int b = weights[i][0];
                    int c = prices[i][1];
                    int d = weights[i][1];
                    int e = prices[i][2];
                    int f = weights[i][2];

                    dp[i][j] = j - a >= 0 ? Math.max(dp[i-1][j], dp[i-1][j-a] + b) : dp[i-1][j];
                    dp[i][j] = j-a-c >= 0 ? Math.max(dp[i][j], dp[i-1][j-a-c] + b + d):dp[i][j];
                    dp[i][j] = j-a-e >= 0 ? Math.max(dp[i][j], dp[i-1][j-a-e] + b + f):dp[i][j];
                    dp[i][j] = j-a-c-e >= 0 ? Math.max(dp[i][j], dp[i-1][j-a-c-e] + b +d + f):dp[i][j];
                }
            }

            System.out.println(dp[m][money] * 10);
        }
    }
}
