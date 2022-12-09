package tool.util;

import java.math.BigDecimal;
import java.util.Random;

/**
 * Description:
 * Author: Liying
 * DateTime: 2021-09-07 14:22
 */
public class NumberUtil {

    public static Boolean isNumber(String str) {
        try {
            new BigDecimal(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getRandomCode(int len) {
        String result = makeRandomCode(len);

        if (result.matches(".*[a-z]{1,}.*") && result.matches(".*[A-Z]{1,}.*") && result.matches(".*\\d{1,}.*") && result.matches(".*[~!@#$%^&*\\.?]{1,}.*")) {
            return result;
        }

        result = makeRandomCode(len);

        return result;
    }

    public static String makeRandomCode(int len) {
        char charr[] = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890~!@#$%^&*.?".toCharArray();

        StringBuilder sb = new StringBuilder();

        Random r = new Random();

        for (int x = 0; x < len; ++x) {
            sb.append(charr[r.nextInt(charr.length)]);
        }

        return sb.toString();
    }
}
