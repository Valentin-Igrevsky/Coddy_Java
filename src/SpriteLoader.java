import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

class SpriteLoader {
    public static ArrayList<Color[][]> loadSprites(String dirPath) {
        ArrayList<Color[][]> sprites = new ArrayList<>();

        try (InputStream is = SpriteLoader.class.getResourceAsStream("/" + dirPath + "/list.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            String fileName;

            while ((fileName = reader.readLine()) != null) {
                try (InputStream imgStream = SpriteLoader.class.getResourceAsStream("/" + dirPath + "/" + fileName)) {

                    BufferedImage image = ImageIO.read(imgStream);
                    sprites.add(loadColors(image));
                }
            }

        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }

        return sprites;
    }

    public static Color[][] loadSprite(String path) {
        try (InputStream is = SpriteLoader.class.getResourceAsStream("/" + path)) {

            if (is == null) {
                throw new RuntimeException("Resource not found: " + path);
            }

            BufferedImage image = ImageIO.read(is);
            return loadColors(image);

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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