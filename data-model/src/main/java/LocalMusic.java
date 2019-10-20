public class LocalMusic implements java.io.Serializable {
  private boolean shared;
  private String mp3Path;

  public boolean getShared() {
    return shared;
  }

  public void setShared(boolean shared) {
    this.shared = shared;
  }

  public String getMp3Path() {
    return mp3Path;
  }

  public void setMp3Path(String mp3Path) {
    this.mp3Path = mp3Path;
  }
}
