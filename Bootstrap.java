import java.io.IOException;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.util.Properties;
import java.io.FileNotFoundException;

class Bootstrap {
  private static String os = System.getProperty("os.name").toLowerCase();

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
        System.out.println(p.getProperty("NAME") + " Shell");
	Shell s = new Shell(args, p, isWindows());
	Thread shell = new Thread(s);
    shell.run();
  }
}
