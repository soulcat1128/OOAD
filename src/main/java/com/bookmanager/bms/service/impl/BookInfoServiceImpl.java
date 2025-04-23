package com.bookmanager.bms.service.impl;

import com.bookmanager.bms.mapper.BookInfoMapper;
import com.bookmanager.bms.model.BookInfo;
import com.bookmanager.bms.service.BookInfoService;
import com.bookmanager.bms.utils.MyResult;
import com.bookmanager.bms.utils.MyUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class BookInfoServiceImpl implements BookInfoService {

    @Resource
    private BookInfoMapper bookInfoMapper;

    @Override
    public Integer getCount() {
        return bookInfoMapper.selectCount();
    }

    @Override
    public List<BookInfo> queryBookInfos() {
        return bookInfoMapper.selectAll();
    }

    @Override
    public BookInfo queryBookInfoById(Integer bookid) {
        return bookInfoMapper.selectByPrimaryKey(bookid);
    }

    @Override
    public Integer getSearchCount(Map<String, Object> params) {
        return bookInfoMapper.selectCountBySearch(params);
    }

    @Override
    public Map<String, Object> searchBookInfosByPage(Map<String, Object> params) {
        MyUtils.parsePageParams(params);
        int count = getSearchCount(params);  // 獲得總數
        List<BookInfo> bookInfos = bookInfoMapper.selectBySearch(params);  // 分頁查詢
        return MyResult.getListResultMap(0, "success", count, bookInfos);
    }

    @Override
    public Integer addBookInfo(BookInfo bookInfo) {
        return bookInfoMapper.insertSelective(bookInfo);
    }

    @Override
    public Integer deleteBookInfo(BookInfo bookInfo) {
        int count = 0;
        try{
            count = bookInfoMapper.deleteByPrimaryKey(bookInfo.getBookid());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public Integer deleteBookInfos(List<BookInfo> bookInfos) {
        int count = 0;
        for(BookInfo bookInfo : bookInfos) {
            count += deleteBookInfo(bookInfo);
        }
        return count;
    }

    @Override
    public Integer updateBookInfo(BookInfo bookInfo) {
        return bookInfoMapper.updateByPrimaryKeySelective(bookInfo);
    }

}
