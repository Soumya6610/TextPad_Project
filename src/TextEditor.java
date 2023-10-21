import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.undo.UndoManager;

@SuppressWarnings("serial")
public class TextEditor extends JFrame implements ActionListener {

	JTextArea textArea;
	JScrollPane scrollPane;
	JSpinner fontSizeSpinner;
	JLabel fontLabel;
	JButton fontColorButton;
	JButton boldButton;
	JButton italicButton;
	@SuppressWarnings("rawtypes")
	JComboBox fontBox;

	JMenuBar menuBar;

	JMenu fileMenu;
	JMenuItem openItem;
	JMenuItem saveItem;
	JMenuItem exitItem;

	JMenu formatMenu;
	JMenuItem cutItem;
	JMenuItem copyItem;
	JMenuItem pasteItem;
	JMenuItem undoItem;
	JMenuItem redoItem;

	UndoManager undo;

	TextEditor() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("My Text Editor");
		this.setSize(800, 800);
		this.setLayout(new FlowLayout());
		this.setLocationRelativeTo(null);

		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setFont(new Font("Time New Roman", Font.PLAIN, 15));

		scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(750, 750));
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		fontLabel = new JLabel("font: ");

		fontSizeSpinner = new JSpinner();

		fontSizeSpinner.setPreferredSize(new Dimension(50, 25));
		fontSizeSpinner.setValue(20);
		fontSizeSpinner.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				// TODO Auto-generated method stub
				textArea.setFont(
						new Font(textArea.getFont().getFamily(), Font.PLAIN, (int) fontSizeSpinner.getValue()));
			}
		});

		fontColorButton = new JButton("Color");
		fontColorButton.addActionListener(this);

		String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

		fontBox = new JComboBox(fonts);
		fontBox.addActionListener(this);
		fontBox.setSelectedItem("Time New Roman");

		undo = new UndoManager();
		textArea.getDocument().addUndoableEditListener(undo);

		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		openItem = new JMenuItem("Open");
		saveItem = new JMenuItem("Save");
		exitItem = new JMenuItem("Exit");

		openItem.addActionListener(this);
		saveItem.addActionListener(this);
		exitItem.addActionListener(this);

		fileMenu.add(openItem);
		fileMenu.add(saveItem);
		fileMenu.add(exitItem);
		menuBar.add(fileMenu);

		formatMenu = new JMenu("Format");
		cutItem = new JMenuItem("Cut");
		copyItem = new JMenuItem("Copy");
		pasteItem = new JMenuItem("Paste");
		undoItem = new JMenuItem("Undo");
		redoItem = new JMenuItem("Redo");

		cutItem.addActionListener(this);
		copyItem.addActionListener(this);
		pasteItem.addActionListener(this);
		undoItem.addActionListener(this);
		redoItem.addActionListener(this);
		
		formatMenu.add(cutItem);
		formatMenu.add(copyItem);
		formatMenu.add(pasteItem);
		formatMenu.add(undoItem);
		formatMenu.add(redoItem);
		menuBar.add(formatMenu);

		this.setJMenuBar(menuBar);
		this.add(fontColorButton);
		this.add(fontBox);
		this.add(fontLabel);
		this.add(fontSizeSpinner);
		this.add(scrollPane);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

		String str = e.getActionCommand();

		if (str.equals("Cut")) {
			textArea.cut();
		} else if (str.equals("Copy")) {
			textArea.copy();
		} else if (str.equals("Paste")) {
			textArea.paste();
		} else if (str.equals("Undo")) {
			undo.undo();
		} else if (str.equals("Redo")) {
			undo.redo();
		}

		if (e.getSource() == fontColorButton) {
			JColorChooser colorChooser = new JColorChooser();

			Color color = colorChooser.showDialog(null, "Choose a color", Color.BLACK);

			textArea.setForeground(color);
		}

		if (e.getSource() == fontBox) {
			textArea.setFont(new Font((String) fontBox.getSelectedItem(), Font.PLAIN, textArea.getFont().getSize()));
		}

		if (e.getSource() == openItem) {
			JFileChooser filechoose = new JFileChooser();
			filechoose.setCurrentDirectory(new File("."));
			FileNameExtensionFilter exFil = new FileNameExtensionFilter("Text Files", "txt");

			filechoose.setFileFilter(exFil);
			int res = filechoose.showOpenDialog(null);

			if (res == JFileChooser.APPROVE_OPTION) {
				File f = new File(filechoose.getSelectedFile().getAbsolutePath());
				Scanner sc = null;
				try {
					sc = new Scanner(f);
					if (f.isFile()) {
						while (sc.hasNextLine()) {
							String line = sc.nextLine() + "\n";
							textArea.append(line);
						}
					}
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} finally {
					sc.close();
				}
			}
		}
		if (e.getSource() == saveItem) {
			JFileChooser filechoose = new JFileChooser();
			filechoose.setCurrentDirectory(new File("."));

			int response = filechoose.showSaveDialog(null);

			if (response == JFileChooser.APPROVE_OPTION) {
				File file;
				PrintWriter pw = null;

				file = new File(filechoose.getSelectedFile().getAbsolutePath());
				try {
					pw = new PrintWriter(file);
					pw.println(textArea.getText());
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} finally {
					pw.close();
				}
			}
		}
		if (e.getSource() == exitItem) {
			System.exit(0);
		}
	}

}
