package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Skupina;
import model.Uzivatel;
import model.Zprava;

/**
 *
 * @author Tomáš Vondra
 */
public class MessageDAOImpl implements MessageDAO {

    private Connection conn;
    private UserDAO userDAO;

    public MessageDAOImpl() {
        try {
            conn = OracleConnection.getConnection();
            userDAO = new UserDAOImpl();
        } catch (SQLException ex) {
            Logger.getLogger(MessageDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    @Override
    public void createMessage(Zprava message) {
        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO ZPRAVY(nazev, telo, datum_vytvoreni, "
                    + "id_uzivatel_odesilatel, id_skupina_prijemce, id_uzivatel_prijemce) "
                    + "VALUES (?,?,?,?,?,?)");
            stmt.setString(1, message.getNazev());
            stmt.setString(2, message.getObsah());
            stmt.setDate(3, message.getDatum_vytvoreni());
            stmt.setInt(4, message.getOdesilatel().getId());

            if (message.getPrijemce_uzivatel() != null) {
                stmt.setNull(5, Types.INTEGER);
                stmt.setInt(6, message.getPrijemce_uzivatel().getId());
            } else {
                stmt.setInt(5, message.getPrijemce_skupina().getId());
                stmt.setNull(6, Types.INTEGER);
            } //Načte buď příjemce skupinu nebo uživatele

            stmt.executeUpdate();
            System.out.println("Message created");
            conn.commit();
        } catch (SQLException ex) {
            Logger.getLogger(MessageDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public Collection<Zprava> getMessagesForChatBetween(Uzivatel uzivatel1, Uzivatel uzivatel2) {
        Collection<Zprava> collection = new ArrayList<>();

        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT * FROM ZPRAVY z WHERE (z.id_uzivatel_odesilatel = ? OR z.id_uzivatel_PRIJEMCE = ?)\n"
                    + "AND (z.id_uzivatel_odesilatel = ? OR z.id_uzivatel_prijemce = ?) "
                    + "ORDER BY z.datum_vytvoreni");
            stmt.setInt(1, uzivatel1.getId());
            stmt.setInt(2, uzivatel1.getId());
            stmt.setInt(3, uzivatel2.getId());
            stmt.setInt(4, uzivatel2.getId());

            ResultSet rs = stmt.executeQuery();
            //Načte zprávy kde je příjemce buď uživatel1 nebo 2 nebo je odesílatel uživatel1 nebo 2
            while (rs.next()) {
                Zprava zprava;
                if (rs.getInt("id_uzivatel_odesilatel") == uzivatel1.getId()) {
                    zprava = new Zprava(
                            rs.getString("nazev"),
                            rs.getString("telo"),
                            uzivatel1,
                            uzivatel2,
                            null,
                            rs.getDate("datum_vytvoreni")
                    ); //pokud je uživatel1 odesilatel, vytvoř tuto zprávu
                } else {
                    zprava = new Zprava(
                            rs.getString("nazev"),
                            rs.getString("telo"),
                            uzivatel2,
                            uzivatel1,
                            null,
                            rs.getDate("datum_vytvoreni")
                    );
                } //Pokud je uživatel1 příjemce, vytvoř tuto
                collection.add(zprava);
            } //Načte zprávy mezi dvouma uživatelama

            return collection;
        } catch (SQLException ex) {
            Logger.getLogger(MessageDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @Override
    public Collection<Zprava> getMessagesForGroupChat(Skupina skupina) {
        Collection<Zprava> collection = new ArrayList<>();

        try {
            PreparedStatement stmt = conn.prepareStatement(
                    "SELECT z.id_zprava, z.nazev, z.telo, z.datum_vytvoreni \"zprava_datum_vytvoreni\" ,u.* \n"
                    + "FROM ZPRAVY z\n"
                    + "JOIN (SELECT * FROM GETUSERS) u ON  u.id_uzivatel = z.id_uzivatel_odesilatel\n"
                    + "WHERE ID_SKUPINA_PRIJEMCE = ? "
                    + "ORDER BY z.datum_vytvoreni"
            );

            stmt.setInt(1, skupina.getId());

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {

                Zprava zprava = new Zprava(
                        rs.getString("nazev"),
                        rs.getString("telo"),
                        userDAO.getUser(rs),
                        null,
                        skupina,
                        rs.getDate("zprava_datum_vytvoreni")
                );
                collection.add(zprava);
            }

            return collection;
        } catch (SQLException ex) {
            Logger.getLogger(MessageDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

//    private Uzivatel getUser(ResultSet rs) throws SQLException {
//        Uzivatel uzivatel;
//        if (rs.getString("uzivatel_typ").equals("student")) {
//            uzivatel = new Student(
//                    new Obor(
//                            rs.getInt("id_obor"),
//                            rs.getString("nazev_obor"),
//                            rs.getString("popis_obor")
//                    ),
//                    rs.getString("rok_studia"),
//                    rs.getInt("id_uzivatel"),
//                    rs.getString("jmeno"),
//                    rs.getString("prijmeni"),
//                    rs.getString("email"),
//                    rs.getDate("datum_vytvoreni")
//            );
//        } else {
//            uzivatel = new Ucitel(
//                    new Predmet(
//                            rs.getInt("id_vyucujici_predmet"),
//                            rs.getString("nazev_vyucujici_predmet"),
//                            rs.getString("popis_vyucujici_predmet")
//                    ),
//                    rs.getString("katedra"),
//                    rs.getInt("id_uzivatel"),
//                    rs.getString("jmeno"),
//                    rs.getString("prijmeni"),
//                    rs.getString("email"),
//                    rs.getDate("datum_vytvoreni")
//            );
//        }
//        return uzivatel;
//
//    }
}
