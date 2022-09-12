package prj.item;

import org.joml.Vector2i;
import prj.ClientThread;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

public class ItemBar implements Serializable {

    private int playerPosX;
    private int playerPosY;
    private boolean isFacingRight;
    private int x;
    private int y;
    private int gap;
    private int cellWidth;
    private int cellHeight;
    private int maxCapacity;
    private int inHandItemIndex;
    private ArrayList<Item> itemBar;

    public ItemBar(int x, int y, int playerPosX, int playerPosY, int gap, int cellWidth, int cellHeight, int maxCapacity, int inHandItemIndex) {
        this.x = x;
        this.y = y;
        this.playerPosX = playerPosX;
        this.playerPosY = playerPosY;
        this.gap = gap;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        this.maxCapacity = maxCapacity;
        this.inHandItemIndex = inHandItemIndex;
        this.itemBar = new ArrayList<>();
        for(int i = 0; i < maxCapacity; i++){
            itemBar.add(null);
        }
    }

    public int getPlayerPosX() {
        return playerPosX;
    }

    public void setPlayerPosX(int playerPosX) {
        this.playerPosX = playerPosX;
    }

    public int getPlayerPosY() {
        return playerPosY;
    }

    public void setPlayerPosY(int playerPosY) {
        this.playerPosY = playerPosY;
    }

    public boolean isFacingRight() {
        return isFacingRight;
    }

    public void setFacingRight(boolean facingRight) {
        isFacingRight = facingRight;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getGap() {
        return gap;
    }

    public void setGap(int gap) {
        this.gap = gap;
    }

    public int getCellWidth() {
        return cellWidth;
    }

    public void setCellWidth(int cellWidth) {
        this.cellWidth = cellWidth;
    }

    public int getCellHeight() {
        return cellHeight;
    }

    public void setCellHeight(int cellHeight) {
        this.cellHeight = cellHeight;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public int getInHandItemIndex() {
        return inHandItemIndex;
    }

    public void setInHandItemIndex(int inHandItemIndex) {
        this.inHandItemIndex = inHandItemIndex;
    }

    public ArrayList<Item> getItemBar() {
        return itemBar;
    }

    public void setItemBar(ArrayList<Item> itemBar) {
        this.itemBar = itemBar;
    }

    public void addItem(int slot, Item item) {
        if(slot >= 0 && slot < maxCapacity) {
            itemBar.set(slot, item);
        }
    }

    public Item getHeldItem(){
        if(inHandItemIndex < itemBar.size())
            return itemBar.get(inHandItemIndex);
        return null;
    }

    public void removeItem() {

    }

    public void draw(Graphics2D g2d) {
        Item it;
        Vector2i off = ClientThread.instance.getCam().getOffset();
        for(int i = 0 ; i < maxCapacity ; i++) {
            it = null;
            if(i < itemBar.size())
                it = itemBar.get(i);
            if(i == inHandItemIndex) {
                g2d.setColor(Color.BLACK);
                g2d.drawRect(x - off.x + i * (cellWidth + gap) + 80, y - off.y + 50, cellWidth, cellHeight);
                g2d.setColor(Color.YELLOW);
                g2d.fillRect(x - off.x + i * (cellWidth + gap) + 1 + 80, y - off.y + 1 + 50, cellWidth - 1, cellHeight - 1);
                if(it != null) {
                    if (isFacingRight) {
                        g2d.drawImage(it.getItemImage(), playerPosX + 25, playerPosY - 25, null);
                    } else {
                        g2d.drawImage(it.getItemImage(), playerPosX - 25, playerPosY - 25, null);
                    }
                }
            }
            else {
                g2d.setColor(Color.GRAY);
                g2d.drawRect(x - off.x + i * (cellWidth + gap) + 80, y - off.y + 50, cellWidth, cellHeight);
                g2d.setColor(Color.WHITE);
                g2d.fillRect(x - off.x + i * (cellWidth + gap) + 1 + 80, y - off.y + 1 + 50, cellWidth - 1, cellHeight - 1);
            }
            if(it != null)
                g2d.drawImage(it.getItemIcon(), x - off.x + 5 + (40 + 5 + gap + 5) * i + 80, y - off.y + 5 + 50, null);
        }
    }
}
