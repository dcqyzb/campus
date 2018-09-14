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
@TableName("tb_stuborrowrecord")
public class StuBorrowRecord extends Model<StuBorrowRecord> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "uuid", type = IdType.UUID)
    private String uuid;
    private String borrowRecord;
    private String studentID;


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getBorrowRecord() {
        return borrowRecord;
    }

    public void setBorrowRecord(String borrowRecord) {
        this.borrowRecord = borrowRecord;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    @Override
    protected Serializable pkVal() {
        return this.uuid;
    }

    @Override
    public String toString() {
        return "Stuborrowrecord{" +
        "uuid=" + uuid +
        ", borrowRecord=" + borrowRecord +
        ", studentID=" + studentID +
        "}";
    }
}
