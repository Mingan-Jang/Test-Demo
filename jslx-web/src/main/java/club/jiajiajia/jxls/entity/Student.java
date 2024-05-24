package club.jiajiajia.jxls.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 学生表
 */
@Data
@AllArgsConstructor
public class Student {
    //姓名
    private String name;
    //科目
    private String subject;
    //成绩
    private Double achievement;
}
