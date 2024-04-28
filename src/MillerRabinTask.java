import parcs.*;

import java.util.Random;

public class MillerRabinTask implements AM {

    public void run(AMInfo info) {
        String encodedData = (String) info.parent.readObject();
        String[] data = encodedData.split(" ");
        int k = Integer.parseInt(data[0]);
        StringBuilder response = new StringBuilder();

        for(int i = 1; i < data.length; i++) {
            int number = Integer.parseInt(data[i]);
            boolean isPrime = millerRabin(number, k);
            response.append(number);
            response.append(" - ");
            response.append(isPrime ? "Prime" : "Not Prime");
            response.append("\n");
        }

        info.parent.write(response.toString());
    }

    private static boolean millerRabin(int n, int k) {
        if (n % 2 == 0 || n <= 1) {
            return false;
        }

        int m = (n - 1) / 2;
        int t = 1;
        while (m % 2 == 0) {
            m /= 2;
            t++;
        }

        Random random = new Random();

        for (int i = 1; i <= k; i++) {
            int a = 1 + random.nextInt(n - 1);
            t++;
            int u = modPow(a, m, n);

            if (u != 1 && u != n - 1) {
                int j = 1;
                boolean isSkip = false;

                while (u != -1 && j < t) {
                    u = (u * u) % n;
                    j++;
                    if (u == 1) {
                        return false;
                    }
                    if (u == n - 1) {
                        isSkip = true;
                        break;
                    }
                }

                if (isSkip) {
                    continue;
                }
                if (u != -1) {
                    return false;
                }
            }
        }
        return true;
    }

    private static int modPow(int base, int exp, int mod) {
        int result = 1;
        base = base % mod;

        while (exp > 0) {
            if ((exp & 1) == 1) {  // If exp is odd
                result = (result * base) % mod;
            }
            exp >>= 1;  // Right shift exp by 1
            base = (base * base) % mod;
        }

        return result;
    }
}
