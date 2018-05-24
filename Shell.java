import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder;
import java.util.List;
import java.util.Arrays;
import java.io.File;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.Properties;
import java.nio.file.Files;
import java.lang.StringBuilder;
import java.util.HashMap;


public class Shell implements Runnable {
  String currentDir;
  BufferedReader inputBuffer = null;
  Process p;
  Properties options;
  boolean hasExecuted = false;
  String lastCmd = null;
  
  //folder for 'cd'
  HashMap<Integer, String> currentFolder = null;
  int folderCursor = -1;
  
  static boolean debugPropFlag = false;
  static boolean debug = false;
	
  static String block;
  static boolean EXEC = true;
  
  char terminator;
  
  public Shell(String[] args, Properties prop, char os) {
	  options = prop;
	  terminator = os;
	  currentFolder = new HashMap<Integer, String>();
	  
	  String[] userdir = System.getProperty("user.dir").split("\\\\");
	  for (int i = 0; i < userdir.length; i++) {
	    currentFolder.put(i, userdir[i]);
	  }
	  folderCursor = currentFolder.size()-1;
	
	  init(args, prop);
  }
  
  public void init(String[] args, Properties prop) {
	if (Arrays.asList(args).contains("--debug") || Arrays.asList(args).contains("-d")) {
      debug = true;
    }
	
    //set properties
    block = prop.getProperty("BLOCK");
    debugPropFlag = Boolean.parseBoolean(prop.getProperty("DEBUG"));
    EXEC = Boolean.parseBoolean(prop.getProperty("EXEC"));
  }
  
  @Override
  public void run() {
	  //annoying...
	  start();
  }
  public void start() {
	/* Thread t = new Thread();
    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        Shell s = new Shell(null, null, '/');
        if(s.hasExecuted) {
			//if externalProcessIsRunning (forced by keystroke)
          s.p.destroy();
        } else {
			Runtime.getRuntime().exit(0);
		}
      }
    }); */
	
    //calls shell;
    loop();
  }

  public void debug(Object cmd) {
    if (debugPropFlag || debug) {
      System.out.println("[DEBUG]" + cmd.toString());
    }
  }
  
  public String getCurrentDir() {
	  StringBuilder sb = new StringBuilder();
	  for (int i = 0; i < currentFolder.size(); i++) {
		  sb.append(currentFolder.get(i) + terminator);
	  }
	  return sb.toString();
  }
  
  public boolean isDebug() {
    return debugPropFlag || debug;
  }

  public String outputBlock() {
	    switch (block) {
		  case ".USERNAME":
			return System.getProperty("user.name") + ">";
		  case ".DIR":
		    return getCurrentDir() + ">";
		  default:
		    return block;
	    }
  }
  public void loop() {
    String line;
    boolean status;
    inputBuffer = new BufferedReader(new InputStreamReader(System.in));

    do {
      line = readLine();
	  List<String> args = null;
	  try {
		args = splitLine(line);
      } catch (java.lang.NullPointerException p) {
		  System.exit(0);
	  }
	  status = execProgram(args);
    } while (status);
  }

  private String readLine() {
    String words = new String();
    try {
      System.out.print(outputBlock()); 
      words = inputBuffer.readLine();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return words;
  }

  private List<String> splitLine(String line) {
    List<String> cmd = Arrays.asList(line.split(" "));
    return cmd;
  }

  private boolean execProgram(List<String> command) {
    debug("< " + String.join(" ", command));
    //shell builtin

    switch (command.get(0)) {
      case("hello"):
        System.out.println("Hello, World");
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
          //debug(currentFolder);
		  System.out.println(currentFolder);
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
	  case "cd":
	    StringBuilder completePath = new StringBuilder(); //strings
		//completePath.append(String.join("\\", currentFolder));
		debug("Complete String: " + getCurrentDir());
		if (command.size() == 1 || command.get(1).equals("--help")) {
			System.out.println("cd: Help me."); //TODO Help
			break;
		} else if (command.get(1).equals("..") && folderCursor != 0) {
				currentFolder.remove(folderCursor);
				folderCursor--;
				debug("cd: " + currentFolder.toString());
				debug(folderCursor);
				break;
		} else {
			//check if directory exists
			File f = new File(getCurrentDir() + command.get(1));
			if (f.isDirectory()) {
				folderCursor++;
				currentFolder.put(folderCursor, command.get(1));
				debug("cd: " + currentFolder.toString());
				break;
			} else {
				debug("folder not found");
				break;
			}
		}	
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
	  hasExecuted = true; //sets the exec flag true for shutdown hook
       try {
        p.waitFor();
	    debug("Exit Value: " + p.exitValue());
		hasExecuted = false; //releases hook
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