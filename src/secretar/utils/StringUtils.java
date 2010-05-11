/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package secretar.utils;

/**
 *
 * @author Степан
 */
public class StringUtils {

    public static boolean isNullOrEmpty(String string) {
        if (string == null) return true;
        return string.isEmpty();
    }
}
