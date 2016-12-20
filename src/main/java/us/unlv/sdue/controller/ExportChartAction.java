package us.unlv.sdue.controller;

import java.awt.event.ActionEvent;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import javax.swing.AbstractAction;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.apache.batik.svggen.SVGGraphics2DIOException;
import org.jfree.chart.JFreeChart;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

/**
 * @author Olivier BOURDON and Maxime LENORMAND
 */
public class ExportChartAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	
	private JFrame frmSdue;
	private JFreeChart chart;
	private File file; // stocks the current file
	
	public ExportChartAction(JFrame frmSdue, JFreeChart chart) {
		this.frmSdue = frmSdue;
		this.chart = chart;
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		JFileChooser jFileChooser = new JFileChooser();
		jFileChooser.setCurrentDirectory(this.file);
		jFileChooser.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("SVG (*.svg)", new String[] {"svg"});
		jFileChooser.setFileFilter(filter);
		int result = jFileChooser.showSaveDialog(this.frmSdue);
		if (result == JFileChooser.APPROVE_OPTION) {
			this.file = jFileChooser.getCurrentDirectory();
			// Get a DOMImplementation
			DOMImplementation domImpl = GenericDOMImplementation.getDOMImplementation();
			// Create an instance of org.w3c.dom.Document
			Document document = domImpl.createDocument(null, "svg", null);
			// Create an instance of the SVG Generator
			SVGGraphics2D svgGenerator = new SVGGraphics2D(document);
			// Set the precision to avoid a null pointer exception in Batik 1.5
			svgGenerator.getGeneratorContext().setPrecision(6);
			// Ask the chart to render into the SVG Graphics2D implementation
			this.chart.draw(svgGenerator, new Rectangle2D.Double(0, 0, 400, 300), null);
			// Finally, stream out SVG to a file using UTF-8 character to byte encoding
			boolean useCSS = true;
			Writer out = null;
			try {
				String fileName = jFileChooser.getSelectedFile().getAbsolutePath() + "." + filter.getExtensions()[0];
				out = new OutputStreamWriter(
				new FileOutputStream(new File(fileName)), "UTF-8");
			} catch (UnsupportedEncodingException | FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				svgGenerator.stream(out, useCSS);
			} catch (SVGGraphics2DIOException e) {
				e.printStackTrace();
			}
		}
	}

}
