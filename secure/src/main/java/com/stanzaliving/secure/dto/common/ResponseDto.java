package com.stanzaliving.secure.dto.common;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class ResponseDto<T> {
    private boolean status;

    private String message;

    private String id;

    private String errorCode;

    private T data;

    private int httpStatusCode;

    private ResponseDto(boolean status) {
        this.status = status;
    }

    protected ResponseDto(boolean status, String message) {
        this(status);
        this.message = message;
    }

    protected ResponseDto(boolean status, String message, T data) {
        this(status, message);
        this.data = data;
    }

    protected ResponseDto(boolean status, String message, T data, int httpStatusCode) {
        this(status, message);
        this.data = data;
        this.httpStatusCode = httpStatusCode;
    }

    private ResponseDto(boolean status, String message, T data, String id) {
        this(status, message, data);
        this.id = id;
    }

    private ResponseDto(boolean status, String message, T data, String id, String errorCode) {
        this(status, message, data, id);
        this.errorCode = errorCode;
    }

    private ResponseDto(boolean status, String message, T data, String id, int httpStatusCode) {
        this(status, message, data);
        this.httpStatusCode = httpStatusCode;
        this.id = id;
    }
    public static <T> ResponseDto<T> success(T data) {
        return new ResponseDto<>(true, null, data, 200);
    }

    public static <T> ResponseDto<T> success(String message) {
        return new ResponseDto<>(true, message, null, 200);
    }

    public static <T> ResponseDto<T> success(String message, T data) {
        return new ResponseDto<>(true, message, data,200);
    }

    public static <T> ResponseDto<T> success(String message, T data, String id) {
        return new ResponseDto<>(true, message, data, id, 200);
    }

    public static <T> ResponseDto<T> success(String message, T data, boolean status) {
        return new ResponseDto<>(true, message, data, 200);
    }

    public static <T> ResponseDto<T> success(String message, T data, String id, String errorCode) {
        return new ResponseDto<>(true, message, data, id, errorCode);
    }

    public static <T> ResponseDto<T> failure(String message) {
        return new ResponseDto<>(false, message);
    }

    public static <T> ResponseDto<T> failure(String message, String id) {
        return new ResponseDto<>(false, message, null, id);
    }

    public static <T> ResponseDto<T> failure(String message, String id, String errorCode) {
        return new ResponseDto<>(false, message, null, id, errorCode);
    }

    public static <T> ResponseDto<T> failure(String message, T data) {
        return new ResponseDto<>(false, message, data);
    }

    public static <T> ResponseDto<T> failure(String message, T data, String id) {
        return new ResponseDto<>(false, message, data, id);
    }

    public static <T> ResponseDto<T> failure(String message, T data, String id, String errorCode) {
        return new ResponseDto<>(false, message, data, id, errorCode);
    }

    public static <T> ResponseDto<T> failure(String message, T data, String id, int httpStatusCode) {
        return new ResponseDto<>(false, message, data, id, httpStatusCode);
    }
}
