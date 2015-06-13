import org.freeplane.plugin.script.proxy.ScriptUtils
import groovy.util.*
import groovy.xml.*

	
/*
 * Set up the details markup
*/
def static setupDetails(langsConfigs) {
	
	def node = ScriptUtils.node()
	
	// Set up the language labels as markup
	def langsWriter = new StringWriter() 
	def langsBuilder = new MarkupBuilder(langsWriter)
	
	// Build the markup for the language config values
	langsBuilder.html {
		head(){}
		body(){
			langsConfigs.each{ind, lang->
				p ind
			}
		}
	}

	// The existing lines of details
	def oldDetails = node.detailsText ?: "<body></body>"
	def oldLines = new XmlParser().parseText(oldDetails)
	def pElementsOld = oldLines.depthFirst().findAll { it.name() == 'p' }

	// The new lines of details
	def newLines = new XmlParser().parseText(langsWriter.toString())
	def pElementsNew = newLines.depthFirst().findAll { it.name() == 'p' }
	
	// Establish the MarkupBuilder
	def writer = new StringWriter() 
	def builder = new MarkupBuilder(writer)
	
	// Build the initial html markup
	builder.html {
		head(){}
		body(){
			
			// todo need to check if any language labels are in the old 
			// but not in the new and eliminate them from old if not
			pElementsOld.each{itemOld->
				p itemOld.text()
			}

			// Check if each new language label is in the old collection and add if not
		    pElementsNew.each{itemNew->
				// Check that the p item.text is in any of the
				// pElementsOld.text() values
				// If not add it
				if ( !pElementsOld.any{ it.text().contains(itemNew.text())}) {
					p itemNew.text()
				}
		    }
		
		}
	}
	
	node.detailsText = writer.toString()
	
}

	

