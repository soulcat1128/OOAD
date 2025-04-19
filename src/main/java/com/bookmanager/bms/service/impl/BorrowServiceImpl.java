package com.bookmanager.bms.service.impl;

import com.bookmanager.bms.mapper.BorrowMapper;
import com.bookmanager.bms.model.Borrow;
import com.bookmanager.bms.service.BorrowService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@Service
public class BorrowServiceImpl implements BorrowService {

    @Resource
    private BorrowMapper borrowMapper;

    @Override
    public Integer getCount() {
        return borrowMapper.selectCount();
    }

    @Override
    public Integer getSearchCount(Map<String, Object> params) {
        return borrowMapper.selectCountBySearch(params);
    }

    @Override
    public List<Borrow> searchBorrowsByPage(Map<String, Object> params) {
        List<Borrow> borrows = borrowMapper.selectBySearch(params);
        // 添加string類型的時間顯示
        for(Borrow borrow : borrows) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if(borrow.getBorrowtime() != null) borrow.setBorrowtimestr(simpleDateFormat.format(borrow.getBorrowtime()));
            if(borrow.getReturntime() != null) borrow.setReturntimestr(simpleDateFormat.format(borrow.getReturntime()));
            if(borrow.getExpectedReturnTime() != null) borrow.setExpectedReturnTimeStr(simpleDateFormat.format(borrow.getExpectedReturnTime()));
        }
        return borrows;
    }

    @Override
    public Integer addBorrow(Borrow borrow) {
        // 將string類型的時間重新調整
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            borrow.setBorrowtime(simpleDateFormat.parse(borrow.getBorrowtimestr()));
            borrow.setReturntime(simpleDateFormat.parse(borrow.getReturntimestr()));

            // 新增：設定預期歸還時間（如果為null）
            if (borrow.getExpectedReturnTime() == null) {
                // 預設為借閱時間 + 30天
                java.util.Calendar calendar = java.util.Calendar.getInstance();
                calendar.setTime(borrow.getBorrowtime());
                calendar.add(java.util.Calendar.DAY_OF_MONTH, 30);
                borrow.setExpectedReturnTime(calendar.getTime());
            }

            // 新增：設定是否延長借閱（如果為null）
            if (borrow.getIsExtended() == null) {
                borrow.setIsExtended(0); // 0表示未延長
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return borrowMapper.insertSelective(borrow);
    }

    // 不會調整時間格式的add
    @Override
    public Integer addBorrow2(Borrow borrow) {
        // 新增：設定預期歸還時間（如果為null）
        if (borrow.getExpectedReturnTime() == null) {
            // 預設為借閱時間 + 30天
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.setTime(borrow.getBorrowtime());
            calendar.add(java.util.Calendar.DAY_OF_MONTH, 30);
            borrow.setExpectedReturnTime(calendar.getTime());
        }

        // 新增：設定是否延長借閱（如果為null）
        if (borrow.getIsExtended() == null) {
            borrow.setIsExtended(0); // 0表示未延長
        }

        return borrowMapper.insertSelective(borrow);
    }

    @Override
    public Integer deleteBorrow(Borrow borrow) {
        // 先查詢有沒有還書
        Borrow borrow1 = borrowMapper.selectByPrimaryKey(borrow.getBorrowid());
        if(borrow1.getReturntime() == null) return 0;
        return borrowMapper.deleteByPrimaryKey(borrow.getBorrowid());
    }

    @Override
    public Integer deleteBorrows(List<Borrow> borrows) {
        int count = 0;
        for(Borrow borrow : borrows) {
            count += deleteBorrow(borrow);
        }
        return count;
    }

    @Override
    public Integer updateBorrow(Borrow borrow) {
        // 將string類型的時間重新調整
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            borrow.setBorrowtime(simpleDateFormat.parse(borrow.getBorrowtimestr()));
            borrow.setReturntime(simpleDateFormat.parse(borrow.getReturntimestr()));
            borrow.setExpectedReturnTimeStr(simpleDateFormat.format(borrow.getExpectedReturnTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return borrowMapper.updateByPrimaryKeySelective(borrow);
    }

    // 不調整時間格式的更新
    @Override
    public Integer updateBorrow2(Borrow borrow) {
        return borrowMapper.updateByPrimaryKeySelective(borrow);
    }

    @Override
    public Borrow queryBorrowsById(Integer borrowid) {
        return borrowMapper.selectByPrimaryKey(borrowid);
    }

}
