package com.axle2.accounting;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class Paystub {
	Double earnings;
	Double earningsYTD;
	Double tax;
	Double taxYTD;

	private static final String EMPLOYER = "My Corporation";
	private static final String EMPRSTREET = "100 Main St Ste 7";
	private static final String EMPRCITYSTZIP = "Anytown, IN 47001";
	
	private static final String PAYGROUP = "MC-Anytown-Consulting";
	private static final String PAYBEGIN = "06/01/2015";
	private static final String PAYEND = "06/30/2015";

	private static final String BIZUNIT = "Innovation";
	private static final String PAYADVICE = "201506300106";
	private static final String PAYDATE = PAYEND;

	private static final String NAME = "Best Employee";
	private static final String STREET = "700 Main St Apt 7";
	private static final String CITYST = "Anytown, IN";
	private static final String CITYSTZIP = "Anytown, IN 47001";

	private static final String EMPID = "IN2015070101";
	private static final String DEPT = BIZUNIT + "-Consulting";
	private static final String LOCATION = CITYST;
	private static final String STATE = "Indiana";
	private static final String COUNTY = "Lake";

	private static final String MARITAL = "Married";
	private static final String FED_DED = "4";
	private static final String ST_DED = "4";
	
	private static final Double MONTHHOURS = 173.33333333;
	private static final Double HOURLYRATE = 5000.00 / MONTHHOURS;  //Monthly pay
	private static int          MONTHNBR = 6; //6 => June

	private static final Font fontTimes = new Font(Font.FontFamily.TIMES_ROMAN, 9);
	private static final Font fontBold = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD);
	private static final Font fontBoldUnd = new Font(Font.FontFamily.TIMES_ROMAN, 9, Font.BOLD | Font.UNDERLINE);
	
	private static final DecimalFormat df = new DecimalFormat("#.00");

	/** Path to the resulting PDF file. */
	public static final String RESULT = "/Users/snayha2/Documents/workspace-sts-3.4.0.RELEASE/paystubs/Hello.pdf";
	public static final String LOGO = "/Users/snayha2/Documents/workspace-sts-3.4.0.RELEASE/paystubs/logoXLXL100.png";

	/**
	 * Creates a PDF file: hello.pdf
	 * 
	 * @param args
	 *            no arguments needed
	 */
	public static void main(String[] args) throws DocumentException,
			IOException {
		new Paystub().createPdf(RESULT);
	}

	/**
	 * Creates a PDF document.
	 * 
	 * @param filename
	 *            the path to the new PDF document
	 * @throws DocumentException
	 * @throws IOException
	 */
	public void createPdf(String filename) throws DocumentException,
			IOException {
		// step 1
		Document doc = new Document();

		// step 2
		PdfWriter.getInstance(doc, new FileOutputStream(filename));

		// step 3
		doc.open();
		doc.newPage();
		
		Image logo = Image.getInstance(LOGO);
		logo.setAlignment(Image.MIDDLE);
		logo.scaleAbsoluteHeight(20);
		logo.scaleAbsoluteWidth(20);
		logo.scalePercent(100);
		
		PdfPTable mainTable = new PdfPTable(1);
		mainTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		PdfPCell imgCell = new PdfPCell(logo);
		imgCell.setHorizontalAlignment(Rectangle.ALIGN_CENTER);
		imgCell.setBorder(Rectangle.NO_BORDER);
		mainTable.addCell(imgCell);
		
		mainTable.addCell(employerInfo(PAYGROUP, PAYBEGIN, PAYEND,
				BIZUNIT, PAYADVICE, PAYDATE));

		mainTable.addCell(employeeInfo(NAME, STREET, CITYSTZIP, EMPID,
				DEPT, LOCATION, COUNTY, MARITAL, FED_DED, ST_DED));

		mainTable.addCell(earningsAndTax(HOURLYRATE, MONTHHOURS, MONTHHOURS * MONTHNBR));

		mainTable.addCell(grossAndNet());

		mainTable.setWidthPercentage(100);
		doc.add(mainTable);
		doc.close();
	}

	public PdfPTable employerInfo(String payGroup, String beginDate,
			String endDate, String businessUnit, String adviceNumber,
			String adviceDate) throws DocumentException {
		List<Phrase> sub1 = new ArrayList<Phrase>();
		sub1.add(new Phrase(EMPLOYER, fontBold));
		sub1.add(new Phrase(EMPRSTREET, fontTimes));
		sub1.add(new Phrase(EMPRCITYSTZIP, fontTimes));
		
		List<String> sub2 = new ArrayList<String>();
		sub2.add("Pay Group:");
		sub2.add(payGroup);
		sub2.add("Pay Begin Date:");
		sub2.add(beginDate);
		sub2.add("Pay End Date:");
		sub2.add(endDate);

		List<String> sub3 = new ArrayList<String>();
		sub3.add("Business Unit:");
		sub3.add(businessUnit);
		sub3.add("Advice Number:");
		sub3.add(adviceNumber);
		sub3.add("Advice Date:");
		sub3.add(adviceDate);

		PdfPTable emprInfoTbl = new PdfPTable(3);
		emprInfoTbl.setWidths(new int[]{4, 5, 5});
		PdfPCell cell0 = new PdfPCell(createPhrasesColumn(3, sub1));
		cell0.setBorder(Rectangle.NO_BORDER);
		emprInfoTbl.addCell(cell0);
		emprInfoTbl.addCell(createCustomTable(2, 3, sub2, fontTimes));
		emprInfoTbl.addCell(createCustomTable(2, 3, sub3, fontTimes));
		emprInfoTbl.setSpacingBefore(30);
		emprInfoTbl.setSpacingAfter(10);
		return emprInfoTbl;
	}

	public PdfPTable employeeInfo(String name, String addressLine1,
			String addressLine2, String employeeID, String department,
			String location, String county, String maritalStatus, String fedDed, String stDed) throws DocumentException {
		List<Phrase> sub1 = new ArrayList<Phrase>();
		sub1.add(new Phrase(name, fontBold));
		sub1.add(new Phrase(addressLine1, fontTimes));
		sub1.add(new Phrase(addressLine2, fontTimes));

		List<String> sub2 = new ArrayList<String>();
		sub2.add("Employee ID:");
		sub2.add(employeeID);
		sub2.add("Department:");
		sub2.add(department);
		sub2.add("Location:");
		sub2.add(location);
		sub2.add("County:");
		sub2.add(county);

		List<Phrase> sub3 = new ArrayList<Phrase>();
		sub3.add(new Phrase("Tax Data", fontBoldUnd));
		sub3.add(new Phrase("Federal", fontBoldUnd));
		sub3.add(new Phrase(STATE, fontBoldUnd));
		sub3.add(new Phrase("Marital Status:", fontTimes));
		sub3.add(new Phrase(maritalStatus, fontTimes));
		sub3.add(new Phrase(maritalStatus, fontTimes));
		sub3.add(new Phrase("Allowances:", fontTimes));
		sub3.add(new Phrase(fedDed, fontTimes));
		sub3.add(new Phrase(stDed, fontTimes));
		sub3.add(new Phrase("Addl. Percent:", fontTimes));
		sub3.add(new Phrase(" ", fontTimes));
		sub3.add(new Phrase(" ", fontTimes));
		sub3.add(new Phrase("Addl. Amount:", fontTimes));
		sub3.add(new Phrase(" ", fontTimes));
		sub3.add(new Phrase(" ", fontTimes));

		PdfPTable employeeInfo = new PdfPTable(3);
		employeeInfo.setWidths(new int[]{4, 5, 5});
		employeeInfo.addCell(createPhrasesColumn(3, sub1));
		employeeInfo.addCell(createCustomTable(2, 4, sub2, fontTimes));
		PdfPTable taxDataTbl = createPhrasesTable(3, 5, sub3);
		taxDataTbl.setWidths(new int[]{3, 2, 2});
		employeeInfo.addCell(taxDataTbl);
		employeeInfo.setSpacingBefore(10);
		employeeInfo.setSpacingAfter(10);
		return employeeInfo;
	}

	public PdfPTable earningsAndTax(Double rate,
			Double hours, Double hoursYTD) throws DocumentException {
		earnings = hours * rate;
		earningsYTD = hoursYTD * rate;
		
		Double currentFedWithholdng = earnings * .1287;
		Double currentFedMedEE = earnings * .062;
		Double currentFedOASDIEE = earnings * .0145;
		Double currentStateWithholdng = earnings * .033;
		Double currentCountyWithholdng = earnings * .011;
		tax = currentFedWithholdng + currentFedMedEE
				+ currentFedOASDIEE + currentStateWithholdng + currentCountyWithholdng;
		
		Double yTDFedWithholdng = earningsYTD * .1287;
		Double yTDFedMedEE = earningsYTD * .062;
		Double yTDFedOASDIEE = earningsYTD * .0145;
		Double yTDStateWithholdng = earningsYTD * .033;
		Double yTDCountyWithholdng = earningsYTD * .011;
		taxYTD = yTDFedWithholdng + yTDFedMedEE + yTDFedOASDIEE
				+ yTDStateWithholdng + yTDCountyWithholdng;
		
		PdfPTable earningsTaxes = new PdfPTable(3);

		PdfPCell benefitUsageEarnings = new PdfPCell(new Phrase(
				"EARNINGS (Current and YTD)", fontBold));
		benefitUsageEarnings.setColspan(2);
		earningsTaxes.addCell(benefitUsageEarnings);

		PdfPCell taxesLabel = new PdfPCell(new Phrase("TAXES (Current and YTD)", fontBold));
		earningsTaxes.addCell(taxesLabel);

		PdfPTable benefitsUsageEarningsAmounts = new PdfPTable(3);
		
		List<Phrase> earningsSubtab1 = new ArrayList<Phrase>();
		earningsSubtab1.add(new Phrase(" ", fontTimes));
		earningsSubtab1.add(new Phrase("Description", fontBoldUnd));
		earningsSubtab1.add(new Phrase("Regular Work", fontTimes));
		PdfPTable benefitsUsageEarningsDescriptions = createPhrasesTable(1, 3, earningsSubtab1);

		benefitsUsageEarningsDescriptions.setPaddingTop(0);
		benefitsUsageEarningsAmounts.addCell(benefitsUsageEarningsDescriptions);
		PdfPTable benefitUsageEarningsCurrent = new PdfPTable(3);

		PdfPCell currentCell = new PdfPCell(new Phrase(
				"------------ Current ----------", fontBold));
		currentCell.setBorder(Rectangle.NO_BORDER);
		currentCell.setColspan(3);
		benefitUsageEarningsCurrent.addCell(currentCell);
		
		List<Phrase> earningsCurrent = new ArrayList<Phrase>();
		earningsCurrent.add(new Phrase("Rate", fontBoldUnd));
		earningsCurrent.add(new Phrase("Hours", fontBoldUnd));
		earningsCurrent.add(new Phrase("Earned", fontBoldUnd));
		earningsCurrent.add(new Phrase(df.format(rate), fontTimes));
		earningsCurrent.add(new Phrase(df.format(hours), fontTimes));
		earningsCurrent.add(new Phrase(df.format(earnings), fontTimes));
		PdfPCell currentEarningsCell = new PdfPCell(new PdfPTable(
				createPhrasesTable(3, 2, earningsCurrent)));
		currentEarningsCell.setColspan(3);
		currentEarningsCell.setBorder(Rectangle.NO_BORDER);
		benefitUsageEarningsCurrent.addCell(currentEarningsCell);
		benefitsUsageEarningsAmounts.addCell(benefitUsageEarningsCurrent);

		PdfPCell yTDCell = new PdfPCell(new Phrase(
				"------------ YTD --------------", fontBold));
		yTDCell.setColspan(2);
		yTDCell.setBorder(Rectangle.NO_BORDER);
		PdfPTable benefitUsageEarningsYTD = new PdfPTable(2);
		benefitUsageEarningsYTD.addCell(yTDCell);
		
		List<Phrase> eaningsYTDlist = new ArrayList<Phrase>();
		eaningsYTDlist.add(new Phrase("Hours", fontBoldUnd));
		eaningsYTDlist.add(new Phrase("Earned", fontBoldUnd));
		eaningsYTDlist.add(new Phrase(df.format(hoursYTD), fontTimes));
		eaningsYTDlist.add(new Phrase(df.format(earningsYTD), fontTimes));
		PdfPCell yTDEarningsCell = new PdfPCell(createPhrasesTable(2, 2, eaningsYTDlist));
		yTDEarningsCell.setColspan(2);
		yTDEarningsCell.setBorder(Rectangle.NO_BORDER);
		benefitUsageEarningsYTD.addCell(yTDEarningsCell);
		benefitsUsageEarningsAmounts.addCell(benefitUsageEarningsYTD);
		
		PdfPCell benefitsUsageEarningsAmountsCell = new PdfPCell(
				benefitsUsageEarningsAmounts);
		benefitsUsageEarningsAmountsCell.setColspan(2);
		earningsTaxes.addCell(benefitsUsageEarningsAmountsCell);

		List<Phrase> taxes = new ArrayList<Phrase>();
		taxes.add(new Phrase("Withholding", fontBoldUnd));
		taxes.add(new Phrase("Current", fontBoldUnd));
		taxes.add(new Phrase("YTD", fontBoldUnd));
		taxes.add(new Phrase("Fed Income", fontTimes));
		taxes.add(new Phrase(df.format(currentFedWithholdng), fontTimes));
		taxes.add(new Phrase(df.format(yTDFedWithholdng), fontTimes));
		taxes.add(new Phrase("Fed MED/EE", fontTimes));
		taxes.add(new Phrase(df.format(currentFedMedEE), fontTimes));
		taxes.add(new Phrase(df.format(yTDFedMedEE), fontTimes));
		taxes.add(new Phrase("Fed OASDI/EE", fontTimes));
		taxes.add(new Phrase(df.format(currentFedOASDIEE), fontTimes));
		taxes.add(new Phrase(df.format(yTDFedOASDIEE), fontTimes));
		taxes.add(new Phrase(STATE, fontTimes));
		taxes.add(new Phrase(df.format(currentStateWithholdng), fontTimes));
		taxes.add(new Phrase(df.format(yTDStateWithholdng), fontTimes));
		taxes.add(new Phrase(COUNTY, fontTimes));
		taxes.add(new Phrase(df.format(currentCountyWithholdng), fontTimes));
		taxes.add(new Phrase(df.format(yTDCountyWithholdng), fontTimes));
		PdfPTable taxesAmountTbl = createPhrasesTable(3, 6, taxes);
		taxesAmountTbl.setWidths(new int[]{3, 2, 2});
		earningsTaxes.addCell(taxesAmountTbl);

		PdfPCell cell35 = new PdfPCell(createCustomTable(5, 1, new Phrase(" ",
				fontTimes)));
		cell35.setColspan(2);
		earningsTaxes.addCell(cell35);
		
		List<String> totalTaxArray = new ArrayList<String>();
		totalTaxArray.add("Total:");
		totalTaxArray.add(df.format(tax));
		totalTaxArray.add(df.format(taxYTD));
		PdfPTable totalTaxTbl = createCustomTable(3, 1, totalTaxArray, fontTimes);
		totalTaxTbl.setWidths(new int[]{3, 2, 2});
		earningsTaxes.addCell(totalTaxTbl);
		
		earningsTaxes.setSpacingBefore(10);
		return earningsTaxes;
	}

	public PdfPTable grossAndNet() throws DocumentException {		
		List<String> netGrossSubtab2 = new ArrayList<String>();
		netGrossSubtab2.add(" ");
		netGrossSubtab2.add("TOTAL GROSS");
		netGrossSubtab2.add("FED TAXABLE GROSS");
		netGrossSubtab2.add("TOTAL TAXES");
		netGrossSubtab2.add("TOTAL DEDUCTIONS");
		netGrossSubtab2.add("NET PAY");
		
		List<String> netGrossSubtab1 = new ArrayList<String>();
		netGrossSubtab1.add("Current");
		netGrossSubtab1.add(df.format(earnings));
		netGrossSubtab1.add(df.format(earnings));
		netGrossSubtab1.add(df.format(tax));
		netGrossSubtab1.add("0.00");
		netGrossSubtab1.add(df.format(earnings - tax));
		
		netGrossSubtab1.add("YTD");
		netGrossSubtab1.add(df.format(earningsYTD));
		netGrossSubtab1.add(df.format(earningsYTD));
		netGrossSubtab1.add(df.format(taxYTD));
		netGrossSubtab1.add("0.00");
		netGrossSubtab1.add(df.format(earningsYTD - taxYTD));
		
		PdfPTable netGross = new PdfPTable(1);
		netGross.addCell(createCustomTable(6, 1, netGrossSubtab2, fontBold));
		netGross.addCell(createCustomTable(6, 2, netGrossSubtab1, fontTimes));
		netGross.setSpacingBefore(10);
		return netGross;
	}

	public PdfPTable createCustomTable(int columns, int rows, Phrase p) {
		PdfPCell cell = new PdfPCell(p);
		cell.setBorder(Rectangle.NO_BORDER);
		PdfPTable subTable1 = new PdfPTable(columns);
		for (int i = 0; i < rows * columns; i++) {
			subTable1.addCell(cell);
		}
		return subTable1;
	}

	private PdfPTable createCustomTable(int columns, int rows,
			List<String> cellStrings, Font fnt) throws DocumentException {
		PdfPTable subTable1 = new PdfPTable(columns);
		for (int i = 0; i < rows * columns; i++) {
			PdfPCell cell = new PdfPCell(new Phrase(cellStrings.get(i), fnt));
			cell.setBorder(Rectangle.NO_BORDER);
			subTable1.addCell(cell);
		}
		return subTable1;
	}

	private PdfPTable createPhrasesTable(int columns, int rows,
			List<Phrase> cellStrings) throws DocumentException {
		PdfPTable subTable1 = new PdfPTable(columns);
		for (int i = 0; i < rows * columns; i++) {
			PdfPCell cell = new PdfPCell(cellStrings.get(i));
			cell.setBorder(Rectangle.NO_BORDER);
			subTable1.addCell(cell);
		}
		return subTable1;
	}

	private PdfPTable createPhrasesColumn(int rows,
			List<Phrase> cellStrings) throws DocumentException {
		PdfPTable subTable1 = new PdfPTable(1);
		for (int i = 0; i < rows; i++) {
			PdfPCell cell = new PdfPCell(cellStrings.get(i));
			cell.setBorder(Rectangle.NO_BORDER);
			subTable1.addCell(cell);
		}
		return subTable1;
	}
}
