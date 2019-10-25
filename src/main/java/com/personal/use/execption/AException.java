package com.personal.use.execption;

import java.util.Map;

/**
 * @author mifuxing
 * <p>
 * wajiu异常类
 */
public class AException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 异常编码
     */
    private ExceptionCode code = ExceptionCode.ERROR_500;

    private Map returnMap = null;

    public AException() {
        super();
    }

    public AException(ExceptionCode code) {
        super();
        this.code = code;
    }

    public AException(String message) {
        super(message);
    }

    public AException(Map returnMap) {
        super();
        this.returnMap = returnMap;
    }


    public AException(ExceptionCode code, String message) {
        super(message);
        this.code = code;
    }

    public AException(Throwable cause) {
        super(cause);
    }

    public AException(ExceptionCode code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public AException(String message, Throwable cause) {
        super(message, cause);
    }

    public AException(ExceptionCode code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public ExceptionCode getCode() {
        return code;
    }

    public Map getReturnMap() {
        return returnMap;
    }

}
