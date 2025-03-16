package controller.registerForm;

import db.DBConnection;
import dto.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import util.Role;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserRegisterImpl implements UserRegisterService{
    @Override
    public User searchUser(Integer id) {
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users=new ArrayList<>();


        try {
            String SQL="SELECT * FROM user";
            Connection connection = DBConnection.getInstance().getConnection();

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQL);

            while (resultSet.next()){
                User user = new User(
                        resultSet.getInt("user_id"),
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        Role.valueOf(resultSet.getString("role").toUpperCase())
                );
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    public ObservableList<Integer> getUserById(){
        List<User> userList = getAllUsers();
        ObservableList<Integer> userIdList = FXCollections.observableArrayList();

        userList.forEach(user -> {
            userIdList.add(user.getUserId());
        });

        return userIdList;
    }

    public User getUserById(Integer userId) {
        // Validate that userId is not null
        if (userId == null) {
            return null;  // If userId is null, return null
        }

        // Get the list of user IDs
        ObservableList<Integer> userIdList = getUserById();  // Use the existing method

        // Check if the userId exists in the list
        if (userIdList.contains(userId)) {
            // If the userId is found, search for the User object
            List<User> userList = getAllUsers();  // Assuming this fetches all users
            for (User user : userList) {
                // Check if the user object is not null and compare userId
                if (user != null && user.getUserId() == userId) {  // Using == to compare primitive int
                    return user;  // Return the User object
                }
            }
        }
        return null;
    }

}
