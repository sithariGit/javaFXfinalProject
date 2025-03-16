package controller.registerForm;

import dto.User;

import java.util.List;

public interface UserRegisterService {
    User searchUser(Integer id);
    List<User> getAllUsers();
}
