package cn.pinmix;

/**
 * Created by icity on 16/9/5.
 */
public class SimpleUtils {

    /*
      跳跃关键字，const is NOT keyword，but containted now
      */
    private static final String[] keywords = {"final", "static", "const", "public", "protected", "private"};


    /**
     * @param k
     * @return
     */
    public static boolean isContaintKeyWorkds(String k) {
        boolean result = false;
        for (String str : keywords) {
            if (k.equalsIgnoreCase(str)) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * @param statement
     * @return className
     */
    public static String getClassName(String statement) {

        String part = null;

        int firstIndex = statement.indexOf("<");

        if (firstIndex >= 0) {
            part = statement.substring(0, firstIndex);
        } else {
            part = statement;
        }
        //final static public private const
        String[] split = part.split("\\s+");

        String className = null;

        for (String str : split) {

            if (str.length() > 0 && !SimpleUtils.isContaintKeyWorkds(str)) {
                className = str;
                break;
            }
        }
        return className;
    }

    /**
     * @param line
     * @return new statement string
     */
    public static String replaceLine(String line) {

        String className = SimpleUtils.getClassName(line);

        if(className == null){
            return null;
        }
        //最后 > 位置
        int last = line.lastIndexOf(">");

        boolean isGenerics = false;

        String statement = null;
        String headPart =null;

        if (last < 0) {
            //think it NO Generics
            int classNameIndex  = line.indexOf(className);
            int endCharIndex = classNameIndex+className.length();

            headPart = line.substring(0,endCharIndex);
            statement =line.substring(endCharIndex);

        }
        else{
            isGenerics = true;
            statement =line.substring(last);
            headPart = line.substring(0,last);

        }

        StringBuilder mBuilder = new StringBuilder();

        String[] vars = statement.split(",");

        if (vars.length == 0) {
            return null;
        }

        boolean appendedFirst = false;

        for (int i = 0; i < vars.length; i++) {

            //trim var name
            String _varName = vars[i].trim();

            if (_varName.length() > 0) {

                if (_varName.endsWith(";")) {

                    if (appendedFirst) {
                        mBuilder.append("," + _varName.replaceFirst(";", className + ";"));

                    } else {
                        appendedFirst = true;
                        mBuilder.append(_varName.replaceFirst(";", className + ";"));
                    }
                } else {
                    if (appendedFirst) {
                        mBuilder.append("," + _varName + className);

                    } else {
                        mBuilder.append(_varName + className);
                        appendedFirst = true;
                    }
                }
            }
        }
        return headPart + " " + mBuilder.toString();
    }

}
