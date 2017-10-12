/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nick.flashtranslator;

import com.flagstone.transform.Movie;
import com.flagstone.transform.MovieTag;
import com.flagstone.transform.text.DefineTextField;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;

/**
 *
 * @author nick
 */
public final class FlashItem {

    private String originalFile;
    private List<Translatable> translations;
    private transient Movie movie;
    private transient int id;

    public FlashItem() {
    }

    public FlashItem(int id) {
        this.id = id;
    }

    public FlashItem(File originalFile, int id) throws DataFormatException, IOException {
        this.id = id;
        loadFile(originalFile);
    }

    public void loadOriginsOnly(File file) throws DataFormatException, IOException {
        movie = new Movie();
        movie.decodeFromFile(file);
        for (MovieTag movieTag : movie.getObjects()) {
            if (movieTag instanceof DefineTextField) {
                DefineTextField dtf = ((DefineTextField) movieTag);
                for (Translatable translatable : translations) {
                    if (translatable.getId() == dtf.getIdentifier()) {
                        translatable.setDefineTextField(dtf);
                    }
                }
            }
        }
    }

    public void loadFile(File file) throws DataFormatException, IOException {
        movie = new Movie();
        movie.decodeFromFile(file);

        translations = new LinkedList<Translatable>();
        for (MovieTag movieTag : movie.getObjects()) {
            if (movieTag instanceof DefineTextField) {
                Translatable tra = new Translatable((DefineTextField) movieTag);
                translations.add(tra);
            }
        }
        originalFile = file.getAbsolutePath();
        //jList1.setModel(model);
        //jList1.setSelectedIndex(0);

    }

    public ListModel<Translatable> makeModel() {
        DefaultListModel<Translatable> model = new DefaultListModel<Translatable>();

        for (Translatable translatable : translations) {
            model.addElement(translatable);
        }

        return model;
    }

    public void setTranslations(List<Translatable> translation) {
        this.translations = translation;
    }

    public List<Translatable> getTranslations() {
        return translations;
    }

    public String getOriginalFile() {
        return originalFile;
    }

    public void setOriginalFile(String originalFile) {
        this.originalFile = originalFile;
        try {
            loadOriginsOnly(new File(originalFile));
        } catch (DataFormatException ex) {
            Logger.getLogger(FlashItem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FlashItem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getWordCount() {
        int count = 0;
        for (Translatable translatable : translations) {
            count += countWords(translatable.getOriginalText());
        }
        return count;
    }

    public void saveFlash(File newFile) throws IOException, DataFormatException {
        if (movie != null) {
//            if (lastSelected != null) {
//                lastSelected.setTranslatedText(jTextArea2.getText());
//            }
            for (Translatable translatable : translations) {
                translatable.applyTranslation();
            }
//            int answer = fileChooser.showSaveDialog(this);
//            if (answer == JFileChooser.APPROVE_OPTION) {

            movie.encodeToFile(newFile);

//            }
        }
    }

    private static int countWords(String s) {
        int counter = 0;
        boolean word = false;
        int endOfLine = s.length() - 1;

        for (int i = 0; i < s.length(); i++) {
            if (Character.isLetter(s.charAt(i)) == true && i != endOfLine) {
                word = true;
            } else if (Character.isLetter(s.charAt(i)) == false && word == true) {
                counter++;
                word = false;
            } else if (Character.isLetter(s.charAt(i)) && i == endOfLine) {
                counter++;
            }
        }
        return counter;
    }

    @Override
    public String toString() {
        return new File(originalFile).getName();
    }

    public void exportTo(File folder) throws IOException, DataFormatException {
        for (Translatable translatable : translations) {
            translatable.applyTranslation();
        }
        movie.encodeToFile(new File(folder.getAbsolutePath()+File.separator+toString()));
    }

}
