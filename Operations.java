import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Operations {
    public static final String[] K = {"01000010100010100010111110011000", "1110001001101110100010010010001", "10110101110000001111101111001111", "11101001101101011101101110100101", "111001010101101100001001011011", "1011001111100010001000111110001", "10010010001111111000001010100100", "10101011000111000101111011010101", "11011000000001111010101010011000", "10010100000110101101100000001", "100100001100011000010110111110", "1010101000011000111110111000011", "1110010101111100101110101110100", "10000000110111101011000111111110", "10011011110111000000011010100111", "11000001100110111111000101110100", "11100100100110110110100111000001", "11101111101111100100011110000110", "1111110000011001110111000110", "100100000011001010000111001100", "101101111010010010110001101111", "1001010011101001000010010101010", "1011100101100001010100111011100", "1110110111110011000100011011010", "10011000001111100101000101010010", "10101000001100011100011001101101", "10110000000000110010011111001000", "10111111010110010111111111000111", "11000110111000000000101111110011", "11010101101001111001000101000111", "110110010100110001101010001", "10100001010010010100101100111", "100111101101110000101010000101", "101110000110110010000100111000", "1001101001011000110110111111100", "1010011001110000000110100010011", "1100101000010100111001101010100", "1110110011010100000101010111011", "10000001110000101100100100101110", "10010010011100100010110010000101", "10100010101111111110100010100001", "10101000000110100110011001001011", "11000010010010111000101101110000", "11000111011011000101000110100011", "11010001100100101110100000011001", "11010110100110010000011000100100", "11110100000011100011010110000101", "10000011010101010000001110000", "11001101001001100000100010110", "11110001101110110110000001000", "100111010010000111011101001100", "110100101100001011110010110101", "111001000111000000110010110011", "1001110110110001010101001001010", "1011011100111001100101001001111", "1101000001011100110111111110011", "1110100100011111000001011101110", "1111000101001010110001101101111", "10000100110010000111100000010100", "10001100110001110000001000001000", "10010000101111101111111111111010", "10100100010100000110110011101011", "10111110111110011010001111110111", "11000110011100010111100011110010"};
    public static final String[] I = {"01101010000010011110011001100111", "10111011011001111010111010000101", "00111100011011101111001101110010", "10100101010011111111010100111010", "01010001000011100101001001111111", "10011011000001010110100010001100", "00011111100000111101100110101011", "01011011111000001100110100011001"};

    /*
    Runs through logical hashing operation.
     */
    public static String hash(String message) {
        StringBuilder hash = new StringBuilder();
        String[] hashes = I;
        String[][] words = words(message);

        // Fills words array.
        // For each block.
        for (int i = 0; i < words.length; i++) {
            // For each null word (16-63).
            for (int k = 16; k < 64; k++) {
                words[i][k] = Functions.sum(new String[] {Functions.lSigma1(words[i][k - 2]), words[i][k - 7], Functions.lSigma0(words[i][k - 15]), words[i][k - 16]});
            }
        }

        /*
        Compression
         */
        // For each block.
        for (String[] word : words) {
            String[] tHashes = hashes;
            // For each word.
            for (int k = 0; k < 64; k++) {
                // Calculates temporary values.

                String t1 = Functions.sum(new String[]{Functions.uSigma1(hashes[4]), Functions.choice(hashes[4], hashes[5], hashes[6]), hashes[7], Functions.pad(K[k], 32), word[k]}); // Temporary VAR1
                String t2 = Functions.sum(new String[]{Functions.uSigma0(hashes[0]), Functions.majority(hashes[0], hashes[1], hashes[2])}); // Temporary VAR2

                hashes = compress(hashes, t1, t2);
            }

            // Adds initial hashes to new hashes.
            for (int y = 0; y < 8; y++) {
                hashes[y] = Functions.sum(new String[]{hashes[y], tHashes[y]});
            }
        }

        // Converts compression stored in hashes[] to the final hex string.
        for (String s : hashes) {
            hash.append(Functions.pad(Long.toHexString(Long.parseLong(s, 2)), 8));
        }

        return hash.toString();
    }

    /*
    Compression algorithm
     */
    public static String[] compress(String[] hashes, String t1, String t2) {
        String[] tHashes = new String[8];
        System.arraycopy(hashes, 0, tHashes, 1, 7);

        tHashes[0] = Functions.sum(new String[]{t1, t2});
        tHashes[4] = Functions.sum(new String[]{tHashes[4], t1});

        return tHashes;
    }

    /*
    Creates blocks and words.
     */
    public static String[][] words(String str) {
        // Standard SHA message padding.
        str = getBits(str);
        String length = Long.toBinaryString(str.length());
        str = str + "1";
        str = str + "0".repeat(Math.max(0, 512 - (str.length() % 512) - 64));
        length = Functions.pad(length, 64);
        str = str + length;

        // Adds indexes for each word. (32 bits)
        StringBuilder str2 = new StringBuilder();
        for (int i = 0; i <= str.length() - 32; i+= 32) {
            str2.append("x").append(str, i, i + 32);
        }

        // Creates two spaces to store words. Second array stores by indexing by block and word.
        String[] tWords = str2.toString().replaceFirst("x","").split("x");
        String[][] words = new String[tWords.length/16][16];

        // Stores words by indexing block and word.
        for (int i = 0; i < tWords.length/16; i++) {
            // Expands each bloc array to 64 for word processing.
            words[i] = Arrays.copyOf(Arrays.copyOfRange(tWords, i * 16, i * 16 + 16), 64);
        }

        return words;
    }

    /*
    Gets binary value of string.
     */
    public static String getBits(String string) {
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);

        String[] str = new String[string.length()];
        for (int i = 0; i < bytes.length; i++) {
            str[i] = Functions.pad(Integer.toBinaryString(bytes[i]), 8);
        }

        return Arrays.toString(str).replace("[","").replace("]","").replace(", ","");
    }
}