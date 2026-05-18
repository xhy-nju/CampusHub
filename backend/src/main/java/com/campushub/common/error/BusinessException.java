package com.campushub.common.error;

public class BusinessException extends RuntimeException {

    private final int code;
    private final int httpStatus;

    public BusinessException(int code, int httpStatus, String message) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public int getCode() {
        return code;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public static BusinessException badRequest(String message) {
        return new BusinessException(400, 400, message);
    }

    public static BusinessException unauthorized(String message) {
        return new BusinessException(401, 401, message);
    }

    public static BusinessException forbidden(String message) {
        return new BusinessException(403, 403, message);
    }

    public static BusinessException conflict(String message) {
        return new BusinessException(409, 409, message);
    }

    public static BusinessException notFound(String message) {
        return new BusinessException(404, 404, message);
    }
}
