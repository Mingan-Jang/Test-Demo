package club.jiajiajia.jxls.test;

import club.jiajiajia.jxls.config.AutoRowHeightCommand;
import club.jiajiajia.jxls.entity.Entity;
import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.common.Context;
import org.jxls.util.JxlsHelper;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 自定义批注实现 自适应行高
 */
public class Test {
    public static void main(String[] args) throws Exception{
        InputStream templateIn= new ClassPathResource("template/template.xlsx").getInputStream();

        OutputStream os =new FileOutputStream("D:\\data\\temp.xlsx");

        Context context = new Context();

        List<Entity> data = new ArrayList<>();
        data.add(new Entity(1,"搜索2342342士大夫士大夫342","啊士大夫扣税的发挥阿斯达克粉红色快递费好可爱的所说的快捷方式打开"));
        data.add(new Entity(2,"ji是可见的福克斯开始变得福克斯的胜多负少步伐加快速度吧","速度快风力发电塑料袋放进塑料袋放进上来看地方就是立刻搭街坊路上看到房价来说"));
        data.add(new Entity(3,"ji是可见的福克斯开始变得福克斯的胜多负少步伐加快速度吧","速度快风力发电塑料袋放进塑料袋放进上来看地方就是立刻搭街坊路上看到房价来说"));
        data.add(new Entity(4,"ji是可见的福克斯开始变得福克斯的胜多负少步伐加快速度吧","速度快风力发电塑料袋放进塑料袋放进上来看地方就是立刻搭街坊路上看到房价来说"));
        data.add(new Entity(5,"ji是可见的福克斯开始变得福克斯的胜多负少步伐加快速度吧","速度快风力发电塑料袋放进塑料袋放进上来看地方就是立刻搭街坊路上看到房价来说"));

        context.putVar("data", data);


        JxlsHelper jxlsHelper = JxlsHelper.getInstance();
        jxlsHelper.getAreaBuilder().getTransformer();

        //设置自适应行高
        XlsCommentAreaBuilder.addCommandMapping("autoRowHeight", AutoRowHeightCommand.class);

        jxlsHelper.processTemplate(templateIn, os, context);

    }
}
