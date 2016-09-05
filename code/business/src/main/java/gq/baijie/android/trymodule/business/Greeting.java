package gq.baijie.android.trymodule.business;

public class Greeting {

  private static final Greeting INSTANCE = new Greeting();

  private static final String template = "Hello, %s!";

  public static Greeting getInstance() {
    return INSTANCE;
  }

  public String say(String name) {
    return String.format(template, name);
  }

}
