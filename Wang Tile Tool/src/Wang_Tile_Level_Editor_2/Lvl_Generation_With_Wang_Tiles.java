package Wang_Tile_Level_Editor_2;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import javafx.scene.control.Slider;

public class Lvl_Generation_With_Wang_Tiles {

	ArrayList<int[]> WangTile_List = new ArrayList<int[]>(); // enthält alle möglichen WangTiles. Die WangTiles werdne
	// dargestellt durch 4 stellige array,
	// bei dennen die Zahl 1 für Wasser, 2 für Wald und die
	// Zahl 3 für Ebene steht, demnach würde es insgesamt
	// 81 verschiedene WangTiles geben bei numOfDif==3

	int[][][] levelArr; // 2-D Array welches das Top Down level darstellt und selbst arrays speichert.
// der erste Wert ist für die x Achse, der zweite für die y Achse des Levels.
// die dritte Dimension ist um die Werte als Array zu Speichern, da ich die
// WangTiles als Array dargestellt habe.
// Bildlich vorgestellt ist es eine Top Down Level/Bild, auf welchem nach oben
// die Vier Werte der WangTile Arrays gespeichert werden, welche besagen
// welche Farbe in welche Richtung zeigt
// Das Level wird aufgebaut von Oben Links anch Oben rechts, demnach ist das
// Kooridnatensystem mit (0,0) oben Links, (0,n) wäre unten links etc

	String[][] levelStringArr; // Das selbe Array wie levelArr, nur dass es anstatt int Arrays Strings enthält,
// die genauso aufgebaut sind wie die int Arrays

	private int[] terrainFactors; // speichert die faktoren die die Häufigkeit der jeweiligen Terrain typen
// entscheiden
	private int[] levelLayout = new int[2]; // speichert die Menge der horizontalen und verticalen WangTiles (und somit
// das Level Layout)
	private int numOfDifferentTerrain; // Speichert wie Viele verschiedene Terrains gerade genutzt werden (wasser,
// Walf, Ebene)

	private ArrayList<ArrayList<Boolean>> optionLists;

	private Random rand;

	private int maxZahlenStellen = 0;

	private ArrayList<ArrayList<int[]>> topKeymap;

	public Lvl_Generation_With_Wang_Tiles(int[] levelLayout, ArrayList<Slider> sliderArray,
			ArrayList<ArrayList<Boolean>> optionLists) {
		// TODO Auto-generated constructor stub

		numOfDifferentTerrain = sliderArray.size();
		terrainFactors = new int[numOfDifferentTerrain];
		int tmp = 0;
		for (Slider slid : sliderArray) {
			terrainFactors[tmp] = (int) slid.getValue();
			tmp++;
		}

		rand = new Random();
		this.levelLayout = levelLayout;
		this.optionLists = optionLists;

		for (double i = (double) (numOfDifferentTerrain); i > 1.0;) {

			maxZahlenStellen++;

			i /= 10.0;

		}

		// System.out.println(this.optionLists.size()+" "+this.colorList.size()+"
		// "+this.terrainFactors.length+" "+this.levelLayout[1]+" num
		// "+this.numOfDifferentTerrain);

		createWangTiles(); // erstelle alle möglichen WangTiles Dynamisch unter beachtung der zugelassenen
							// angrenzungen)

		createLevelArr(); // Siehe Beschreibung von der Var LevelArr

		selectFirstTile();
		// Diese Funktion sucht sich random eine WangTile für den start aus, unter
		// betrachtung der Terrainfaktoren und mögliche Nachbarn.
		// Es werden Wang Tiles mit mehr möglichen Nachbarn bevorzugt, um eine grö0ere
		// Kette bilden zu können
		// (Wenn die Starttile eine wäre, die nur an sichs elbst grenzen kann, wäre die
		// ganze fläche nur einfarbig)
		// Danach werden die Faktoren der übrigen betrachtet. Wenn also z.b. der Faktor
		// für Wasser bei 5 ist und alle anderen bei 1, dann
		// hat die WangTile mit 4 mal Wasser die höchste Chance, aber jede der übrigen
		// ist möglich

		selectFirstRow();// Die Funktion füllt die erste Zeile mit passenden WangTiles. Dabei werden wie
		// immer die Terrain Faktoren beachtet

		// selectFirstColumn();// Die Funktion füllt die erste Reihe mit passenden
		// WangTiles mit betrachtung
		// der T-Faktoren.
		fillRestOfLevel(); // Füllt das Restliche Level Zeile für Zeile passend auf, unter breücksichtigung
		// der Terrain Faktoren

		convertToStringArray();// Erstellt ein zweites Level Array welches Strings statt int arrays speichert,
								// da mit den int arrays die logik in dieser Klasse leichter umsetzbar ist, man
								// mit
		// Strings jedoch leichter eine Ausgabe machen kan, bzw schon Text hat (außerdem
		// hat man ein 2-D statt 3-D Array)

		// System.out.println(WangTile_List.size());
		// System.out.println(
		// levelArr[0][0][0] + "" + levelArr[0][0][1] + "" + levelArr[0][0][2] + "" +
		// levelArr[0][0][3] + "");

	}

	private void createWangTiles() {

		for (int oben = 0; oben < numOfDifferentTerrain; oben++) {

			for (int rechts = 0; rechts < numOfDifferentTerrain; rechts++) {

				for (int unten = 0; unten < numOfDifferentTerrain; unten++) {

					for (int links = 0; links < numOfDifferentTerrain; links++) {

						if (neighboursInvalid(oben, rechts, unten, links))
							continue;

						int[] tmp = { oben, rechts, unten, links };
						WangTile_List.add(tmp);

					}
				}
			}
		}

		topKeymap = generateTopWangKeyMap(WangTile_List);
	}

	private Boolean neighboursInvalid(int oben, int rechts, int unten, int links) {

		Boolean invalid = false;

		if (!(optionLists.get(oben).get(rechts) && optionLists.get(oben).get(unten)
				&& optionLists.get(oben).get(links))) // Wenn ein einziger Tile nicht benachbart sein sarf, dann ist die
														// Wang Tile Invalide
			invalid = true;

		if (!(optionLists.get(rechts).get(oben) && optionLists.get(rechts).get(unten)
				&& optionLists.get(rechts).get(links))) // Wenn ein einziger Tile nicht benachbart sein sarf, dann ist
														// die Wang Tile Invalide
			invalid = true;

		if (!(optionLists.get(unten).get(rechts) && optionLists.get(unten).get(oben)
				&& optionLists.get(unten).get(links))) // Wenn ein einziger Tile nicht benachbart sein sarf, dann ist
														// die Wang Tile Invalide
			invalid = true;

		if (!(optionLists.get(links).get(rechts) && optionLists.get(links).get(unten)
				&& optionLists.get(links).get(oben))) // Wenn ein einziger Tile nicht benachbart sein sarf, dann ist die
														// Wang Tile Invalide
			invalid = true;

		return invalid;
	}

	private void createLevelArr() {

		levelArr = new int[levelLayout[0]][levelLayout[1]][4];
	}

	private void selectFirstTile() {

		ArrayList<int[]> possibaleWangsT = new ArrayList<int[]>();

		ArrayList<int[]> selectFromWangs = considerNeighbourForSelection();

		int tmp;
		for (int[] wang : selectFromWangs) {
			tmp = 0;
			tmp = (considerTerrainFactors(wang) + upTerrainFactors(wang) + leftTerrainFactors(wang));

			for (int i = 0; i < tmp; i++) {
				possibaleWangsT.add(wang);
			}
		}
		levelArr[0][0] = possibaleWangsT.get(rand.nextInt(possibaleWangsT.size()));
	}

	private ArrayList<int[]> considerNeighbourForSelection() {

		ArrayList<int[]> goodNeighbourWangTiles = new ArrayList<int[]>();

		ArrayList<Integer> goodNeigTiles = new ArrayList<Integer>();
		int maxNeighbour = 0;
		int neighbours;
		for (ArrayList<Boolean> options : optionLists) {

			neighbours = 0;

			for (Boolean is : options) {

				if (is) {
					neighbours++;
				}
			}

			if (maxNeighbour < neighbours)
				maxNeighbour = neighbours;

		}
		for (int i = 0; i < optionLists.size(); i++) {

			neighbours = 0;

			for (Boolean is : optionLists.get(i)) {
				if (is)
					neighbours++;
			}

			if (neighbours == maxNeighbour) {
				goodNeigTiles.add(i);
			}

		}

		Boolean isUsableStarttile = true;

		for (int[] wangTile : WangTile_List) {

			isUsableStarttile = false;

			/*
			 * for (Integer integer : goodNeigTiles) {
			 * 
			 * if(integer.intValue()==wangTile[0]) { isUsableStarttile=true; }
			 * 
			 * }
			 */

			if (goodNeigTiles.contains(wangTile[0]) && goodNeigTiles.contains(wangTile[1])) {
				isUsableStarttile = true;
				// System.out.println(wangTile[0]+""+wangTile[1]+""+wangTile[2]+""+wangTile[3]);
			}

			if (isUsableStarttile) {
				goodNeighbourWangTiles.add(wangTile);
			}
		}

		return goodNeighbourWangTiles;
	}

	private void selectFirstRow() {

		ArrayList<int[]> possibaleWangsT;
		int tmp;

		for (int tilePos = 1; tilePos < levelLayout[0]; tilePos++) {

			possibaleWangsT = new ArrayList<int[]>();

			for (int[] wang : WangTile_List) {

				tmp = 0;
				if (leftSideisCompatible(wang, tilePos, 0)) {

					tmp = (considerTerrainFactors(wang) + upTerrainFactors(wang));

					for (int a = 0; a < tmp; a++) {
						possibaleWangsT.add(wang);
					}
				}
			}
			levelArr[tilePos][0] = possibaleWangsT.get(rand.nextInt(possibaleWangsT.size()));
		}
	}

	private void selectFirstColumn() {

		ArrayList<int[]> possibaleWangsT = new ArrayList<int[]>();
		int tmp;

		for (int tilePos = 1; tilePos < levelLayout[1]; tilePos++) {

			possibaleWangsT = new ArrayList<int[]>();

			for (int[] wang : WangTile_List) {

				tmp = 0;
				if (topSideisCompatible(wang, 0, tilePos)) {

					tmp = (considerTerrainFactors(wang) + leftTerrainFactors(wang));

					for (int a = 0; a < tmp; a++) {
						possibaleWangsT.add(wang);
					}
				}
			}
			levelArr[0][tilePos] = possibaleWangsT.get(rand.nextInt(possibaleWangsT.size()));
		}

	}

	private void fillRestOfLevel() {

		// Füllt das Restliche Level Zeile für Zeile
		ArrayList<int[]> possibaleWangsT = new ArrayList<int[]>();
		int tmp;

		for (int yPos = 1; yPos < levelLayout[1]; yPos++) { // Da es Zeile für Zeile füllt ist es wichtig dass die
															// äußere Schleife die der Zeile ist, sodass man in der
															// inneren immer eine Reihe nach vorne geht

			for (int xPos = 0; xPos < levelLayout[0]; xPos++) {

				possibaleWangsT = new ArrayList<int[]>();

				for (int[] wang : WangTile_List) {

					tmp = 0;

					if (xPos == 0) {

						if (topSideisCompatible(wang, xPos, yPos) && allNextRightUpsCompatible(wang, xPos, yPos// &&
																												// rightUpisCompatible(wang,
																												// xPos,
																												// yPos
						)) {

							tmp = (considerTerrainFactors(wang) + leftTerrainFactors(wang));

							for (int a = 0; a < tmp; a++) {
								possibaleWangsT.add(wang);
							}
						}

					} else {
						if (topSideisCompatible(wang, xPos, yPos) && leftSideisCompatible(wang, xPos, yPos)
								&& allNextRightUpsCompatible(wang, xPos, yPos // && rightUpisCompatible(wang, xPos, yPos
								)) {

							tmp = considerTerrainFactors(wang);

							for (int a = 0; a < tmp; a++) {
								possibaleWangsT.add(wang);
							}
						}
					}
				}

				levelArr[xPos][yPos] = possibaleWangsT.get(rand.nextInt(possibaleWangsT.size()));

			}
		}

	}

	private int considerTerrainFactors(int[] wangTile) {

		// Die Methode berücksichtigt die TerrainFactoren für die nicht Abhängigen Teile
		// der WangTile

		int tmp = 0;

		for (int i = 1; i < 3; i++) {

			tmp += terrainFactors[wangTile[i]];
		}

		return tmp;

	}

	private int upTerrainFactors(int[] wangTile) {

		return terrainFactors[wangTile[0]];
	}

	private int leftTerrainFactors(int[] wangTile) {

		return terrainFactors[wangTile[3]];
	}

	private boolean leftSideisCompatible(int[] wang, int xPos, int yPos) {

		// Die Methode testet ob die Linke Seite der WangTile die gleiche Seite ist wie
		// die Rechte Seite des linken Nachbarn der WangTile

		int wangLeft = wang[3];
		int neigboursRightTile = levelArr[(xPos - 1)][yPos][1];

		// System.out.println("LeftSide tile ist: "+levelArr[(xPos -
		// 1)][yPos][0]+""+levelArr[(xPos - 1)][yPos][1]+""+levelArr[(xPos -
		// 1)][yPos][2]+""+levelArr[(xPos - 1)][yPos][3]);

		return (wangLeft == neigboursRightTile);

	}

	private boolean topSideisCompatible(int[] wang, int xPos, int yPos) {

		// Die Methode testet ob die obere Seite der WangTile die gleiche Seite ist wie
		// die untere Seite des oberen Nachbarn der WangTile

		int wangTop = wang[0];
		int neigboursDownTile = levelArr[xPos][(yPos - 1)][2];

		// System.out.println("TopSide Tile ist: "+levelArr[xPos][(yPos -
		// 1)][0]+""+levelArr[xPos][(yPos - 1)][1]+""+levelArr[xPos][(yPos -
		// 1)][2]+""+levelArr[xPos][(yPos - 1)][3]);

		return (wangTop == neigboursDownTile);

	}

	private boolean rightUpisCompatible(int[] wang, int xPos, int yPos) {

		if (xPos < (levelLayout[0] - 1)) {
			int wangRight = wang[1];
			int diagonalNeighDownTile = levelArr[(xPos + 1)][(yPos - 1)][2];

			// System.out.println("Right up Side ist: "+levelArr[(xPos + 1)][(yPos -
			// 1)][0]+""+levelArr[(xPos + 1)][(yPos - 1)][1]+""+levelArr[(xPos + 1)][(yPos -
			// 1)][2]+""+levelArr[(xPos + 1)][(yPos - 1)][3]);

			return (optionLists.get(wangRight).get(diagonalNeighDownTile));
		} else {
			return true;
		}

	}

	private boolean allNextRightUpsCompatible(int[] wang, int xPos, int yPos) {

		ArrayList<int[]> tmpList = new ArrayList<int[]>();
		tmpList.add(wang);

		if (xPos < (levelLayout[0] - 1)) {

			return rekursivAllRowsCompatible(tmpList, xPos, yPos);

		} else {
			return true;
		}

	}

	private boolean rekursivAllRowsCompatible(ArrayList<int[]> wangList, int xPos, int yPos) {

		if (wangList.isEmpty()) {
			return false;
		}

		ArrayList<int[]> tmpWangList = new ArrayList<int[]>();
		if (!(xPos < (levelLayout[0] - 1))) {

			return true;

		} else {

			//ArrayList<int[]> leftValidTiles = new ArrayList<int[]>();


			int rightUpDownValue = levelArr[(xPos + 1)][(yPos - 1)][2];

			ArrayList<Integer> compatibaleRights = new ArrayList<Integer>();

			
			for (int[] wang : wangList) {
				
				if(!(compatibaleRights.contains(wang[1]))) {
					compatibaleRights.add(wang[1]);
				}
			}
			
			for (int i = 0; i < numOfDifferentTerrain; i++) {

				if (!(optionLists.get(rightUpDownValue).get(i))) {

					compatibaleRights.remove(Integer.valueOf(i));
				}
			}
			
			for (int[] topWang : topKeymap.get(rightUpDownValue)) {
				
				if(compatibaleRights.contains(topWang[3]))
				tmpWangList.add(topWang);
			}
		

			/*
			 * for (int[] is : wangList) {
			 * 
			 * if (rightUpisCompatible(is, xPos, yPos)) {
			 * 
			 * for (int[] wang : WangTile_List) { if (wang[3] == is[1]) {
			 * leftValidTiles.add(wang);
			 * 
			 * }
			 * 
			 * } } }
			 
			for (int[] topWang : leftValidTiles) {
				if (topSideisCompatible(topWang, (xPos + 1), yPos)) {
					tmpWangList.add(topWang);
				}
			}
*/
			return rekursivAllRowsCompatible(tmpWangList, (xPos + 1), yPos);
		}

	}

	private ArrayList<ArrayList<int[]>> generateTopWangKeyMap(ArrayList<int[]> wangs) {

		ArrayList<ArrayList<int[]>> keymap = new ArrayList<ArrayList<int[]>>();

		for(int i=0; i< numOfDifferentTerrain;i++) {
			
			keymap.add(i, new ArrayList<int[]>());
			
		}
		
		for (int[] wangTile : wangs) {

			keymap.get(wangTile[0]).add(wangTile);

		}

		return keymap;

	}

	private ArrayList<ArrayList<int[]>> generateRightWangKeyMap(ArrayList<int[]> wangs) {

		ArrayList<ArrayList<int[]>> keymap = new ArrayList<ArrayList<int[]>>();

		for (int[] wangTile : wangs) {

			keymap.get(wangTile[1]).add(wangTile);

		}

		return keymap;

	}

	private void convertToStringArray() {

		levelStringArr = new String[levelLayout[0]][levelLayout[1]];

		// System.out.println("Max zaheln sit "+maxZahlenStellen);
		String tmp;
		for (int yPos = 0; yPos < levelLayout[1]; yPos++) {

			for (int xPos = 0; xPos < levelLayout[0]; xPos++) {

				tmp = "";
				for (int i = 0; i < 4; i++) {
					tmp += getFührendeNullen(levelArr[xPos][yPos][i]);
					tmp += levelArr[xPos][yPos][i] + "";
				}

				levelStringArr[xPos][yPos] = tmp;
			}
		}

	}

	private String getFührendeNullen(int i) {

		String tmp = "";
		int tmpNumStellen = 0;

		if (i < 2)
			i = 2;
		for (double x = (double) i; x >= 1.0;) {
			tmpNumStellen++;
			x /= 10.0;
		}
		// System.out.println("Für zahl "+i+" sind stellen "+tmpNumStellen);
		for (int stellDiff = (maxZahlenStellen - tmpNumStellen); stellDiff > 0; stellDiff--) {
			tmp += 0;
		}

		return tmp;
	}

	public int[][][] getlvlIntArr() {
		return levelArr;
	}

	public String[][] getLvlStringArr() {
		return levelStringArr;
	}
	
	public ArrayList<int[]> getAllWangTiles(){
		
		return WangTile_List;
	}

}
