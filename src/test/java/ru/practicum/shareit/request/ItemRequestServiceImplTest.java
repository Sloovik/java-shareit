package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.dto.CreateItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemRequestServiceImplTest {

    private final EntityManager em;
    private final UserService userService;
    private final ItemRequestService itemRequestService;

    private CreateItemRequestDto createItemRequestDto;
    private ItemRequestResponseDto itemRequestResponseDto;

    private final Pageable pageRequest = PageRequest.of(0, 20, Sort.by("created").descending());

    @BeforeEach
    void init() {
        createItemRequestDto = CreateItemRequestDto.builder()
                .description("test")
                .build();

        userService.create(UserDto.builder()
                .id(1L)
                .name("test")
                .email("test@email.ru")
                .build());

        itemRequestResponseDto = ItemRequestResponseDto.builder()
                .id(1L)
                .description("test")
                .created(LocalDateTime.now())
                .build();
    }

    @Test
    void testFindAllByUserId() {
        itemRequestService.create(1L, createItemRequestDto);

        assertEquals(1L, itemRequestService.findById(1L, 1L).getId());
    }

    @Test
    void testFindAllByUserIdNotFoundUserError() {
        itemRequestService.create(1L, createItemRequestDto);

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                itemRequestService.findAllByUserId(5L));

        assertThat(exception.getMessage(), equalTo(String.format("User with id %x not found!", 5L)));
    }

    @Test
    void testFindAll() {
        UserDto userDto = UserDto.builder()
                .id(2L)
                .name("user")
                .email("user@email.ru")
                .build();

        userService.create(userDto);
        itemRequestService.create(1L, createItemRequestDto);
        itemRequestService.create(2L, createItemRequestDto);

        List<ItemRequestWithItemsDto> items = itemRequestService.findAll(2L, pageRequest);

        assertEquals(items.size(), 1);
        assertEquals(items.get(0).getId(), 1L);
    }

    @Test
    void testFindAllNotFoundUserError() {
        itemRequestService.create(1L, createItemRequestDto);

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                itemRequestService.findAll(5L, pageRequest));

        assertThat(exception.getMessage(), equalTo(String.format("User with id %x not found!", 5L)));
    }

    @Test
    void testFindById() {
        itemRequestService.create(1L, createItemRequestDto);

        assertThat(itemRequestService.findById(1L, 1L).getId(), equalTo(1L));
        assertThat(itemRequestService.findById(1L, 1L).getDescription(), equalTo("test"));
    }

    @Test
    void testFindByIdNotFoundUserError() {
        itemRequestService.create(1L, createItemRequestDto);

        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                itemRequestService.findById(5L, 1L));

        assertThat(exception.getMessage(), equalTo(String.format("User with id %x not found!", 5L)));
    }

    @Test
    void testCreate() {
        itemRequestService.create(1L, createItemRequestDto);

        TypedQuery<ItemRequest> query = em.createQuery(
                "SELECT i FROM ItemRequest i WHERE i.id = :id", ItemRequest.class);

        ItemRequest itemRequest = query.setParameter("id", itemRequestResponseDto.getId()).getSingleResult();

        assertThat(itemRequest.getId(), equalTo(1L));
        assertThat(itemRequest.getDescription(), equalTo(itemRequestResponseDto.getDescription()));
    }

    @Test
    void testCreateItemRequestUserNotFound() {
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                itemRequestService.create(5L, createItemRequestDto));

        assertThat(exception.getMessage(), equalTo(String.format("User with id %x not found!", 5L)));
    }

}
