package edu.school21.tictactoe.models;

public class SignUpRequest {
    private String login;
    private String password;

    public SignUpRequest() {}

    public SignUpRequest(String login, String password) {
        this.login = login;
        this.password = password;
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
}
