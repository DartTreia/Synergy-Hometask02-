

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class HomeWork2 {

    /**
     * Задание:
     * 1. Создайте вложенный класс LoginValidationException, унаследуйте его от Exception
     * 2. Реализуйте проверку "Логина" в методе validateLogin по следуюзим правилам:
     *     - должен содержать только латинские буквы, цифры и знак подчеркивания
     *     - должен содержать как минимум одну маленькую, одну большую букву, цифру и нижнее подчеркивание
     *     - максимальная длинна логина- 20 символов
     *     - если логин не соответствует требованиям - выбросить LoginValidationException
     *     - можно использовать регулярные выражения
     * 3. Реализуйте проверку логина в методе isLoginValid по следующим правилам
     *     - метод должен вызывать метод validateLogin
     *     - если метод validateLogin не выбросил ошибку - вернуть true
     *     - если метод validateLogin выбросил ошибку - вернуть false
     */

    public static List<String> loginList = Arrays.asList(
            "Minecraft_12",                                     // true
            "Player_3433",                                      // true
            "Dok_a111",                                         // true
            "Java",                                             // false
            "1122233",                                          // false
            "Play__",                                           // false
            "_Sun2_",                                           // true
            "____",                                             // false
            "Winx!",                                            // false
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa12_",            // false
            "WOWOWOOWOWOWOOWOWOWOWOWOW",                        // false
            "Correct_22"                                        // true
    );

    public static List<Boolean> checkLoginResults = Arrays.asList(
            true, true, true, false, false, false, true, false, false, false, false, true
    );

    public static void main(String[] args) {
        System.out.println("\nTests for validateLogin");
        AntiCheat.run();
        for (int i = 0; i < loginList.size(); i++) {
            try {
                validateLogin(loginList.get(i));
                printTestCase(i, checkLoginResults.get(i), true, 20);
            } catch(Exception e) {
                printTestCase(i, checkLoginResults.get(i), false, 20);
            }
        }

        System.out.println("\nTests for isLoginValid");
        AntiCheat.run();
        for (int i = 0; i < loginList.size(); i++)
            printTestCase(i + loginList.size(),
                    checkLoginResults.get(i),
                    isLoginValid(loginList.get(i)),
                    20);
    }

    public static void validateLogin(String login) throws LoginValidationException {
    	int k=0;
    	int a=0;
    	int b=0;
    	int e=0;
    	int d=0;
    	if(login.length()>20) throw new LoginValidationException("Login's length can be 20 max");
        for(int i=0;i<login.length();i++) {
        	char c = login.charAt(i);
        	if(c>='A' && c<='Z')
        		a=1;
        	if(c>='a' && c<='z')
        		b=1;
        	if(c>='0' && c<='9')
        		e=1;
        	if(c=='_')
        		d=1;
        	if(!(c>='A' && c<='Z') || !(c>='a' && c<='z') || !(c>='0' && c<='9') || !(c=='_')) 
        	k++;
        	
        	
        }
        if(k>0) throw new LoginValidationException("Login must has only symbols, english letters, and underscore");
        if(a+b+d+e!=4) throw new LoginValidationException("Login must has minimum 1 uppercase letter, 1 lowercase letter, 1 number and 1 underscore");
    }

    public static Boolean isLoginValid(String login) {
        try {
        	validateLogin(login);
        	return true;
        }catch(LoginValidationException e){
        	System.err.println(e);
        return false;
        }
    }

    public static class AntiCheat {
        public static void run() {
            StringBuilder sb = new StringBuilder("");
            List<String> antiCheatList = new ArrayList<>();
            antiCheatList.addAll(loginList);
            antiCheatList.addAll(checkLoginResults.stream().map(Object::toString).toList());
            antiCheatList.add(sb.toString());
            calcHash(antiCheatList);
        };

        public static String bytesToHex(byte[] bytes) {
            char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
            char[] hexChars = new char[bytes.length * 2];
            for (int j = 0; j < bytes.length; j++) {
                int v = bytes[j] & 0xFF;
                hexChars[j * 2] = HEX_ARRAY[v >>> 4];
                hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
            }
            return new String(hexChars);
        }

        public static void calcHash(List<String> list) {
            String total = String.join("", list);
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(total.getBytes());
                byte[] digest = md.digest();
                System.out.println("AntiCheatCheck: " + bytesToHex(digest));
            } catch (NoSuchAlgorithmException ignored) {}
        }
    }

    public static String constLen(String str, int len) {
        StringBuilder sb = new StringBuilder(str);
        while (len-- - str.length() > 0) sb.append(" ");
        return sb.toString();
    }

    public static void printTestCase(int n, Boolean exp, Boolean act, int minLen) {
        Function<String, String> green = str -> "\u001B[34m" + str + "\u001B[0m";
        Function<String, String> yellow = str -> "\u001B[33m" + str + "\u001B[0m";
        System.out.print( "TEST CASE " + constLen(String.valueOf(n), 4));
        System.out.print( "Ожидание: " + yellow.apply(constLen(exp.toString(), minLen)) + " Реальность: " + green.apply(constLen(act.toString(), minLen) + " "));
        if (Objects.equals(exp, act)) System.out.print("✅"); else System.out.print("❌");
        System.out.println();
    }

}