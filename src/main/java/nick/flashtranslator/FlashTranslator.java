/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nick.flashtranslator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.UIManager;

/**
 * starter class
 * @author nick
 */
public class FlashTranslator {

    public static Properties settings = new Properties();

    public static void main(String[] args) throws IOException {
        bundlesDebug();

        try {
            ResourceBundle bundle = ResourceBundle.getBundle("Bundle", Locale.getDefault(), FlashTranslator.class.getClassLoader());
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadSettings();
        getLanguageChoices();

        String lang = settings.getProperty("lang", "auto");
        if (!lang.equals("auto")) {
            setLocale(lang);
        }
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            TranslationFrame gui = new TranslationFrame();
            gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            gui.setVisible(true);

        } catch (Exception ex) {
            Logger.getLogger(FlashTranslator.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void bundlesDebug() {
        try {
            URL url = ClassLoader.getSystemClassLoader().getResource(".");
            System.out.println(url);
            for (File listFile : new File(url.getFile()).listFiles()) {
                //System.out.println(listFile.getName());
                if (listFile.getName().startsWith("Bundle")) {
                    String bundleFile = listFile.getName().substring(0, listFile.getName().lastIndexOf("."));
                    System.out.println(bundleFile);
                    String[] bundle = bundleFile.split("_");
                    if (bundle.length == 3) {
                        Locale alocale = new Locale(bundle[1], bundle[2]);
                        System.out.println(capitalize(alocale.getDisplayLanguage()));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
        Locale alocale = new Locale("es", "ES");
        System.out.println("Tests:");
        System.out.println(alocale.getLanguage());
        System.out.println(alocale.getDisplayLanguage());
        System.out.println(alocale.getDisplayLanguage(alocale));
        System.out.println(alocale.getDisplayCountry());
        //System.exit(0);
    }

    private static String capitalize(String string) {
        //capitalise lang first letter?
        string = string.substring(0, 1).toUpperCase() + string.substring(1);
        return string;
    }

    public static List<String> getLanguageChoices() {
        List<String> list = new LinkedList<>();
        for (Object keyO : settings.keySet()) {
            String key = (String) keyO;
            System.out.println("key: " + key);
            if (key.startsWith("lang_")) {
                list.add(key.substring(key.indexOf("_") + 1));
            }
        }
        return list;
    }

    private static void loadSettings() {
        FileInputStream fIn = null;
        try {
            fIn = new FileInputStream(new File("settings"));
            settings.load(fIn);
            fIn.close();
        } catch (FileNotFoundException ex) {
            //Logger.getLogger(FlashTranslator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            //Logger.getLogger(FlashTranslator.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (fIn != null) {
                    fIn.close();
                }
            } catch (IOException ex) {
                //Logger.getLogger(FlashTranslator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static void setLocale(String lang) {
        String[] L = lang.split("_");
        try {
            Locale.setDefault(new Locale(L[0], L[1]));
        } catch (Exception e) {
            Logger.getLogger(FlashTranslator.class.getName()).log(Level.WARNING,
                    "Language setting incorrect: " + lang + " ... Applying default.");
        }
    }
}
