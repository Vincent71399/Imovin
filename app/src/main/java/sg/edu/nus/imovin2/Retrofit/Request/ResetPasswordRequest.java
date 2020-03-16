package sg.edu.nus.imovin2.Retrofit.Request;

public class ResetPasswordRequest {
    private String email;

    public ResetPasswordRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
