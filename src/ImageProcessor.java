public final class ImageProcessor {
    private ImageProcessor() {}

    // ================Filters=================
    // ---------- Gray scale-------------
    private static int grayscalePixel(int[] rgb, float rCoeff, float gCoeff, float bCoeff) {
        return (int) (rgb[0] * rCoeff + rgb[1] * gCoeff + rgb[2] * bCoeff);
    }

    public static int[][] grayscale(int[][] pixelArr) {
        int[][] output = new int[pixelArr.length][pixelArr[0].length];

        for (int x = 0; x < pixelArr.length; x++) {
            // Debug
            StringBuilder outputValues = new StringBuilder();

            for (int y = 0; y < pixelArr[0].length; y++) {
                int pixel = pixelArr[x][y];
                int[] rgb = hexToRGB(pixel);

                int grayscalePixel = grayscalePixel(rgb, 0.299f, 0.587f, 0.114f);

                int newPixel = rgbToHex(new int[] {grayscalePixel, grayscalePixel, grayscalePixel});

                output[x][y] = newPixel;
            }
        }

        return output;
    }

    // ------------ Binary Image-----------------
    public static int[][] binary(int[][] pixelArr, int threshold) {
        int[][] output = new int[pixelArr.length][pixelArr[0].length];

        for (int x = 0; x < pixelArr.length; x++) {
            for (int y = 0; y < pixelArr[0].length; y++) {
                int pixel = pixelArr[x][y];
                int[] rgb = hexToRGB(pixel);

                int grayscalePixel = grayscalePixel(rgb, 0.299f, 0.587f, 0.114f);
                int value = grayscalePixel > threshold ? 255 : 0;

                int newPixel = rgbToHex(new int[] {value, value, value});

                output[x][y] = newPixel;
            }
        }

        return output;
    }




    // Hex methods
    public static int[] hexToRGB(int color) {
        int[] rgb = new int[3];

        rgb[0] = color & 0x00ff;
        rgb[1] = (color >> 8) & 0x00ff;
        rgb[2] = (color >>> 16) & 0x00ff;

        return rgb;
    }

    public static int rgbToHex(int[] rgb) {
        return (rgb[2] << 16) + (rgb[1] << 8) + rgb[0];
    }

    public static char[] reverseArray(char[] arr) {
        for (int i = 0; i < arr.length / 2; i++) {
            char temp = arr[i];
            arr[i] = arr[arr.length - i - 1];
            arr[arr.length - i - 1] = temp;
        }

        return arr;
    }
}
