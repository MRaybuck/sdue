package us.unlv.sdue.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeriesCollection;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import us.unlv.sdue.controller.AboutUsAction;
import us.unlv.sdue.controller.AddEdgeAction;
import us.unlv.sdue.controller.CreateNetworkAction;
import us.unlv.sdue.controller.ExportChartAction;
import us.unlv.sdue.controller.ExportDataAction;
import us.unlv.sdue.controller.HelpAction;
import us.unlv.sdue.controller.LoadModelAction;
import us.unlv.sdue.controller.RunAction;

/**
 * @author Olivier BOURDON and Maxime LENORMAND
 */
public class Window {

	private JFrame frmSdue;

	private JPanel panelSettings;
	private JPanel panelNetwork;
	private JPanel panelSimulation;
	private JPanel panelGraph;
	private JPanel panelChart;

	private JLabel lblNumberOfNodes;
	private JLabel lblSource;
	private JLabel lblTarget;
	private JLabel lblWeight;
	private JLabel lblModel;
	private JLabel lblOrigin;
	private JLabel lblDestination;
	private JLabel lblTripRate;
	private JLabel lblNumberIterations;
	private JLabel lblParameter;

	private JTextField textFieldNbNodes;
	private JTextField textFieldSource;
	private JTextField textFieldTarget;
	private JTextField textFieldWeight;
	private JTextField textFieldOrigin;
	private JTextField textFieldDestination;
	private JTextField textFieldTripRate;
	private JTextField textFieldNumberIterations;
	private JTextField textFieldParameter;

	private JRadioButton rdbtnLogit;
	private JRadioButton rdbtnProbit;
	private JRadioButton rdbtnMixedlogit;
	private JButton btnCreateNetwork;
	private JButton btnAddEdge;
	private JButton btnRun;
	private JMenuItem mntmExportChart;
	private JMenuItem mntmExportTimesData;
	private JMenuItem mntmExportFlowsData;
	private JMenuItem mntmExportLinkFlowsData;

	private mxGraph graph;
	private ArrayList<Object> vertices;
	
	private JFreeChart chart;
	private XYSeriesCollection dataset;

	/**
	 * Creates the application.
	 */
	public Window() {
		this.initializeFrame();
		this.initializeMenuBar();
		this.initializeComponents();
		this.initializeLayout();
		this.initializeGraph();
		this.initializeChart();
		this.initializeActions(); // actions must be initialized at the end
	}

	/**
	 * Initializes the frame.
	 */
	private void initializeFrame() {
		try {
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException e) {
			Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, e);
		} catch (InstantiationException e) {
			Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, e);
		} catch (IllegalAccessException e) {
			Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, e);
		} catch (javax.swing.UnsupportedLookAndFeelException e) {
			Logger.getLogger(Window.class.getName()).log(Level.SEVERE, null, e);
		}
		this.frmSdue = new JFrame();
		URL imgURL = getClass().getResource("/resources/UNLVLogo.png");
		if (imgURL != null) {
			Image icone = Toolkit.getDefaultToolkit().getImage(imgURL);
			this.frmSdue.setIconImage(icone);
		} else {
			System.err.println("Couldn't find file: " + "/resources/UNLVLogo.png");
		}
		this.frmSdue.setTitle("SDUE");
		this.frmSdue.setBounds(100, 100, 1000, 720);
		this.frmSdue.setLocationRelativeTo(null);
		this.frmSdue.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public JFrame getFrame() {
		return this.frmSdue;
	}

	/**
	 * Initializes the menu bar.
	 */
	private void initializeMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		this.frmSdue.setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		mnFile.setMnemonic('F');
		menuBar.add(mnFile);

		JMenuItem mntmNew = new JMenuItem("New");
		mntmNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
		mntmNew.setMnemonic('N');
		mntmNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Window window = new Window();
				window.getFrame().setVisible(true);
			}
		});
		mnFile.add(mntmNew);
		mnFile.addSeparator();
		
		this.mntmExportChart = new JMenuItem("Export Chart");
		this.mntmExportChart.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));
		this.mntmExportChart.setMnemonic('E');
		mnFile.add(this.mntmExportChart);
		
		this.mntmExportTimesData = new JMenuItem("Export Times Data");
		this.mntmExportTimesData.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_MASK));
		this.mntmExportTimesData.setMnemonic('T');
		this.mntmExportTimesData.setActionCommand("Times");
		mnFile.add(this.mntmExportTimesData);
		
		this.mntmExportFlowsData = new JMenuItem("Export Flows Data");
		this.mntmExportFlowsData.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK));
		this.mntmExportFlowsData.setMnemonic('F');
		this.mntmExportFlowsData.setActionCommand("Flows");
		mnFile.add(this.mntmExportFlowsData);
		
		this.mntmExportLinkFlowsData = new JMenuItem("Export Link Flows Data");
		this.mntmExportLinkFlowsData.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK));
		this.mntmExportLinkFlowsData.setMnemonic('L');
		this.mntmExportLinkFlowsData.setActionCommand("Link Flows");
		mnFile.add(this.mntmExportLinkFlowsData);
		mnFile.addSeparator();
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
		mntmExit.setMnemonic('x');
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
		mnFile.add(mntmExit);

		JMenu mnHelp = new JMenu("Help");
		mnHelp.setMnemonic('H');
		menuBar.add(mnHelp);
		
		JMenuItem mntmHelp = new JMenuItem("Help SDUE");
		mntmHelp.setMnemonic('H');
		mntmHelp.addActionListener(new HelpAction());
		mnHelp.add(mntmHelp);
		
		JMenuItem mntmAboutUS = new JMenuItem("About us");
		mntmAboutUS.setMnemonic('A');
		mntmAboutUS.addActionListener(new AboutUsAction());
		mnHelp.add(mntmAboutUS);
	}

	/**
	 * Initializes the panels, the labels, the text fields and the buttons.
	 */
	private void initializeComponents() {
		this.panelSettings = new JPanel();
		this.panelSettings.setBorder(new TitledBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null),
				"Settings", TitledBorder.LEADING, TitledBorder.TOP, new Font("TimesRoman", Font.BOLD, 17), Color.RED));
		this.panelGraph = new JPanel();
		this.panelGraph.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null), "Graph",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("TimesRoman", Font.BOLD, 17), Color.RED));
		this.panelChart = new JPanel();
		this.panelChart.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null), "Chart",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("TimesRoman", Font.BOLD, 17), Color.RED));
		this.panelNetwork = new JPanel();
		this.panelNetwork.setBorder(new TitledBorder(null, "Network", TitledBorder.CENTER, TitledBorder.TOP, null,
				SystemColor.textHighlight));
		this.panelSimulation = new JPanel();
		this.panelSimulation.setBorder(new TitledBorder(null, "Simulation", TitledBorder.CENTER, TitledBorder.TOP, null,
				SystemColor.textHighlight));

		this.lblNumberOfNodes = new JLabel("Number of nodes:");
		this.lblSource = new JLabel("Source:");
		this.lblTarget = new JLabel("Target:");
		this.lblWeight = new JLabel("Weight:");
		this.lblModel = new JLabel("Model:");
		this.lblOrigin = new JLabel("Origin:");
		this.lblDestination = new JLabel("Destination:");
		this.lblTripRate = new JLabel("Trip Rate:");
		this.lblNumberIterations = new JLabel("Number of Iterations:");
		this.lblParameter = new JLabel("Parameter Theta:");
		
	    this.textFieldNbNodes = new JTextField();
		this.textFieldNbNodes.setColumns(10);
		this.textFieldNbNodes.setHorizontalAlignment(JFormattedTextField.CENTER);
		this.textFieldSource = new JTextField();
		this.textFieldSource.setColumns(10);
		this.textFieldSource.setHorizontalAlignment(JFormattedTextField.CENTER);
		this.textFieldTarget = new JTextField();
		this.textFieldTarget.setColumns(10);
		this.textFieldTarget.setHorizontalAlignment(JFormattedTextField.CENTER);
		this.textFieldWeight = new JTextField();
		this.textFieldWeight.setColumns(10);
		this.textFieldWeight.setHorizontalAlignment(JFormattedTextField.CENTER);
		this.textFieldOrigin = new JTextField();
		this.textFieldOrigin.setColumns(10);
		this.textFieldOrigin.setHorizontalAlignment(JFormattedTextField.CENTER);
		this.textFieldDestination = new JTextField();
		this.textFieldDestination.setColumns(10);
		this.textFieldDestination.setHorizontalAlignment(JFormattedTextField.CENTER);
		this.textFieldTripRate = new JTextField();
		this.textFieldTripRate.setColumns(10);
		this.textFieldTripRate.setHorizontalAlignment(JFormattedTextField.CENTER);
		this.textFieldNumberIterations = new JTextField();
		this.textFieldNumberIterations.setColumns(10);
		this.textFieldNumberIterations.setHorizontalAlignment(JFormattedTextField.CENTER);
		this.textFieldParameter = new JTextField();
		this.textFieldParameter.setColumns(10);
		this.textFieldParameter.setHorizontalAlignment(JFormattedTextField.CENTER);

		
		this.rdbtnLogit = new JRadioButton("Logit");
		this.rdbtnLogit.setActionCommand("Logit");
		this.rdbtnLogit.setSelected(true);
		this.rdbtnProbit = new JRadioButton("Probit");
		this.rdbtnProbit.setActionCommand("Probit");
		this.rdbtnMixedlogit = new JRadioButton("Mixed-Logit");
		this.rdbtnMixedlogit.setActionCommand("Mixed-Logit");
		
		ButtonGroup buttonGroup = new ButtonGroup();
		buttonGroup.add(this.rdbtnLogit);
		buttonGroup.add(this.rdbtnProbit);
		buttonGroup.add(this.rdbtnMixedlogit);
		
		this.btnCreateNetwork = new JButton("CREATE NETWORK");
		this.btnAddEdge = new JButton("ADD EDGE");
		this.btnRun = new JButton("RUN");
	}

	/**
	 * Initializes the layout.
	 */
	private void initializeLayout() {
		GroupLayout groupLayout = new GroupLayout(this.frmSdue.getContentPane());
		groupLayout
				.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
								.addComponent(panelSettings, GroupLayout.PREFERRED_SIZE, 298,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
										.addComponent(panelGraph, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(panelChart, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE))));
		groupLayout
				.setVerticalGroup(
						groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup()
										.addComponent(panelGraph, GroupLayout.PREFERRED_SIZE, 318,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(panelChart, GroupLayout.DEFAULT_SIZE, 334, Short.MAX_VALUE))
								.addComponent(panelSettings, GroupLayout.DEFAULT_SIZE, 658, Short.MAX_VALUE));
		this.frmSdue.getContentPane().setLayout(groupLayout);

		GroupLayout gl_panelSettings = new GroupLayout(this.panelSettings);
		gl_panelSettings.setHorizontalGroup(gl_panelSettings.createParallelGroup(Alignment.TRAILING)
				.addComponent(this.panelSimulation, GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
				.addComponent(this.panelNetwork, GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE));
		gl_panelSettings.setVerticalGroup(gl_panelSettings.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panelSettings.createSequentialGroup()
						.addComponent(this.panelNetwork, GroupLayout.DEFAULT_SIZE, 295, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED).addComponent(this.panelSimulation,
								GroupLayout.PREFERRED_SIZE, 326, GroupLayout.PREFERRED_SIZE)));
		this.panelSettings.setLayout(gl_panelSettings);

		GroupLayout gl_panelNetwork = new GroupLayout(this.panelNetwork);
		gl_panelNetwork.setHorizontalGroup(gl_panelNetwork.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelNetwork.createSequentialGroup().addGroup(gl_panelNetwork
						.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelNetwork.createSequentialGroup().addGap(43).addComponent(lblNumberOfNodes)
								.addPreferredGap(ComponentPlacement.RELATED).addComponent(textFieldNbNodes,
										GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelNetwork.createSequentialGroup().addGap(79).addComponent(btnAddEdge))
						.addGroup(gl_panelNetwork.createSequentialGroup().addGap(54).addComponent(btnCreateNetwork))
						.addGroup(
								gl_panelNetwork.createSequentialGroup().addGap(22)
										.addGroup(gl_panelNetwork.createParallelGroup(Alignment.LEADING)
												.addComponent(textFieldSource, GroupLayout.PREFERRED_SIZE, 46,
														GroupLayout.PREFERRED_SIZE)
												.addComponent(lblSource))
										.addGap(34)
										.addGroup(gl_panelNetwork.createParallelGroup(Alignment.LEADING)
												.addComponent(textFieldTarget, GroupLayout.PREFERRED_SIZE, 47,
														GroupLayout.PREFERRED_SIZE)
												.addComponent(lblTarget))
										.addGap(34)
										.addGroup(gl_panelNetwork.createParallelGroup(Alignment.LEADING)
												.addComponent(textFieldWeight, GroupLayout.PREFERRED_SIZE, 46,
														GroupLayout.PREFERRED_SIZE)
												.addComponent(lblWeight))))
						.addContainerGap(21, Short.MAX_VALUE)));
		gl_panelNetwork.setVerticalGroup(gl_panelNetwork.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelNetwork.createSequentialGroup().addGap(32)
						.addGroup(gl_panelNetwork.createParallelGroup(Alignment.TRAILING).addComponent(lblNumberOfNodes)
								.addComponent(textFieldNbNodes, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addGap(28).addComponent(btnCreateNetwork)
						.addPreferredGap(ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
						.addGroup(gl_panelNetwork.createParallelGroup(Alignment.BASELINE).addComponent(lblWeight)
								.addComponent(lblTarget).addComponent(lblSource))
						.addGap(7)
						.addGroup(gl_panelNetwork.createParallelGroup(Alignment.BASELINE)
								.addComponent(textFieldSource, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(textFieldWeight, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(textFieldTarget, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addGap(27).addComponent(btnAddEdge).addGap(15)));
		this.panelNetwork.setLayout(gl_panelNetwork);

		GroupLayout gl_panelSimulation = new GroupLayout(this.panelSimulation);
		gl_panelSimulation
				.setHorizontalGroup(
						gl_panelSimulation
								.createParallelGroup(
										Alignment.LEADING)
								.addGroup(
										gl_panelSimulation.createSequentialGroup()
												.addGroup(
														gl_panelSimulation.createParallelGroup(Alignment.LEADING)
																.addGroup(gl_panelSimulation.createSequentialGroup()
																		.addGap(96).addComponent(btnRun))
																.addGroup(
																		gl_panelSimulation.createSequentialGroup()
																				.addContainerGap()
																				.addComponent(lblModel)
																				.addPreferredGap(
																						ComponentPlacement.RELATED)
																				.addComponent(rdbtnLogit)
																				.addPreferredGap(
																						ComponentPlacement.RELATED)
																				.addComponent(rdbtnProbit)
																				.addPreferredGap(
																						ComponentPlacement.RELATED)
																				.addComponent(
																						rdbtnMixedlogit,
																						GroupLayout.PREFERRED_SIZE, 87,
																						Short.MAX_VALUE))
																.addGroup(Alignment.TRAILING, gl_panelSimulation
																		.createSequentialGroup().addGap(26)
																		.addGroup(gl_panelSimulation
																				.createParallelGroup(Alignment.LEADING)
																				.addGroup(gl_panelSimulation
																						.createSequentialGroup()
																						.addGroup(gl_panelSimulation
																								.createParallelGroup(
																										Alignment.LEADING)
																								.addComponent(
																										lblNumberIterations)
																								.addComponent(
																										lblParameter)
																								.addComponent(
																										textFieldOrigin,
																										GroupLayout.PREFERRED_SIZE,
																										76,
																										GroupLayout.PREFERRED_SIZE))
																						.addPreferredGap(
																								ComponentPlacement.UNRELATED)
																						.addGroup(gl_panelSimulation
																								.createParallelGroup(
																										Alignment.LEADING)
																								.addComponent(
																										lblDestination,
																										GroupLayout.DEFAULT_SIZE,
																										93,
																										Short.MAX_VALUE)
																								.addComponent(
																										textFieldDestination,
																										Alignment.TRAILING,
																										GroupLayout.DEFAULT_SIZE,
																										93,
																										Short.MAX_VALUE)
																								.addComponent(
																										textFieldTripRate,
																										Alignment.TRAILING,
																										GroupLayout.DEFAULT_SIZE,
																										93,
																										Short.MAX_VALUE)
																								.addComponent(
																										textFieldNumberIterations,
																										GroupLayout.DEFAULT_SIZE,
																										93,
																										Short.MAX_VALUE)
																								.addComponent(
																										textFieldParameter,
																										0,
																										0,
																										Short.MAX_VALUE)))
																				.addGroup(gl_panelSimulation
																						.createParallelGroup(
																								Alignment.LEADING,
																								false)
																						.addComponent(lblTripRate,
																								GroupLayout.DEFAULT_SIZE,
																								GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE)
																						.addComponent(lblOrigin)))))
												.addContainerGap()));
		gl_panelSimulation.setVerticalGroup(gl_panelSimulation.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelSimulation.createSequentialGroup().addGap(7)
						.addGroup(gl_panelSimulation.createParallelGroup(Alignment.BASELINE).addComponent(lblModel)
								.addComponent(rdbtnLogit).addComponent(rdbtnProbit).addComponent(rdbtnMixedlogit))
						.addGap(14)
						.addGroup(gl_panelSimulation.createParallelGroup(Alignment.BASELINE).addComponent(lblOrigin)
								.addComponent(lblDestination))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_panelSimulation.createParallelGroup(Alignment.BASELINE)
								.addComponent(textFieldOrigin, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(textFieldDestination, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
						.addGroup(gl_panelSimulation.createParallelGroup(Alignment.BASELINE).addComponent(lblTripRate)
								.addComponent(textFieldTripRate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addGap(31)
						.addGroup(gl_panelSimulation.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblNumberIterations).addComponent(textFieldNumberIterations,
										GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addGap(29)
						.addGroup(gl_panelSimulation.createParallelGroup(Alignment.TRAILING).addComponent(lblParameter)
								.addComponent(textFieldParameter, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addGap(18).addComponent(btnRun).addContainerGap()));
		this.panelSimulation.setLayout(gl_panelSimulation);
	}

	/**
	 * Initializes the graph.
	 */
	private void initializeGraph() {
		this.graph = new mxGraph();
		this.graph.setCellsLocked(true);
		this.graph.setCellsSelectable(false);
		this.graph.setAllowDanglingEdges(false);
		mxGraphComponent graphComponent = new mxGraphComponent(this.graph);
		graphComponent.setPreferredSize(new Dimension(670, 280));
		this.panelGraph.add(graphComponent);
		this.vertices = new ArrayList<Object>();
	}

	/**
	 * Initializes the chart.
	 */
	private void initializeChart() {
		this.dataset = new XYSeriesCollection();
		this.chart = ChartFactory.createXYLineChart("SDUE Simulation", "Number of Iterations",
				"Travel Time for each Path", dataset);
		XYPlot plot = chart.getXYPlot();
		NumberAxis domainAxis = new LogarithmicAxis("Number of Iterations");
		NumberAxis rangeAxis = new LogarithmicAxis("Travel Time");
		plot.setDomainAxis(domainAxis);
		plot.setRangeAxis(rangeAxis);
		
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(670, 295));
		chartPanel.setMaximumDrawWidth(600);
		chartPanel.setMinimumDrawHeight(500);
		chartPanel.setMaximumDrawHeight(500);
		this.panelChart.add(chartPanel);
	}
	
	/**
	 * Initializes the actions.
	 */
	private void initializeActions() {
		CreateNetworkAction createNetworkAction = new CreateNetworkAction(this.frmSdue, this.textFieldNbNodes,
				this.graph, this.vertices);
		// createNetworkAction owns the updated network
		AddEdgeAction addEdgeAction = new AddEdgeAction(this.frmSdue, this.textFieldSource, this.textFieldTarget,
				this.textFieldWeight, this.graph, this.vertices, createNetworkAction);
		// loadModelAction owns the current model selected
		LoadModelAction loadModelAction = new LoadModelAction(this.lblParameter);
		RunAction runAction = new RunAction(this.frmSdue, this.textFieldOrigin, this.textFieldDestination,
				this.textFieldTripRate, this.textFieldNumberIterations, this.textFieldParameter, this.vertices,
				this.dataset, createNetworkAction, loadModelAction);
		ExportChartAction exportChartAction = new ExportChartAction(this.frmSdue, this.chart);
		ExportDataAction exportDataAction = new ExportDataAction(this.frmSdue, runAction);

		this.btnCreateNetwork.addActionListener(createNetworkAction);
		this.btnAddEdge.addActionListener(addEdgeAction);
		this.rdbtnLogit.addActionListener(loadModelAction);
		this.rdbtnProbit.addActionListener(loadModelAction);
		this.rdbtnMixedlogit.addActionListener(loadModelAction);
		this.btnRun.addActionListener(runAction);
		this.mntmExportChart.addActionListener(exportChartAction);
		this.mntmExportTimesData.addActionListener(exportDataAction);
		this.mntmExportFlowsData.addActionListener(exportDataAction);
		this.mntmExportLinkFlowsData.addActionListener(exportDataAction);
	}
	
}
