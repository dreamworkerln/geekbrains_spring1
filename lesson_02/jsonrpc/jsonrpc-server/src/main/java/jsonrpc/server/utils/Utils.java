package jsonrpc.server.utils;

public class Utils {

    public static boolean isNullOrEmpty(Object object) {

        return object == null || object.getClass() == String.class && ((String)object).trim().isEmpty();
    }


    public static String getNullIfEmpty(String s) {

        if (s != null && s.trim().isEmpty())
            s = null;

        return s;
    }

    public static String getEmptyIfNull(String s) {

        if (s == null)
            s = "";

        return s;
    }

    public static int boolToInt(boolean b) {
        return b ? 1 : 0;
    }

    public static String boolToStr(boolean b) {
        return b ? "1" : "0";
    }

    /*
    public static String milliTimeToEltexTimeString($milliTime)
    {
        return date("Y_m_d_H_i_s", $milliTime / 1000) + "_" + $milliTime % 1000;
    }

    public static String microTimeFloatToMYSQLtimestamp3($microtimeFloat)
    {
        return date("Y-m-d H:i:s", $microtimeFloat) + "." + $microtimeFloat * 1000 % 1000;
    }
    */


/*

    public static String milliTimeFloatToMYSQLtimestamp3(float millitimeFloat)
    {
        return date("Y-m-d H:i:s", $millitimeFloat / 1000) + "." + $millitimeFloat % 1000;
        String newstring = datetime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss."));
        System.out.println(newstring); // 2011-01-18
    }

*/






}

