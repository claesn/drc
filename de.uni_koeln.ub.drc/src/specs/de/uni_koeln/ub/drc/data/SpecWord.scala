/**************************************************************************************************
 * Copyright (c) 2010 Fabian Steeg. All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0 which accompanies this
 * distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 * <p/>
 * Contributors: Fabian Steeg - initial API and implementation
 *************************************************************************************************/
 
package de.uni_koeln.ub.drc.data
import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
/**
 * @see Word
 * @author Fabian Steeg (fsteeg)
 */
@RunWith(classOf[JUnitRunner])
private[drc] class SpecWord extends Spec with ShouldMatchers {

  describe("A Word") {
    val word = Word("test", Box(1,1,1,1))
    it("should contain an original form") { expect("test") { word.original } }
    it("should contain a position") { expect(Box(1,1,1,1)) { word.position } }
    it("should contain a first history entry") { 
        expect(Modification("test", "OCR")) { word.history(0) } 
    } 
    it("can be serialized as XML") { 
        expect(word.original) { (word.toXml \ "@original")(0).text.trim } 
    }
    it("can be deserialized from XML") {
        expect(1) {
            Word.fromXml(<word original="test">
                            <box width="1" height="1" y="1" x="1"></box>
                            <modification form="test" score="1" author="OCR">
                              <voters>
                                <voter name="me"></voter>
                              </voters>
                            </modification>
                          </word>).history.top.score
        }
    }
    it("provides edit suggestions") {
      val suggestions = Word("slaunt", Box(1,1,1,1)).suggestions
      println("Suggestions: " + suggestions.mkString(", "))
      expect(10) { suggestions.size }
      expect(31916) {Index.lexicon.size}
    }
  }
  
}