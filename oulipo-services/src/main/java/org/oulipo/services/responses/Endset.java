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
package org.oulipo.services.responses;

import java.util.Collection;
import java.util.HashSet;

import org.oulipo.net.TumblerAddress;
import org.oulipo.rdf.annotations.ObjectIRI;
import org.oulipo.rdf.annotations.Predicate;
import org.oulipo.resources.utils.Add;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Endset {

	@Predicate("fromVSpan")
	@ObjectIRI
	public HashSet<TumblerAddress> fromVSpans;

	@Predicate("toVSpan")
	@ObjectIRI
	public HashSet<TumblerAddress> toVSpans;

	@Predicate("linkType")
	@ObjectIRI
	public TumblerAddress[] types;

	public void addType(Collection<TumblerAddress> type) {
		types = Add.both(types, type, TumblerAddress.class);
	}

	public void addType(TumblerAddress type) {
		types = Add.one(types, type);
	}

	public void addType(TumblerAddress[] type) {
		types = Add.both(types, type, TumblerAddress.class);
	}
}