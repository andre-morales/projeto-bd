package vetcare.gui;

import javafx.fxml.FXML;

public class MainMenuController {

	@FXML
	public void goPacientes() {
		VetCareApp.screens.switchScreen("/vetcare/gui/Scenes/pacientes.fxml");
	}
}