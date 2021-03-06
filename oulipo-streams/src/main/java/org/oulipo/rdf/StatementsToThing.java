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
package org.oulipo.rdf;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import org.oulipo.rdf.annotations.ObjectBoolean;
import org.oulipo.rdf.annotations.ObjectDate;
import org.oulipo.rdf.annotations.ObjectEnum;
import org.oulipo.rdf.annotations.ObjectIRI;
import org.oulipo.rdf.annotations.ObjectNonNegativeInteger;
import org.oulipo.rdf.annotations.ObjectNumber;
import org.oulipo.rdf.annotations.ObjectString;
import org.oulipo.rdf.annotations.ObjectURL;
import org.oulipo.rdf.annotations.ObjectXSD;
import org.oulipo.rdf.annotations.Predicate;
import org.oulipo.rdf.model.Document;
import org.oulipo.rdf.model.Link;
import org.oulipo.rdf.model.Schema;
import org.oulipo.rdf.model.User;
import org.oulipo.streams.IRI;

public final class StatementsToThing {

	public static Thing create(String type) {
		if (Schema.DOCUMENT.equals(type)) {
			return new Document();
		} else if (Schema.LINK.equals(type)) {
			return new Link();
		} else if (Schema.USER.equals(type)) {
			return new User();
		}
		return null;
	}

	private static String getType(Statement[] statements) {
		for (Statement statement : statements) {
			if (DataType.RDF_TYPE.equals(statement.getPredicate().getValue())) {
				return statement.getObject().getValue();
			}
		}
		return null;
	}

	/**
	 * PredicateValue -> statement
	 * 
	 * @return
	 */
	private static HashMap<String, ArrayList<Statement>> toHash(Statement[] statements) {
		HashMap<String, ArrayList<Statement>> map = new HashMap<>();
		for (Statement statement : statements) {
			String key = statement.getPredicate().getValue();
			if (map.containsKey(key)) {
				map.get(key).add(statement);
			} else {
				ArrayList<Statement> stmts = new ArrayList<>();
				stmts.add(statement);
				map.put(key, stmts);
			}

		}
		return map;
	}

	public static Thing transform(Statement[] statements) throws Exception {
		if (statements == null || statements.length == 0) {
			return null;
		}

		Thing thing = create(getType(statements));
		if (thing == null) {
			return null;
		}
		HashMap<String, ArrayList<Statement>> map = toHash(statements);
		for (Field field : thing.getClass().getFields()) {
			field.setAccessible(true);
			for (Annotation annotation : field.getAnnotations()) {
				if (annotation instanceof Predicate) {
					continue;
				}
				Annotation predicateAnnotation = field.getAnnotation(Predicate.class);
				if (predicateAnnotation == null) {
					continue;
				}
				String predicateIRI = ((Predicate) predicateAnnotation).value();

				ArrayList<Statement> statements2 = map.get("schema://oulipo/" + predicateIRI);
				if (statements2 == null || statements2.isEmpty()) {
					continue;
				}

				if (annotation instanceof ObjectIRI) {
					if (field.getType().isArray()) {
						IRI[] addresses = new IRI[statements2.size()];
						for (int i = 0; i < statements2.size(); i++) {
							addresses[i] = new IRI(statements2.get(i).getObject().getValue());
						}
						field.set(thing, addresses);
					}
				} else if (annotation instanceof ObjectString) {
					if (field.getType().isArray()) {
						String[] array = new String[statements2.size()];
						for (int i = 0; i < statements2.size(); i++) {
							array[i] = (statements2.get(i).getObject().getValue());
						}
						field.set(thing, array);
					} else {
						field.set(thing, statements2.iterator().next().getObject().getValue());
					}
				} else if (annotation instanceof ObjectEnum) {

				} else if (annotation instanceof ObjectBoolean) {
					field.set(thing, Boolean.valueOf(statements2.iterator().next().getObject().getValue()));
				} else if (annotation instanceof ObjectDate) {

				} else if (annotation instanceof ObjectNumber) {
					if (field.getType().isArray()) {

					} else {

					}
				} else if (annotation instanceof ObjectURL) {
					if (field.getType().isArray()) {

					} else {

					}
				} else if (annotation instanceof ObjectXSD) {
					if (field.getType().isArray()) {

					} else {

					}
				} else if (annotation instanceof ObjectNonNegativeInteger) {
					if (field.getType().isArray()) {
						Integer[] array = new Integer[statements2.size()];
						for (int i = 0; i < statements2.size(); i++) {
							array[i] = Integer.parseInt((statements2.get(i).getObject().getValue()));
						}
						field.set(thing, array);
					} else {
						field.set(thing, Integer.parseInt(statements2.iterator().next().getObject().getValue()));
					}
				}
			}
		}

		return thing;

	}

	public static Collection<Thing> transformThings(Statement[] statements) throws Exception {
		ArrayList<Thing> things = new ArrayList<>();
		if (statements == null || statements.length == 0) {
			return things;
		}
		// break into statement blocks (same subject) and call transform
		LinkedList<Statement> current = new LinkedList<>();
		RdfSubject subject = statements[0].getSubject();
		for (Statement stmt : statements) {
			if (!stmt.getSubject().equals(subject)) {
				if (current.isEmpty()) {
					continue;
				}
				subject = stmt.getSubject();
				Thing thing = transform(current.toArray(new Statement[current.size()]));
				if (thing != null) {
					things.add(thing);
				}
				current.clear();
				current.add(stmt);
			} else {
				current.add(stmt);
			}
		}
		Thing thing = transform(current.toArray(new Statement[current.size()]));
		things.add(thing);
		return things;
	}
}
