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

//    protected Path getInputFilePath(final String path) {
//        try {
//            return Paths.get(getClass().getResource(path).toURI());
//        } catch (URISyntaxException e) {
//            throw new RuntimeException(e);
//        }
//    }
//    protected Scanner createInputFileScanner(final String inputFilePath) throws IOException {
//        return new Scanner(getInputFilePath(inputFilePath));
//    }

}
