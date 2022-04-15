package login.result;

import login.result.LoginResult;

public class LoginFailed extends LoginResult {
    public static final int USERNAME_NOT_EXISTS = 1;
    public static final int WRONG_PASSWORD = 2;

    private final int code;

    public LoginFailed(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String getString() {
        switch (code) {
            case USERNAME_NOT_EXISTS:
                return "Tên đăng nhập không tồn tại!";
            case WRONG_PASSWORD:
                return "Sai mật khẩu!";
            default:
                return "Lỗi không xác định!";
        }
    }
}
