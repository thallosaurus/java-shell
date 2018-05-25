import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.FileOutputStream;
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
import java.util.regex.Pattern;

public class Shell implements Runnable {
 String currentDir;
 BufferedReader inputBuffer = null;
 Process p;
 Properties options;
 boolean hasExecuted = false;
 String lastCmd = null;

 //folder for 'cd'
 HashMap < Integer, String > currentFolder = null;
 int folderCursor = -1;

 static boolean debugPropFlag = false;
 static boolean debug = false;
 static String block;
 static String name;
 //static boolean EXEC = true;
 char terminator;
 Pattern regex;
 boolean running = true;

 public Shell(String[] args, Properties prop, boolean isWindows) {
  options = prop;
  if (isWindows) {
   terminator = '\\';
   regex = Pattern.compile("\\\\");
  } else {
   terminator = '/';
   regex = Pattern.compile("/");
  }
  currentFolder = new HashMap < Integer, String > ();
    String[] userdir = System.getProperty("user.dir").split(regex.toString());
  for (int i = 0; i < userdir.length; i++) {
   currentFolder.put(i, userdir[i]);
  }
  folderCursor = currentFolder.size() - 1;
  init(args, prop);
 }

 public void shutdown() {
  running = false;
  System.exit(0);
 }

 public void init(String[] args, Properties prop) {
  if (Arrays.asList(args).contains("--debug") || Arrays.asList(args).contains("-d")) {
   debug = true;
   debug("Please report all bugs, thx");
  }

  //apply options
  block = prop.getProperty("BLOCK");
  debugPropFlag = Boolean.parseBoolean(prop.getProperty("DEBUG"));
  name = prop.getProperty("NAME");
 }

 @Override
 public void run() {
  //annoying...
  start();
 }
 public void start() {
/*  Thread t = new Thread();
    Runtime.getRuntime().addShutdownHook(new Thread() {
      public void run() {
        //Shell s = new Shell(null, null, '/');
        if(this.hasExecuted) {
			//if externalProcessIsRunning (forced by keystroke)
          s.p.destroy();
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
  int status;
  inputBuffer = new BufferedReader(new InputStreamReader(System.in));
  //System.out.println(name + " Shell");
  do {
   line = readLine(true, null);
   List < String > args = null;
   try {
    args = splitLine(line);
   } catch (java.lang.NullPointerException p) {
    System.exit(0);
   }
   status = execProgram(args);
   debug("Last line returned " + status);
  } while (running);
 }

 private String readLine(boolean useBlock, String msg) {
  String words = new String();
  try {
   if (useBlock) {
    System.out.print(outputBlock());
   } else {
    System.out.println(msg);
   }
   words = inputBuffer.readLine();
  } catch (IOException e) {
   e.printStackTrace();
  }
  return words;
 }

 private List < String > splitLine(String line) {
  List < String > cmd = Arrays.asList(line.split(" "));
  return cmd;
 }

 private int execProgram(List < String > command) {
  //debug("< " + String.join(" ", command));
  //shell builtin
  int exitcode = 0;
  switch (command.get(0)) {
   case ("hello"):
    System.out.println("Hello, World");
    break;
   case ("help"):
    System.out.println("Shell 0.1b \n");
    System.out.println("Built-ins: ");
    System.out.println("hello - print 'Hello World'");
    System.out.println("exit - exits Shell");
    System.out.println("help - prints this text");
    debug("getdir - prints current directory [DEBUG MODE]");
    System.out.println("info - print info text");
    debug("! - re-run last command [NON WORKING]");
    debug("live_in_coalmine");
    debug("test_errorcodes - test if errorcodes are working");
    debug("setproperty - sets properties");
    System.out.println("cd - changes current working directory");
    break;
   case ("exit"):
    shutdown();
    break;
   case("live_in_coalmine"):
    System.out.println("Set DEBUG in properties.conf to true. Set it to false, to disable Debug Mode.");
    System.out.println("...or run the shell with --debug");
    break;
   case "setproperty":
    if (command.size() == 3) {
     options.setProperty(command.get(1), command.get(2));
    } else {
     System.out.println("setpropery: <KEY> <PROP>");
    }
    break;
   case ("getdir"):
    //debug(currentFolder);
    System.out.println(currentFolder);
    if (isDebug()) {
     System.out.println("Enable Debug mode to run this command");
    }
    break;
   case "info":
    System.out.println("info goes here");
    break;
   case "test_errorcodes":
    debug("Enter code from 0 to 255");
    int lvl = 0;
    try {
     lvl = Integer.parseInt(readLine(false, "[0-255]"));
    } catch (java.lang.NumberFormatException n) {
     debug("Input was not a Number");
    }
    if (lvl < 256 && lvl > -1) {
     exitcode = lvl;
    } else {
     debug("please input a number from 0 to 256");
    }
    break;
   case ("!"):
    debug(lastCmd);
    try {
     externRun(splitLine(lastCmd));
    } catch (NullPointerException e) {
     System.out.println("You haven't run any Commands yet.");
     break;
    }
    break;
   case "meow":
    System.out.println("My cat told me she was hungry and I stroked her fur.\n\n");
    System.out.println("Needless to say, my hand is now covered by several scratches...\n\n");
    System.out.println("Ok, then.");
    break;

   case "TODO":
    //see your TODO list inside the program!
    //append your stuff so you'll never have to search again!
    debug("append your things to this list to see, what you want to do here.\n");
    debug("ASM Emulator");
    debug("Fix ^C (CRTL-C) Shutdownhook");
    debug("argument folder");
    debug("fix re-run command (!)");
   break;
   case "cd":
    StringBuilder completePath = new StringBuilder(); //strings
    //completePath.append(String.join("\\", currentFolder));
    //debug("Complete String: " + getCurrentDir());
    if (command.size() == 1 || command.get(1).equals("--help")) {
     System.out.println("cd: Help me."); //TODO Help
     break;
    } else if (command.get(1).equals("..")) {
     if (folderCursor > 0) {
      currentFolder.remove(folderCursor);
      folderCursor--;
      //System.out.println("cd: " + currentFolder.toString());
      //debug(folderCursor);
      //System.setProperty("user.dir", getCurrentDir());
     } else {
      System.out.println("Can't, already at root dir...");
      exitcode = 2;
     }
     break;
    } else {
     //check if directory exists
     File f = new File(getCurrentDir() + command.get(1));
     if (f.isDirectory()) {
      folderCursor++;
      currentFolder.put(folderCursor, command.get(1));
      //debug("cd: " + currentFolder.toString());
      //System.setProperty("user.dir", getCurrentDir());
      break;
     } else {
      System.out.println("folder not found");
      exitcode = 1;
      break;
     }
    }
   default:
    exitcode = externRun(command);
    break;
  }
  if (command.get(0) != "!") {
   lastCmd = String.join(" ", command);
  }
  return exitcode;

  /*
   * return true, if you want to continue execution of the shell
   * return false, if you don't want.
   *
   */
 }

 private int externRun(List < String > command) {
  try {
   ProcessBuilder pb = new ProcessBuilder(command);
   pb.directory(new File(getCurrentDir()));
   p = pb.inheritIO().start();
   hasExecuted = true; //sets the exec flag true for shutdown hook
   try {
    p.waitFor();
    hasExecuted = false; //releases hook
    return p.exitValue();
   } catch (InterruptedException e) {
    e.printStackTrace();
   }
  } catch (IOException e) {
   //File not Found
   System.out.println(command.get(0) + ": not found");
   //e.printStackTrace();
  }
  return 1;
 }
}
