package prj;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ImgLoader {
    private static final Map<String, Image> images = new HashMap<>();
    public static final String IMG_PREFIX = "images/";
    public static final Image noImage = new ImageIcon(ClassLoader.getSystemResource("images/none.png")).getImage();

    public static void loadImage(String name, String filepath){
        URL location = ClassLoader.getSystemResource(IMG_PREFIX + filepath);
        if(location != null) {
            images.put(name, new ImageIcon(location).getImage());
        }
    }

    public static Image get(String name){
        Image i = images.get(name);
        if(i == null){
            loadImage(name, name + ".png");
            i = images.get(name);
            if(i == null){
                i = noImage;
            }
        }
        return i;
    }
}
