package com.transport.app.rest.mapper;

import com.transport.app.rest.domain.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static User toUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .userName(userDto.getUserName())
                .password(userDto.getPassword())
                .resetToken(userDto.getResetToken())
                .fullName(userDto.getFullName())
                .companyName(userDto.getCompanyName())
                .address(userDto.getAddress())
                .zip(userDto.getZip())
                .latitude(userDto.getLatitude())
                .longitude(userDto.getLongitude())
                .phones(userDto.getPhones())
                .email(userDto.getEmail())
                .type(userDto.getType())
                .build();
    }

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .password(user.getPassword())
                .resetToken(user.getResetToken())
                .fullName(user.getFullName())
                .companyName(user.getCompanyName())
                .address(user.getAddress())
                .zip(user.getZip())
                .latitude(user.getLatitude())
                .longitude(user.getLongitude())
                .phones(user.getPhones())
                .email(user.getEmail())
                .type(user.getType())
                .build();
    }

    public static User toUpdatedUser(User user, User userUpdate) {
        user.setId(userUpdate.getId() == null ? user.getId() : userUpdate.getId());
        user.setUserName(userUpdate.getUserName() == null ? user.getUserName() : userUpdate.getUserName());
        user.setPassword(userUpdate.getPassword() == null ? user.getPassword() : userUpdate.getPassword());
        user.setResetToken(userUpdate.getResetToken() == null ? user.getResetToken() : userUpdate.getResetToken());
        user.setFullName(userUpdate.getFullName() == null ? user.getFullName() : userUpdate.getFullName());
        user.setCompanyName(userUpdate.getCompanyName() == null ? user.getCompanyName() : userUpdate.getCompanyName());
        user.setAddress(userUpdate.getAddress() == null ? user.getAddress() : userUpdate.getAddress());
        user.setZip(userUpdate.getZip() == null ? user.getZip() : userUpdate.getZip());
        user.setLatitude(userUpdate.getLatitude() == null ? user.getLatitude() : userUpdate.getLatitude());
        user.setLongitude(userUpdate.getLongitude() == null ? user.getLongitude() : userUpdate.getLongitude());
        user.setPhones(userUpdate.getPhones() == null ? user.getPhones() : userUpdate.getPhones());
        user.setEmail(userUpdate.getEmail() == null ? user.getEmail() : userUpdate.getEmail());
        user.setType(userUpdate.getType() == null ? user.getType() : userUpdate.getType());
        return user;
    }

    public static List<User> toUsers(List<UserDto> userDtos) {
        return userDtos.stream().map(c -> toUser(c)).collect(Collectors.toList());
    }

    public static List<UserDto> toUserDtos(List<User> users) {
        return users.stream().map(c -> toUserDto(c)).collect(Collectors.toList());
    }
}
