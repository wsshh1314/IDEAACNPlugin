package cn.pinmix;

/**
 * Created by icity on 16/9/5.
 */
public class SimpleUtils {

    /*
      跳跃关键字，const is NOT keyword
      */
    private static final  String[] keywords = {"final", "static", "const", "public", "protected", "private"};



    /**
     * @param k
     * @return
     */
    public static  boolean isContaintKeyWorkds(String k) {
        boolean result = false;
        for (String str : keywords) {
            if (k.equalsIgnoreCase(str)) {
                result = true;
                break;
            }
        }
        return result;
    }

}
