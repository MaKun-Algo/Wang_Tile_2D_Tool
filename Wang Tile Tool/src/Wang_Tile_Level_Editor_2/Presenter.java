package Wang_Tile_Level_Editor_2;

import Wang_Tile_Level_Editor_2.Model;
import Wang_Tile_Level_Editor_2.View;

public class Presenter {

	
	
	private View view;
	private Model model;
	
	public void setView(View view) {
		this.view = view;
	}
	
	public void setModel (Model model) {
		this.model = model;
	}
	
	public void createButtonClicked() {
		
		model.setLevelLayout(view.getLvlLayout());
		model.setSliderArr(view.getSliderList());
		model.setOptionArr(view.getOptionLists());
		model.setColorArr(view.getColorList());
		model.makeLevel();
		
		
	}
	
	
	public void createImageButtonClicked() {
		model.createAndShowImage();
	}
	
}
