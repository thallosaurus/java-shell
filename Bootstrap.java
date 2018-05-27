import java.io.IOException;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.util.Properties;
import java.io.FileNotFoundException;
import java.util.Arrays;

class Bootstrap {
  private static String os = System.getProperty("os.name").toLowerCase();
  private static boolean debugFlag = false;
  private static boolean isWindows() {
		if (os.indexOf("win") >= 0) {
			return true;
		} else {
			return false;
		}
  }

  public static void main(String[] args) {
    Properties p = new Properties();
    try {
      BufferedInputStream stream = new BufferedInputStream(new FileInputStream("properties.conf"));
      try {
        p.load(stream);
        stream.close();
      } catch (IOException i) {
        i.printStackTrace();
      }
    } catch (FileNotFoundException f) {
      f.printStackTrace();
    }

      if(Arrays.asList(args).contains("--debug") || Arrays.asList(args).contains("-d")) {
         debugFlag = true;
        }

      //prepare plugin support
        PluginManager plugin = new PluginManager(true);
      //use the space to configure the Manager







        System.out.println(p.getProperty("NAME") + " Shell");
	Shell s = new Shell(args, p, isWindows(), debugFlag, plugin);
	Thread shell = new Thread(s);
    shell.run();
  }
}
