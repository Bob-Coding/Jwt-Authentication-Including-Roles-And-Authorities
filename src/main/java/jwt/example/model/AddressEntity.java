package jwt.example.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jwt.example.userDto.UserDto;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "addresses")
public class AddressEntity implements Serializable {
    private static final long serialVersionUID = 7808438972L;
    @Id
    @GeneratedValue
    private long id;
    @Column(length = 30, nullable = false)
    private String addressId;
    @Column(length = 25, nullable = false)
    private String country;
    @Column(length = 25, nullable = true)
    private String city;
    @Column(length = 50, nullable = true)
    private String streetName;
    @Column(length = 10, nullable = true)
    private String postalCode;
    @Column(length = 25)
    private long phoneNumber;
    @Column(length = 20, nullable = true)
    private String type;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name="users_id")
    private UserEntity userDetails;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UserEntity getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserEntity userDetails) {
        this.userDetails = userDetails;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
