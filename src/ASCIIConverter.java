public final class ASCIIConverter {

    private ASCIIConverter() {}

    private char valueToChar(int value, char[] arr) {
        int len = arr.length;

        int value_check = 0;
        int i = 0;

        while (value_check + 255/len < value) {
            value_check += 255/len;
            i++;
        }

        if (i >= len)
            i = len-1;

        return arr[i];
    }

    public final char[] TEN_CLASSIC = {' ','.',':','-','=','+','*','#','%','@'};

    public String toASCII(int[][] pixelArr, int scale, char[] charSet) {

        int yScale = scale*3;
        int xScale = scale;

        String output = "";

        for (int y = scale; y < pixelArr[0].length - yScale; y += yScale + 1) {
            for (int x = scale; x < pixelArr.length - scale; x += scale + 1) {
                int sum = 0;

                for (int i = -scale; i <= scale; i++) {
                    for (int j = -scale; j <= scale; j++) {
                        int[] rgb = ImageProcessor.hexToRGB(pixelArr[x+i][y+j]);

                        int average = (rgb[0] + rgb[1] + rgb[2])/3;

                        sum += average;
                    }
                }

                char c = valueToChar((int) Math.ceil(sum/Math.pow(scale*2+1, 2)), charSet);

                output += c;
            }
            output += '\n';
        }


        return output;
    }
}
