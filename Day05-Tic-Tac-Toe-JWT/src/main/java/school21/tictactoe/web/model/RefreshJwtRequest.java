package school21.tictactoe.web.model;

public class RefreshJwtRequest {
    private String refreshToken;

    public RefreshJwtRequest() {}
    
    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
}
