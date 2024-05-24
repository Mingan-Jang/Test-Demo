package club.jiajiajia.jxls.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

/**
 * 组
 */
@Data
@AllArgsConstructor
public class Group {
    private String name;
    private List<Student> students;
}
