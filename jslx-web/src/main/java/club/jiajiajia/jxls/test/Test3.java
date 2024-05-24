package club.jiajiajia.jxls.test;

import club.jiajiajia.jxls.config.AutoRowHeightCommand;
import club.jiajiajia.jxls.entity.Entity;
import club.jiajiajia.jxls.entity.Group;
import club.jiajiajia.jxls.entity.Student;
import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.springframework.core.io.ClassPathResource;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 更复杂的导出方式（统计以及含有公式等）
 */
public class Test3 {
    public static void main(String[] args) throws Exception{
        InputStream templateIn= new ClassPathResource("template/template3.xlsx").getInputStream();

        OutputStream os =new FileOutputStream("D:\\data\\temp3.xlsx");

        Context context = new Context();

        List<Student> data = new ArrayList<>();
        data.add(new Student("张三","语文",99.9));
        data.add(new Student("张三","数学",88.0));
        data.add(new Student("张三","英语",88.9));

        data.add(new Student("李四","语文",99.9));
        data.add(new Student("李四","数学",76.9));

        data.add(new Student("王五","语文",94D));
        data.add(new Student("王五","数学",33D));
        data.add(new Student("王五","英语",45.8));
        data.add(new Student("王五","物理",49.8));
        data.add(new Student("王五","化学",77.8));

        data.add(new Student("赵六","语文",97.2));
        data.add(new Student("赵六","英语",86.9));

        //分组进行统计，姓名相同的放同一个map中,你不用考虑万一重名了怎么办~

        List<Group> groups = new ArrayList<>();

        Map<String,List<Student>> stumap=new HashMap<>();
        for (Student datum : data) {
            if(stumap.containsKey(datum.getName())){
                List<Student> students = stumap.get(datum.getName());
                students.add(datum);
            }else{
                List<Student> students = new ArrayList<>();
                students.add(datum);
                Group group=new Group(datum.getName(),students);
                groups.add(group);
                stumap.put(datum.getName(),students);
            }
        }

        context.putVar("data", groups);

        JxlsHelper jxlsHelper = JxlsHelper.getInstance();
        jxlsHelper.getAreaBuilder().getTransformer();

        //设置自适应行高
        XlsCommentAreaBuilder.addCommandMapping("autoRowHeight", AutoRowHeightCommand.class);

        jxlsHelper.processTemplate(templateIn, os, context);

    }
}
