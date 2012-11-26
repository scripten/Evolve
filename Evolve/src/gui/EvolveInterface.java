package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.LineBorder;

import model.World;
import model.WorldListener;
import model.WorldParameters;

public class EvolveInterface extends JFrame implements WorldListener, ActionListener {
	private static final String startingPath = "/home/blad/Teaching/421/notes/evolve/";
	/**
	 * The application; provides services to the interface.
	 */
	private Evolve app;

	private BufferedImage worldImage;

	private JTabbedPane tabbedPane;
	private JPanel worldPanel;

	private final JFileChooser fileChooser;
	/**
	 * File menu items for the given actions
	 */
	private JMenuItem fileNewGame;
	private JMenuItem fileLoadGame;
	private JMenuItem fileQuit;

	private WorldParameters current;

	private JLabel lblGencount;

	private WorldCanvas worldCanvas;
	/**
	 * Create the application.
	 */
	public EvolveInterface(Evolve app) {
		this.app = app;
		this.fileChooser = new JFileChooser();
		System.out.println(fileChooser.getCurrentDirectory().getName());
		this.fileChooser.setCurrentDirectory(new File(startingPath));
		System.out.println(fileChooser.getCurrentDirectory().getName());
		initialize();
	}

	private void initializeMenu() {
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mFile = new JMenu("File");
		fileNewGame = new JMenuItem("New Game");
		mFile.add(fileNewGame);
		fileLoadGame = new JMenuItem("Load Game");
		fileLoadGame.addActionListener(this);
		mFile.add(fileLoadGame);
		fileQuit = new JMenuItem("Quit");
		fileQuit.addActionListener(this);
		mFile.add(fileQuit);
		menuBar.add(mFile);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setBounds(100, 100, 640, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout(0, 0));

		initializeMenu();

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);

		worldPanel = new JPanel();
		tabbedPane.addTab("World", null, worldPanel, null);
		worldPanel.setLayout(new BorderLayout(0, 0));

		worldCanvas = new WorldCanvas();
		worldPanel.add(worldCanvas, BorderLayout.CENTER);

		JPanel toolPanel = new JPanel();
		toolPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		worldPanel.add(toolPanel, BorderLayout.SOUTH);
		toolPanel.setLayout(new BoxLayout(toolPanel, BoxLayout.X_AXIS));

		JLabel lblGeneration = new JLabel("Generation:");
		toolPanel.add(lblGeneration);

		Component horizontalStrut = Box.createHorizontalStrut(20);
		toolPanel.add(horizontalStrut);

		lblGencount = new JLabel("9999999");
		toolPanel.add(lblGencount);

		JPanel speciesPanel = new JPanel();
		speciesPanel.setLayout(new BoxLayout(speciesPanel, BoxLayout.Y_AXIS));
		worldPanel.add(speciesPanel, BorderLayout.EAST);

		List<JButton> species = new ArrayList<JButton>();
		for (int i = 0; i < 12; ++i) {
			JButton btnSpecies = new JButton(String.format("Species %2d",i+1));
			speciesPanel.add(btnSpecies);
			species.add(btnSpecies);
		}
		JPanel statisticsPanel = new JPanel();
		tabbedPane.addTab("Statistics", null, statisticsPanel, null);
	}

	public void exit() {
		System.exit(0);
	}

	@Override
	public void next(World sender) {
		sender.update(worldImage);
		lblGencount.setText(String.format("%7d", sender.getGeneration()));
		repaint();
	}

	private void setCurrentWorld(File file) {
		current = app.load(file);
		World world = app.makeWorld(current);
		worldImage = new BufferedImage(current.getWidth(), current.getHeight(), BufferedImage.TYPE_INT_ARGB);
		worldCanvas.setWorldImage(worldImage);
		app.runWorld(world);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object src = e.getSource();
		if (src == fileQuit) {
			exit();
		} else if (src == fileLoadGame) {
			int retval = fileChooser.showOpenDialog(this);
			if (retval == JFileChooser.APPROVE_OPTION) {
				File openFile = fileChooser.getSelectedFile();
				setCurrentWorld(openFile);
			}
		}
	}

}
