public class Functions {
    /*
    Default is SHA-256.
     */
    public static int wordLength = 32;
    public static int[] K = new int[]{2, 13, 22, 6, 11, 25, 7, 18, 3, 17, 19, 10};

    /*
    Pad:
    Adds k *desiredLength - str.length* "0".
     */
    public static String pad(String str, int desiredLength) {
        int pads = desiredLength - str.length();
        StringBuilder strBuilder = new StringBuilder(str);
        for (int i = 1; i <= pads; i++) {
            strBuilder.insert(0, "0");
        }
        return strBuilder.toString();
    }

    /*
    Shift right:
    Creates a substring ignoring *shift* first characters and pads it to word length.
     */
    public static String shiftRight(String str, int shift) {
        return pad(str.substring(0, wordLength - shift % str.length()), wordLength);
    }

    /*
    Rotation right:
    Creates two substrings taking a midpoint *rots* and reverses their order.
     */
    public static String rotateRight(String str, int rots) {
        return pad(str.substring(wordLength - rots % str.length()) + str.substring(0, wordLength - rots % str.length()), wordLength);
    }

    /*
    Exclusive Or:
    XORs three strings together. Loops through every char and creates a new string.
     */
    public static String xor(String str1, String str2, String str3) {
        String str = "";
        for (int i = 0; i < wordLength; i++) {
            str = String.valueOf(str1.charAt(i)).equals("1") ^ String.valueOf(str2.charAt(i)).equals("1") ^ String.valueOf(str3.charAt(i)).equals("1") ? str + "1" : str +  "0";
        }
        return pad(str, wordLength);
    }

    /*
    Addition/Summation:
    Adds the values of an array of binary strings. Parses to long -> adds ->
    returns remainder after / by 2**wordLength.
     */
    public static String sum(String[] values) {
        long sum = 0;
        for (String val: values) {
            sum += Long.parseLong(val, 2);
        }
        return pad(Long.toBinaryString((long) (sum % Math.pow(2, wordLength))) ,wordLength);
    }

    /*
    Σ0/U-case Sigma 0:
    rtr(2)^rtr(13)^rtr(22)
     */
    public static String uSigma0(String str) {
        return pad(xor(rotateRight(str, K[0]), rotateRight(str, K[1]), rotateRight(str, K[2])), wordLength);
    }

    /*
    Σ1/U-case Sigma 1:
    rtr(6)^rtr(11)^rtr(25)
     */
    public static String uSigma1(String str) {
        return pad(xor(rotateRight(str, K[3]), rotateRight(str, K[4]), rotateRight(str, K[5])), wordLength);
    }

    /*
    σ0/L-case Sigma 0:
    rtr(7)^rtr(18)^shr(3)
     */
    public static String lSigma0(String str) {
        return pad(xor(rotateRight(str, K[6]), rotateRight(str, K[7]), shiftRight(str, K[8])), wordLength);
    }

    /*
    σ1/L-case Sigma 1:
    rtr(17)^rtr(19)^shr(10)
     */
    public static String lSigma1(String str) {
        return pad(xor(rotateRight(str, K[9]), rotateRight(str, K[10]), shiftRight(str, K[11])), wordLength);
    }

    /*
    Choice:
    Loops through every char of three strings and uses the first one to determine the
    choice between the second and third one. If it's 1 then the bit is taken from the first
    otherwise it is taken from the second.
     */
    public static String choice(String x, String y, String z) {
        String str = "";
        for (int i = 0; i < wordLength; i++) {
            str = x.charAt(i) == '1' ? str + y.charAt(i) : str + z.charAt(i);
        }
        return pad(str,wordLength);
    }

    /*
    Majority:
    Takes the most common bit from the three strings.
     */
    public static String majority(String x, String y, String z) {
        String str = "";
        long sum;

        for (int i = 0; i < wordLength; i++) {
            sum = Long.parseLong(String.valueOf(x.charAt(i))) + Long.parseLong(String.valueOf(y.charAt(i))) + Long.parseLong(String.valueOf(z.charAt(i))); // 0 - 3
            str = sum > 1 ? str + "1" : str + "0";
        }
        return pad(str, wordLength);
    }
}
