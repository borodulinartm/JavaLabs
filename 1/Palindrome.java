public class Palindrome {
    public static String ReverseString(String input_string) {
        StringBuilder ReversedString = new StringBuilder();
        for(int index = input_string.length() - 1; index >= 0; --index) {
            ReversedString.append(String.valueOf(input_string.charAt(index)));
        }
        return ReversedString.toString();
    }

    public static boolean isPalindrome(String input_string) {
        String reversed_input_string = ReverseString(input_string);
        return input_string.equals(reversed_input_string);
    }

    public static void main(String[] args) {
        for(int index = 0; index < args.length; ++index) {
            System.out.printf("Слово \"%s\"", args[index]);
            if (isPalindrome(args[index])) {
                System.out.print(" является палиндромом\n");
            } else {
                System.out.print(" не является палиндромом\n");
            }
        }
    }
}
