import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main3 {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String str;
        while ((str = reader.readLine()) != null) {
            String[] nums = str.split("\\.");
            boolean flag = true;

            if (nums.length == 4) {
                for (String num : nums) {
                    // 01.23...
                    if (!num.equals("0") && num.startsWith("0"))
                        flag = false;
                    //+1.23...
                    if (num.startsWith("+")||num.startsWith("-")){
                        flag = false;
                    }
                    // 非数字
                    try {
                        int i = Integer.parseInt(num);
                        // 数字范围
                        if (i < 0 || i > 255)
                            flag = false;
                    } catch (NumberFormatException e) {
                        flag = false;
                        break;
                    }

                }
            } else {
                flag = false;
            }
            System.out.println(flag == true ? "YES" : "NO");
        }
    }
}
