package ch.uzh.ifi.hase.soprafs24.rest.dto;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdatePutDTO {
  private String username;

  private String birthDate;

  private long id;

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) throws ParseException {
        if(birthDate != null){
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatter.parse(birthDate);
            formatter = new SimpleDateFormat("dd-MM-yyyy");
            this.birthDate = formatter.format(date);
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
