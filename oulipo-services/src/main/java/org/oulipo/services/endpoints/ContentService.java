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
package org.oulipo.services.endpoints;

import java.io.IOException;
import java.util.List;

import org.oulipo.net.MalformedSpanException;
import org.oulipo.net.TumblerAddress;
import org.oulipo.resources.ResourceNotFoundException;
import org.oulipo.security.auth.AuthenticationException;
import org.oulipo.security.auth.UnauthorizedException;
import org.oulipo.services.EditResourceException;
import org.oulipo.services.MissingBodyException;
import org.oulipo.services.OulipoRequest;
import org.oulipo.services.ResourceSessionManager;
import org.oulipo.streams.OulipoMachine;
import org.oulipo.streams.StreamLoader;
import org.oulipo.streams.VariantSpan;
import org.oulipo.streams.impl.StreamOulipoMachine;

public class ContentService {

	private final ResourceSessionManager sessionManager;

	private final StreamLoader streamLoader;

	public ContentService(ResourceSessionManager sessionManager, StreamLoader streamLoader) {
		this.sessionManager = sessionManager;
		this.streamLoader = streamLoader;
	}

	public String copyContent(OulipoRequest oulipoRequest)
			throws AuthenticationException, UnauthorizedException, ResourceNotFoundException, IOException,
			MalformedSpanException, EditResourceException, MissingBodyException {
		oulipoRequest.authenticate();
		oulipoRequest.authorize();

		TumblerAddress elementAddress = oulipoRequest.getElementAddress();

		OulipoMachine om = StreamOulipoMachine.create(streamLoader, oulipoRequest.getDocumentAddress(), true);

		if (!elementAddress.isBytesElement()) {
			throw new EditResourceException(elementAddress, "Attempting to copy content outside of byte space");
		}

		List<VariantSpan> vspans = oulipoRequest.getVariantSpans();
		om.copy(elementAddress.getElement().get(1), vspans);

		return "{}";

	}

	public String deleteContent(OulipoRequest oulipoRequest) throws AuthenticationException, UnauthorizedException,
			ResourceNotFoundException, IOException, MalformedSpanException, EditResourceException {
		oulipoRequest.authenticate();
		oulipoRequest.authorize();

		TumblerAddress elementAddress = oulipoRequest.getElementAddress();

		OulipoMachine om = StreamOulipoMachine.create(streamLoader, elementAddress, true);

		if (elementAddress.hasSpan()) {
			om.delete(new VariantSpan(elementAddress));
			return "{}";// OK: Response
		} else {
			throw new EditResourceException(elementAddress, "Attempting to delete content without specifying a span");

		}
	}

	public String insertContent(OulipoRequest oulipoRequest) throws AuthenticationException, UnauthorizedException,
			ResourceNotFoundException, IOException, MalformedSpanException, EditResourceException {
		oulipoRequest.authenticate();
		oulipoRequest.authorize();

		TumblerAddress elementAddress = oulipoRequest.getElementAddress();

		OulipoMachine om = StreamOulipoMachine.create(streamLoader, elementAddress, true);

		if (elementAddress.getElement().get(0) != 1) {
			throw new EditResourceException(elementAddress, "Attempting to insert content outside of byte space");
		}
		// TODO:
		// om..insert(elementAddress.getElement().get(1), request.body());
		return "{}";

	}

	public String swap(OulipoRequest oulipoRequest)
			throws AuthenticationException, UnauthorizedException, ResourceNotFoundException, IOException,
			MalformedSpanException, EditResourceException, MissingBodyException {
		oulipoRequest.authenticate();
		oulipoRequest.authorize();

		TumblerAddress documentAddress = oulipoRequest.getDocumentAddress();

		OulipoMachine om = StreamOulipoMachine.create(streamLoader, documentAddress, true);
		String spanField = oulipoRequest.getSpans();
		String[] spans = spanField.split("[,]");
		String[] span1 = spans[0].split("~");
		String[] span2 = spans[1].split("~");
		om.swap(new VariantSpan(Integer.parseInt(span1[0]), Integer.parseInt(span1[1])),
				new VariantSpan(Integer.parseInt(span2[0]), Integer.parseInt(span2[1])));
		return "{}";

	}
}