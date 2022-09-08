package com.company;

import javax.swing.*;
import java.awt.*;

public abstract class Item {

    private Image itemImage;
    private Image itemIcon;
    private int range;

    public Item(String pathToImage, String pathToIcon, int range) {
        loadImage(pathToImage, pathToIcon);
        this.range = range;
    }

    public Image getItemImage() {
        return itemImage;
    }

    public void setItemImage(Image itemImage) {
        this.itemImage = itemImage;
    }

    public Image getItemIcon() {
        return itemIcon;
    }

    public void setItemIcon(Image itemIcon) {
        this.itemIcon = itemIcon;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public void loadImage(String pathToImage, String pathToIcon) {
        ImageIcon ii = new ImageIcon(pathToImage);
        itemImage = ii.getImage();

        ii = new ImageIcon(pathToIcon);
        itemIcon = ii.getImage();
    }
}
