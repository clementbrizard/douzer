import java.util.HashSet;
import java.util.UUID;

public class LocalMusic implements java.io.Serializable {
  private Boolean shared;
  private String mp3Path;

  public Boolean getShared() {
    return shared;
  }

  public void setShared(Boolean shared) {
    this.shared = shared;
  }

  public String getMp3Path() {
    return mp3Path;
  }

  public void setMp3Path(String mp3Path) {
    this.mp3Path = mp3Path;
  }
}
