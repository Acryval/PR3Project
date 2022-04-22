package com.company;

import javax.swing.*;
import java.awt.*;

public class Item {

    private String itemType;
    private Image itemImage;
    private Image itemIcon;

    public Item(String itemType, String pathToImage, String pathToIcon) {
        this.itemType = itemType;
        loadImage(pathToImage, pathToIcon);
    }

    /* Settery i gettery */
    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
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
    /* ****** */

    public void loadImage(String pathToImage, String pathToIcon) {
        ImageIcon ii = new ImageIcon(pathToImage);
        itemImage = ii.getImage();

        ii = new ImageIcon(pathToIcon);
        itemIcon = ii.getImage();
    }
}
