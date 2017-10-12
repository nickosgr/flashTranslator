/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nick.flashtranslator;

import com.flagstone.transform.text.DefineTextField;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;

/**
 *
 * @author nick
 */
public final class Translatable implements UndoableEditListener{

    private int id;
    private transient String originalText;
    private String translatedText;
    private transient DefineTextField tf;
    private transient UndoManager undoManager;

    public Translatable() {
    }

    public Translatable(DefineTextField textF) {
        this.id = textF.getIdentifier();
        this.originalText = textF.getInitialText();
        this.translatedText = originalText;
        tf = textF;
    }

    @Override
    public String toString() {
        if (originalText.length() > 50) {
            return originalText.substring(0, 50) + "...";
        } else {
            return originalText;
        }
    }

    public int getId() {
        return id;
    }

    public String getOriginalText() {
        return originalText;
    }

    public String getTranslatedText() {
        return translatedText;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void applyTranslation() {
        tf.setInitialText(translatedText);
    }

    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }

    public void setDefineTextField(DefineTextField tf) {
        this.tf = tf;
        originalText = tf.getInitialText();
    }

    public UndoManager getUndoManager() {
        if (undoManager == null) {
            undoManager = new UndoManager();
        }
        return undoManager;
    }
    public boolean hasUndoManager(){
        return undoManager!= null;
    }

    public void undoableEditHappened(UndoableEditEvent e) {
        getUndoManager().addEdit(e.getEdit());
    }

}
