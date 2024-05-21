package Wang_Tile_Level_Editor_2;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

public class Model {

	
	ArrayList<int[]> WangTile_List = new ArrayList<int[]>();
	
	
	private int[] levelLayout = new int[2];
	private ArrayList<Slider> sliderArray;
	private ArrayList<ArrayList<Boolean>> optionLists;
	private ArrayList<Color> colorList;
	private int[][][] levelIntArray;
	private String[][] levelStringArray; // Diese Arrays speichern das von WantTile_Lvl_Generation erzeugte level mit
	// Strings oder int arrays (für genaueres zu diesne arrays schaue in die
	// Klasse WangTile_Lvl_Generation

	public void setLevelLayout(int[] layout) {

		levelLayout[0] = layout[0];
		levelLayout[1] = layout[1];
	}

	public void setSliderArr(ArrayList<Slider> slid) {

		sliderArray = slid;
	}

	public void setOptionArr(ArrayList<ArrayList<Boolean>> options) {

		optionLists = options;

	}

	public void setColorArr(ArrayList<Color> colors) {

		colorList = colors;

	}

	public void makeLevel() {

		Lvl_Generation_With_Wang_Tiles LevelGen = new Lvl_Generation_With_Wang_Tiles(levelLayout, sliderArray,
				optionLists);
		
		
		levelIntArray = LevelGen.getlvlIntArr();
		levelStringArray = LevelGen.getLvlStringArr();
		this.WangTile_List = LevelGen.getAllWangTiles();

		//testOutput();
		writeIntoFile();

	}
	
	private void testOutput() {		//Debuging Methode, unwichtig für ablauf des Programs

		for (int yPos = 0; yPos < levelLayout[1]; yPos++) {

			for (int xPos = 0; xPos < levelLayout[0]; xPos++) {

				System.out.print(levelStringArray[xPos][yPos] + " ");

			}
			System.out.println("");
		}
	}
	
	
	private void writeIntoFile() {

		String tmp = "";

		PrintWriter writer;
		try {
			writer = new PrintWriter("WangTile_Text_Datei.txt", "UTF-8");

			for (int yPos = 0; yPos < levelLayout[1]; yPos++) {

				tmp = "";
				tmp += "[";

				for (int xPos = 0; xPos < levelLayout[0]; xPos++) {

					tmp += levelStringArray[xPos][yPos] + ",";

				}
				tmp += "]";
				writer.println(tmp);
			}
			writer.close();

		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	public void createAndShowImage() {
		
		
		
		Create_Lvl_Image test = new Create_Lvl_Image(levelIntArray,levelLayout,colorList,WangTile_List);
	}
}
