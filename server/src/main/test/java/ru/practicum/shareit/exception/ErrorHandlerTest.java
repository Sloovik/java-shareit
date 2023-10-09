package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.errorHandler.ErrorHandler;
import ru.practicum.shareit.errorHandler.ErrorResponse;
import ru.practicum.shareit.exeption.AccessException;
import ru.practicum.shareit.exeption.AvailableException;
import ru.practicum.shareit.exeption.BookingItemByOwnerException;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.exeption.ValidationException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ErrorHandlerTest {

    private final ErrorHandler errorHandler = new ErrorHandler();

    @Test
    void testAccessException() {
        AccessException exception = new AccessException("User is not the owner of item");
        ErrorResponse response = errorHandler.handleAccessException(exception);
        assertEquals(exception.getMessage(), response.getError());
    }

    @Test
    void testAvailableException() {
        AvailableException exception = new AvailableException("Item with is not available!");
        ErrorResponse response = errorHandler.handleBadRequestException(exception);
        assertEquals(exception.getMessage(), response.getError());
    }

    @Test
    void testBookingItemByOwnerException() {
        BookingItemByOwnerException exception = new BookingItemByOwnerException("The owner of the item cannot booking it");
        ErrorResponse response = errorHandler.handleBadRequestException(exception);
        assertEquals(exception.getMessage(), response.getError());
    }

    @Test
    void testNotFoundException() {
        NotFoundException exception = new NotFoundException("User not found!");
        ErrorResponse response = errorHandler.handleBadRequestException(exception);
        assertEquals(exception.getMessage(), response.getError());
    }

    @Test
    void testValidationException() {
        ValidationException exception = new ValidationException("The end date must be after the start date");
        ErrorResponse response = errorHandler.handleBadRequestException(exception);
        assertEquals(exception.getMessage(), response.getError());
    }

    @Test
    public void handleThrowableTest() {
        Exception exception = Mockito.mock(Exception.class);

        Mockito.when(exception.getMessage()).thenReturn("Произошла непредвиденная ошибка");

        ErrorResponse response = errorHandler.handleThrowable(exception);

        assertEquals("Error: Произошла непредвиденная ошибка", response.getError());
    }

}
