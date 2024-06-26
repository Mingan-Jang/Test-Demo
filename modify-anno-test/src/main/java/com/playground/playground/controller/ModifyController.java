package com.playground.playground.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.playground.playground.dao.UserRepository;
import com.playground.playground.po.UserPO;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/users")
public class ModifyController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private EntityManager entityManager;

  @PostMapping("/create")
  public String createName(@RequestParam String name, @RequestParam Integer age) {
    UserPO userPO = new UserPO();
    userPO.setName(name);
    userPO.setAge(age);
    userRepository.save(userPO);

    return "success create";
  }

  // 更新用户的姓名
  @PutMapping("/{id}/name")
  public String updateUserName(@PathVariable Long id, @RequestParam String name) {

    UserPO userPO = userRepository.findById(1L).orElseThrow();
    System.out.println("Before modifying = " + userPO.getName());

    userRepository.updateUserNameById(id, name);

    userPO = userRepository.findById(1L).orElseThrow();
    System.out.println("Search After modifying = " + userPO.getName());

    return "User name updated successfully";
  }

  @PutMapping("/{id}/nameWithoutTranscation")
  public String updateUserNameWithout(@PathVariable Long id, @RequestParam String name) {
    userRepository.updateUserNameById(id, name);
    return "User name updated successfully";
  }

  @PutMapping("/{id}/updateUserNameByIdWithFlushAutomatically")
  public String flushAutomatically(@PathVariable Long id, @RequestParam String name) {
    UserPO userPO = userRepository.findById(1L).orElseThrow();
    System.out.println("Before modifying = " + userPO.getName());

    userRepository.updateUserAgeById(id, -2);
    System.out.println("After modifying age with normal modify = " + userPO.getAge());

    userRepository.updateUserNameByIdWithFlushAutomatically(id, name);
    userPO = userRepository.findById(1L).orElseThrow();
    System.out.println("After modifying with flush modify = " + userPO.getName() +  " " + userPO.getAge());
    

    return "User name updated successfully";
  }
  
  
  @PutMapping("/{id}/updateUserNameByIdWithClearAutomatically")
  public String updateUserNameByIdWithClearAutomatically(@PathVariable Long id, @RequestParam String name) {
    UserPO userPO = userRepository.findById(1L).orElseThrow();
    System.out.println("Before modifying = " + userPO.getName());

    userRepository.updateUserAgeById(id, 25);
    System.out.println("After modifying age with normal modify = " + userPO.getAge());

    userRepository.updateUserNameByIdWithClearAutomatically(id, name);
    userPO = userRepository.findById(1L).orElseThrow();
    System.out.println("After modifying with flush modify = " + userPO.getName() +  " " + userPO.getAge());
    

    return "User name updated successfully";
  }

  @PutMapping("/{id}/nameWithoutModify")
  @Transactional
  public String updateUserNameWithModify(@PathVariable Long id, @RequestParam String name) {
    userRepository.updateUserNameByIdWithoutModify(id, name);
    return "User name updated successfully";
  }

  // 获取用户信息
  @GetMapping("/{id}")
  public UserPO getUserById(@PathVariable Long id) {

    UserPO userPO = userRepository.findById(id).orElse(null);
    System.out.println(userPO);

//      
//      System.out.println(userPO.getName());
    return userRepository.findById(id).orElse(null);
  }
}