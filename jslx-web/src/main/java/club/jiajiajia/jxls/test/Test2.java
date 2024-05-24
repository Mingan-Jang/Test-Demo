package club.jiajiajia.jxls.test;

import club.jiajiajia.jxls.config.AutoRowHeightCommand;
import club.jiajiajia.jxls.entity.Entity;
import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.springframework.core.io.ClassPathResource;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 一般导出（导出一个列表数据）
 */
public class Test2 {
    public static void main(String[] args) throws Exception{
        InputStream templateIn= new ClassPathResource("template/template2.xlsx").getInputStream();

        OutputStream os =new FileOutputStream("D:\\data\\temp2.xlsx");

        Context context = new Context();

        List<Entity> data = new ArrayList<>();
        data.add(new Entity(1,"张三","张三的简介"));
        data.add(new Entity(2,"李四","里斯的简介"));
        data.add(new Entity(3,"王五","王五的简介"));
        data.add(new Entity(4,"赵六","赵六的简介"));

        context.putVar("data", data);

        JxlsHelper jxlsHelper = JxlsHelper.getInstance();
        jxlsHelper.getAreaBuilder().getTransformer();

        //设置自适应行高
        XlsCommentAreaBuilder.addCommandMapping("autoRowHeight", AutoRowHeightCommand.class);

        jxlsHelper.processTemplate(templateIn, os, context);

    }
}
