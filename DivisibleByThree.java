import java.util.Arrays;

public class Solution {
    private static final boolean ENABLE_LOGGING = true;
    public static void main(String[] args) {

        // Test numbers 0 to n
        boolean runTests = true;

        if (runTests) {
            int iterations = 10000;

            for (int i = 0; i < iterations; i++) {
                int numberOfDigits = Integer.toString(i).length();
                int[] array = createTestArray(numberOfDigits, i);

                for (int j = 0; j < array.length; j++) {
                    log(array[j] + " ", false);
                }
                log();

                findLargestNumberDivisibleByThree(array);
            }
        }

        // Test specific numbers
        int[] digits = new int[args.length];

        for (int i = 0; i < args.length; i++) {
            digits[i] = Integer.parseInt(args[i]);
        }

        int maxNumber = solution(digits);

        log("Largest number divisible by 3: " + maxNumber);
    }

    public static int solution(int[] l) {
        return findLargestNumberDivisibleByThree(l);
    }

    private static int findLargestNumberDivisibleByThree(int[] numbers) {
        // If sum < 3, cannot be divisible by 3
        int sum = 0;
        for (int num : numbers) {
            sum += num;
        }

        if (sum == 0 || sum < 3) {
            log("Sum < 3: return 0");
            log();
    
            return 0;
        }

        // Use dynamic programming to improve performance for remainder 0, 1, 2
        int[][] dp = new int[numbers.length + 1][3];

        for (int digit = 1; digit <= numbers.length; digit++) {
            int currentDigit = numbers[digit - 1];

            // Find the previous maximum number for remainder 0, 1, 2
            int maxWithoutCurrentDigit = Math.max(
                Math.max(dp[digit - 1][0], dp[digit - 1][1]), dp[digit - 1][2]);

            // Add the current digit and shift left for the new maximum number
            int maxWithCurrentDigit = maxWithoutCurrentDigit * 10 + currentDigit;

            for (int remainder = 0; remainder < 3; remainder++) {

                // Example: currentDigit = 0, previous maximum = 9, then new maximum is 90
                if (currentDigit == 0 && dp[digit - 1][0] > 0) {
                    dp[digit][0] = dp[digit - 1][0] * 10;
                }

                // Example: currentDigit = 3, previous maximum = 0
                if (currentDigit % 3 == 0 && dp[digit - 1][0] < currentDigit) {
                    dp[digit][0] = currentDigit;
                }

                // Update new and previous maximum numbers
                if (maxWithCurrentDigit % 3 == remainder) {
                    dp[digit][remainder] = maxWithCurrentDigit;
                }
                else {
                    dp[digit][remainder] = dp[digit-1][remainder];
                }

                log("Digit: " + digit + ", Remainder: " + remainder);
                log("maxWithoutCurrentDigit: " + maxWithoutCurrentDigit);
                log("maxWithCurrentDigit: " + maxWithCurrentDigit);
            }

            // Dynamic programming improves performance, but we need to use recursion to get the
            // largest maximum number when the number is more than 2 digits, up to and including 111
            if (maxWithCurrentDigit > dp[digit][0] && maxWithCurrentDigit > 111) {
                int length = (int) Math.log10(maxWithCurrentDigit) + 1;

                int[] digitsToCheck = new int[length];
        
                for (int i = length - 1; i >= 0; i--) {
                    digitsToCheck[i] = maxWithCurrentDigit % 10;
                    maxWithCurrentDigit /= 10;
                }

                // Without sorting: [1 2 5 9] returns 921 insetad of 951
                Arrays.sort(digitsToCheck);
                reverseArray(digitsToCheck);

                int maxNumber = findMaxNumber(digitsToCheck, 0, 0, 0);

                dp[digit][0] = Math.max(dp[digit][0], maxNumber);
            }

            if (ENABLE_LOGGING) {
                for (int i = 0; i <= numbers.length; i++) {
                    log(Arrays.toString(dp[i]));
                }
            }
        }

        int result = dp[numbers.length][0];
        log("result: " + result);

        int sortedResult = getSortedResult(result);
        log("Largest number divisible by 3: " + sortedResult);
        log();

        return sortedResult;
    }

    private static int getSortedResult(int result) {
        String resultString = Integer.toString(result);

        // Convert string to array of characters
        char[] digits = resultString.toCharArray();    

        Arrays.sort(digits);

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = digits.length - 1; i >= 0; i--) {
            stringBuilder.append(digits[i]);
        }

        return Integer.parseInt(stringBuilder.toString());
    }

    private static void reverseArray(int[] array) {
        int start = 0;
        int end = array.length - 1;
    
        while (start < end) {
            int temp = array[start];
            array[start] = array[end];
            array[end] = temp;
    
            start++;
            end--;
        }
    }

    private static int[] createTestArray(int maxLength, int index) {
        int[] array = new int[maxLength];

        for (int i = maxLength - 1; i >= 0; i--) {
            array[i] = index % 10;
            index /= 10;
        }

        return array;
    }

    private static int findMaxNumber(int[] digits, int index, int currentNumber, int maxNumber) {
        // New maximum number found
        if (currentNumber % 3 == 0 && currentNumber > maxNumber) {
            log("Valid Combination: " + Arrays.toString(digits) + " Number: " + currentNumber);
            maxNumber = currentNumber;
        }

        for (int i = index; i < digits.length; i++) {
            int newNumber = currentNumber * 10 + digits[i];

            log("Recursion: " + Arrays.toString(digits) + " currIndex=" + index +  " newIndex=" + (i + 1) +
                ", currentNumber=" + currentNumber + " newNumber=" + newNumber + ", maxNumber=" + maxNumber);

                maxNumber = findMaxNumber(digits, i + 1, newNumber, maxNumber);
        }
    
        return maxNumber;
    }

    private static void log() {
        log("", true);
    }

    private static void log(String message) {
        log(message, true);
    }

    private static void log(String message, boolean newLine) {
        if (ENABLE_LOGGING) {
            if (newLine) {
                System.out.println(message);
            } else {
                System.out.print(message);
            }
        }
    }
}
