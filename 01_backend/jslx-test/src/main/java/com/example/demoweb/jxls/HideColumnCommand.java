package com.example.demoweb.jxls;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.jxls.area.Area;
import org.jxls.command.AbstractCommand;
import org.jxls.command.Command;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.common.Size;
import org.jxls.transform.poi.PoiTransformer;
import org.jxls.util.Util;

public class HideColumnCommand extends AbstractCommand {

	public static final String ROW = "row";
	public static final String COL = "col";

	private String hidTag;
	private String condition;
	private Area area;

	@Override
	public String getName() {
		return "hiddenDef";
	}

	@Override
	public Command addArea(Area area) {
		if (super.getAreaList().size() >= 1) {
			throw new IllegalArgumentException("You can add only a single area to 'merge' command");
		}
		this.area = area;
		return super.addArea(area);
	}

	@Override
	public Size applyAt(CellRef cellRef, Context context) {

		System.out.println("Enter the HideColumnCommand");
		System.out.println(cellRef.getRow());
		System.out.println(cellRef.getCol());

		Area area = getAreaList().get(0);
		Size size = area.applyAt(cellRef, context);
		Boolean conditionResult = Util.isConditionTrue(getTransformationConfig().getExpressionEvaluator(), condition,
				context);

		if (conditionResult.booleanValue()) {
			PoiTransformer transformer = (PoiTransformer) this.getTransformer();
			Sheet sheet = transformer.getWorkbook().getSheet(cellRef.getSheetName());

			CellRef srcCell = area.getStartCellRef();
			if (ROW.equals(hidTag)) {
				int rowNub = srcCell.getRow();
				Row row = sheet.getRow(rowNub);
				short rowHeight = 0;
				row.setHeight(rowHeight);
			}
			if (COL.equals(hidTag)) {
				int col = srcCell.getCol();
				sheet.setColumnHidden(col, true);
			}
		}
		return area.applyAt(cellRef, context);

	}

	public String getHidTag() {
		return hidTag;
	}

	public void setHidTag(String hidTag) {
		this.hidTag = hidTag;
	}

	public static String getCommandName() {
		return "hiddenDef";
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}
}
