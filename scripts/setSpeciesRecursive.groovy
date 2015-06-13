// @ExecutionModes({ON_SELECTED_NODE_RECURSIVELY})

import groovy.util.*
import groovy.xml.*

	// Also there exists a node.hasStyle 
	if (node.style.name == "Species") {
		
/*		def nodeAttributes = node.attributes*/
		
		// Collect the config attribute list
		def collected = ConfigsLib.getConfigs("Config", "Species")

		// Add attributes if they are missing
		AttsLib.addNewAtts(collected)

		// Sort
		AttsLib.sortAttsTable()

		// Set the attribute table to optimal width
		AttsLib.setTableOptWidth()	

		// Add the companion planting sub-nodes
		NodesLib.addSubNode("Compatible Plantings", "Compatible")
		NodesLib.addSubNode("Incompatible Plantings", "Compatible")
		
		// Set up the details section with the common name language labels
		// Get the languages from the config section
		def langsConfigs = ConfigsLib.getConfigs("Config", "Languages")

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
		
		// Set up the core text to have smaller font sizes for each lower p element
		CoreLib.setupCoreFontSizes(htmlUtils)

		DetailsLib.setupDetails(langsWriter.toString())
	
	}
	
