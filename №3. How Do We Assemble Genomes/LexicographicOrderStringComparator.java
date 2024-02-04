import java.util.Comparator;

class LexicographicOrderStringComparator implements Comparator<String> {

    @Override
    public int compare(String str1, String str2) {
        int str1Len = str1.length();
        int str2Len = str2.length();
        int lenMin = Math.min(str1Len, str2Len);

        for (int i = 0; i < lenMin; ++i) {
            char str1Char = str1.charAt(i);
            char str2Char = str2.charAt(i);

            if (str1Char != str2Char) {
                return Character.compare(str1Char, str2Char);
            }
        }

        return Integer.compare(str1Len, str2Len);
    }
}
