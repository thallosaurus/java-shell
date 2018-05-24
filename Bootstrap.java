import java.io.IOException;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.util.Properties;
import java.io.FileNotFoundException;

class Bootstrap {
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
    Shell.run(args, p);
  }
}
