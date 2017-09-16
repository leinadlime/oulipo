/*******************************************************************************
 * OulipoMachine licenses this file to you under the Apache License, Version 2.0
 * (the "License");  you may not use this file except in compliance with the License.  
 *
 * You may obtain a copy of the License at
 *   
 *       http://www.apache.org/licenses/LICENSE-2.0
 *    
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. See the NOTICE file distributed with this work for 
 * additional information regarding copyright ownership. 
 *******************************************************************************/
package org.oulipo.browser.pages;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.oulipo.browser.api.AddressController;
import org.oulipo.browser.controls.OulipoTable;
import org.oulipo.browser.routers.ViewSourcePageRouter;
import org.oulipo.net.MalformedTumblerException;
import org.oulipo.resources.model.Document;

import com.jfoenix.controls.JFXButton;

import javafx.application.Platform;
import javafx.scene.control.Button;
import retrofit2.Call;
import retrofit2.Response;

public class UpdateDocumentController extends BaseController {

	private void attachAction(Button button, Document document, OulipoTable table) {
		button.setOnAction(e -> {
			document.title = table.getValue("Title");
			document.description = table.getValue("Description");
			try {
				tumblerService.createOrUpdateDocument(document, new retrofit2.Callback<Document>() {

					@Override
					public void onFailure(Call<Document> arg0, Throwable arg1) {

					}

					@Override
					public void onResponse(Call<Document> arg0, Response<Document> arg1) {
						address.setScheme("ted");
						Platform.runLater(() -> {
							try {
								addressController.show(address.toExternalForm());
							} catch (IOException e) {
								e.printStackTrace();
							}
						});

					}

				});
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	@Override
	public void show(AddressController controller) throws MalformedTumblerException, IOException {
		super.show(controller);

		tumblerService.getDocument(address.toTumblerAuthority(), new retrofit2.Callback<Document>() {

			@Override
			public void onFailure(Call<Document> arg0, Throwable arg1) {
				arg1.printStackTrace();
			}

			@Override
			public void onResponse(Call<Document> arg0, Response<Document> response) {
				JFXButton submit = new JFXButton("Update");

				if (response.isSuccessful()) {
					final Document document = response.body();

					Platform.runLater(() -> {
						try {
							OulipoTable table = new OulipoTable(300, 350)
									.addEditText("Tumbler Address", address.toTumblerFields(), false)
									.addEditText("Title", document.title, false)
									.addEditText("Description", document.description).addActions(submit);
							attachAction(submit, document, table);

							ViewSourcePageRouter.showPageSource(ctx.getTabManager(), address, table);
							addressController.addContent(table, "Update Document");

						} catch (MalformedTumblerException e1) {
							e1.printStackTrace();
							return;
						}
					});
				} else {
					Platform.runLater(() -> {
						OulipoTable table = new OulipoTable(300, 350);
						try {
							table.addEditText("Tumbler Address", address.toTumblerFields(), false);
						} catch (MalformedTumblerException e) {
							e.printStackTrace();
						}
						table.addEditText("Title", "").addEditText("Description", "").addActions(submit);

						Document document = new Document();
						document.resourceId = address;

						attachAction(submit, document, table);

						addressController.addContent(table, "Create Document");

					});

				}
			}
		});
	}

}
