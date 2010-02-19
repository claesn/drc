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

/**
 * @see Page
 * @author Fabian Steeg
 */
class PageSpec extends Spec with ShouldMatchers {

  val file: java.io.File = java.io.File.createTempFile("testing", "scala")
  file.deleteOnExit
  
  describe("A Page") {
    
    val page = Page(List(Word("test", Box(1,1,1,1))))
      
    it("should contain words") { 
        expect(1) { page.words.size } 
    }
    
    it("can be serialized to XML") {
        expect(1) { (page.toXml \ "word").size }
    }
    
    it("can save a page of words to disk") {
      Page.mock.save(file)
      expect(true) {file.exists}
    }
    
  }
  
  describe("The Page companion object") {
  
    it("should provide usable test data") { expect(5) { Page.mock.words.size } }
  
    it("can load a page of words from disk") {
      val words: List[Word] = Page.load(file).words
      expect(Page.mock.words.size) { words.size }
      expect(Page.mock.words.toList) { words.toList }
    }
    
    it("should serialize and deserialize added modifications") {
      val page = Page.mock
      val word = page.words(0)
      val newMod = Modification(word.original.reverse, "tests")
      word.history push newMod
      expect(true) { word.history.contains(newMod) }
      expect(1) { word.history.size }
      page.save(file)
      val loadedWord = Page.load(file).words(0)
      val loadedMod = loadedWord.history.top
      expect(1) { loadedWord.history.size }
      expect(newMod) { loadedMod }
    }
    
    it("should be desializable both from a file and an input stream") {
        val loadedFromFile = Page.load(file).words(0)
        expect(1) { loadedFromFile.history.size }
        val loadedFromStream = Page.load(file.toURL.openStream).words(0)
        expect(1) { loadedFromFile.history.size }
    }
    
  }
  
}
