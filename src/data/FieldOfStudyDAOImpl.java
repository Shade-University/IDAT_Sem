package data;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Obor;

/**
 *
 * @author Tomáš Vondra
 */
public class FieldOfStudyDAOImpl implements FieldOfStudyDAO {

    private Connection conn;

    public FieldOfStudyDAOImpl() {
        try {
            conn = OracleConnection.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(FieldOfStudyDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public Collection<Obor> getAllFields(){
        
        Collection<Obor> collection = new ArrayList<>();
        try {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery(
                    "SELECT * FROM STUDIJNI_OBORY");

            while (rs.next()) {
                Obor obor = new Obor(
                        rs.getInt("id_obor"),
                        rs.getString("nazev"),
                        rs.getString("popis")
                );
                
                collection.add(obor);
            } //Načte všechny obory

            return collection;
        } catch (SQLException ex) {
            Logger.getLogger(GroupDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @Override
    public void deleteField(Obor obor) {
        try {
            CallableStatement pstm = conn.prepareCall(
                    "call delete_field(?)"
            );
            pstm.setInt(1, obor.getId());

            pstm.execute();
            conn.commit();
            System.out.println("Field deleted");
        } catch (SQLException ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public void insertField(Obor obor) {
         try {
            PreparedStatement pstm = conn.prepareStatement(
                    "INSERT INTO STUDIJNI_OBORY(nazev, popis)"
                    + "VALUES(?, ?)"
            );
            pstm.setString(1, obor.getNazev());
            pstm.setString(2, obor.getPopis());

            pstm.executeUpdate();
            conn.commit();
            System.out.println("Field created");
            
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery("SELECT MAX(id_obor) \"id\" FROM STUDIJNI_OBORY");
            if (rs.next()) {
                obor.setId(rs.getInt("id"));
            } //Získání id z databáze
            //TODO Toto vlastně asi ani nepotřebuju, protože po každým insertu aktualizuju listview z databáze
        } catch (SQLException ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void updateField(Obor obor) {
       try {
            PreparedStatement pstm = conn.prepareStatement(
                    "UPDATE STUDIJNI_OBORY SET "
                    + "nazev = ?, "
                    + "popis = ?"
                    + " where id_obor = ?"
            );
            pstm.setString(1, obor.getNazev());
            pstm.setString(2, obor.getPopis());
            pstm.setInt(3, obor.getId());

            pstm.executeUpdate();
            conn.commit();
            System.out.println("Field updated");
        } catch (SQLException ex) {
            Logger.getLogger(UserDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

}
