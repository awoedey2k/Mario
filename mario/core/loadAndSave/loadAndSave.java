/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mario.core.loadAndSave;

import java.awt.FileDialog;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import mario.Game;
import mario.core.MapObject;

/**
 *
 * @author Danny
 */
public class loadAndSave
{
    public static void save(Game game)
    {
        FileDialog filedialog = new FileDialog(new JFrame(), "Save the Mario", FileDialog.SAVE);
        filedialog.setVisible(true);

        String fileLocation = filedialog.getDirectory();
        if (filedialog.getFile() != null)
        {
            try
            {
                FileOutputStream fos = new FileOutputStream(fileLocation + filedialog.getFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);

                SaveObject saveObject = new SaveObject(game);
                oos.writeObject(saveObject);
                oos.close();
            } catch (IOException ex)
            {
                System.out.println(ex);
                JOptionPane.showMessageDialog(null, "Er is een fout opgetreden \n Voor het bevoegde gezag:" + ex, "Het lezen is mislukt", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    public static Game load()
    {
        FileDialog filedialog = new FileDialog(new JFrame(), "Load Dansgroep", FileDialog.LOAD);
        filedialog.setVisible(true);
        SaveObject savedObject = null;

        String fileLocation = filedialog.getDirectory();
        if (filedialog.getFile() != null)
        {
            ObjectInputStream savedGame = null;
            try
            {
                FileInputStream f = new FileInputStream(fileLocation + filedialog.getFile());
                savedGame = new ObjectInputStream(f);
                savedObject = (SaveObject) savedGame.readObject();

            }// <editor-fold defaultstate="collapsed" desc="Catch and finally blocks">
            catch (ClassNotFoundException ex)
            {
                Logger.getLogger(loadAndSave.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex)
            {
                JOptionPane.showMessageDialog(null, "Het inladen is mislukt \n Voor het bevoegde gezag:" + ex, "Er is een fout opgetreden", JOptionPane.WARNING_MESSAGE);
            } finally
            {
                try
                {
                    savedGame.close();
                } catch (IOException ex)
                {
                    JOptionPane.showMessageDialog(null, "Het lezen is mislukt \n Voor het bevoegde gezag:" + ex, "Er is een fout opgetreden", JOptionPane.WARNING_MESSAGE);
                }
            }// </editor-fold>
        }
        Game game = new Game();

        game.getScoreBalk().setCoins(savedObject.getCoins());
        game.getScoreBalk().setKilledEnemys(savedObject.getKills());
        game.getScoreBalk().setLives(savedObject.getLives());
        game.getScoreBalk().setScore(savedObject.getScore());
        game.getMario().setBig(savedObject.isBig());
        return game;
    }
}