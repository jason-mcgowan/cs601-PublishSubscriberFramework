package cs601.project2.FilterApp;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Methods for building a database from two files for Project1.
 *
 * @author Jason McGowan
 */
public final class FileJsonParser {

  // Prevent instantiation
  private FileJsonParser() {
  }

  public static <T> Collection<T> parseFile(String fileName, Class<T> classType) {

    Path path = Paths.get(fileName);

    Gson gson = new GsonBuilder().setLenient().create();

    // Lazily reads each line, parses to object, adds to database if not null
    try (Stream<String> lines = Files.lines(path, StandardCharsets.ISO_8859_1)) {
      return lines.map(line -> parse(gson, line, classType)).collect(Collectors.toList());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return new ArrayList<>();
  }

  // This could have been folded into the stream intermediate map call. However, the default
  // JsonSyntaxException error message doesn't provide much information. This way, the user can know
  // which records are causing syntax errors.
  private static <T> T parse(Gson gson, String json, Class<T> classType) {
    try {
      return gson.fromJson(json, classType);
    } catch (JsonSyntaxException e) {
      System.out.println(
          "\nWarning! JSON syntax violation, omitting input: "
              + json
              + "\n"
              + e.getLocalizedMessage());
      return null;
    }
  }
}
