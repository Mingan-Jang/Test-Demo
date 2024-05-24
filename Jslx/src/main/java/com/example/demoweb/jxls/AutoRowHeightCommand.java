package com.example.demoweb.jxls;

import org.apache.poi.ss.usermodel.Row;
import org.jxls.area.Area;
import org.jxls.command.AbstractCommand;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.common.Size;
import org.jxls.transform.poi.PoiTransformer;

public class AutoRowHeightCommand extends AbstractCommand {

	@Override
	public String getName() {
	    return "autoRowHeight";
	}

	@Override
	public Size applyAt(CellRef cellRef, Context context) {
		Area area = getAreaList().get(0);
	    Size size = area.applyAt(cellRef, context);

	    PoiTransformer transformer = (PoiTransformer) area.getTransformer();
	    Row row = transformer.getWorkbook().getSheet(cellRef.getSheetName()).getRow(cellRef.getRow());
	    row.setHeight((short) -1);
	    
		return size;
	}

}
