package club.jiajiajia.jxls.config;

import org.apache.poi.ss.usermodel.Row;
import org.jxls.area.Area;
import org.jxls.command.AbstractCommand;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.common.Size;
import org.jxls.transform.poi.PoiTransformer;

/**
 * 自定义指令配置，在批注中添加
 * 例如：
 * jx:autoRowHeight(lastCell ="C3")
 * lastCell参数是指定该批注作用的范围
 */
public class AutoRowHeightCommand extends AbstractCommand {

    /**
     * 返回批注的名称
     * @return
     */
    @Override
    public String getName() {
        return "autoRowHeight";
    }

    /**
     * @param cellRef
     * @param context
     * @return
     */
    @Override
    public Size applyAt(CellRef cellRef, Context context) {
        Area area=getAreaList().get(0);
        Size size = area.applyAt(cellRef, context);

        PoiTransformer transformer = (PoiTransformer) area.getTransformer();
        Row row = transformer.getWorkbook().getSheet(cellRef.getSheetName()).getRow(cellRef.getRow());
        row.setHeight((short) -1);

        return size;
    }
}
