import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder;
import java.util.List;
import java.util.Arrays;
import java.io.File;
import java.util.Properties;

public class Shell {
  String currentDir;
  BufferedReader inputBuffer = null;
  Process p;
  Properties options;
  boolean hasExecuted = false;
  String lastCmd = null;
  static boolean debug = false;
  static String block;
  static boolean debugPropFlag = false;
  static boolean EXEC = true;
  public static void run(String[] args, Properties prop) {
    if (Arrays.asList(args).contains("--debug") || Arrays.asList(args).contains("-d")) {
      debug = true;
    }

    //set properties
    String block2 = prop.getProperty("BLOCK");
    if (block2.equals(".USERNAME")) {
      block = System.getProperty("user.name");
    } else {
      block = block2;
    }
    debugPropFlag = Boolean.parseBoolean(prop.getProperty("DEBUG"));
    EXEC = Boolean.parseBoolean(prop.getProperty("EXEC"));
    Thread t = new Thread();
    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        Shell s = new Shell();
        if(s.hasExecuted) {
          s.p.destroy();
        }
      }
    });

    Shell obj = new Shell();
    obj.loop();
  }

  public void debug(String cmd) {
    if (debugPropFlag || debug) {
      System.out.println("[DEBUG]" + cmd.toString());
    }
  }

  public boolean isDebug() {
    return debugPropFlag || debug;
  }

  public void loop() {
    String line;
    currentDir = System.getProperty("user.dir");
    boolean status;
    inputBuffer = new BufferedReader(new InputStreamReader(System.in));

    do {
      line = readLine();
      List<String> args = splitLine(line);
      status = execProgram(args);
    } while (status);
  }

  private String readLine() {
    String words = "";
    try { //TODO implement custom offset message (via JavaProperties)
      System.out.print(block + ">");
      words = inputBuffer.readLine();
      //return words;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return words;
  }

  private List<String> splitLine(String line) {
    List<String> cmd = Arrays.asList(line.split("\\s"));
    return cmd;
  }

  private boolean execProgram(List<String> command) {
    debug("< " + String.join(" ", command));
    //shell builtin

    switch (command.get(0)) {
      case("hello"):
        System.out.println("Hello, World!");
        break;
      case("help"):
        System.out.println("Shell 0.1b ");
        System.out.println("Built-ins: ");
        System.out.println("hello - print 'Hello World'");
	System.out.println("exit - exits Shell");
        System.out.println("help - prints this text");
	System.out.println("getdir - prints current directory [DEBUG MODE]");
	System.out.println("! - re-run last command [NON WORKING]");
	break;
      case("exit"):
        System.out.println("Godbye!");
        System.exit(0);
      case("getdir"):
          debug(currentDir);
          if(isDebug()) {
            System.out.println("Enable Debug mode to run this command");
          }
        break;
      case("!"):
	System.out.println(lastCmd);
	try {
	  externRun(splitLine(lastCmd));
        } catch (NullPointerException e) {
          System.out.println("You haven't run any Commands yet.");
	  break;
        }
        break;
      case "meow":
        System.out.println("Meow.");
	break;
      default:
        externRun(command);
        break;
    }
    return true;

    /*
     * return true, if you want to continue execution of the shell
     * return false, if you don't want.
     *
    */
  }

  private boolean externRun(List<String> command) {
    try {
      p = new ProcessBuilder(command).inheritIO().start();
       try {
        p.waitFor();
	debug("Exit Value: " + p.exitValue());
        lastCmd = String.join(" ", command);
        return true;
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      } catch (IOException e) {
        //File not Found
	System.out.println(command.get(0) + ": not found");
        //e.printStackTrace();
    }
    return true;
  }
}
