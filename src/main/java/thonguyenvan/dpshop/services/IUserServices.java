package thonguyenvan.dpshop.services;

import thonguyenvan.dpshop.dtos.UserDTO;
import thonguyenvan.dpshop.exeptions.DataNotFoundException;
import thonguyenvan.dpshop.models.User;

public interface IUserServices {

    User createUser(UserDTO userDTO) throws Exception;

    User findByPhoneNumber(String phoneNumber) throws DataNotFoundException;

    String login(String phoneNumber, String password) throws DataNotFoundException;
}
