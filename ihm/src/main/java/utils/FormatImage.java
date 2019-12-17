package utils;

import static impl.org.controlsfx.tools.MathTools.min;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.util.Hashtable;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;

public class FormatImage {
  /**
   * this function permit to convert a RenderedImage into a BufferedImage
   * I took this function here http://www.jguru.com/faq/view.jsp?EID=114602.
   * @param img a RenderedImage
   * @return a BufferedImage
   */
  public static BufferedImage convertRenderedImage(RenderedImage img) {
    if (img instanceof BufferedImage) {
      return (BufferedImage)img;
    }
    ColorModel cm = img.getColorModel();
    int width = img.getWidth();
    int height = img.getHeight();
    WritableRaster raster = cm.createCompatibleWritableRaster(width, height);
    boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
    Hashtable properties = new Hashtable();
    String[] keys = img.getPropertyNames();
    if (keys != null) {
      for (int i = 0; i < keys.length; i++) {
        properties.put(keys[i], img.getProperty(keys[i]));
      }
    }
    BufferedImage result = new BufferedImage(cm, raster, isAlphaPremultiplied, properties);
    img.copyData(raster);
    return result;
  }

  /**
   * this function show the avatar image.
   *
   * @param imgRend an image of type RenderedImage
   * @param imgAvatar the ImageView FXML field where to show the image
   */
  public static void cropAvatar(RenderedImage imgRend, ImageView imgAvatar) {
    // thanks to SwingFXUtils we can convert a bufferedImage into an Image
    Image img = SwingFXUtils.toFXImage(FormatImage.convertRenderedImage(imgRend),null);
    imgAvatar.setImage(img);

    // crop of the image to make square
    double minSide = min(img.getHeight(), img.getWidth());
    PixelReader imgReader = img.getPixelReader();

    // if the image isn't square, we crop it
    if (img.getHeight() != img.getWidth()) {
      if (img.getHeight() < img.getWidth()) {
        // define crop in image coordinates:
        double x = (img.getWidth() - img.getHeight()) / 2;
        double y = 0;
        // define the square for cropping
        Rectangle2D croppedPortion = new Rectangle2D(x, y, minSide, minSide);
        imgAvatar.setViewport(croppedPortion);
      } else if (img.getHeight() > img.getWidth()) {
        // define crop in image coordinates:
        double x = 0;
        double y = (img.getHeight() - img.getWidth()) / 2;
        // define the square for cropping
        Rectangle2D croppedPortion = new Rectangle2D(x, y, minSide, minSide);
        imgAvatar.setViewport(croppedPortion);
      }
    }
  }
}
