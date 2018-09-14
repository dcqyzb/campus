package com.mit.campus.rest.modular.book.model;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author lw
 * @company mitesofor
 * @since 2018-09-06
 */
@TableName("tb_subbookcirculationinfo")
public class SubBookCirculationInfo extends Model<SubBookCirculationInfo> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "uuid", type = IdType.UUID)
    private String uuid;
    private String bookName;
    private String bookUseRate;
    private String borrowTimes;
    private String duplicateNum;
    private String newBookTotalNum;
    private String newBookTotalType;
    private String totalNum;
    private String year;


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookUseRate() {
        return bookUseRate;
    }

    public void setBookUseRate(String bookUseRate) {
        this.bookUseRate = bookUseRate;
    }

    public String getBorrowTimes() {
        return borrowTimes;
    }

    public void setBorrowTimes(String borrowTimes) {
        this.borrowTimes = borrowTimes;
    }

    public String getDuplicateNum() {
        return duplicateNum;
    }

    public void setDuplicateNum(String duplicateNum) {
        this.duplicateNum = duplicateNum;
    }

    public String getNewBookTotalNum() {
        return newBookTotalNum;
    }

    public void setNewBookTotalNum(String newBookTotalNum) {
        this.newBookTotalNum = newBookTotalNum;
    }

    public String getNewBookTotalType() {
        return newBookTotalType;
    }

    public void setNewBookTotalType(String newBookTotalType) {
        this.newBookTotalType = newBookTotalType;
    }

    public String getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(String totalNum) {
        this.totalNum = totalNum;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }

    @Override
    public String toString() {
        return "Subbookcirculationinfo{" +
        "uuid=" + uuid +
        ", bookName=" + bookName +
        ", bookUseRate=" + bookUseRate +
        ", borrowTimes=" + borrowTimes +
        ", duplicateNum=" + duplicateNum +
        ", newBookTotalNum=" + newBookTotalNum +
        ", newBookTotalType=" + newBookTotalType +
        ", totalNum=" + totalNum +
        ", year=" + year +
        "}";
    }
}
