package ch.uzh.ifi.hase.soprafs24.rest.dto;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class UpdatePutDTO {
  private String username;

  private Date birthDate;

  private long id;

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        if(birthDate != null){
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-dd-MM");
            try {
                this.birthDate = formatter.parse(birthDate);
            }
            //TODO: Status bad request reason is not returned
            catch (ParseException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The format of your birth date is not correct.");
            }
        }

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
