package edu.brown.cs.user.CS32Final;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import edu.brown.cs.user.CS32Final.GUI.GUI;
import edu.brown.cs.user.CS32Final.SQL.SqliteDatabase;
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
        GUI gui = new GUI();
    }
}
