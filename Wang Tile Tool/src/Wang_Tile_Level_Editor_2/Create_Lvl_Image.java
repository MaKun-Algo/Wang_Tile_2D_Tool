package Wang_Tile_Level_Editor_2;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class Create_Lvl_Image {

	ArrayList<int[]> WangTile_List = new ArrayList<int[]>();

	HashMap<int[], BufferedImage> WangImageMap;

	private int[] levelLayout = new int[2];
	private ArrayList<Color> colorList;
	private int[][][] levelIntArray;

	private Polygon topPart;
	private Polygon rightPart;
	private Polygon downPart;
	private Polygon leftPart;

	public Create_Lvl_Image(int[][][] levelIntArray, int[] levelLayout, ArrayList<Color> colorList,
			ArrayList<int[]> WangTile_List) {
		// TODO Auto-generated constructor stub

		this.levelIntArray = levelIntArray;
		this.levelLayout = levelLayout;
		this.colorList = colorList;
		this.WangTile_List = WangTile_List;

		createWangTileImages();
		createImage();

	}

	private void createWangTileImages() {

		WangImageMap = new HashMap<int[], BufferedImage>();

		topPart = new Polygon(new int[] { 0, 32, 16 }, new int[] { 0, 0, 16 }, 3);
		rightPart = new Polygon(new int[] { 32, 32, 16 }, new int[] { 0, 32, 16 }, 3);
		downPart = new Polygon(new int[] { 0, 32, 16 }, new int[] { 32, 32, 16 }, 3);
		leftPart = new Polygon(new int[] { 0, 0, 16 }, new int[] { 0, 32, 16 }, 3);

		for (int[] wangTile : WangTile_List) {

			BufferedImage result = new BufferedImage(32, 32, // work these out
					BufferedImage.TYPE_INT_RGB);
			Graphics g = result.getGraphics();

			g.setColor(colorList.get(wangTile[0]));
			g.fillPolygon(topPart);
			g.setColor(colorList.get(wangTile[1]));
			g.fillPolygon(rightPart);
			g.setColor(colorList.get(wangTile[2]));
			g.fillPolygon(downPart);
			g.setColor(colorList.get(wangTile[3]));
			g.fillPolygon(leftPart);

			WangImageMap.put(wangTile, result);

		}

	}

	private void createImage() {

		BufferedImage result = new BufferedImage((32 * levelLayout[0]), (32 * levelLayout[1]), // work these out
				BufferedImage.TYPE_INT_RGB);
		Graphics g = result.getGraphics();
		int[]  tmpWangTile;

		for (int yPos = 0; yPos < levelLayout[1]; yPos++) {

			for (int xPos = 0; xPos < levelLayout[0]; xPos++) {

				tmpWangTile = levelIntArray[xPos][yPos];
				g.drawImage(WangImageMap.get(tmpWangTile), xPos * 32, yPos * 32, null);

			}
		}

		
		  try {
				ImageIO.write(result,"png",new File("WangTile_Level.png"));
				
				
				//Show Image on Screen
				File file = new File("WangTile_Level.png");
				Desktop desktop = Desktop.getDesktop();
				desktop.open(file);
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

}
