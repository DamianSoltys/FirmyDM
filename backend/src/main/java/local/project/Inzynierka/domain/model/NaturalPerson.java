package local.project.Inzynierka.domain.model;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class NaturalPerson {
    //TODO Add all fields
    private long id;
    private String firstName;
    private String lastName;
    private String city;
    private String street;
    private String apartmentNo;
    private String buildingNo;
    private String phoneNo;
    private Timestamp createdAt;
    private Timestamp modifiedAt;

}
