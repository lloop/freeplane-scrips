
// @ExecutionModes({ON_ALL_SELECTED_NODES})
	
	// Collect the config attribute list
	def collected = ConfigsLib.getConfigs("Config", "Order")
	
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
	
	node.style.name = "Order"
	

