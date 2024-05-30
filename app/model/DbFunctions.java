package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DbFunctions {
    public Connection connect_to_db(String dbname,String user,String password) throws DbConnectionException{
        Connection conn=null;
        try{
            Class.forName("org.postgresql.Driver");
            conn= DriverManager.getConnection("jdbc:postgresql://localhost:5432/"+dbname,user,password);
            if(conn!=null){
                System.out.println("Connection Established");
            }
            else{
                System.out.println("Connection Failed");
            }

        }catch (Exception e){
            throw new DbConnectionException();
        }
        return conn;
    }

    public void addNewURLEntry(Connection conn,URL url) throws DbConnectionException{
        Statement statement;
        try {
            String query=String.format("INSERT INTO  \"URL_TABLE\"(\"shortURL\", \"longURL\", \"viewCount\") values('%s','%s',1);",url.getShortURL(),url.getLongURL());
            statement=conn.createStatement();
            statement.executeUpdate(query);
            System.out.println("Row Inserted");
        }catch (Exception e){
            throw new DbConnectionException();
        }
    }

    public void getLongURL(Connection conn,URL url ) throws DbConnectionException{
        Statement statement;
        ResultSet rs=null;
        String shortURL = url.getShortURL();
        try {
            String query=String.format("select \"longURL\" from \"URL_TABLE\" WHERE \"shortURL\" =  '%s\'",shortURL);
            statement=conn.createStatement();
            rs=statement.executeQuery(query);
            if (!rs.isBeforeFirst() ) {    
                //no data
            }
            else{
                url.setLongURL(rs.getString("longURL"));
            } 

        }
        catch (Exception e){
            throw new DbConnectionException();
        }
    }

    

}
