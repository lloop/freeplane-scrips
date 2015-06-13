
// @ExecutionModes({ON_ALL_SELECTED_NODES})

import groovy.util.*
import groovy.xml.*
	
	// Collect the attribute configs 
	def configAtts = ConfigsLib.getConfigs("Config", "Species")
	
	// Add the config atts to the existing
	if (configAtts) {
		AttsLib.addNewAtts(configAtts)	
	}
	
	// Sort atts and set width of table
	if (node.attributes) {
		// Sort
		AttsLib.sortAttsTable()
		// Optimal width
		AttsLib.setTableOptWidth()
	}
	
	// Add sub-nodes
	NodesLib.addSubNode("Compatible Plantings", "Compatible")
	NodesLib.addSubNode("Incompatible Plantings", "Compatible")
	
	// Collect the languages configs
	def langsConfigs = ConfigsLib.getConfigs("Config", "Languages")
	
	//
	DetailsLib.setupDetails(langsConfigs)
	
	// Set up the core text to have smaller font sizes for each lower p element
	CoreLib.setupCoreFontSizes(htmlUtils)
	
	node.style.name = "Species"
	
	