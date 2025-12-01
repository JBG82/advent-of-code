package de.geburtig.advent.util;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Helper-Klasse für den Bezug von Input-Informationen zu den Aufgaben aus den Resources
 */
public class InputResolver {

    public static List<String> fetchLinesFromInputFile(final String path, final Class<?> ref) {
        try  {
            return Files.readAllLines(Paths.get(ref.getResource(path).toURI()));
        } catch (Exception e) {
            throw new RuntimeException("Cannot fetch lines from input file: " + e.getMessage(), e);
        }
    }

    public static char[][] fetchInputAsCharArray(final String path, final Class<?> ref) {
        List<String> lines = fetchLinesFromInputFile(path, ref);
        char[][] result = new char[lines.getFirst().length()][lines.size()];
        for (int y = 0; y < lines.size(); y++) {
            char[] chars = lines.get(y).toCharArray();
            for (int x = 0; x < chars.length; x++) {
                result[x][y] = chars[x];
            }
        }
        return result;
    }

    public static int[][] fetchInputAsIntArray(final String path, final Class<?> ref) {
        List<String> lines = fetchLinesFromInputFile(path, ref);
        int[][] result = new int[lines.getFirst().length()][lines.size()];
        for (int y = 0; y < lines.size(); y++) {
            char[] chars = lines.get(y).toCharArray();
            for (int x = 0; x < chars.length; x++) {
                result[x][y] = chars[x] - 48;
            }
        }
        return result;
    }


}
