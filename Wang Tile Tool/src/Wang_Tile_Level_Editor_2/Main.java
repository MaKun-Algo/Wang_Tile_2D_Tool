package Wang_Tile_Level_Editor_2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

public class Main extends Application {

	
	
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
		
		View view = new View();
		Presenter presenter = new Presenter();
		Model model = new Model();
		
		presenter.setView(view);
		presenter.setModel(model);
		view.setPresenter(presenter);
		
		ScrollPane sp = new ScrollPane();
		sp.setContent(view.getView());
		
		view.setStage(primaryStage);
		
		Scene scene = new Scene (sp,900,900);
		primaryStage.setScene(scene);
		primaryStage.setTitle("WangTile Level Generator_2");
		primaryStage.show();
		
	}
	
	
	public static void main(String[]args) {
		
		
		launch(args);
	}
}
