package com.jefesimpson.service.sample.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.time.LocalDate;
import java.util.Objects;

@DatabaseTable
public class Client {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "first_name")
    private String firstName;
    @DatabaseField(columnName = "last_name")
    private String lastName;
    @DatabaseField
    private String email;
    @DatabaseField
    private String phone;
    @DatabaseField
    private String login;
    @DatabaseField
    private String password;
    @DatabaseField
    private String token;
    @DatabaseField(dataType = DataType.SERIALIZABLE, columnName = "destruction_time")
    private LocalDate destructionTime;

    public Client() {
    }

    public Client(int id, String firstName, String lastName, String email, String phone, String login, String password, String token, LocalDate destructionTime) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.login = login;
        this.password = password;
        this.token = token;
        this.destructionTime = destructionTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDate getDestructionTime() {
        return destructionTime;
    }

    public void setDestructionTime(LocalDate destructionTime) {
        this.destructionTime = destructionTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return id == client.id &&
                Objects.equals(firstName, client.firstName) &&
                Objects.equals(lastName, client.lastName) &&
                Objects.equals(email, client.email) &&
                Objects.equals(phone, client.phone) &&
                Objects.equals(login, client.login) &&
                Objects.equals(password, client.password) &&
                Objects.equals(token, client.token) &&
                Objects.equals(destructionTime, client.destructionTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, email, phone, login, password, token, destructionTime);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", token='" + token + '\'' +
                ", destructionTime=" + destructionTime +
                '}';
    }

    public static String COLUMN_ID = "id";
    public static String COLUMN_FIRST_NAME = "first_name";
    public static String COLUMN_LAST_NAME = "last_name";
    public static String COLUMN_EMAIL = "email";
    public static String COLUMN_PHONE = "phone";
    public static String COLUMN_LOGIN = "login";
    public static String COLUMN_PASSWORD = "password";
    public static String COLUMN_TOKEN = "token";
    public static String COLUMN_DESTRUCTION_TIME = "destruction_time";
}
