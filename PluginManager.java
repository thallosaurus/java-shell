//import oracle.adfmf.json.*;
import java.util.HashMap;
import java.util.List;
class PluginManager {
 HashMap<String, BuiltIn> activePlugins = null;
 DebugTools dbg = new DebugTools(true);
 public final String KEY = "PluginManager";
 public PluginManager(boolean debug) {
  activePlugins = new HashMap<String, BuiltIn>();
  //dbg.setDebugState(debug);
  scan();
 }

 public void scan() {
/*  FileInputStream is = new FileInputStream("plugins.json");
  JsonReader r = is Json.createReader(is);
  JsonObject obj =  r.readObject();
  JsonArray results = obj.getJsonArray("plugins"); */
  //DEBUG CODE:
  activePlugins.put("test", new Test()); //adding our test plugin
  activePlugins.put("helloworld", new HelloWorld()); //adding hello world
  //TODO clear this mess up with a nice and smooth loop
  //TODO or JSON Parsing, we'll see...
 }
 // 0        1        2         3          4
 //builtin command <argument><argument2><argument3>
 public int exec(List<String> command, HashMap<Integer, String> dir) {
  BuiltIn builtin = activePlugins.get(command.get(1)); //change this to command.get(0) in production (when plans go like I want them lol)
  int exitcode = 0;
  if (activePlugins.get(command.get(1)) == null) {
   dbg.debug("PluginManager/exec()", "Can't find plugin " + command.get(1)); //this too plz
   return 1;
  } else {
   dbg.debug(KEY, command);
   exitcode = builtin.start(command, dir);
  }
  return exitcode;
 }
}
