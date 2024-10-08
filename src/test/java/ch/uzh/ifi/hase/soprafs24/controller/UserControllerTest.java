package ch.uzh.ifi.hase.soprafs24.controller;

import ch.uzh.ifi.hase.soprafs24.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs24.entity.User;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UpdatePutDTO;
import ch.uzh.ifi.hase.soprafs24.rest.dto.UserPostDTO;
import ch.uzh.ifi.hase.soprafs24.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * UserControllerTest
 * This is a WebMvcTest which allows to test the UserController i.e. GET/POST
 * request without actually sending them over the network.
 * This tests if the UserController works.
 */
@WebMvcTest(UserController.class)
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @Test
  public void givenUsers_whenGetUsers_thenReturnJsonArray() throws Exception {
    // given
    User user = new User();
    user.setPassword("Pass");
    user.setUsername("firstname@lastname");
    user.setStatus(UserStatus.OFFLINE);

    List<User> allUsers = Collections.singletonList(user);

    // this mocks the UserService -> we define above what the userService should
    // return when getUsers() is called
    given(userService.getUsers()).willReturn(allUsers);

    // when
    MockHttpServletRequestBuilder getRequest = get("/users").contentType(MediaType.APPLICATION_JSON);

    // then
    mockMvc.perform(getRequest).andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        //.andExpect(jsonPath("$[0].password", is(user.getPassword())))
        .andExpect(jsonPath("$[0].username", is(user.getUsername())))
        .andExpect(jsonPath("$[0].status", is(user.getStatus().toString())));
  }

  @Test
  public void createUser_validInput_userCreated() throws Exception {
    // given
    User user = new User();
    user.setId(1L);
    user.setPassword("Password");
    user.setUsername("testUsername");
    user.setToken("1");
    user.setStatus(UserStatus.ONLINE);

    UserPostDTO userPostDTO = new UserPostDTO();
    userPostDTO.setPassword("Password");
    userPostDTO.setUsername("testUsername");

    given(userService.createUser(Mockito.any())).willReturn(user);

    // when/then -> do the request + validate the result
    MockHttpServletRequestBuilder postRequest = post("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(asJsonString(userPostDTO));

    // then
    mockMvc.perform(postRequest)
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(user.getId().intValue())))
        .andExpect(jsonPath("$.username", is(user.getUsername())))
        .andExpect(jsonPath("$.status", is(user.getStatus().toString())))
        .andExpect(jsonPath("$.birthDate", is(user.getBirthDate())))
        .andExpect(jsonPath("$.token", is(user.getToken())));
  }

  @Test
  public void createUser_invalidInput_throwError() throws Exception {
      // Create second user with same username
      UserPostDTO userPostDTO = new UserPostDTO();
      userPostDTO.setPassword("Password");
      userPostDTO.setUsername("testUsername");

      given(userService.createUser(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.CONFLICT, "add User failed because username already exists"));

      // when/then -> do the request + validate the result
      MockHttpServletRequestBuilder postRequest = post("/users")
              .contentType(MediaType.APPLICATION_JSON)
              .content(asJsonString(userPostDTO));

      // then
      mockMvc.perform(postRequest)
              .andExpect(status().isConflict())
              .andExpect(status().reason(containsString("add User failed because username already exists")));
  }

  @Test
  public void getUser_validInput_userReturned() throws Exception {
      User user = new User();
      user.setId(1L);
      user.setPassword("Password");
      user.setUsername("testUsername");
      user.setToken("1");
      user.setStatus(UserStatus.ONLINE);

      given(userService.getUserByID(Mockito.any())).willReturn(user);

      MockHttpServletRequestBuilder getRequest = get("/users/{userid}", 1L);

      mockMvc.perform(getRequest)
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.id", is(user.getId().intValue())))
              .andExpect(jsonPath("$.username", is(user.getUsername())))
              .andExpect(jsonPath("$.status", is(user.getStatus().toString())))
              .andExpect(jsonPath("$.token", is(user.getToken())));
  }

    @Test
    public void getUser_invalidUser_throwError() throws Exception {
        given(userService.getUserByID(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "User with ID: 2 was not found"));

        MockHttpServletRequestBuilder getRequest = get("/users/{userid}", 2L);

        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound())
                .andExpect(status().reason("User with ID: 2 was not found"));
    }

    @Test
    public void putUser_validInput_NoReturn() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setPassword("Password");
        user.setUsername("testUsername");
        user.setBirthDate("15.09.2002");
        user.setToken("1");
        user.setStatus(UserStatus.ONLINE);

        UpdatePutDTO updatePutDTO = new UpdatePutDTO();
        updatePutDTO.setId(user.getId());
        updatePutDTO.setUsername("testChangeUsername");
        updatePutDTO.setBirthDate("15-09-2002");

        given(userService.getUserByID(Mockito.any())).willReturn(user);

        MockHttpServletRequestBuilder putRequest = put("/users/{userid}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(updatePutDTO));

        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent());
    }

    @Test
    public void putUser_invalidUser_NoReturn() throws Exception {
        given(userService.getUserByID(Mockito.any())).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "User with ID: 2 was not found"));

        MockHttpServletRequestBuilder getRequest = get("/users/{userid}", 2L);

        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound())
                .andExpect(status().reason("User with ID: 2 was not found"));
    }

  /**
   * Helper Method to convert userPostDTO into a JSON string such that the input
   * can be processed
   * Input will look like this: {"name": "Test User", "username": "testUsername"}
   * 
   * @param object
   * @return string
   */
  private String asJsonString(final Object object) {
    try {
      return new ObjectMapper().writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
          String.format("The request body could not be created.%s", e.toString()));
    }
  }
}