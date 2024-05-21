package Wang_Tile_Level_Editor_2;

import java.awt.Color;
import java.util.ArrayList;

import Wang_Tile_Level_Editor_2.NumberTextField;
import Wang_Tile_Level_Editor_2.Presenter;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class View extends VBox {

	private Presenter presenter;
	private Slider waterSlider;
	private Slider inlandSlider;
	private TextField horizWangs;
	private TextField verticWangs;
	private Button createLvlButton;
	private Button createAndShowImageButton;
	private ArrayList<TextField> textFieldArray;
	private ArrayList<Slider> sliderArray;
	private ArrayList<ArrayList<Boolean>> optionLists;
	private ArrayList<ComboBox<String>> colorList;
	private ComboBox<String> colors;
	private ArrayList<Button> optionsButtonArray;
	private ArrayList<Button> deleteButtonArray;
	private Button moreTeraBut;
	private int amountSlider = 0;
	private GridPane sliderGrid;

	public View() {

		setSpacing(10);
		initView();
		initFirstExample();

	}

	public void initView() {

		colors = new ComboBox<String>();

		colors.getItems().addAll("BLACK", "GRAY", "BLUE", "CYAN", "RED", "MAGENTA", "GREEN", "ORANGE", "YELLOW",
				"PINK");
		// colors.getSelectionModel().select(index);

		textFieldArray = new ArrayList<TextField>();
		sliderArray = new ArrayList<Slider>();
		optionLists = new ArrayList<ArrayList<Boolean>>();
		colorList = new ArrayList<ComboBox<String>>();
		optionsButtonArray = new ArrayList<Button>();
		deleteButtonArray = new ArrayList<Button>();
		moreTeraBut = new Button("Füge neues Terrain zu");
		moreTeraBut.setOnAction(e -> onButtonMoreTera());
		sliderGrid = new GridPane();
		sliderGrid.setHgap(50);
		sliderGrid.setVgap(20);
		sliderGrid.setPadding(new Insets(10, 10, 10, 10));

		waterSlider = new Slider(1, 10, 1);
		inlandSlider = new Slider(1, 10, 1);
		horizWangs = new NumberTextField();
		verticWangs = new NumberTextField();

		Slider[] tmpArr = { waterSlider, inlandSlider };

		for (Slider slider : tmpArr) {

			slider.setShowTickLabels(true);
			slider.setShowTickMarks(true);
			slider.setMajorTickUnit(1);
			slider.setMinorTickCount(0);
			slider.setSnapToTicks(true);
		}

		createLvlButton = new Button("Erzeuge Level");
		createLvlButton.setOnAction(e -> {
			correctLayout();
			presenter.createButtonClicked();
			createAndShowImageButton.setDisable(false);
			;
		});
		createAndShowImageButton = new Button("Zeige erzeugtes Level als Bild");
		createAndShowImageButton.setDisable(true);
		createAndShowImageButton.setOnAction(e -> presenter.createImageButtonClicked());

		HBox horizBox = new HBox();
		HBox verticBox = new HBox();
		horizBox.getChildren().add(new Label("Horizontale WangTiles Menge: "));
		verticBox.getChildren().add(new Label("Verticale WangTiles Menge: "));
		horizBox.getChildren().add(horizWangs);
		verticBox.getChildren().add(verticWangs);

		getChildren().add(horizBox);
		getChildren().add(verticBox);
		getChildren().add(createLvlButton);
		getChildren().add(createAndShowImageButton);
		getChildren().add(new Label("Terrain Multiplikatoren für das Auftreten jeweiliger Flächen: "));
		getChildren().add(sliderGrid);

		sliderGrid.add(moreTeraBut, 0, 0);
		horizWangs.setText("10");
		verticWangs.setText("10");

	}
	
	
	private void initFirstExample() {
		
		for(int i=0;i<5;i++)
			onButtonMoreTera();
		
		//1 Wiese, 2 Wasser, 3 Wasserübergang, 4 Stadt, 5 Häuser
		
		colorList.get(0).getSelectionModel().select(6);
		colorList.get(1).getSelectionModel().select(2);
		colorList.get(2).getSelectionModel().select(7);
		colorList.get(3).getSelectionModel().select(1);
		colorList.get(4).getSelectionModel().select(4);
		
		textFieldArray.get(0).setText("Wiese");
		textFieldArray.get(1).setText("Wasser");
		textFieldArray.get(2).setText("Wasserübergang");
		textFieldArray.get(3).setText("Stadt");
		textFieldArray.get(4).setText("Häuser");
		
		sliderArray.get(0).setValue(3);
		sliderArray.get(1).setValue(4);
		sliderArray.get(2).setValue(3);
		sliderArray.get(3).setValue(5);
		sliderArray.get(4).setValue(3);
		
		optionLists.get(0).set(2, true);
		optionLists.get(0).set(3, true);
		optionLists.get(1).set(2, true);
		optionLists.get(2).set(0, true);
		optionLists.get(2).set(1, true);
		optionLists.get(4).set(3, true);
		optionLists.get(3).set(0, true);
		optionLists.get(3).set(4, true);
		
		sliderGrid.getChildren().remove(deleteButtonArray.get(0));
		
		
	}

	public void setPresenter(Presenter presen) {
		this.presenter = presen;
	}

	public VBox getView() {
		return this;
	}

	private void onButtonMoreTera() {

		sliderGrid.getChildren().remove(moreTeraBut);

		TextField tmpField = new TextField("Benenne neues Terrain");
		Slider tmpSlider = new Slider(1, 10, 1);
		Button tmpOptions = new Button("Optionen");
		Button tmpDelete = new Button("Lösche Terrain");
		ComboBox<String> tmpColor = new ComboBox<String>();
		tmpColor.setItems(colors.getItems());

		tmpOptions.setOnAction(e -> makeOptionScene(tmpOptions));
		tmpDelete.setOnAction(e -> deleteTerrain(tmpDelete));

		addNewObjectsToGrid(tmpField, tmpSlider, tmpOptions, tmpDelete, tmpColor);

		sliderGrid.add(moreTeraBut, 0, (amountSlider));

	}

	private void addNewObjectsToGrid(TextField tmpTex, Slider tmpSlid, Button option, Button delete,
			ComboBox<String> colorBox) {

		tmpSlid.setShowTickLabels(true);
		tmpSlid.setShowTickMarks(true);
		tmpSlid.setMajorTickUnit(1);
		tmpSlid.setMinorTickCount(0);
		tmpSlid.setSnapToTicks(true);

		sliderArray.add(tmpSlid);
		textFieldArray.add(tmpTex);
		optionsButtonArray.add(option);
		deleteButtonArray.add(delete);
		colorList.add(colorBox);

		colorBox.getSelectionModel().select((amountSlider % colorBox.getItems().size()));

		sliderGrid.add(colorBox, 0, amountSlider);
		sliderGrid.add(tmpTex, 1, amountSlider);
		sliderGrid.add(tmpSlid, 2, amountSlider);
		sliderGrid.add(option, 3, amountSlider);
		sliderGrid.add(delete, 4, amountSlider);

		addNewObjectToOption();
		createNewOptionList();

		amountSlider++;

	}

	private void createNewOptionList() {

		ArrayList<Boolean> tmpOptionList = new ArrayList<Boolean>();

		for (int i = 0; i < amountSlider; i++) {
			tmpOptionList.add(i, false);
		}
		tmpOptionList.add(amountSlider, true);

		optionLists.add(tmpOptionList);

	}

	private void addNewObjectToOption() {

		for (ArrayList<Boolean> opList : optionLists) {

			opList.add(false);
		}

	}

	private Stage primStage;

	public void setStage(Stage primSt) {

		primStage = primSt;

	}

	private void makeOptionScene(Button but) {

		int index=optionsButtonArray.indexOf(but);
		
		Stage optionStage = new Stage();

		optionStage.initModality(Modality.WINDOW_MODAL);
		optionStage.initOwner(primStage);

		GridPane optionPane = optionPane(index, optionStage);

		ScrollPane sp = new ScrollPane();
		sp.setContent(optionPane);

		Scene optionScene = new Scene(sp, 1000, 1000);

		optionStage.setScene(optionScene);
		optionStage.setTitle("WangTileator_2");
		optionStage.show();

	}
	
	private void deleteTerrain(Button but) {
		
		int index =deleteButtonArray.indexOf(but);
		sliderGrid.getChildren().remove(moreTeraBut);
		
		sliderGrid.getChildren().remove(colorList.get(index));
		sliderGrid.getChildren().remove(textFieldArray.get(index));
		sliderGrid.getChildren().remove(sliderArray.get(index));
		sliderGrid.getChildren().remove(optionsButtonArray.get(index));
		sliderGrid.getChildren().remove(deleteButtonArray.get(index)); 
		
		
		for(int i =index;i<(amountSlider-1);i++) {
			
			sliderGrid.getChildren().remove(colorList.get(i+1));
			sliderGrid.getChildren().remove(textFieldArray.get(i+1));
			sliderGrid.getChildren().remove(sliderArray.get(i+1));
			sliderGrid.getChildren().remove(optionsButtonArray.get(i+1));
			sliderGrid.getChildren().remove(deleteButtonArray.get(i+1));
			
			sliderGrid.add(colorList.get(i+1), 0, i);
			sliderGrid.add(textFieldArray.get(i+1), 1, i);
			sliderGrid.add(sliderArray.get(i+1), 2, i);
			sliderGrid.add(optionsButtonArray.get(i+1), 3, i);
			sliderGrid.add(deleteButtonArray.get(i+1), 4, i);
			
			
		}
		amountSlider--;
		
		sliderGrid.add(moreTeraBut, 0, (amountSlider));
		
		textFieldArray.remove(index);
		colorList.remove(index);
		sliderArray.remove(index);
		optionsButtonArray.remove(index);
		deleteButtonArray.remove(index);
		
	}

	private GridPane optionPane(int index, Stage optionStage) {

		GridPane optionPane = new GridPane();

		optionPane.setHgap(50);
		optionPane.setVgap(20);
		optionPane.setPadding(new Insets(10, 10, 10, 10));

		optionPane.add(new Label("Angrenzungsoptionen für " + textFieldArray.get(index).getText()), 0, 0);

		optionPane.add(new Label("Terrainart:"), 0, 1);
		optionPane.add(new Label("Darf daran angrenzen:"), 1, 1);

		int[] tmpIndexArr = new int[(optionLists.size() - 1)];

		int tmpIndex = 0;

		for (int i = 0; i < optionLists.size(); i++) {
			if (i == index)
				continue;
			tmpIndexArr[tmpIndex] = i;
			tmpIndex++;
		}

		ArrayList<CheckBox> checkBoxList = new ArrayList<CheckBox>();

		int tmpRow = 2;

		for (int i : tmpIndexArr) {

			CheckBox tmpBox = new CheckBox();

			checkBoxList.add(tmpBox);

			optionPane.add(new Label(textFieldArray.get(i).getText()), 0, (tmpRow));
			optionPane.add(tmpBox, 1, (tmpRow));

			tmpBox.setSelected(optionLists.get(index).get(i));

			tmpRow++;

		}

		Button safe = new Button("Speichere Änderungen und Beende Editieren");

		optionPane.add(safe, 0, tmpRow);

		safe.setOnAction(e -> {
			updateNeighbourOptions(index, checkBoxList);
			optionStage.close();
		});

		return optionPane;
	}

	private void updateNeighbourOptions(int index, ArrayList<CheckBox> checkboxList) {

		int optionIndex = 0;
		for (int i = 0; i < optionLists.get(index).size(); i++) { // Speichere die Änderungen an der Liste des Terrains

			if (index == i)
				continue;

			optionLists.get(index).set(i, checkboxList.get(optionIndex).isSelected());
			optionIndex++;
		}

		optionIndex = 0;
		for (ArrayList<Boolean> otherOptionLists : optionLists) { // Speichere entstandene Wechselwirkungen von anderen
																	// terrains

			otherOptionLists.set(index, optionLists.get(index).get(optionIndex));
			optionIndex++;

		}

	}

	private void correctLayout() {

		if (Integer.parseInt(horizWangs.getText()) < 2)
			horizWangs.setText("2");

		if (Integer.parseInt(verticWangs.getText()) < 2)
			verticWangs.setText("2");
	}

	public int[] getLvlLayout() { // Gibt die Menge der Horizontalen und verticalen WangTiles zurück (Layout des
		// Levels)

		int[] tmp = new int[2];
		tmp[0] = Integer.parseInt(horizWangs.getText());
		tmp[1] = Integer.parseInt(verticWangs.getText());

		return tmp;
	}

	public ArrayList<Slider> getSliderList() {

		return sliderArray;
	}

	public ArrayList<ArrayList<Boolean>> getOptionLists() {

		return optionLists;
	}

	public ArrayList<Color> getColorList() {

		ArrayList<Color> tmp = new ArrayList<Color>();

		// Color.BLACK, Color.GRAY, Color.BLUE, Color.CYAN, Color.RED, Color.MAGENTA,
		// Color.GREEN,
		// Color.ORANGE, Color.YELLOW, Color.PINK

		String tmpStr;
		for (ComboBox<String> box : colorList) {

			// tmp.add(box.getValue());

			tmpStr = box.getValue();

			if (tmpStr.equals("BLACK")) {
				tmp.add(Color.BLACK);
			} else if (tmpStr.equals("GRAY")) {

				tmp.add(Color.GRAY);
			} else if (tmpStr.equals("BLUE")) {

				tmp.add(Color.BLUE);
			} else if (tmpStr.equals("CYAN")) {

				tmp.add(Color.CYAN);
			} else if (tmpStr.equals("RED")) {

				tmp.add(Color.RED);
			} else if (tmpStr.equals("MAGENTA")) {

				tmp.add(Color.MAGENTA);
			} else if (tmpStr.equals("GREEN")) {

				tmp.add(Color.GREEN);
			} else if (tmpStr.equals("ORANGE")) {

				tmp.add(Color.ORANGE);
			}
			else if (tmpStr.equals("YELLOW")) {

				tmp.add(Color.YELLOW);
			}
			else if (tmpStr.equals("PINK")) {

				tmp.add(Color.PINK);
			}

		}

		return tmp;
	}

}
