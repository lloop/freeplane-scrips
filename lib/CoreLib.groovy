
import org.freeplane.plugin.script.proxy.ScriptUtils
import groovy.util.*
import groovy.xml.*

	
/*
 * Set up the core text with taxon markup
*/
def static setupCoreFontSizes(htmlUtils) {
	
	def node = ScriptUtils.node()
	
	// It is set up now to only size p elements that exist.
	// Establishing empty p elements in the node core text only increases the 
	// size needlessly. After adding a newline of test the script will size the 
	// addition again when run
	
	// Get the existing core text p elements	
	def nodeCoreText = htmlUtils.isHtmlNode(node.text) ? node.text : htmlUtils.plainToHTML(node.text)
	def nodeLines = new XmlParser().parseText(nodeCoreText)
	def pElements = nodeLines.depthFirst().findAll{it.name() == 'p'}
	def pElementsCount = pElements.count{it}
	
	def writer = produceFontHierarchyWriter(pElements)
	
	node.text = writer.toString()
}


/*
 * Set up the core text with todays date or keep the original date
*/
def static setupJournalEntryCore() {
	
	def node = ScriptUtils.node()
	def today = new Date()
	if(!node.text) {
		node.text = today
	} 
}
	

/*
 * Produces the markup for core text that has smaller font sizes for each level of
 * p elements.
*/
def static produceFontHierarchyWriter(elements) {
	
	// Establish the MarkupBuilder
	def writer = new StringWriter() 
	def builder = new MarkupBuilder(writer)
			
	// Build the html markup
	builder.html {
		head(){}
		body(){
			
			// For each of the lines that exist
			elements.eachWithIndex{ elem, index ->
				
				def eleText = elem.text()
				
				if (index == 0) {
					p eleText
				} else {
					def fontSize = 5 - index	
					
					// todo - If the font element already exists within the p element
					// then the elem.text will return an empty string
					// because it is looking for text on the wrong level
					if (eleText.length() == 0) {
						def subElem = elem.depthFirst().findAll{it.text().length() > 0} 
						def subElemText = subElem[0].text()

						/* logger.info("fontElem: " + subElemText) */
						
						p {
							font (size: fontSize, subElemText) 
						}						
					} else {
						p {
							font (size: fontSize, eleText) 
						}
					}						
				}	
			}
		}	
	}
	return writer
}
