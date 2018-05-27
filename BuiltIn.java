import java.util.List;
import java.util.HashMap;

class BuiltIn {
 int exitcode = 0;
 String builtin_name = "test";
 String description = "a small help text will go here";

 public int start(List<String> command, HashMap<Integer, String> dir) {
  //do nothing
  return exitcode;
 }
}
