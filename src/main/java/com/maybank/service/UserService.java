package com.maybank.service;

import java.sql.SQLException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.maybank.dto.CreateUserDto;
import com.maybank.dto.UserDto;
import com.maybank.dto.UserGenderDto;
import com.maybank.entity.User;
import com.maybank.repository.UserRepository;
import com.maybank.util.MapperUtil;

@Service
public class UserService {
	
	@Autowired
	MapperUtil mapperUtil;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	RestTemplate template;
	
	final static String GENDER_API = "https://api.genderize.io/?name=";
	
	public Page<UserDto> getAllUsers(Pageable pageable){
		return mapperUtil.entityPageToDtoPage(userRepository.findAll(pageable), UserDto.class);
	}
	
	public UserDto getUser(String userId) {
		return MapperUtil.modelMapper.map(userRepository.findById(userId).get(), UserDto.class);
	}
	
	public UserDto getUserByName(String name) {
		return MapperUtil.modelMapper.map(userRepository.findByName(name), UserDto.class);
	}
	
	public void addUser(CreateUserDto createUserDto) {
		userRepository.save(MapperUtil.modelMapper.map(createUserDto, User.class));
	}

	public void updateUser(String userId, CreateUserDto createUserDto) {
		User user = userRepository.findById(userId).get();
		user.setName(createUserDto.getName());
		userRepository.save(user);
	}
	
	public void deleteUser(String userId) {
		User user = userRepository.findById(userId).get();
		userRepository.delete(user);
	}
	
	public UserDto getUserGender(String userId){
		User user = userRepository.findById(userId).get();
		//Make API call to retrieve gender
		UserGenderDto userGenderDto = template.getForObject(GENDER_API + user.getName(), UserGenderDto.class);
		UserDto userDto = MapperUtil.modelMapper.map(userGenderDto, UserDto.class);
		userDto.setId(userId);
		return userDto;
	}
	
	@Transactional( rollbackOn = SQLException.class)
	public void addUserWithDemoRollback(CreateUserDto createUserDto) throws Exception {
		MapperUtil.modelMapper.map(createUserDto, User.class);
	    throw new SQLException();
	}
}
