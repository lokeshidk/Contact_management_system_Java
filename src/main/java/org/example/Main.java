package org.example;

import org.example.models.UserInfo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class Main {
    public static final String USER_TABLE = "user";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PHONE_NUMBER = "phoneNumber";
    public static final String COLUMN_EMAIL = "email";

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        String url = "jdbc:sqlite:userDb.db";

        String createTableSQL = "CREATE TABLE IF NOT EXISTS " + USER_TABLE + " (id integer PRIMARY KEY AUTOINCREMENT, " +
                "name text, " +
                "phoneNumber integer, " +
                "email text)";

        String insertRecordSQL = "INSERT INTO " + USER_TABLE + " (name, phoneNumber, email) VALUES (?, ?, ?)";
        String selectData = "SELECT * FROM " + USER_TABLE;

        try {
            Connection connection = DriverManager.getConnection(url);
            System.out.println("Connected");

            Statement statement = connection.createStatement();
            statement.execute(createTableSQL);


            List<UserInfo> userList = new ArrayList<>();
            ResultSet rs = statement.executeQuery(selectData);
            while (rs.next()) {
                int id = rs.getInt(COLUMN_ID);
                String name = rs.getString(COLUMN_NAME);
                int phoneNumber = rs.getInt(COLUMN_PHONE_NUMBER);
                String email = rs.getString(COLUMN_EMAIL);

                UserInfo user = new UserInfo(id, phoneNumber, name, email);
                userList.add(user);
            }

            for (UserInfo user : userList) {
                System.out.println("User ID: " + user.getId());
                System.out.println("Name: " + user.getName());
                System.out.println("Phone Number: " + user.getPhoneNumber());
                System.out.println("Email: " + user.getEmail()+"\n\n\n");
            }


            System.out.println("What do you wanna do Add or Delete user ?");
            String User = input.next();
            if (User.toUpperCase().equals("ADD")) {
                System.out.println("How mny users you want to add");
                int n = input.nextInt();
                for (int i = 1; i <= n; i++) {

                    System.out.println("Enter Name ");
                    String Name = input.next();
                    System.out.println("Enter Phone number");
                    int Phone = input.nextInt();
                    System.out.println("Enter email");
                    String Email = input.next();

                    UserInfo user1 = new UserInfo(i, Phone, Name, Email);
                    PreparedStatement preparedStatement = connection.prepareStatement(insertRecordSQL);
                    preparedStatement.setString(1, user1.getName());
                    preparedStatement.setInt(2, user1.getPhoneNumber());
                    preparedStatement.setString(3, user1.getEmail());
                    preparedStatement.executeUpdate();
                    System.out.println("Inserted Sucessfuly\n");


                }
            }
            else if (User.toUpperCase().equals("DELETE")){
                System.out.println("Enter the phone number of the user you want to delete:");
                int phoneToDelete = input.nextInt();

                String deleteRecordSQL = "DELETE FROM " + USER_TABLE + " WHERE " + COLUMN_PHONE_NUMBER + " = ?";
                PreparedStatement deleteStatement = connection.prepareStatement(deleteRecordSQL);
                deleteStatement.setInt(1, phoneToDelete);
                int rowsAffected = deleteStatement.executeUpdate();
            }






            connection.close();
        } catch (SQLException e) {
            System.out.println("Error");
            e.printStackTrace();
        }
    }
}
