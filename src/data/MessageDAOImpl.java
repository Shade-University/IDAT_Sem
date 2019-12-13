package data;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.*;

/**
 * @author Tomáš Vondra
 */
public class MessageDAOImpl implements MessageDAO {

    private Connection conn;
    private UserDAO userDAO = new UserDAOImpl();
    private GroupDAO groupDAO = new GroupDAOImpl();
    private FileDAO fileDAO = new FileDAOImpl();

    public MessageDAOImpl() {
        try {
            conn = OracleConnection.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(MessageDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Collection<Message> getAllMessages() throws SQLException {
        Collection<Message> collection = new ArrayList<>();

        PreparedStatement preparedStatement = conn.prepareStatement(
                "SELECT * FROM ZPRAVY z " +
                        "left join getUzivatelOdesilatel uo ON z.ID_UZIVATEL_ODESILATEL = uo.\"id_odesilatel\" " +
                        "left join getUzivatelPrijemce up ON z.ID_UZIVATEL_PRIJEMCE = up.\"id_prijemce\" " +
                        "left join GETSKUPINY sk on z.ID_SKUPINA_PRIJEMCE = sk.ID_SKUPINA " +
                        "left join ZPRAVY zp on z.ID_ZPRAVA = zp.ID_RODIC " +
                        "left join SOUBORY s on z.ID_SOUBORU = s.ID_SOUBORU");
        ResultSet rs = preparedStatement.executeQuery();

        while (rs.next()) {
            collection.add(getMessage(rs));
        }

        return collection;
    }

    @Override
    public Message getMessageById(int id) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(
                "SELECT * FROM ZPRAVY WHERE ID_ZPRAVA = " + id);
        ResultSet rs = preparedStatement.executeQuery();

        if (rs.next())
            return getMessage(rs);
        return null;
    }

    @Override
    public Message getMessage(ResultSet rs) throws SQLException {
        ResultSetMetaData rsmd = rs.getMetaData();
        System.out.println(rsmd.getColumnName(10));
        System.out.println(rs.getInt("id_odesilatel"));
     //   User odesilatel = new User(rs.getInt("uo.\"id_odesilatel\""), rs.getString("uo.\"jmeno_odesilatel\""), rs.getString("uo.\"prijmeni_odesilatel\""), rs.getString("uo.\"email_odesilatel\""),rs.getDate("uo.\"datum_vytvoreni_odesilatel\""), USER_TYPE.get(rs.getString("uo.\"uzivatel_typ_odesilatel\"")), userDAO.readImage(rs.getBlob("uo.\"avatar_odesilatel\"")));
       // User prijemce = new User(rs.getInt("up.\"id_prijemce\""), rs.getString("up.\"jmeno_prijemce\""), rs.getString("up.\"prijmeni_prijemce\""), rs.getString("up.\"email_prijemce\""),rs.getDate("up.\"datum_vytvoreni_prijemce\""), USER_TYPE.get(rs.getString("up.\"uzivatel_typ_prijemce\"")), userDAO.readImage(rs.getBlob("up.\"avatar_prijemce\"")));
        Message message = new Message(
                rs.getInt("ID_ZPRAVA"),
                rs.getString("nazev"),
                rs.getString("telo"),
                null,
                null,
                groupDAO.getGroup(rs),
                rs.getDate("datum_vytvoreni"),
                null,
                fileDAO.getFile(rs)
        );
        return message;
    }

    @Override
    public void createMessage(Message message) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(
                "INSERT INTO ZPRAVY(nazev, telo, datum_vytvoreni, "
                        + "id_uzivatel_odesilatel, id_skupina_prijemce, id_uzivatel_prijemce) "
                        + "VALUES (?,?,?,?,?,?)");
        preparedStatement.setString(1, message.getNazev());
        preparedStatement.setString(2, message.getObsah());
        preparedStatement.setDate(3, message.getDatum_vytvoreni());
        preparedStatement.setInt(4, message.getOdesilatel().getId());

        if (message.getPrijemce_uzivatel() != null) {
            preparedStatement.setNull(5, Types.INTEGER);
            preparedStatement.setInt(6, message.getPrijemce_uzivatel().getId());
        } else {
            preparedStatement.setInt(5, message.getPrijemce_skupina().getId());
            preparedStatement.setNull(6, Types.INTEGER);
        } //Načte buď příjemce skupinu nebo uživatele

        preparedStatement.executeUpdate();
        System.out.println("Message created");
        conn.commit();
    }

    @Override
    public Collection<Message> getMessagesForChatBetween(User uzivatel1, User uzivatel2) throws SQLException {
        Collection<Message> collection = new ArrayList<>();

        PreparedStatement preparedStatement = conn.prepareStatement(
                "SELECT * FROM ZPRAVY z WHERE (z.id_uzivatel_odesilatel = ? OR z.id_uzivatel_PRIJEMCE = ?)\n"
                        + "AND (z.id_uzivatel_odesilatel = ? OR z.id_uzivatel_prijemce = ?) "
                        + "ORDER BY z.datum_vytvoreni");
        preparedStatement.setInt(1, uzivatel1.getId());
        preparedStatement.setInt(2, uzivatel1.getId());
        preparedStatement.setInt(3, uzivatel2.getId());
        preparedStatement.setInt(4, uzivatel2.getId());

        ResultSet rs = preparedStatement.executeQuery();
        //Načte zprávy kde je příjemce buď uživatel1 nebo 2 nebo je odesílatel uživatel1 nebo 2
        while (rs.next()) {
            Message zprava;
            if (rs.getInt("id_uzivatel_odesilatel") == uzivatel1.getId()) {
                zprava = new Message(
                        rs.getString("nazev"),
                        rs.getString("telo"),
                        uzivatel1,
                        uzivatel2,
                        null,
                        rs.getDate("datum_vytvoreni")
                ); //pokud je uživatel1 odesilatel, vytvoř tuto zprávu
            } else {
                zprava = new Message(
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
    }

    @Override
    public Collection<Message> getMessagesForGroupChat(Group skupina) throws SQLException {
        Collection<Message> collection = new ArrayList<>();

        PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM ZPRAVY z\n" +
                        "join (select * from GETUZIVATELE) on ID_UZIVATEL = ID_UZIVATEL_ODESILATEL\n" +
                        "where ID_SKUPINA_PRIJEMCE = ?\n" +
                        "order by z.DATUM_VYTVORENI"
        );

        stmt.setInt(1, skupina.getId());

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {

            Message zprava = new Message(
                    rs.getInt("id_zprava"),
                    rs.getString("nazev"),
                    rs.getString("telo"),
                    userDAO.getUser(rs),
                    null,
                    skupina,
                    rs.getDate("datum_vytvoreni")
            );
            collection.add(zprava);
        }

        return collection;
    }



//    private User getUser(ResultSet rs) throws SQLException {
//        User uzivatel;
//        if (rs.getString("uzivatel_typ").equals("STUDENT")) {
//            uzivatel = new Student(
//                    new Field(
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
//            uzivatel = new Teacher(
//                    new Subject(
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
