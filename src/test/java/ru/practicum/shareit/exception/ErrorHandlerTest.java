package ru.practicum.shareit.exception;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.errorHandler.ErrorHandler;
import ru.practicum.shareit.errorHandler.ErrorResponse;

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
        InvalidStateException exception = new InvalidStateException("Item is not available!");
        ErrorResponse response = errorHandler.handleBadRequestException(exception);
        assertEquals(exception.getMessage(), response.getError());
    }

    @Test
    void testBookingItemByOwnerException() {
        BookingItemByOwnerException exception = new BookingItemByOwnerException("The owner of the item cannot book it");
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
    void testPageableParamsException() {
        PageableParamsException exception = new PageableParamsException("Invalid size values entered");
        ErrorResponse response = errorHandler.handleBadRequestException(exception);
        assertEquals(exception.getMessage(), response.getError());
    }

    @Test
    void testStateException() {
        StateException exception = new StateException("Unknown state");
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