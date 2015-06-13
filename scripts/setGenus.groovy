
// @ExecutionModes({ON_ALL_SELECTED_NODES})


import groovy.util.*
import groovy.xml.*
	
	// Collect the config attribute list
	def collected = ConfigsLib.getConfigs("Config", "Genus")
	
	if (collected) {
		// Add in the attributes
		AttsLib.addNewAtts(collected)	
	}
	
	if (node.attributes) {
		// Sort
		AttsLib.sortAttsTable()
		// Optimal width
		AttsLib.setTableOptWidth()
	}
		
	// Set up the core text to have smaller font sizes for each lower p element
	CoreLib.setupCoreFontSizes(htmlUtils)
		
	// Assign the style to the node
	node.style.name = "Genus"
	

