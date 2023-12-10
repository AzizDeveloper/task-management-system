package dev.aziz.taskmanagementsystem.mappers;

import dev.aziz.taskmanagementsystem.dtos.SignUpDto;
import dev.aziz.taskmanagementsystem.dtos.UserDto;
import dev.aziz.taskmanagementsystem.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User user);
    User toUser(UserDto userDto);

    List<UserDto> usersToUserDtos(List<User> users);

    @Mapping(target = "password", ignore = true)
    User signUpToUser(SignUpDto signUpDto);

}