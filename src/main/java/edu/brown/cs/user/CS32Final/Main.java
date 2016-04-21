package edu.brown.cs.user.CS32Final;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import freemarker.template.Configuration;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;


/**
 * The main class. Handles args, commandline input, and GUI
 *
 * @author Adam DeHovitz
 */
public final class Main {
    private String[] args;
    private OptionSet options;
    private OptionSpec<Integer> led;
    private final Gson gson = new Gson();

    private Main(String[] args) {
        this.args = args;
    }

    /**
     * Takes in command line input and performs corresponding actions.
     * Also establishes a WordData class with corresponding trie
     * Additionally, it host all the work behind the GUI.
     *
     * @param args contains file location for stars and also specifications like
     *             --gui
     */
    public static void main(String[] args) {
        new Main(args).run();
    }

    private static FreeMarkerEngine createEngine() {
        Configuration config = new Configuration();
        File templates = new File("src/main/resources/spark/template/freemarker");
        try {
            config.setDirectoryForTemplateLoading(templates);
        } catch (IOException ioe) {
            System.out.printf(
                    "ERROR: Unable use %s for template loading.\n", templates);
            System.exit(1);
        }
        return new FreeMarkerEngine(config);
    }

    /**
     * Prepares for autocorrect by reading options and establishing a trie.
     */
    private void run() {
        OptionParser parser = new OptionParser();
        parser.accepts("gui");
        options = parser.parse(args);


    /*
    //parser.accepts("led").withRequiredArg().ofType( Integer.class );
    led = parser.accepts("led").withRequiredArg().ofType(Integer.class);
    parser.accepts("prefix");
    parser.accepts("whitespace");
    parser.accepts("smart");
    OptionSpec<File> fileSpec = parser.nonOptions().ofType(File.class);

    List<File> dicts = options.valuesOf(fileSpec);
    if (dicts.size() < 1) {
      System.out.println("ERROR: Please specify a file");
      System.exit(1);
    }
    if (options.has("led")) {
      if (led.value(options) < 0) {
        System.out.println("ERROR: levenshtein distance less than 0");
        System.exit(1);
      }
    }*/


        if (options.has("gui")) {
            runSparkServer();
        } else {
            try {
                InputStreamReader isr = new InputStreamReader(System.in, "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                String line = "";
                System.out.println("Ready");
                while ((line = br.readLine()) != null && line.length() > 0) {

                }
                isr.close();
            } catch (IOException ioe) {
                System.out.println("ERROR: input unreadable");
                System.exit(1);
            }
        }
    }


    /**
     * Runs the spark server.
     */
    private void runSparkServer() {
        Spark.externalStaticFileLocation("src/main/resources/static");
        Spark.exception(Exception.class, new ExceptionPrinter());

        FreeMarkerEngine freeMarker = createEngine();

        // Setup Spark Routes
        Spark.get("/", new FrontHandler(), freeMarker);
    }

    /**
     * Exception Printer for errors I think. I didn't write it.
     */
    private static class ExceptionPrinter implements ExceptionHandler {
        @Override
        public void handle(Exception e, Request req, Response res) {
            res.status(500);
            StringWriter stacktrace = new StringWriter();
            try (PrintWriter pw = new PrintWriter(stacktrace)) {
                pw.println("<pre>");
                e.printStackTrace(pw);
                pw.println("</pre>");
            }
            res.body(stacktrace.toString());
        }
    }

    /**
     * Handles the main page.
     */
    private class FrontHandler implements TemplateViewRoute {
        @Override
        public ModelAndView handle(Request req, Response res) {
            Map<String, Object> variables =
                    ImmutableMap.of("title", "Type Away!", "content", "");
            return new ModelAndView(variables, "main.ftl");
        }
    }
}
