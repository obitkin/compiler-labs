import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Tests {

    @Test
    public void parseTest() {
        String sentence = "I have been waiting, eating and sleeping for 3 hours.";
        Grammar grammar = new Grammar(null, null);
        grammar.parse(sentence).forEach(s -> System.out.println("'" + s + "'"));
    }

    @Test
    public void grammarTest() throws IOException {
        String path = "src/main/resources/Relation.txt";
        System.out.println(Files.lines(Path.of(path)).count());
    }

}
