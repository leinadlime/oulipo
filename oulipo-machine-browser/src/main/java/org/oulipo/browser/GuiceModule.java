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
package org.oulipo.browser;

import java.io.IOException;

import org.oulipo.browser.api.ApplicationContext;
import org.oulipo.browser.framework.PageRouter;
import org.oulipo.browser.framework.StorageContext;
import org.oulipo.browser.pages.AddressRouter;

import com.google.inject.AbstractModule;

import javafx.fxml.FXMLLoader;

public class GuiceModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(FXMLLoader.class).toProvider(FXMLLoaderProvider.class);
		try {
			bind(StorageContext.class).toInstance(new StorageContext());
		} catch (IOException e) {
			e.printStackTrace();
		}
		bind(ApplicationContext.class).toInstance(new ApplicationContext());
		bind(PageRouter.class).toInstance(new AddressRouter());

	}
}
