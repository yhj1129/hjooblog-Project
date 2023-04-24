package com.hjoo.hjooblog.core.exception.csr;


import com.hjoo.hjooblog.dto.ResponseDTO;
import lombok.Getter;
import org.springframework.http.HttpStatus;
// 권한 없음
@Getter
public class ExceptionApi404 extends RuntimeException {
    public ExceptionApi404(String message) {
        super(message);
    }

    public ResponseDTO<?> body(){
        ResponseDTO<String> responseDto = new ResponseDTO<>();
        responseDto.fail(HttpStatus.NOT_FOUND, "notFound", getMessage());
        return responseDto;
    }

    public HttpStatus status(){
        return HttpStatus.NOT_FOUND;
    }
}