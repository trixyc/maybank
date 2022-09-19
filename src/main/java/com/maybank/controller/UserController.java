package com.maybank.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

import com.google.gson.Gson;
import com.maybank.dto.CreateUserDto;
import com.maybank.dto.UserDto;
import com.maybank.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("api/user")
public class UserController {
	
    Logger logger = LoggerFactory.getLogger(UserController.class);

	static final int PAGE_SIZE = 10;

	@Autowired
	UserService userService;
	
	@GetMapping("/getAll/{currentPage}")
	public ResponseEntity<Page<UserDto>> getAllUsers(@PathVariable int currentPage) {
		logger.info("REQUEST [GET], mapping = [/getAll/"+currentPage+"]");
		Pageable paging = PageRequest.of(currentPage, PAGE_SIZE);
		Page<UserDto> usersPage = userService.getAllUsers(paging);
		logger.info("RESPONSE [GET], mapping = [/getAll/"+currentPage+"] ,body:["+new Gson().toJson(usersPage)+"]" );
		return new ResponseEntity<Page<UserDto>>(usersPage,HttpStatus.OK);
	}
	
	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getUser(@PathVariable String userId) {
		logger.info("REQUEST [GET], mapping = [/"+userId+"]");
		UserDto userDto = userService.getUser(userId);
		logger.info("RESPONSE [GET], mapping = [/"+userId+"] ,body:["+new Gson().toJson(userDto)+"]" );
		return new ResponseEntity<UserDto>(userDto, new HttpHeaders(),HttpStatus.OK);
	}
	
	@GetMapping("/getGender/{userId}")
	public ResponseEntity<UserDto> getUserGender(@PathVariable String userId) {
		logger.info("REQUEST [GET], mapping = [/getGender/"+userId+"]");
		UserDto userDto = userService.getUserGender(userId);
		logger.info("RESPONSE [GET], mapping = [/getGender/"+userId+"] ,body:["+new Gson().toJson(userDto)+"]" );
		return new ResponseEntity<UserDto>(userDto, new HttpHeaders(),HttpStatus.OK);
	}
	
	@PostMapping
	public @ResponseBody ResponseEntity<HttpStatus> addUser(@RequestBody CreateUserDto createUserDto) {
		logger.info("REQUEST [POST], mapping = [], body=["+new Gson().toJson(createUserDto)+"]");

		try {
			userService.addUser(createUserDto);
			UserDto userDto = userService.getUserByName(createUserDto.getName());
			logger.info("RESPONSE [POST], mapping = [], body=["+new Gson().toJson(userDto)+"]");
		} catch (BadRequest e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@PostMapping("/rollback")
	public @ResponseBody ResponseEntity<HttpStatus> addUserWithRollback(@RequestBody CreateUserDto createUserDto) {
		logger.info("REQUEST [POST], mapping = [/rollback], body=["+new Gson().toJson(createUserDto)+"]");

		try {
			userService.addUserWithDemoRollback(createUserDto);
		} catch (BadRequest e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			logger.info("RESPONSE [POST], mapping = [/rollback], body=["+e+"]");
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(HttpStatus.CREATED);
	}
	
	@PutMapping("/{userId}")
	public @ResponseBody ResponseEntity<HttpStatus> updateUser(@PathVariable String userId, @RequestBody CreateUserDto createUserDto) {
		logger.info("REQUEST [PUT], mapping = [/"+userId+"], body=["+new Gson().toJson(createUserDto)+"]");

		try {
			userService.updateUser(userId, createUserDto);
			UserDto userDto = userService.getUser(userId);
			logger.info("RESPONSE [PUT], mapping = [/"+userId+"], body=["+new Gson().toJson(userDto)+"]");
		} catch (BadRequest e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@DeleteMapping("/{userId}")
	public @ResponseBody ResponseEntity<HttpStatus>  deleteUser(@PathVariable String userId) {
		logger.info("REQUEST [DELETE], mapping = [/"+userId+"]");
		try {
			userService.deleteUser(userId);
			logger.info("REQUEST [DELETE], mapping = [/"+userId+"] successfully deleted.");
		} catch (BadRequest e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(HttpStatus.OK);	
	}
}
