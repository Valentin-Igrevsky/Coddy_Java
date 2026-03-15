import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

class SpriteLoader {
    public static ArrayList<Color[][]> loadSprites(String dir_path) {
        ArrayList<Color[][]> sprites = new ArrayList<>();

        for(int i = 1; i <=4; i++) {
            try (InputStream is = SpriteLoader.class.getResourceAsStream(dir_path + "/" + i + ".png")) {
                BufferedImage image = ImageIO.read(is);
                sprites.add(loadColors(image));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sprites;
    }

    public static Color[][] loadColors(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        Color[][] colors = new Color[height][width];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int argb = image.getRGB(x, y);
                Color c = new Color(argb, true);

                if (c.getAlpha() == 0) {
                    colors[y][x] = null;
                } else {
                    colors[y][x] = new Color(c.getRed(), c.getGreen(), c.getBlue());
                }
            }
        }
        return colors;
    }
}