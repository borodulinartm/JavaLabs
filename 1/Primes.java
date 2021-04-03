public class Primes {
    public static boolean isPrime(int n) {
        for(int i = 2; i * i <= n; ++i) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        for(int number = 1; number < 100; ++number) {
            if (isPrime(number)) {
                System.out.printf("%d ", number);
            }
        }
    }
}
