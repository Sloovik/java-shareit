package ru.practicum.shareit.comment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class CommentMapperTest {

    private User user;
    private Item item;
    private Comment comment;
    private CommentRequestDto commentRequestDto;

    private final LocalDateTime now = LocalDateTime.now();

    @BeforeEach
    void init() {
        item = Item.builder()
                .id(1L)
                .name("Name")
                .description("New Description")
                .available(true)
                .owner(User.builder().build())
                .build();

        user = User.builder()
                .id(1L)
                .name("Name")
                .email("some@email.ru")
                .build();

        comment = Comment.builder()
                .id(1L)
                .text("new text")
                .author(user)
                .created(now)
                .item(item)
                .build();

        commentRequestDto = CommentRequestDto.builder()
                .text("text")
                .build();
    }

    @Test
    void testToDto() {
        CommentDto dto = CommentMapper.toDto(comment);

        assertNotNull(dto);
        assertEquals(comment.getId(), dto.getId());
        assertEquals(comment.getText(), dto.getText());
        assertEquals(comment.getAuthor().getName(), dto.getAuthorName());
        assertEquals(comment.getCreated(), dto.getCreated());
    }

    @Test
    void testToComment() {
        Comment model = CommentMapper.toComment(commentRequestDto, item, user);

        assertNotNull(model);
        assertEquals("text", model.getText());
        assertEquals(user, model.getAuthor());
        assertEquals(item, model.getItem());
        assertEquals(now, comment.getCreated());
    }

}
