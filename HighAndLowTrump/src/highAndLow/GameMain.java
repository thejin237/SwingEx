package highAndLow;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GameMain {

	private JFrame frame;
	private final String TITLE = "HIGH & LOW";
	private final int LOCATION_X = 400;
	private final int LOCATION_Y = 50;
	private final int FRAME_WIDTH = 600;
	private final int FRAME_HEIGHT = 400;

	private ArrayList<Card> deck = new ArrayList<>();

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				GameMain window = new GameMain();
				window.frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public GameMain() {
		initialize();
	}

	public void initialize() {
		frame = new JFrame(TITLE);
		frame.setBounds(LOCATION_X, LOCATION_Y, FRAME_WIDTH, FRAME_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Top Panel
		JPanel topPanel = new JPanel();
		JLabel topLabel = new JLabel("一発勝負！HIGHかLOWかあててください。");

		topLabel.setFont(new Font("Meiryo", Font.PLAIN, 25));

		topPanel.add(topLabel);
		topPanel.setBackground(Color.ORANGE);

		deck = makeTrump();
		Card myCardDeck = deck.remove(deck.size() - 1);
		Card yourCardDeck = deck.remove(deck.size() - 1);

		//Middle Panel
		JPanel middlePanel = new JPanel(new GridLayout());
		JPanel middleWestPanel = new JPanel(new BorderLayout());
		JPanel middleEastPanel = new JPanel(new BorderLayout());

		middleWestPanel.setPreferredSize(new Dimension(frame.getSize().width / 2, 200));
		middleEastPanel.setPreferredSize(new Dimension(frame.getSize().width / 2, 200));

		middleWestPanel.setBackground(Color.CYAN);
		middleEastPanel.setBackground(Color.GRAY);
		middlePanel.setBackground(Color.CYAN);
		
		JLabel myCardLabel = new JLabel("私のカード");
		JLabel yourCardLabel = new JLabel("あなたのカード");
		JLabel myNumLabel = new JLabel(myCardDeck.toString().split("of")[0]);
		JLabel yourNumLabel = new JLabel("?");

		//Insert Picture
		JLabel myPicLabel = null;
		JLabel yourPicLabel = null;

		myPicLabel = insertImage(myCardDeck);
		yourPicLabel = insertImage(yourCardDeck);
		
		middleWestPanel.add(myCardLabel, BorderLayout.NORTH);
		middleWestPanel.add(myPicLabel, BorderLayout.CENTER);
		middleWestPanel.add(myNumLabel, BorderLayout.SOUTH);
		middleEastPanel.add(yourCardLabel, BorderLayout.NORTH);
		middleEastPanel.add(yourPicLabel, BorderLayout.CENTER);
		middleEastPanel.add(yourNumLabel, BorderLayout.SOUTH);

		middlePanel.add(middleWestPanel);
		middlePanel.add(middleEastPanel);

		JPanel bottomPanel = new JPanel(new GridLayout());
		bottomPanel.setPreferredSize(new Dimension(frame.getSize().width, 80));

		//Create High and Low buttons
		JButton buttonHigh = new JButton("HIGH");
		JButton buttonLow = new JButton("LOW");

		buttonHigh.setFont(new Font("Meiryo", Font.PLAIN, 30));
		buttonHigh.setPreferredSize(new Dimension(frame.getSize().width / 2, 80));

		buttonLow.setFont(new Font("Meiryo", Font.PLAIN, 30));
		buttonLow.setPreferredSize(new Dimension(frame.getSize().width / 2, 80));

		int comparison = myCardDeck.compareTo(yourCardDeck);
		boolean isHigher = comparison < 0;
		boolean isLower = comparison > 0;

		buttonHigh.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (isHigher) {
					topLabel.setText("大正解、あなたのかちです！");
				} else if (isLower) {
					topLabel.setText("不正解、あなたの負けです！");
				} else {
					topLabel.setText("奇遇ですね。引き分けです！");
				}
				yourNumLabel.setText(yourCardDeck.toString().split("of")[0]);
			}
		});

		buttonLow.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (isLower) {
					topLabel.setText("大正解、あなたのかちです！");
				} else if (isHigher) {
					topLabel.setText("不正解、あなたの負けです！");
				} else {
					topLabel.setText("奇遇ですね。引き分けです！");
				}
				yourNumLabel.setText(yourCardDeck.toString().split("of")[0]);
			}
		});

		bottomPanel.add(buttonHigh, BorderLayout.CENTER);
		bottomPanel.add(buttonLow, BorderLayout.EAST);

		frame.getContentPane().add(topPanel, BorderLayout.NORTH);
		frame.getContentPane().add(middlePanel, BorderLayout.CENTER);
		frame.getContentPane().add(bottomPanel, BorderLayout.SOUTH);
		frame.setVisible(true);
	}

	private ArrayList<Card> makeTrump() {
		ArrayList<Card> deck = new ArrayList<>();
		for (int rank = 2; rank <= 14; rank++) {
			for (Shape suit : Shape.values()) {
				deck.add(new Card(rank, suit));
			}
		}
		Collections.shuffle(deck);

		return deck;
	}

	public JLabel insertImage(Card deck) {
		JLabel fileLabel = null;
		String filePath = "";
		try {
			if (deck.toString().split("of")[1].trim().equals("♠")) {
				filePath = "C:\\java-workspace\\HighAndLowTrump\\img\\spade.jpg";
			} else if (deck.toString().split("of")[1].trim().equals("♦")) {
				filePath = "C:\\java-workspace\\HighAndLowTrump\\img\\diamond.jpg";
			} else if (deck.toString().split("of")[1].trim().equals("♥")) {
				filePath = "C:\\java-workspace\\HighAndLowTrump\\img\\heart.jpg";
			} else {
				filePath = "C:\\java-workspace\\HighAndLowTrump\\img\\clover.jpg";
			}
			BufferedImage myPic = ImageIO.read(new File(filePath));
			fileLabel = new JLabel(new ImageIcon(myPic));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return fileLabel;
	}
}

enum Shape {
	SPADE, HEART, DIAMOND, CLOVER
}

class Card implements Comparable<Card> {
	private final int rank;
	private final Shape shape;

	public Card(int rank, Shape shape) {
		this.rank = rank;
		this.shape = shape;
	}

	@Override
	public String toString() {
		String[] rankNames = { "", "", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace" };
		String[] suitSymbols = { "♠", "♥", "♦", "♣" };
		return rankNames[rank] + " of " + suitSymbols[shape.ordinal()];
	}

	@Override
	public int compareTo(Card other) {
		return Integer.compare(this.rank, other.rank);
	}
}
