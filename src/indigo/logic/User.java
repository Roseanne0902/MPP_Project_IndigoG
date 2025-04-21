package indigo.logic;

import indigo.repository.UserRepository;

import java.util.List;

public class User {

    public static void startUserMainFlow() {
        //

        boolean isUser = UserRepository.authenticateUser("thaiphan", "1234");
        if (isUser) {
            System.out.println("Login success!");
        } else {
            System.out.println("User information wrong!");
        }

        List<indigo.model.User> aa = UserRepository.getUserByUserId("jasmine");
        if (!aa.isEmpty()) {
            System.out.println("User available");
        }
    }
}
