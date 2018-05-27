class DebugTools {
 private boolean isDebug = false;
 private final String KEY = "DebugTools";
 public DebugTools(boolean debugFlag) {
  isDebug = debugFlag;
  debug(KEY, "Initialized DebugTools");
 }

 public void debug(String key, Object msg) {
  if (isDebug) {
   System.out.println("["+key+"]"+msg.toString());
  }
 }

 public void setDebugState(boolean newState) {
  isDebug = newState;
  if (newState == true) {
   System.out.println("H4xXoOor M0d3 enabl3d. 8)\n\nThings you discover here can be really buggy. (REALLY BUGGY.)\nPlease note that some functions can be altered in future versions of this program.\nUse with caution.\n");
  }else{
   System.out.println("I hope, you liked the Hacker Mode :)");
  }
 }

 public boolean getDebugState() {
  return isDebug;
 }
}
