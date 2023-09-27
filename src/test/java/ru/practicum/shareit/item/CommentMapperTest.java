package ru.practicum.shareit.item;

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

    private Item item;
    private User user1;
    private Comment comment;
    private CommentRequestDto commentRequestDto;

    @BeforeEach
    void init() {
        User user = User.builder()
                .id(1L)
                .name("test")
                .email("test@email.ru")
                .build();
        user1 = User.builder()
                .id(1L)
                .name("test")
                .email("test@email.ru")
                .build();

        item = Item.builder()
                .id(1L)
                .name("test")
                .description("test")
                .available(true)
                .owner(user)
                .requestId(1L)
                .build();

        commentRequestDto = CommentRequestDto.builder()
                .text("test")
                .build();

        comment = Comment.builder()
                .text(commentRequestDto.getText())
                .item(item)
                .author(user1)
                .created(LocalDateTime.now())
                .build();
    }

    @Test
    void testToDto() {
        CommentDto dto = CommentMapper.toDto(comment);

        assertNotNull(dto);
        assertEquals(dto.getId(), comment.getId());
        assertEquals(dto.getText(), comment.getText());
        assertEquals(dto.getAuthorName(), comment.getAuthor().getName());
        assertEquals(dto.getCreated(), comment.getCreated());
    }

    @Test
    void testToComment() {
        Comment model = CommentMapper.toComment(commentRequestDto, item, user1);

        assertNotNull(model);
        assertEquals(model.getText(), commentRequestDto.getText());
        assertEquals(model.getAuthor().getName(), user1.getName());
        assertEquals(model.getItem().getName(), item.getName());
    }

}
