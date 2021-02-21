import java.util.HashMap;

public final class BrailleConverter {
    // Modes
    public static final int DISCORD_DESKTOP_LINE = 90;
    public static final int DISCORD_MOBILE_LINE = 32;
    public static final int DISCORD_MAX = 2000;

    private BrailleConverter() {}

    // Converts a 2 by 8 int array of 1s and 0s to a braille unicode char
    private static char arrayBlockToBraille(int[][] block) {
        // Gets the permutation value (last value in the unicode name) from a 2x8 array of int
        int unicodeName = arrayToBrailleUnicodeName(block);

        // 10240 for 0x2800
        int unicodeValue = 10240 + combinationSequenceIndex(unicodeName);

        return Character.toChars(unicodeValue)[0];
    }

    /**
     *
     * @param pixelArr  An 2D array of pixel values in integers
     * @param invert    Whether or not to invert the binary image
     * @param fillGaps  Whether or not to replace empty braille characters with a single braille value, used for equal
     *                  width in case that the target application does not support it
     * @return          A 2D pixel array of braille unicode
     */
    public static String generateBraille(int[][] pixelArr, boolean invert, boolean fillGaps) {

        int xScale = 2;
        int yScale = 4;

        StringBuilder output = new StringBuilder();

        for (int y = 0; y < pixelArr[0].length - yScale; y += yScale) {
            for (int x = 0; x < pixelArr.length - xScale; x += xScale) {
                int sum = 0;
                int[][] block = new int[4][2];

                for (int i = 0; i < xScale; i++) {
                    for (int j = 0; j < yScale; j++) {
                        int[] rgb = ImageProcessor.hexToRGB(pixelArr[x+i][y+j]);

                        // TODO Only taking R value under the assumption that image is binary,
                        //  might change
                        // Divide to map to 0 and 1; invert using absolute value
                        block[j][i] = Math.abs(rgb[0] / 255 - (invert ? 0 : 1));
                        sum += block[j][i];
                    }
                }

                if (fillGaps && sum == 0) {
                    block[3][0] = 1;
                }

                char brailleChar = arrayBlockToBraille(block);
                output.append(brailleChar);
            }
            output.append('\n');
        }

        return output.toString();
    }


    // region  ========================Helper functions===========================

    // Reference: https://www.unicode.org/charts/PDF/U2800.pdf

    // Finds the number at the end of the unicode name given a 2D int array of 1s and 0s
    // The number is a combination that can be used with combinationSequenceIndex()
    private static int arrayToBrailleUnicodeName(int[][] array) {

        int positionValues = 0;

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 3; j++) {
                if (array[j][i] == 1) {
                    positionValues = (j + (i * 3) + 1) + positionValues * 10;
                }
            }
        }

        // For the 7th and 8th position, as they are formatted differently
        for (int i = 0; i < 2; i++) {
            if (array[3][i] == 1) {
                positionValues = (i + 7) + positionValues * 10;
            }
        }

        return positionValues;
    }

    // Finds the n value given a combination of numbers ordered into an integer (1, 3, 4 -> 134)
    // Use to find the offset value of the unicode for a given braille
    private static int combinationSequenceIndex(int value) {
        if (value == 0)
            return 0;

        // Get the lowest digit (since the lowest digit points to the n^2 position)
        int lowestDigit = value % 10;
        // Using the lowest digit, find the position using 2^(n-1), then recurse until 0
        return (int) Math.pow(2, lowestDigit - 1) + combinationSequenceIndex(value / 10);
    }

    // ===============Unused for now======================
    public static int[] generateBrailleArray() {
        int[] arr = new int[256];

        int c = 1; // current value
        int i = 1; // index
        arr[i] = c;

        while (i < 256) {
            int target = i;
            for (int j = 0; j < target; j++) {
                arr[i] = arr[j] * 10 + c;

                i++;
            }
            c++;
        }
        return arr;
    }

    private static HashMap<Integer, Character> generateBrailleMap(int[] arr) {
        HashMap<Integer, Character> hash = new HashMap<Integer, Character>();
        for (int i = 0; i < arr.length; i++) {
            //String hex = Integer.toHexString(i);
            //hex += hex.length() == 1 ? "0" : "";

            int value = 10240 + i;


            hash.put(arr[i], Character.toChars(value)[0]);
        }

        return hash;
    }

    // endregion
}
