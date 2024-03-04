package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.*;
import ch.uzh.ifi.hase.soprafs24.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * User Controller
 * This class is responsible for handling all REST request that are related to
 * the user.
 * The controller will receive the request and delegate the execution to the
 * UserService and finally return the result.
 */
@RestController
public class UserController {

  private final UserService userService;

  UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("/users")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public List<UserGetDTO> getAllUsers() {
    // fetch all users in the internal representation
    List<User> users = userService.getUsers();
    List<UserGetDTO> userGetDTOs = new ArrayList<>();

    // convert each user to the API representation
    for (User user : users) {
      userGetDTOs.add(DTOMapper.INSTANCE.convertEntityToUserGetDTO(user));
    }
    return userGetDTOs;
  }

    //TODO: Query instead of RequestBody?
    @GetMapping("/users/{userID}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO getUserByID(@PathVariable("userID") long id) {
        //TODO: REST specification wants userID as a long type input?
        return DTOMapper.INSTANCE.convertEntityToUserGetDTO(userService.getUserByID(id));
    }

    //TODO: Query instead of RequestBody?
    @GetMapping("/getUserByToken")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public UserGetDTO getUserByToken(@RequestBody UserByTokenGetDTO userByTokenGetDTO) {
      User userByToken = DTOMapper.INSTANCE.convertUserByTokenGetDTOToEntity(userByTokenGetDTO);
      return DTOMapper.INSTANCE.convertEntityToUserGetDTO(userService.getUserByToken(userByToken));
    }

  @PostMapping("/users")
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public UserGetDTO createUser(@RequestBody UserPostDTO userPostDTO) {
    // convert API user to internal representation
    User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

    // create user
    User createdUser = userService.createUser(userInput);
    // convert internal representation of user back to API
    return DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdUser);
  }

  @PostMapping("/logout")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void logoutUser(@RequestBody LogoutPostDTO logoutPostDTO) {
    // convert API user to internal representation
    User userToLogout = DTOMapper.INSTANCE.convertLogoutPostDTOtoEntity(logoutPostDTO);

    // perform logout
    User createdUser = userService.logoutUser(userToLogout);
  }
  @PostMapping("/login")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public UserGetDTO loginUser(@RequestBody UserPostDTO userPostDTO) {
    // convert API user to internal representation
    User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

    // check credentials
    User createdUser = userService.loginUser(userInput);

    // convert internal representation of user back to API
    return DTOMapper.INSTANCE.convertEntityToUserGetDTO(createdUser);
  }

  @PutMapping("/updateUser")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @ResponseBody
  public void updateUser(@RequestBody UpdatePutDTO updatePutDTO) {

      // convert API user to internal representation
      User userInput = DTOMapper.INSTANCE.convertUpdatePutDTOtoEntity(updatePutDTO);

      // check credentials
      userService.updateUser(userInput);
  }
}
