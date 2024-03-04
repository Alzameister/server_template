package ch.uzh.ifi.hase.soprafs24.service;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UpdatePutDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserByTokenGetDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class UserService {

  private final Logger log = LoggerFactory.getLogger(UserService.class);

  private final UserRepository userRepository;

  @Autowired
  public UserService(@Qualifier("userRepository") UserRepository userRepository) {
      this.userRepository = userRepository;
  }

  public List<User> getUsers() {
    return this.userRepository.findAll();
  }

  public User createUser(User newUser) {
      newUser.setToken(UUID.randomUUID().toString());
      newUser.setStatus(UserStatus.ONLINE);
      checkIfUserExists(newUser);
      // saves the given entity but data is only persisted in the database once
      // flush() is called
      newUser = userRepository.save(newUser);
      userRepository.flush();

      log.debug("Created Information for User: {}", newUser);
      return newUser;
  }

  public User loginUser(User userToBeLoggedIn) {
      User userByUsernameAndPassword = userRepository.findByUsernameAndPassword(userToBeLoggedIn.getUsername(), userToBeLoggedIn.getPassword());

      if (userByUsernameAndPassword == null) {
          throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The credentials provided do not exist. Therefore, you could not be logged in!");
      }
      userByUsernameAndPassword.setStatus(UserStatus.ONLINE);
      userToBeLoggedIn = userRepository.save(userByUsernameAndPassword);
      userRepository.flush();
      return userToBeLoggedIn;
  }

    public User logoutUser(User userToBeLoggedOut) {
      userToBeLoggedOut = userRepository.findBytoken(userToBeLoggedOut.getToken());
      userToBeLoggedOut.setStatus(UserStatus.OFFLINE);
      userRepository.save(userToBeLoggedOut);
      userRepository.flush();
      return userToBeLoggedOut;
    }

    public void updateUser(User userInput) {
        User userToBeUpdated = userRepository.findByid(userInput.getId());

        if(userInput.getUsername() != null){
            userToBeUpdated.setUsername(userInput.getUsername());
        }

        if(userInput.getBirthDate() != null){
            userToBeUpdated.setBirthDate(userInput.getBirthDate());
        }

        if(userInput.getUsername() != null || userInput.getBirthDate() != null){
            userRepository.save(userToBeUpdated);
            userRepository.flush();
        }

    }

    /**
     * Helper method to find a User in the repository based on the userID.
     * @param userID
     * @return
     */
    public User getUserByID(Long userID) {
        User user = userRepository.findByid(userID);
        if(user == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user does not exist.");
        }
        return userRepository.findByid(userID);
    }

    /**
     * Helper method to find a logged-in User in the repository based on the token.
     * @param userByToken
     * @return
     */
    public User getUserByToken(User userByToken) {
        User user = userRepository.findBytoken(userByToken.getToken());
        if(user == null){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The user does not exist.");
        }
        return user;
    }

  /**
   * This is a helper method that will check the uniqueness criteria of the
   * username and the name
   * defined in the User entity. The method will do nothing if the input is unique
   * and throw an error otherwise.
   *
   * @param userToBeCreated
   * @throws org.springframework.web.server.ResponseStatusException
   * @see User
   */
  private void checkIfUserExists(User userToBeCreated) {
    User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());

    String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
    if (userByUsername != null) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(baseErrorMessage, "username", "is"));
    }
  }
}
