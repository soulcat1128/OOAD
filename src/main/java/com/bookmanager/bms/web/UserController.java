package com.bookmanager.bms.web;

import com.bookmanager.bms.model.User;
import com.bookmanager.bms.service.UserService;
import com.bookmanager.bms.utils.MyResult;
import com.bookmanager.bms.utils.MyUtils;
import com.bookmanager.bms.utils.TokenProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    UserService userService;

    // 登錄
    @RequestMapping(value = "/login")
    public Map<String, Object> login(@RequestBody User user) {
        // 登錄
        User userObj = userService.login(user);
        if(userObj == null) {   // 帳號或密碼錯誤
            // 返回結果對象
            return MyResult.getResultMap(420, "帳號或密碼錯誤");
        } else {    // 帳號密碼正確
            // 創建token
            String token = TokenProcessor.getInstance().makeToken();
            // 保存到Redis
            userService.saveUser(token, userObj);
            // 返回結果對象
            return MyResult.getResultMap(200, "登錄成功",
                    new HashMap<String, String>(){{ put("token", token); }});
        }
    }

    // 查看用戶資訊
    @RequestMapping(value = "/info")
    public Map<String, Object> info(String token) {
        // 從redis中取用戶
        User user = userService.getUser(token);
        if(user == null) {  // 獲取失敗
            return MyResult.getResultMap(420, "獲取用戶資訊失敗");
        } else {    // 獲取成功
            return MyResult.getResultMap(200, "獲取用戶資訊成功", user);
        }
    }

    // 退出登錄
    @RequestMapping(value = "/logout")
    public Map<String, Object> logout(String token) {
        // 從redis中移除用戶
        userService.removeUser(token);
        return MyResult.getResultMap(200, "退出登錄成功" );
    }

    // 註冊
    @RequestMapping(value = "/register")
    public Integer register(String username, String password){
        return userService.register(username, password);
    }

    // 修改密碼
    @RequestMapping(value = {"/alterPassword", "reader/alterPassword"})
    public Integer alterPassword(Integer userid, String username, Byte isadmin, String oldPassword, String newPassword){
        //檢查舊密碼是否正確
        User userObj = new User();
        userObj.setUserid(userid);
        userObj.setUsername(username);
        userObj.setUserpassword(oldPassword);
        userObj.setIsadmin(isadmin);

        User user = userService.login(userObj);
        if(user == null) {  //舊密碼不正確
            return 0;
        } else {    //舊密碼正確，設置新密碼
            userService.setPassword(userObj.getUserid(), newPassword);
            return 1;
        }
    }

    // 獲得數量
    @GetMapping(value = "/getCount")
    public Integer getCount(){
        return userService.getCount();
    }

    // 查詢所有用戶
    @GetMapping(value = "/queryUsers")
    public List<User> queryUsers(){
        return userService.queryUsers();
    }

    // 分頁查詢用戶 params: {page, limit, username}
    @GetMapping(value = "/queryUsersByPage")
    public Map<String, Object> queryUsersByPage(@RequestParam Map<String, Object> params){
        MyUtils.parsePageParams(params);
        int count = userService.getSearchCount(params);
        List<User> users = userService.searchUsersByPage(params);
        return MyResult.getListResultMap(0, "success", count, users);
    }

    // 添加用戶
    @PostMapping(value = "/addUser")
    public Integer addUser(@RequestBody User user){
        return userService.addUser(user);
    }

    // 刪除用戶
    @DeleteMapping(value = "/deleteUser")
    public Integer deleteUser(@RequestBody User user){
        return userService.deleteUser(user);
    }

    // 刪除一些用戶
    @DeleteMapping(value = "/deleteUsers")
    public Integer deleteUsers(@RequestBody List<User> users){
        return userService.deleteUsers(users);
    }

    // 更新用戶
    @RequestMapping(value = "/updateUser")
    public Integer updateUser(@RequestBody User user){
        return userService.updateUser(user);
    }
}
