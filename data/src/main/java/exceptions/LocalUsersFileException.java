package exceptions;

public class LocalUsersFileException extends Exception {
  public LocalUsersFileException(String s) {
    super(s);
  }

  public LocalUsersFileException(String s, Throwable throwable) {
    super(s, throwable);
  }
}
