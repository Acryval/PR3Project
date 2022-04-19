package com.company;

import java.awt.*;
import java.util.ArrayList;

public class ItemBar {

    int x;
    int y;
    int gap;
    int cellWidth;
    int cellHeight;
    int maxCapacity;
    int inHandItemIndex;
    ArrayList<Item> itemBar = new ArrayList<>();

    public ItemBar(int x, int y, int gap, int cellWidth, int cellHeight, int maxCapacity, int inHandItemIndex) {
        this.x = x;
        this.y = y;
        this.gap = gap;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        this.maxCapacity = maxCapacity;
        this.inHandItemIndex = inHandItemIndex;
    }

    public void addItem(Item item) {
        if(itemBar.size() != maxCapacity) {
            itemBar.add(item);
        }
    }

    public void removeItem() {

    }

    public void draw(Graphics2D g2d) {
        for(int i = 0 ; i < maxCapacity ; i++) {
            if(i == inHandItemIndex) {
                g2d.setColor(Color.BLACK);
                g2d.drawRect(x + i * (cellWidth + gap), y, cellWidth, cellHeight);
                g2d.setColor(Color.YELLOW);
                g2d.fillRect(x + i * (cellWidth + gap) + 1, y + 1, cellWidth - 1, cellHeight - 1);
            }
            else {
                g2d.setColor(Color.GRAY);
                g2d.drawRect(x + i * (cellWidth + gap), y, cellWidth, cellHeight);
                g2d.setColor(Color.WHITE);
                g2d.fillRect(x + i * (cellWidth + gap) + 1, y + 1, cellWidth - 1, cellHeight - 1);
            }
        }
    }
}
