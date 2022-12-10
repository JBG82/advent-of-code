package de.geburtig.advent.base;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public abstract class DayBase {

    private List<String> lines = null;

    public abstract String resolvePuzzle1() throws Exception;
    public abstract String resolvePuzzle2() throws Exception;

    protected List<String> getLinesFromInputFile(final String path) {
        if (lines == null) {
            lines = fetchLinesFromInputFile(path);
        }
        return lines;
    }

    protected Path getInputFilePath(final String path) {
        try {
            return Paths.get(getClass().getResource(path).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    private List<String> fetchLinesFromInputFile(final String path) {
        try  {
            return Files.readAllLines(Paths.get(getClass().getResource(path).toURI()));
        } catch (Exception e) {
            throw new RuntimeException("Cannot fetch lines from input file: " + e.getMessage(), e);
        }
    }

    protected Scanner createInputFileScanner(final String inputFilePath) throws IOException {
        return new Scanner(getInputFilePath(inputFilePath));
    }
}
