package jsonrpc.utils;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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




    public static Set<String> rolesToSet(Object authorities) {
        //noinspection unchecked
        return new HashSet<>(((List<String>) authorities));
    }

    public static Set<String> grantedAuthorityToSet(Collection<? extends GrantedAuthority> authorities) {

        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
    }


    public static Set<GrantedAuthority> rolesToGrantedAuthority(Set<String> authorities) {
        //noinspection unchecked
        return authorities
            .stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }

    public static Set<GrantedAuthority> rolesToGrantedAuthority(Object authorities) {
        //noinspection unchecked
        return ((List<String>)authorities)
               .stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet());
    }



//    public static void idSetter(Object o, Long id) {
//
//        try {
//            Class<?> clazz = o.getClass();
//            Field field = null;
//            do {
//                try {
//                    field = clazz.getDeclaredField("id");
//                } catch(Exception ignore) {}
//            }
//            while((clazz = clazz.getSuperclass()) != null);
//
//            assert field != null;
//            field.setAccessible(true);
//            field.set(o, id);
//        }
//        catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }


    public static void fieldSetter(String fieldName, Object o, Object value) {

        try {
            Class<?> clazz = o.getClass();
            Field field = null;
            do {
                try {
                    field = clazz.getDeclaredField(fieldName);
                } catch(Exception ignore) {}
            }
            while((clazz = clazz.getSuperclass()) != null);

            assert field != null;
            field.setAccessible(true);
            field.set(o, value);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static long toLong(String s) {

        return Long.parseLong(s);
    }

    public static int toInt(String s) {

        return Integer.parseInt(s);
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

    /**
     * Find first field with BigDecimal type<br>
     * Not base in super classes
     */

    /**
     *
     * @param source searching in this class
     * @param pattern which class to find
     * @return field name
     */
    public static String getPriceFieldName(Class source, Class pattern) {

        String result = null;
        outerLoop:
        do {
            for (Field f : source.getDeclaredFields()) {
                if (f.getType() == pattern) {
                    result = f.getName();
                    break outerLoop;
                }
            }
        }
        while((source = source.getSuperclass()) != null);

        return result;
    }












}

