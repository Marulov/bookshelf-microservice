package com.bookshelf.libraryservice.client;

import com.bookshelf.libraryservice.exception.BookNotFoundException;
import com.bookshelf.libraryservice.exception.ExceptionMessage;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class RetrieveMessageErrorCode implements ErrorDecoder {

    private final ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        ExceptionMessage message = null;
        try (InputStream body = response.body().asInputStream()) {
            message = ExceptionMessage.builder()
                    .timestamp(response.headers().get("date").toArray()[0].toString())
                    .status(response.status())
                    .error(HttpStatus.resolve(response.status()).getReasonPhrase())
                    .message(IOUtils.toString(body, StandardCharsets.UTF_8))
                    .path(response.request().url())
                    .build();

        } catch (IOException exception) {
            return new Exception(exception.getMessage());
        }

        switch (response.status()) {
            case 404:
                throw new BookNotFoundException(message);
            default:
                return errorDecoder.decode(methodKey, response);
        }
    }
}