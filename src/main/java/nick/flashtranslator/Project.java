/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nick.flashtranslator;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.DataFormatException;

/**
 *
 * @author nick
 */
public final class Project {

    private int itemIdLoaded;
    private List<FlashItem> items;

    
    public Project(List<FlashItem> items) {
        this.items = items;
    }

    public Project() {
        itemIdLoaded = 0;
        items = new LinkedList<FlashItem>();
    }

    public void addItem(FlashItem fm) {
        items.add(fm);
    }


    public void setItemIdLoaded(int itemIdLoaded) {
        this.itemIdLoaded = itemIdLoaded;
    }

    public void setItems(List<FlashItem> items) {
        System.out.println("setting items");
        this.items = items;
    }

    public int getItemIdLoaded() {
        return itemIdLoaded;
    }

    public List<FlashItem> getItems() {
        return items;
    }

    public void addFile(File file) throws DataFormatException, IOException {

        if (file.exists() && file.getName().toLowerCase().endsWith("swf")) {
            FlashItem flash = new FlashItem(file, items.size());
            items.add(flash);
        }
    }

    public FlashItem getItem(int id) {
        return items.get(id);
    }

    public FlashItem getCurrentItem() {
        return getItem(itemIdLoaded);
    }

    public boolean changed() {
        for (FlashItem flashItem : items) {
            for (Translatable translatable : flashItem.getTranslations()) {
                if(translatable.hasUndoManager() && translatable.getUndoManager().canUndo()){
                    return true;
                }
            }
        }
        return false;
    }

    public void setCurrent(FlashItem editingFlash) {
        for (int i = 0; i < items.size(); i++) {
            FlashItem flashItem = items.get(i);
            if (flashItem.getOriginalFile().equals(editingFlash.getOriginalFile())) {
                itemIdLoaded = i;
            }
        }
    }

    public void prepare() throws DataFormatException, IOException {
        for (FlashItem flashItem : items) {
            flashItem.loadOriginsOnly(new File(flashItem.getOriginalFile()));
        }
    }
    
}
