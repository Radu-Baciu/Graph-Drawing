import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

public class MyPanel extends JPanel implements ActionListener {
	private int nodeNr = 1;
	private int node_diam = 30;
	private Vector<Node> listaNoduri;
	private Vector<Arc> listaArce;
	private List<List<Integer>> adjList;
	Point pStart = null;
	Point pEnd = null;
	boolean isDragging = false;
	JToggleButton grafN = new JToggleButton();
	int index = -1;

	public MyPanel() {
		listaNoduri = new Vector<Node>();
		listaArce = new Vector<Arc>();
		adjList = new ArrayList<List<Integer>>();
		Font font = new Font("TimesRoman", Font.BOLD, 12);
		grafN.setFont(font);
		grafN.setText("Graf neorientat");
		grafN.addActionListener(this);
		add(grafN);

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				pStart = e.getPoint();
			}
			public void mouseReleased(MouseEvent e) {
				if (!isDragging) {
					addNode(e.getX(), e.getY());
				} else {
					if (pStart != pEnd)
						addArc(pStart, pEnd);
				}
				pStart = null;
				isDragging = false;
				repaint();
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent e) {
				pEnd = e.getPoint();
				isDragging = true;
				repaint();
			}
		});

	}
	private void addNode(int x, int y) {

		boolean overlap = false;
		int spatiuNod = node_diam + 10;
		for (int index = 0; index < listaNoduri.size(); ++index)
			if (x > listaNoduri.get(index).getCoordX() - spatiuNod
					&& x < listaNoduri.get(index).getCoordX() + spatiuNod
					&& y > listaNoduri.get(index).getCoordY() - spatiuNod
					&& y < listaNoduri.get(index).getCoordY() + spatiuNod) {
				overlap = true;
				break;
			}
		if (!overlap) {
			Node node = new Node(x, y, nodeNr);
			listaNoduri.add(node);
			nodeNr++;
			this.adjList.add(new ArrayList<Integer>());
			displayMatrix();
			displayMatrixN();
			repaint();
		}
	}
	private void addArc(Point s, Point f) {
		Point startCenter = null, endCenter = null, currentCenter = null;
		int startIndex = -1, endIndex = -1;
		boolean isOk = true;

		for (int index = 0; index < listaNoduri.size(); ++index) {
			currentCenter = new Point(listaNoduri.get(index).getCoordX() + node_diam / 2,
					listaNoduri.get(index).getCoordY() + node_diam / 2);
			if ((int) Math.sqrt((double) ((s.x - currentCenter.x) * (s.x - currentCenter.x)
					+ (s.y - currentCenter.y) * (s.y - currentCenter.y))) < node_diam / 2) {
				startCenter = currentCenter;
				startIndex = index;
				break;
			}
		}
		for (int index = 0; index < listaNoduri.size(); ++index) {
			currentCenter = new Point(listaNoduri.get(index).getCoordX() + node_diam / 2,
					listaNoduri.get(index).getCoordY() + node_diam / 2);
			if ((int) Math.sqrt((double) ((f.x - currentCenter.x) * (f.x - currentCenter.x)
					+ (f.y - currentCenter.y) * (f.y - currentCenter.y))) < node_diam / 2) {
				endCenter = currentCenter;
				endIndex = index;
				break;
			}
		}
		for (int index = 0; index < this.adjList.size(); ++index)
			for (int element : adjList.get(index))
				if (startIndex == index && element == endIndex)
					isOk = false;

		if (isOk && startCenter != null && endCenter != null
				&& (startCenter.getX() != endCenter.getX() || startCenter.getY() != endCenter.getY())) {

			Arc arc = new Arc(startCenter, endCenter);
			listaArce.add(arc);
			this.adjList.get(startIndex).add(endIndex);
			displayMatrix();
			displayMatrixN();

		}
	}
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawString("This is my Graph!", 190, 20);
		for (Arc a : listaArce) {
			if (!grafN.isSelected())
				a.drawArc(g, node_diam);
			else
				a.drawArcN(g, node_diam);
		}

		if (pStart != null) {
			g.setColor(Color.BLACK);
			g.drawLine(pStart.x, pStart.y, pEnd.x, pEnd.y);
		}
		for (int i = 0; i < listaNoduri.size(); i++) {
			listaNoduri.elementAt(i).drawNode(g, node_diam);
		}
		grafN.setBounds(165, 30, 150, 75);
	}

	public void actionPerformed(ActionEvent e) {
		repaint();
	}

	public void displayMatrix() {
		String pathname = "matriceO.txt";
		File myFile = new File(pathname);
		FileWriter fr = null;
		try {
			fr = new FileWriter(myFile);
			fr.write(String.valueOf(this.listaNoduri.size()));

			for (int row = 0; row < this.listaNoduri.size(); ++row) {
				fr.write(System.lineSeparator());
				for (int col = 0; col < this.listaNoduri.size(); ++col)
					if (this.adjList.get(row).contains(col))
						fr.write("1 ");
					else
						fr.write("0 ");
			}
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void displayMatrixN() {
		String pathname = "matriceN.txt";
		File myFile = new File(pathname);
		FileWriter fr = null;
		try {
			fr = new FileWriter(myFile);
			fr.write(String.valueOf(this.listaNoduri.size()));

			for (int row = 0; row < this.listaNoduri.size(); ++row) {
				fr.write(System.lineSeparator());
				for (int col = 0; col < this.listaNoduri.size(); ++col)
					if (this.adjList.get(row).contains(col) || this.adjList.get(col).contains(row))
						fr.write("1 ");
					else
						fr.write("0 ");
			}
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
