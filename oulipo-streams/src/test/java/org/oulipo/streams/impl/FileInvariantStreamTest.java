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
package org.oulipo.streams.impl;

import java.io.File;

import org.junit.Test;
import org.oulipo.streams.InvariantStream;

public class FileInvariantStreamTest {

	@Test
	public void streamLoadable() throws Exception {
		File file = new File("target/streams-junit/FileInvariantStream-" + System.currentTimeMillis() + ".txt");
		InvariantStream stream = new FileInvariantStream(file, null, "fakeHash", null);
		stream.append("Hello");
		stream.append("Xanadu");

	}
}
