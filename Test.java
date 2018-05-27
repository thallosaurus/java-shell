import java.util.List;
import java.util.HashMap;

class Test extends BuiltIn {
 int exitcode = 0;

 DebugTools dbg = new DebugTools(true);

 String builtin_name = "test";
 String description = "a small help text will go here";

 public int start(List<String> command, HashMap<Integer, String> dir) {
  List<String> cut = command;
  ListIterator li = command.listIterator();
  //command.remove(0);
  //command.remove(1);

  String joined = String.join(" ", command);
  dbg.debug("Test", joined);
  System.out.println(joined);
  return exitcode;
 }
}
