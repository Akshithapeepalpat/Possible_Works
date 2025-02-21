import org.json.JSONObject;
import java.math.BigInteger;
import java.util.*;

public class ShamirSecretSharing {
    public static void main(String[] args) {
        try {
            // Read JSON manually from file
            String content = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get("testcase.json")));
            JSONObject jsonObject = new JSONObject(content);

            // Solve for the secret
            System.out.println("Secret: " + solveSecret(jsonObject));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static BigInteger solveSecret(JSONObject json) {
        int n = json.getJSONObject("keys").getInt("n");
        int k = json.getJSONObject("keys").getInt("k");
        int m = k - 1; // Degree of polynomial

        List<BigInteger> xValues = new ArrayList<>();
        List<BigInteger> yValues = new ArrayList<>();

        // Parse and decode roots manually
        for (String key : json.keySet()) {
            if (key.equals("keys")) continue;
            JSONObject root = json.getJSONObject(key);
            int x = Integer.parseInt(key);
            int base = root.getInt("base");
            BigInteger y = new BigInteger(root.getString("value"), base);
            xValues.add(BigInteger.valueOf(x));
            yValues.add(y);
        }

        // Use Lagrange Interpolation to find the constant term
        return lagrangeInterpolation(xValues, yValues, BigInteger.ZERO);
    }

    public static BigInteger lagrangeInterpolation(List<BigInteger> x, List<BigInteger> y, BigInteger xTarget) {
        BigInteger result = BigInteger.ZERO;
        int k = x.size();

        for (int i = 0; i < k; i++) {
            BigInteger term = y.get(i);
            BigInteger numerator = BigInteger.ONE;
            BigInteger denominator = BigInteger.ONE;

            for (int j = 0; j < k; j++) {
                if (i != j) {
                    numerator = numerator.multiply(xTarget.subtract(x.get(j)));
                    denominator = denominator.multiply(x.get(i).subtract(x.get(j)));
                }
            }
            term = term.multiply(numerator).divide(denominator);
            result = result.add(term);
        }

        return result;
    }
}
