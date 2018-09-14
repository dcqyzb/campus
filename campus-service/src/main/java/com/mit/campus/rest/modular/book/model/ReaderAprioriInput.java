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
@TableName("tb_readeraprioriinput")
public class ReaderAprioriInput extends Model<ReaderAprioriInput> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "uuid", type = IdType.UUID)
    private String uuid;
    private String borrowTimes;
    private String cluster;
    private String collegeName;
    private String dormitory;
    private String major;
    private String readerID;
    private String readerIdentity;
    private String readerName;
    private String readerSex;
    private String schoolYear;


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getBorrowTimes() {
        return borrowTimes;
    }

    public void setBorrowTimes(String borrowTimes) {
        this.borrowTimes = borrowTimes;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public String getDormitory() {
        return dormitory;
    }

    public void setDormitory(String dormitory) {
        this.dormitory = dormitory;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getReaderID() {
        return readerID;
    }

    public void setReaderID(String readerID) {
        this.readerID = readerID;
    }

    public String getReaderIdentity() {
        return readerIdentity;
    }

    public void setReaderIdentity(String readerIdentity) {
        this.readerIdentity = readerIdentity;
    }

    public String getReaderName() {
        return readerName;
    }

    public void setReaderName(String readerName) {
        this.readerName = readerName;
    }

    public String getReaderSex() {
        return readerSex;
    }

    public void setReaderSex(String readerSex) {
        this.readerSex = readerSex;
    }

    public String getSchoolYear() {
        return schoolYear;
    }

    public void setSchoolYear(String schoolYear) {
        this.schoolYear = schoolYear;
    }

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }

    @Override
    public String toString() {
        return "Readeraprioriinput{" +
        "uuid=" + uuid +
        ", borrowTimes=" + borrowTimes +
        ", cluster=" + cluster +
        ", collegeName=" + collegeName +
        ", dormitory=" + dormitory +
        ", major=" + major +
        ", readerID=" + readerID +
        ", readerIdentity=" + readerIdentity +
        ", readerName=" + readerName +
        ", readerSex=" + readerSex +
        ", schoolYear=" + schoolYear +
        "}";
    }
}
