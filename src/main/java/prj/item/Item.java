package prj.item;

import prj.ImgLoader;

import java.awt.*;
import java.io.Serializable;

public abstract class Item implements Serializable {

    private String itemImage;
    private String itemIcon;
    private int range;

    public Item(String pathToImage, String pathToIcon, int range) {
        this.itemImage = pathToImage;
        this.itemIcon = pathToIcon;
        this.range = range;
    }

    public Image getItemImage() {
        return ImgLoader.get(itemImage);
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }

    public Image getItemIcon() {
        return ImgLoader.get(itemIcon);
    }

    public void setItemIcon(String itemIcon) {
        this.itemIcon = itemIcon;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public String getItemName(){
        return itemImage;
    }
}
