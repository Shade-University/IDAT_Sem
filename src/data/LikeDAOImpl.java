package data;

import model.Like;
import model.Message;
import model.Rating;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LikeDAOImpl implements LikeDAO{

    private Connection conn;

    public LikeDAOImpl() {
        try {
            conn = OracleConnection.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(MessageDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void createLike(Like like) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(
                "INSERT INTO OBLIBENE_ZPRAVY(ID_UZIVATEL, ID_ZPRAVA)\n"
                        + "VALUES(?, ?)"
        );
        preparedStatement.setInt(1, like.getId_user());
        preparedStatement.setInt(2, like.getId_message());

        preparedStatement.executeUpdate();
        conn.commit();
        preparedStatement.close();
        System.out.println("createLike");
    }

    @Override
    public void deleteLike(Like like) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement(
                "DELETE FROM OBLIBENE_ZPRAVY WHERE ID_UZIVATEL = ? AND ID_ZPRAVA = ?"
        );
        preparedStatement.setInt(1, like.getId_user());
        preparedStatement.setInt(2, like.getId_message());

        preparedStatement.executeUpdate();
        conn.commit();
        preparedStatement.close();
        System.out.println("deleteLike");
    }

    @Override
    public Like getLike(ResultSet rs) throws SQLException {
        try{
            return new Like(rs.getInt("id_uzivatel"), rs.getInt("id_zprava"));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public int getLikeCount(Message msg) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("select COUNT(*) \"pocet\" FROM OBLIBENE_ZPRAVY where ID_ZPRAVA = ?;");
        preparedStatement.setInt(1, msg.getId());
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()){
            int result = rs.getInt("pocet");
            preparedStatement.close();
            conn.commit();
            return result;
        }
        preparedStatement.close();
        conn.commit();
        System.out.println("getLikeCount");
        return 0;
    }

    @Override
    public Collection<Like> getAllLikes() throws SQLException {
        Collection<Like> collection = new ArrayList<>();

        PreparedStatement preparedStatement = conn.prepareStatement(
                "SELECT * FROM OBLIBENE_ZPRAVY");
        ResultSet rs = preparedStatement.executeQuery();
        while (rs.next()){
            Like like = getLike(rs);
            collection.add(like);
        }
        preparedStatement.close();
        conn.commit();
        System.out.println("getAllLikes");
        return collection;
    }
}
