package lukuvinkit.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;

import lukuvinkit.domain.ReadingTip;
import lukuvinkit.domain.Tag;

import org.springframework.stereotype.Component;

@Component
public class ReadingTipDao {
    
    private Database database;
    
    public ReadingTipDao() {
        // default constructor for Spring
    }
    
    public ReadingTipDao(Database database) {
        this.database = database;
    }
    
    public ArrayList<ReadingTip> findAll() throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "SELECT * FROM ReadingTip"
        );

        ResultSet result = statement.executeQuery();

        ArrayList<ReadingTip> allReadingTips = new ArrayList<>();

        while (result.next()) {
            allReadingTips.add(new ReadingTip(
                result.getInt("id"),
                result.getString("title"),
                result.getString("type"),
                result.getString("url"),
                result.getString("author"),
                result.getString("isbn"),
                result.getString("description"),
                result.getBoolean("read")
            ));
        }

        result.close();
        statement.close();
        connection.close();

        return allReadingTips;
    }
    
    public ReadingTip save(ReadingTip tip) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement statement;
        
        statement = connection.prepareStatement(
            "INSERT INTO ReadingTip(title, type, url, author, isbn, description, read) " 
            + "values (?, ?, ?, ?, ?, ?, ?)"
        );
        statement.setString(1, tip.getTitle());
        statement.setString(2, tip.getType());
        statement.setString(3, tip.getUrl());
        statement.setString(4, tip.getAuthor());
        statement.setString(5, tip.getIsbn());
        statement.setString(6, tip.getDescription());
        statement.setBoolean(7, tip.isRead());
        statement.executeUpdate();
        
        statement = connection.prepareStatement("select last_insert_rowid()");
        ResultSet result = statement.executeQuery();
        result.next();

        int id = result.getInt(1);
        tip.setId(id);

        result.close();
        statement.close();
        connection.close();
        
        return tip;
    }
    
    public void toggleRead(int readingTipId) throws SQLException {
        Connection connection = database.getConnection();
        PreparedStatement statement = connection.prepareStatement(
            "UPDATE ReadingTip SET read=CASE read WHEN true THEN false ELSE true END WHERE id=?"
        );
        
        statement.setInt(1, readingTipId);
        
        statement.executeUpdate();

        statement.close();
        connection.close();
    }
    
    public void bindTagsToReadingTip(ReadingTip tip, ArrayList<Tag> tags) throws SQLException {
        if (tags.isEmpty()) {
            return;
        }
        
        Connection connection = database.getConnection();
        PreparedStatement statement = null;

        for (Tag tag : tags) {
            statement = connection.prepareStatement(
                "INSERT INTO ReadingTipTag(readingtip_id, tag_id) values (?, ?)"
            );
            statement.setInt(1, tip.getId());
            statement.setInt(2, tag.getId());
            statement.executeUpdate();
            statement.close();
        }

        connection.close();
    }
    
}
