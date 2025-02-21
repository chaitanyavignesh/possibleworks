import java.io.FileReader;
import java.math.BigInteger;
import java.util.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class SecretSharing {

    public static void main(String[] args) {
        try {
            // Process both test case files
            System.out.println("Secret for First Test Case: " + solveForC("first_test.json"));
            System.out.println("Secret for Second Test Case: " + solveForC("second_test.json"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static long solveForC(String filename) {
        try {
            // Load JSON file
            JSONObject testCase = (JSONObject) new JSONParser().parse(new FileReader(filename));
            JSONObject keys = (JSONObject) testCase.get("keys");

            int n = ((Long) keys.get("n")).intValue();
            int k = ((Long) keys.get("k")).intValue();

            // Extract x and y values
            List<Integer> xVals = new ArrayList<>();
            List<BigInteger> yVals = new ArrayList<>();

            for (Object key : testCase.keySet()) {
                if (!key.equals("keys")) {
                    int x = Integer.parseInt((String) key);
                    JSONObject valueObj = (JSONObject) testCase.get(key);
                    int base = Integer.parseInt((String) valueObj.get("base"));
                    BigInteger y = new BigInteger((String) valueObj.get("value"), base);

                    xVals.add(x);
                    yVals.add(y);
                }
            }

            // Use only k roots to determine the polynomial
            List<Integer> xSubset = xVals.subList(0, k);
            List<BigInteger> ySubset = yVals.subList(0, k);

            // Compute the constant term (c) using Lagrange Interpolation
            return lagrangeInterpolation(xSubset, ySubset, 0).longValue();

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static BigInteger lagrangeInterpolation(List<Integer> xVals, List<BigInteger> yVals, int targetX) {
        BigInteger result = BigInteger.ZERO;
        int k = xVals.size();

        for (int i = 0; i < k; i++) {
            BigInteger term = yVals.get(i);
            BigInteger numerator = BigInteger.ONE;
            BigInteger denominator = BigInteger.ONE;

            for (int j = 0; j < k; j++) {
                if (i != j) {
                    numerator = numerator.multiply(BigInteger.valueOf(targetX - xVals.get(j)));
                    denominator = denominator.multiply(BigInteger.valueOf(xVals.get(i) - xVals.get(j)));
                }
            }
            term = term.multiply(numerator).divide(denominator);
            result = result.add(term);
        }

        return result;
    }
}
