package com.bookmanager.bms.web;

import com.bookmanager.bms.model.BookType;
import com.bookmanager.bms.service.BookTypeService;
import com.bookmanager.bms.utils.MyResult;
import com.bookmanager.bms.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/bookType")
public class BookTypeController {

    @Autowired
    BookTypeService bookTypeService;

    // 獲得數量
    @GetMapping(value = "/getCount")
    public Integer getCount(){
        return bookTypeService.getCount();
    }

    // 查詢所有類型
    @GetMapping(value = {"/queryBookTypes", "/reader/queryBookTypes"})
    public List<BookType> queryBookTypes(){
        return bookTypeService.queryBookTypes();
    }

    // 分頁查詢圖書類型 params: {page, limit, booktypename}
    @GetMapping(value = "/queryBookTypesByPage")
    public Map<String, Object> queryBookTypesByPage(@RequestParam Map<String, Object> params){
        MyUtils.parsePageParams(params);
        int count = bookTypeService.getSearchCount(params);
        List<BookType> bookTypes = bookTypeService.searchBookTypesByPage(params);
        return MyResult.getListResultMap(0, "success", count, bookTypes);
    }

    // 添加類型
    @PostMapping(value = "/addBookType")
    public Integer addBookType(@RequestBody BookType bookType){
        return bookTypeService.addBookType(bookType);
    }

    // 刪除類型
    @DeleteMapping(value = "/deleteBookType")
    public Integer deleteBookType(@RequestBody BookType bookType){
        return bookTypeService.deleteBookType(bookType);
    }

    // 刪除一些類型
    @DeleteMapping(value = "/deleteBookTypes")
    public Integer deleteBookTypes(@RequestBody List<BookType> bookTypes){
        return bookTypeService.deleteBookTypes(bookTypes);
    }

    // 更新類型
    @PutMapping(value = "/updateBookType")
    public Integer updateBookType(@RequestBody BookType bookType){
        return bookTypeService.updateBookType(bookType);
    }
}
